package cn.jzyunqi.common.third.tencent.sms;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.sms.send.TencentSmsSendApiProxy;
import cn.jzyunqi.common.third.tencent.sms.send.enums.Action;
import cn.jzyunqi.common.third.tencent.sms.send.model.SmsData;
import cn.jzyunqi.common.third.tencent.sms.send.model.SmsListData;
import cn.jzyunqi.common.third.tencent.sms.send.model.TencentBaseRsp;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@Slf4j
public class TencentSmsClient {

    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    private static final String END_INDICATE = "tc3_request";

    @Resource
    private TencentSmsAuthRepository tencentSmsAuthRepository;

    @Resource
    private TencentSmsSendApiProxy tencentSmsSendApiProxy;

    public final Sender sender = new Sender();

    public class Sender {

        public List<SmsData> sendSms(String secretId, String smsSign, List<String> phoneList, String templateCode, List<String> templateParam) throws BusinessException {
            TencentSmsAuth tencentSmsAuth = tencentSmsAuthRepository.choosTencentSmsAuth(secretId);
            TencentBaseRsp<SmsListData> body;

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
            TencentBaseRsp<SmsListData> rsp = tencentSmsSendApiProxy.sendSms(tencentSmsAuth.getSecretId(), Action.SendSms, params);
            return rsp.getResult().getSendStatusList();
        }
    }
}
