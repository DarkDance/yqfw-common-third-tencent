package cn.jzyunqi.common.third.tencent.sms.send.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/5/14.
 */
@Getter
@Setter
public class TencentBaseResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 7051943550920548494L;

    /**
     * 唯一请求 ID
     */
    @JsonProperty("RequestId")
    private String requestId;

    /**
     * 错误信息
     */
    @JsonProperty("Error")
    private TencentBaseError error;
}
