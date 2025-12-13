package cn.jzyunqi.common.third.tencent.qq.robot;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.qq.robot.model.MsgSendParam;
import cn.jzyunqi.common.third.tencent.qq.robot.model.MsgSendRsp;
import cn.jzyunqi.common.third.tencent.qq.token.TencentQQTokenApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Slf4j
public class TencentQQRobotApi {

    private static final String TOKEN_PREFIX = "QQBot ";

    @Resource
    private TencentQQTokenApi tencentQQTokenApi;

    @Resource
    private TencentQQRobotApiProxy tencentQQMessageApiProxy;

    /**
     * 发送私聊消息
     *
     * @param tencentQQAppId 应用id
     * @param openid         用户openid
     * @param request        消息请求
     * @return 消息发送结果
     * @throws BusinessException 业务异常
     */
    public MsgSendRsp sendUserMessage(String tencentQQAppId, String openid, MsgSendParam request) throws BusinessException {
        String accessToken = tencentQQTokenApi.getClientToken(tencentQQAppId);
        return tencentQQMessageApiProxy.sendUserMessage(openid, request, TOKEN_PREFIX + accessToken);
    }

    /**
     * 发送群聊消息
     *
     * @param tencentQQAppId 应用id
     * @param groupOpenid    群组openid
     * @param request        消息请求
     * @return 消息发送结果
     * @throws BusinessException 业务异常
     */
    public MsgSendRsp sendGroupMessage(String tencentQQAppId, String groupOpenid, MsgSendParam request) throws BusinessException {
        String accessToken = tencentQQTokenApi.getClientToken(tencentQQAppId);
        return tencentQQMessageApiProxy.sendGroupMessage(groupOpenid, request, TOKEN_PREFIX + accessToken);
    }
}
