package cn.jzyunqi.common.third.tencent.qq.robot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
public class MsgSendParam {
    /**
     * 消息类型: 0 是文本，2 是 markdown， 3 ark，4 embed，7 media 富媒体
     */
    @JsonProperty("msg_type")
    private Integer msgType;

    /**
     * 消息内容(文本内容)
     */
    @JsonProperty("content")
    private String content;

    /**
     * markdown内容
     */
    @JsonProperty("markdown")
    private Object markdown;

    /**
     * keyboard内容
     */
    @JsonProperty("keyboard")
    private Object keyboard;

    /**
     * 富媒体内容
     */
    @JsonProperty("media")
    private MediaData media;

    /**
     * ark模版消息
     */
    @JsonProperty("ark")
    private Object ark;

    /**
     * 【暂未支持】消息引用
     */
    @JsonProperty("message_reference")
    private Object messageReference;

    /**
     * 前置收到的用户发送过来的消息 ID，用于发送被动消息（回复）
     */
    @JsonProperty("msg_id")
    private String msgId;

    /**
     * 回复消息的序号，与 msg_id 联合使用，避免相同消息id回复重复发送，不填默认是 1。相同的 msg_id + msg_seq 重复发送会失败。
     */
    @JsonProperty("msg_seq")
    private Integer msgSeq;

    /**
     * 前置收到的事件 ID，用于发送被动消息，支持事件："INTERACTION_CREATE"、"GROUP_ADD_ROBOT"、"GROUP_MSG_RECEIVE"
     */
    @JsonProperty("event_id")
    private String eventId;

    @Getter
    @Setter
    public static class MediaData {
        /**
         * 媒体类型：1 图片，2 视频，3 语音，4 文件（暂不开放）
         * 资源格式要求
         * 图片：png/jpg，视频：mp4，语音：silk
         */
        @JsonProperty("file_type")
        private String fileType;

        /**
         * 需要发送媒体资源的url
         */
        @JsonProperty("url")
        private String url;

        /**
         * 设置 true 会直接发送消息到目标端，且会占用主动消息频次
         */
        @JsonProperty("srv_send_msg")
        private Boolean srvSendMsg;

        /**
         * 【暂未支持】
         */
        @JsonProperty("file_data")
        private Object file_data;
    }
}
