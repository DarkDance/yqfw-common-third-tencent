package cn.jzyunqi.common.third.tencent.sms.send;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.common.TencentHttpExchange;
import cn.jzyunqi.common.third.tencent.sms.send.enums.Action;
import cn.jzyunqi.common.third.tencent.sms.send.model.SmsListData;
import cn.jzyunqi.common.third.tencent.common.model.TencentRspV1;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.TreeMap;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@TencentHttpExchange
@HttpExchange(url = "https://sms.tencentcloudapi.com", accept = {"application/json"}, contentType = "application/json")
public interface TencentSmsSenderApiProxy {

    @PostExchange
    TencentRspV1<SmsListData> sendSms(@RequestAttribute String secretId, @RequestAttribute Action action, @RequestBody TreeMap<String, Object> params) throws BusinessException;

}
