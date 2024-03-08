package cn.jzyunqi.common.third.tencent.response;

import cn.jzyunqi.common.third.tencent.model.SmsResultData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2021/5/14.
 */
@Getter
@Setter
public class SmsResult extends TencentBaseResult {
    @Serial
    private static final long serialVersionUID = -3560683449425726791L;

    /**
     * 短信发送状态
     */
    @JsonProperty("SendStatusSet")
    private List<SmsResultData> sendStatusList;
}
