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
public class TencentBaseRsp<T extends TencentBaseResult> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7409897864136311195L;

    /**
     * 短信发送结果
     */
    @JsonProperty("Response")
    private T result;
}
