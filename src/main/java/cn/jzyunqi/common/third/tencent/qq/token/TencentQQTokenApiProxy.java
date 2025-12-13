package cn.jzyunqi.common.third.tencent.qq.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.common.TencentHttpExchange;
import cn.jzyunqi.common.third.tencent.qq.token.model.ClientTokenData;
import cn.jzyunqi.common.third.tencent.qq.token.model.TokenParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@TencentHttpExchange
@HttpExchange(url = "https://bots.qq.com", accept = {"application/json"}, contentType = "application/json")
public interface TencentQQTokenApiProxy {

    /**
     * 获取应用访问令牌
     *
     * @param tokenRequest token请求参数
     * @return 访问令牌数据
     * @throws BusinessException 业务异常
     */
    @PostExchange(url = "/app/getAppAccessToken")
    ClientTokenData getClientToken(@RequestBody TokenParam tokenRequest) throws BusinessException;
}
