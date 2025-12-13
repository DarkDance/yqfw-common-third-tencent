package cn.jzyunqi.common.third.tencent.qq.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
@ToString
public class MsgCb {

    /**
     * 事件id
     */
    @JsonProperty("id")
    private String id;

    /**
     * 指的是 opcode，参考连接维护
     */
    @JsonProperty("op")
    private Integer opCode;

    /**
     * 下行消息都会有一个序列号，标识消息的唯一性，客户端需要再发送心跳的时候，携带客户端收到的最新的s
     */
    @JsonProperty("s")
    private String serialNo;

    /**
     * 代表事件类型。主要用在opCode 为 0 Dispatch 的时候
     */
    @JsonProperty("t")
    private String opType;

    /**
     * 代表事件内容，不同事件类型的事件内容格式都不同，请注意识别。主要用在op为 0 Dispatch 的时候
     */
    @JsonProperty("d")
    private Object dispatch;
}
