package cn.jzyunqi.common.third.tencent.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.enums.Action;
import cn.jzyunqi.common.third.tencent.model.SmsResultData;
import cn.jzyunqi.common.third.tencent.response.SmsResult;
import cn.jzyunqi.common.third.tencent.response.TencentBaseRsp;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author wiiyaya
 * @date 2021/5/13.
 */
@Slf4j
public class TencentSmsClient extends TencentBaseClient {

    private static final String SMS_ENDPOINT = "https://sms.tencentcloudapi.com/";

    private final String smsSdkAppId;

    public TencentSmsClient(String secretId, String secretKey, String smsSdkAppId) {
        super(secretId, secretKey);
        this.smsSdkAppId = smsSdkAppId;
    }

    /**
     * 发送短信
     *
     * @param smsSign       短信签名
     * @param phoneList     短信接收号码，上限为200个手机号码，要么全为国内，要么全为国际(代码暂不支持)
     * @param templateCode  短信模板ID
     * @param templateParam 短信模板变量替换JSON串
     * @throws BusinessException 异常
     */
    public List<SmsResultData> sendSms(String smsSign, List<String> phoneList, String templateCode, List<String> templateParam) throws BusinessException {
        TencentBaseRsp<SmsResult> body;
        try {
            //1. 填充业务参数
            TreeMap<String, Object> params = new TreeMap<>();
            //params.put("Action", Action.SendSms.name());
            params.put("PhoneNumberSet", phoneList); //短信接收号码,批量上限为200个手机号码
            params.put("TemplateID", templateCode);
            params.put("SmsSdkAppid", smsSdkAppId);
            params.put("Sign", smsSign);//国内短信为必填参数
            params.put("TemplateParamSet", templateParam); //模板参数
            params.put("SessionContext", "test"); //需要原样异步返回的数据

            HttpHeaders headers = new HttpHeaders();
            super.addHeaderSign(HttpMethod.POST, headers, params, Action.SendSms);

            //2. 构造请求url
            RequestEntity<TreeMap<String, Object>> requestEntity = new RequestEntity<>(params, headers, HttpMethod.POST, new URI(SMS_ENDPOINT));
            ParameterizedTypeReference<TencentBaseRsp<SmsResult>> responseType = new ParameterizedTypeReference<TencentBaseRsp<SmsResult>>() {};
            ResponseEntity<TencentBaseRsp<SmsResult>> sendSmsRsp = super.getRestTemplate().exchange(requestEntity, responseType);
            body = Optional.ofNullable(sendSmsRsp.getBody()).orElseGet(TencentBaseRsp::new);
        } catch (Exception e) {
            log.error("======TencentSmsHelper sendSms other error:", e);
            throw new BusinessException("common_error_tencent_sms_send_error");
        }

        if(body.getResult().getError() == null){
            body.getResult().getSendStatusList().forEach(smsResultData -> {
                if (!StringUtilPlus.equals(smsResultData.getCode(), "Ok")) {
                    log.warn("TencentSmsHelper sendSms 200 item error [{}][{}][{}][{}]:", body.getResult().getRequestId(), smsResultData.getPhoneNumber(), smsResultData.getCode(), smsResultData.getMessage());
                }
                smsResultData.setRequestId(body.getResult().getRequestId());
            });
            return body.getResult().getSendStatusList();
        }else{
            log.error("======TencentSmsHelper sendSms 200 error [{}][{}]:", body.getResult().getError().getCode(), body.getResult().getError().getMessage());
            throw new BusinessException("common_error_tencent_sms_send_error");
        }
    }
}
