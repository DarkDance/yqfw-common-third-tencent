package cn.jzyunqi.common.third.tencent.sms;

import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
public abstract class TencentSmsAuthRepository implements InitializingBean {

    private final Map<String, TencentSmsAuth> authMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TencentSmsAuth> tencentSmsAuthList = initTencentSmsAuthList();
        for (TencentSmsAuth tencentSmsAuth : tencentSmsAuthList) {
            authMap.put(tencentSmsAuth.getSecretId(), tencentSmsAuth);
        }
    }

    public TencentSmsAuth choosTencentSmsAuth(String secretId) {
        return authMap.get(secretId);
    }

    public void addTencentSmsAuth(TencentSmsAuth tencentSmsAuth) {
        authMap.put(tencentSmsAuth.getSecretId(), tencentSmsAuth);
    }

    public void removeTencentSmsAuth(String secretId) {
        authMap.remove(secretId);
    }

    public List<TencentSmsAuth> getTencentSmsAuthList() {
        return new ArrayList<>(authMap.values());
    }

    public abstract List<TencentSmsAuth> initTencentSmsAuthList();
}
