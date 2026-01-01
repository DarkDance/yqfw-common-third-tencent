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
public class RobotActionData extends BaseDispatchData {

    /**
     * 加入/移除的时间戳
     */
    private String timestamp;

    /**
     * 用户 openid(单操作)
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 加入/移除群的群openid(群操作)
     */
    @JsonProperty("group_openid")
    private String groupOpenId;

    /**
     * 操作添加/移除机器人进群的群成员openid(群操作)
     */
    @JsonProperty("op_member_openid")
    private String opMemberOpenId;
}
