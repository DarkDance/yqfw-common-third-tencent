package cn.jzyunqi.common.third.tencent.client;

import cn.jzyunqi.common.third.tencent.enums.Action;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author wiiyaya
 * @date 2021/5/14.
 */
@Slf4j
public abstract class TencentBaseClient {

    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    private static final String END_INDICATE = "tc3_request";

    /**
     * app id
     */
    private final String secretId;

    /**
     * app密码
     */
    private final String secretKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    protected TencentBaseClient(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.objectMapper = new ObjectMapper();
        this.restTemplate = new RestTemplate();
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void addHeaderSign(HttpMethod method, HttpHeaders headers, TreeMap<String, Object> params, Action action) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String currentDate = LocalDateTime.ofEpochSecond(timestamp, 0, DateTimeUtilPlus.GMT0_ZONE_OFFSET).format(DateTimeUtilPlus.SYSTEM_DATE_FORMAT);

        headers.set("Content-Type", action.getContentType());
        headers.set("Host", action.getHost());
        headers.set("X-TC-Action", action.name());
        headers.set("X-TC-Timestamp", Long.toString(timestamp));
        headers.set("X-TC-Version", action.getVersion());
        if (StringUtilPlus.isNotEmpty(action.getRegion())) {
            headers.set("X-TC-Region", action.getRegion());
        }
        //headers.set("X-TC-Token", "xxx");

        // ************* 步骤 1：拼接规范请求串 *************
        String canonicalQueryString = method == HttpMethod.POST
                ? StringUtilPlus.EMPTY
                : URLEncoder.encode(
                params.entrySet().stream()
                        .map(entry -> StringUtilPlus.join(entry.getKey(), StringUtilPlus.LINK, entry.getValue()))
                        .collect(Collectors.joining(StringUtilPlus.AND)),
                StringUtilPlus.UTF_8);
        String canonicalHeaders = headers.toSingleValueMap().entrySet().stream()
                .map(entry -> StringUtilPlus.join(entry.getKey().toLowerCase(), StringUtilPlus.COLON, entry.getValue().toLowerCase(), StringUtilPlus.ENTER))
                .sorted()
                .collect(Collectors.joining());
        String signedHeaders = headers.toSingleValueMap().keySet().stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.joining(StringUtilPlus.SEMICOLON));
        String paramsJson = objectMapper.writeValueAsString(params);
        String hashedRequestPayload = method == HttpMethod.GET
                ? StringUtilPlus.EMPTY
                : DigestUtilPlus.SHA.sign(paramsJson, DigestUtilPlus.SHAAlgo._256, Boolean.FALSE).toLowerCase();
        String canonicalRequest = StringUtilPlus.join(
                method, StringUtilPlus.ENTER,
                StringUtilPlus.SLASH, StringUtilPlus.ENTER,
                canonicalQueryString, StringUtilPlus.ENTER,
                canonicalHeaders, StringUtilPlus.ENTER,
                signedHeaders, StringUtilPlus.ENTER,
                hashedRequestPayload
        );

        // ************* 步骤 2：拼接待签名字符串 *************
        String credentialScope = String.format("%s/%s/%s", currentDate, action.getService(), END_INDICATE);
        String hashedCanonicalRequest = DigestUtilPlus.SHA.sign(canonicalRequest, DigestUtilPlus.SHAAlgo._256, Boolean.FALSE).toLowerCase();
        String stringToSign = StringUtilPlus.join(
                ALGORITHM, StringUtilPlus.ENTER,
                timestamp, StringUtilPlus.ENTER,
                credentialScope, StringUtilPlus.ENTER,
                hashedCanonicalRequest
        );

        // ************* 步骤 3：计算签名 *************
        String secretDate = DigestUtilPlus.Mac.sign(currentDate, "TC3" + secretKey, DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
        String secretService = DigestUtilPlus.Mac.sign(action.getService(), DigestUtilPlus.Hex.decodeHex(secretDate), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);
        String secretSigning = DigestUtilPlus.Mac.sign(END_INDICATE, DigestUtilPlus.Hex.decodeHex(secretService), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);

        String signature = DigestUtilPlus.Mac.sign(stringToSign, DigestUtilPlus.Hex.decodeHex(secretSigning), DigestUtilPlus.MacAlgo.H_SHA256, Boolean.FALSE);

        // ************* 步骤 4：拼接 Authorization *************
        String authorization = String.format(
                "%s Credential=%s/%s, SignedHeaders=%s, Signature=%s",
                ALGORITHM,
                secretId,
                credentialScope,
                signedHeaders,
                signature
        );

        headers.set("Authorization", authorization);
    }
}
