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
    private String id;
    private String content;
    private String timestamp;
    private Author author;
    @JsonProperty("group_openid")
    private String groupOpenId;
    private List<AttachData> attachments;

    @Getter
    @Setter
    public static class Author {
        @JsonProperty("member_openid")
        private String memberOpenId;
    }
}
