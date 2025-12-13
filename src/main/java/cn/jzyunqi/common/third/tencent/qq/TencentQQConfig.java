package cn.jzyunqi.common.third.tencent.qq;

import cn.jzyunqi.common.third.tencent.common.TencentHttpExchangeWrapper;
import cn.jzyunqi.common.third.tencent.qq.robot.TencentQQRobotApiProxy;
import cn.jzyunqi.common.third.tencent.qq.token.TencentQQTokenApiProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Configuration
@Slf4j
public class TencentQQConfig {

    @Bean
    @ConditionalOnMissingBean
    public TencentHttpExchangeWrapper tencentHttpExchangeWrapper() {
        return new TencentHttpExchangeWrapper();
    }

    @Bean
    public TencentQQClient tencentQQClient() {
        return new TencentQQClient();
    }

    @Bean
    public TencentQQTokenApiProxy tencentQQTokenApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(TencentQQTokenApiProxy.class);
    }

    @Bean
    public TencentQQRobotApiProxy tencentQQRobotApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(TencentQQRobotApiProxy.class);
    }
}

