package cn.jzyunqi.common.third.tencent.qq.robot;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.common.TencentHttpExchange;
import cn.jzyunqi.common.third.tencent.qq.robot.model.MsgSendParam;
import cn.jzyunqi.common.third.tencent.qq.robot.model.MsgSendRsp;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@TencentHttpExchange
@HttpExchange(url = "https://api.sgroup.qq.com", accept = {"application/json"}, contentType = "application/json")
public interface TencentQQRobotApiProxy {

    /**
     * 发送私聊消息
     *
     * @param accessToken 访问令牌
     * @param openid      用户openid
     * @param request     消息请求
     * @return 消息发送结果
     * @throws BusinessException 业务异常
     */
    @PostExchange("/v2/users/{openid}/messages")
    MsgSendRsp sendUserMessage(@PathVariable String openid, @RequestBody MsgSendParam request, @RequestHeader("Authorization") String accessToken) throws BusinessException;

    /**
     * 发送群聊消息
     *
     * @param accessToken 访问令牌
     * @param groupOpenid 群组openid
     * @param request     消息请求
     * @return 消息发送结果
     * @throws BusinessException 业务异常
     */
    @PostExchange("/v2/groups/{groupOpenid}/messages")
    MsgSendRsp sendGroupMessage(@PathVariable String groupOpenid, @RequestBody MsgSendParam request, @RequestHeader("Authorization") String accessToken) throws BusinessException;
}
