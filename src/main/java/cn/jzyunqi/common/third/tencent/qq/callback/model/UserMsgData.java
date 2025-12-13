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
public class UserMsgData extends BaseDispatchData {
    private String id;
    private String content;
    private String timestamp;
    private Author author;
    private List<AttachData> attachments;

    @Getter
    @Setter
    public static class Author {
        @JsonProperty("user_openid")
        private String userOpenId;
    }
}
