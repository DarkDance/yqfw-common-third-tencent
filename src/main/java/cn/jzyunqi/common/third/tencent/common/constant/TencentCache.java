package cn.jzyunqi.common.third.tencent.common.constant;

import cn.jzyunqi.common.feature.redis.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@AllArgsConstructor
public enum TencentCache implements Cache {

    /**
     * QQ机器人缓存
     */
    THIRD_TENCENT_QQ_V(Duration.ZERO, Boolean.FALSE),
    ;

    private final Duration expiration;

    private final Boolean autoRenew;
}
