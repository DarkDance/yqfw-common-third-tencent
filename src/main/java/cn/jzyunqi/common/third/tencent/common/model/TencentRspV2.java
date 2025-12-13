package cn.jzyunqi.common.third.tencent.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
public class TencentRspV2 implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @JsonProperty("code")
    private Integer code;

    /**
     * 具体信息
     */
    @JsonProperty("message")
    private String message;
}
