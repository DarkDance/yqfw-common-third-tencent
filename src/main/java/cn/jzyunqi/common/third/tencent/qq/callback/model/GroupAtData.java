package cn.jzyunqi.common.third.tencent.qq.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2025/12/13
 */
@Getter
@Setter
public class GroupAtData extends BaseDispatchData {
    /**
     * 平台方消息 ID，可以用于被动消息发送
     */
    private String id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息生产时间（RFC3339）
     */
    private String timestamp;

    /**
     * 发送者
     */
    private Author author;

    /**
     * 群聊的 openid
     */
    @JsonProperty("group_openid")
    private String groupOpenId;

    /**
     * 富媒体文件附件，文件类型："图片，语音，视频，文件"
     */
    private List<AttachData> attachments;

    @Getter
    @Setter
    public static class Author {

        /**
         * 用户在本群的 member_openid
         */
        @JsonProperty("member_openid")
        private String memberOpenId;
    }
}
