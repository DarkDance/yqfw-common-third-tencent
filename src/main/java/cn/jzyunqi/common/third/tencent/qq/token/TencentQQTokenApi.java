package cn.jzyunqi.common.third.tencent.qq.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.model.ThirdTokenRedisDto;
import cn.jzyunqi.common.third.tencent.common.constant.TencentCache;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuth;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuthHelper;
import cn.jzyunqi.common.third.tencent.qq.token.model.ClientTokenData;
import cn.jzyunqi.common.third.tencent.qq.token.model.TokenParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Slf4j
public class TencentQQTokenApi {

    @Resource
    private TencentQQTokenApiProxy tencentQQTokenApiProxy;

    @Resource
    private TencentQQAuthHelper tencentQQAuthHelper;

    @Resource
    private RedisHelper redisHelper;

    public String getClientToken(String tencentQQAppId) throws BusinessException {
        TencentQQAuth tencentQQAuth = tencentQQAuthHelper.chooseTencentQQAuth(tencentQQAppId);
        return redisHelper.lockAndGet(TencentCache.THIRD_TENCENT_QQ_V, tencentQQAppId, Duration.ofSeconds(3), (locked) -> {
            if (locked) {
                TokenParam tokenRequest = new TokenParam();
                tokenRequest.setAppId(tencentQQAuth.getAppId());
                tokenRequest.setClientSecret(tencentQQAuth.getClientSecret());
                ClientTokenData clientTokenData = tencentQQTokenApiProxy.getClientToken(tokenRequest);
                ThirdTokenRedisDto clientToken = new ThirdTokenRedisDto();
                clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
                clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

                redisHelper.vPut(TencentCache.THIRD_TENCENT_QQ_V, tencentQQAppId, clientToken);
                return clientTokenData.getAccessToken();
            } else {
                ThirdTokenRedisDto clientToken = (ThirdTokenRedisDto) redisHelper.vGet(TencentCache.THIRD_TENCENT_QQ_V, tencentQQAppId);
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                } else {
                    return null;
                }
            }
        });
    }
}
