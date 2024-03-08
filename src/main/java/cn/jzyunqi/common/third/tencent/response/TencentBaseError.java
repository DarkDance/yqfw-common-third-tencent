package cn.jzyunqi.common.third.tencent.response;

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
public class TencentBaseError implements Serializable {
    @Serial
    private static final long serialVersionUID = 444635331102556537L;

    /**
     * 错误码
     */
    @JsonProperty("Code")
    private String code;

    /**
     * 具体信息
     */
    @JsonProperty("Message")
    private String message;
}
