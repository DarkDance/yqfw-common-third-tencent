package cn.jzyunqi.common.third.tencent.qq.robot.model;

import cn.jzyunqi.common.third.tencent.common.model.TencentRspV2;
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
public class MsgSendRsp implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误信息
     */
    @JsonProperty("error")
    private TencentRspV2 error;

    /**
     * 消息id
     */
    @JsonProperty("id")
    private String id;

    /**
     * 消息时间戳
     */
    @JsonProperty("timestamp")
    private String timestamp;
}
