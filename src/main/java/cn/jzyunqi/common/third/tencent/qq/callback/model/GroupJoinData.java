package cn.jzyunqi.common.third.tencent.qq.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2026/1/1
 */
@Getter
@Setter
public class GroupJoinData extends BaseDispatchData {

    /**
     * 加入的时间戳
     */
    private String timestamp;

    /**
     * 加入群的群openid
     */
    @JsonProperty("group_openid")
    private String groupOpenId;

    /**
     * 操作添加机器人进群的群成员openid
     */
    @JsonProperty("op_member_openid")
    private String opMemberOpenId;
}
