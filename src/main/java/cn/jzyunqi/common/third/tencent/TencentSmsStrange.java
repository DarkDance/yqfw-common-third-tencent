package cn.jzyunqi.common.third.tencent;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.sms.SmsHelper;
import cn.jzyunqi.common.feature.sms.SmsResultDto;
import cn.jzyunqi.common.third.tencent.client.TencentSmsClient;
import cn.jzyunqi.common.utils.BeanUtilPlus;

import java.util.List;

/**
 * @author wiiyaya
 * @date 2022/11/30.
 */
public class TencentSmsStrange implements SmsHelper {

    private final TencentSmsClient tencentSmsHelper;

    public TencentSmsStrange(TencentSmsClient tencentSmsHelper) {
        this.tencentSmsHelper = tencentSmsHelper;
    }

    @Override
    public List<SmsResultDto> sendSms(String smsSign, List<String> phoneList, String templateCode, List<String> templateParam) throws BusinessException {
        return tencentSmsHelper.sendSms(smsSign, phoneList, templateCode, templateParam)
                .stream()
                .map(smsResultData -> BeanUtilPlus.copyAs(smsResultData, SmsResultDto.class))
                .toList();
    }
}
