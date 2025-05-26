package cn.jzyunqi.common.third.tencent.sms;

import cn.jzyunqi.common.third.tencent.common.TencentHttpExchangeWrapper;
import cn.jzyunqi.common.third.tencent.sms.send.TencentSmsSendApiProxy;
import cn.jzyunqi.common.third.tencent.sms.send.enums.Action;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@Configuration
@Slf4j
public class TencentSmsConfig {

    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    private static final String END_INDICATE = "tc3_request";

    @Bean
    @ConditionalOnMissingBean
    public TencentHttpExchangeWrapper tencentHttpExchangeWrapper() {
        return new TencentHttpExchangeWrapper();
    }

    @Bean
    public TencentSmsClient tencentSmsClient() {
        return new TencentSmsClient();
    }

    @Bean
    public TencentSmsSendApiProxy tencentSmsSendApiProxy(WebClient.Builder webClientBuilder, TencentSmsAuthRepository tencentSmsAuthRepository) {
        WebClient webClient = webClientBuilder.clone()
                //.codecs(WxFormatUtils::jackson2Config)
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    String secretId = (String) request.attribute("secretId").orElse(null);
                    Action action = (Action) request.attribute("action").orElse(null);
                    TencentSmsAuth auth = tencentSmsAuthRepository.choosTencentSmsAuth(secretId);

                    ClientRequest.Builder amendRequest = ClientRequest.from(request);
                    if (request.method() == HttpMethod.GET) {
                        Map<String, String> actionHeaders = getSignHttpHeaders(request.method(), request.url().getQuery(), action, auth);
                        amendRequest.headers(headers -> headers.setAll(actionHeaders));
                    } else {
                        amendRequest.body((outputMessage, context) -> request.body().insert(new ClientHttpRequestDecorator(outputMessage) {
                            @Override
                            public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                                return DataBufferUtils.join(body).flatMap(buffer -> {
                                    String bodyStr = buffer.toString(StringUtilPlus.UTF_8);
                                    Map<String, String> actionHeaders = getSignHttpHeaders(request.method(), bodyStr, action, auth);
                                    getHeaders().setAll(actionHeaders);
                                    return super.writeWith(Mono.just(buffer));
                                });
                            }
                        }, context));
                    }
                    return Mono.just(amendRequest.build());
                })).build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(TencentSmsSendApiProxy.class);
    }

    private Map<String, String> getSignHttpHeaders(HttpMethod method, String bodyOrQueryStr, Action action, TencentSmsAuth auth) {
        long timestamp = System.currentTimeMillis() / 1000;
        String currentDate = LocalDateTime.ofEpochSecond(timestamp, 0, DateTimeUtilPlus.GMT0_ZONE_OFFSET).format(DateTimeUtilPlus.SYSTEM_DATE_FORMAT);
        Map<String, String> actionHeaders = getActionHeaders(action, timestamp);

        // ************* 步骤 1：拼接规范请求串 *************
        String canonicalQueryString = method == HttpMethod.POST ? StringUtilPlus.EMPTY : bodyOrQueryStr;
        String hashedRequestPayload = method == HttpMethod.GET ? StringUtilPlus.EMPTY : DigestUtilPlus.SHA.sign(bodyOrQueryStr, DigestUtilPlus.SHAAlgo._256, Boolean.FALSE).toLowerCase();

        String canonicalHeaders = actionHeaders.entrySet().stream().map(entry -> StringUtilPlus.join(entry.getKey().toLowerCase(), StringUtilPlus.COLON, entry.getValue().toLowerCase(), StringUtilPlus.ENTER)).sorted().collect(Collectors.joining());
        String signedHeaders = actionHeaders.keySet().stream().map(String::toLowerCase).sorted().collect(Collectors.joining(StringUtilPlus.SEMICOLON));
        String canonicalRequest = StringUtilPlus.join(method, StringUtilPlus.ENTER, StringUtilPlus.SLASH, StringUtilPlus.ENTER, canonicalQueryString, StringUtilPlus.ENTER, canonicalHeaders, StringUtilPlus.ENTER, signedHeaders, StringUtilPlus.ENTER, hashedRequestPayload);

        // ************* 步骤 2：拼接待签名字符串 *************
        String credentialScope = String.format("%s/%s/%s", currentDate, action.getService(), END_INDICATE);
        String hashedCanonicalRequest = DigestUtilPlus.SHA.sign(canonicalRequest, DigestUtilPlus.SHAAlgo._256, Boolean.FALSE).toLowerCase();
        String stringToSign = StringUtilPlus.join(ALGORITHM, StringUtilPlus.ENTER, timestamp, StringUtilPlus.ENTER, credentialScope, StringUtilPlus.ENTER, hashedCanonicalRequest);

        // ************* 步骤 3：计算签名 *************
        String signature = StringUtilPlus.EMPTY;
        try {
            String secretDate = DigestUtilPlus.Mac.sign(currentDate, "TC3" + auth.getSecretKey(), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
            String secretService = DigestUtilPlus.Mac.sign(action.getService(), DigestUtilPlus.Hex.decodeHex(secretDate), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
            String secretSigning = DigestUtilPlus.Mac.sign(END_INDICATE, DigestUtilPlus.Hex.decodeHex(secretService), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
            signature = DigestUtilPlus.Mac.sign(stringToSign, DigestUtilPlus.Hex.decodeHex(secretSigning), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
        } catch (Exception e) {
            log.error("======Tencent request [{}][{}] signature error=======", auth.getSecretId(), action, e);
        }
        // ************* 步骤 4：拼接 Authorization *************
        String authorization = String.format("%s Credential=%s/%s, SignedHeaders=%s, Signature=%s", ALGORITHM, auth.getSecretId(), credentialScope, signedHeaders, signature);
        actionHeaders.put("Authorization", authorization);
        return actionHeaders;
    }

    private Map<String, String> getActionHeaders(Action action, long timestamp) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", action.getContentType());
        headers.put("Host", action.getHost());
        headers.put("X-TC-Action", action.name());
        headers.put("X-TC-Timestamp", Long.toString(timestamp));
        headers.put("X-TC-Version", action.getVersion());
        if (StringUtilPlus.isNotEmpty(action.getRegion())) {
            headers.put("X-TC-Region", action.getRegion());
        }
        //headers.put("X-TC-Token", "xxx");
        return headers;
    }

}
