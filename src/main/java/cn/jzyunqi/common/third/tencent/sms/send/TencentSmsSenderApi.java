package cn.jzyunqi.common.third.tencent.sms.send;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.common.model.TencentRspV1;
import cn.jzyunqi.common.third.tencent.sms.TencentSmsAuth;
import cn.jzyunqi.common.third.tencent.sms.TencentSmsAuthHelper;
import cn.jzyunqi.common.third.tencent.sms.send.enums.Action;
import cn.jzyunqi.common.third.tencent.sms.send.model.SmsData;
import cn.jzyunqi.common.third.tencent.sms.send.model.SmsListData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.TreeMap;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Slf4j
public class TencentSmsSenderApi {

    @Resource
    private TencentSmsSenderApiProxy tencentSmsSendApiProxy;

    @Resource
    private TencentSmsAuthHelper tencentSmsAuthHelper;

    public List<SmsData> sendSms(String secretId, String smsSign, List<String> phoneList, String templateCode, List<String> templateParam) throws BusinessException {
        TencentSmsAuth tencentSmsAuth = tencentSmsAuthHelper.choosTencentSmsAuth(secretId);

        //1. 填充业务参数
        TreeMap<String, Object> params = new TreeMap<>();
        //params.put("Action", Action.SendSms.name());
        params.put("PhoneNumberSet", phoneList); //短信接收号码,批量上限为200个手机号码
        params.put("TemplateID", templateCode);
        params.put("SmsSdkAppid", tencentSmsAuth.getSmsSdkAppId());
        params.put("Sign", smsSign);//国内短信为必填参数
        params.put("TemplateParamSet", templateParam); //模板参数
        params.put("SessionContext", "test"); //需要原样异步返回的数据

        //2. 构造请求url
        TencentRspV1<SmsListData> rsp = tencentSmsSendApiProxy.sendSms(tencentSmsAuth.getSecretId(), Action.SendSms, params);
        return rsp.getResult().getSendStatusList();
    }
}
