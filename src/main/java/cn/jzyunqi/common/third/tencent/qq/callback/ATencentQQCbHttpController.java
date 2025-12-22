package cn.jzyunqi.common.third.tencent.qq.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuth;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuthHelper;
import cn.jzyunqi.common.third.tencent.qq.TencentQQClient;
import cn.jzyunqi.common.third.tencent.qq.callback.model.BaseDispatchData;
import cn.jzyunqi.common.third.tencent.qq.callback.model.GroupAtData;
import cn.jzyunqi.common.third.tencent.qq.callback.model.MsgCb;
import cn.jzyunqi.common.third.tencent.qq.callback.model.UserMsgData;
import cn.jzyunqi.common.third.tencent.qq.callback.model.VerifyData;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Slf4j
public class ATencentQQCbHttpController {

    @Resource //供子类使用
    protected TencentQQClient tencentQQClient;

    @Resource
    private TencentQQAuthHelper tencentQQAuthHelper;

    @Resource
    private ObjectMapper objectMapper;

    /***
     * QQ消息回调
     *
     * @param msgCbStr 消息详细
     * @return 根据情况返回回调数据或"failed"或"success"
     */
    @RequestMapping
    @ResponseBody
    public Object userMessageCallback(@PathVariable String appId,
                                      @RequestBody MsgCb msgCb,
                                      @RequestBody String msgCbStr,
                                      @RequestHeader Map<String, String[]> headers,
                                      @RequestHeader("X-Signature-Ed25519") String signature,
                                      @RequestHeader("X-Signature-Timestamp") String timestamp
    ) throws Exception {
        log.debug("""

                        ======Request Header    : {}
                        ======Request AppId     : {}
                        ======Request BodyStr   : {}
                        """,
                headers,
                appId,
                msgCbStr
        );
        TencentQQAuth tencentQQAuth = tencentQQAuthHelper.chooseTencentQQAuth(appId);
        String seed = StringUtilPlus.repeat(tencentQQAuth.getClientSecret(), 2).substring(0, 32);
        // 从seed生成密钥对
        String[] keyPair = DigestUtilPlus.Ed25519.generateKey(seed.getBytes(StringUtilPlus.UTF_8), Boolean.FALSE);
        byte[] publicKey = DigestUtilPlus.Hex.decodeHex(keyPair[0]);
        byte[] privateKey = DigestUtilPlus.Hex.decodeHex(keyPair[1]);

        //验证签名
        String waitCheck = timestamp + msgCbStr;
        boolean result = DigestUtilPlus.Ed25519.verify(waitCheck.getBytes(), DigestUtilPlus.Hex.decodeHex(signature), publicKey);
        if (!result) {
            throw new BusinessException("签名验证失败");
        }

        //回调地址验证
        if (13 == msgCb.getOpCode()) {
            // 解析验证数据
            VerifyData verifyData = objectMapper.convertValue(msgCb.getDispatch(), VerifyData.class);
            String waitSign = verifyData.getEventTimestamp() + verifyData.getPlainToken();
            String signatureHex = DigestUtilPlus.Ed25519.sign(waitSign.getBytes(), privateKey, Boolean.FALSE);

            // 构造响应
            VerifyData rsp = new VerifyData();
            rsp.setSignature(signatureHex);
            rsp.setPlainToken(verifyData.getPlainToken());
            return rsp;
        }

        if (0 == msgCb.getOpCode()) {
            switch (msgCb.getOpType()) {
                case "C2C_MESSAGE_CREATE" -> processC2CMessageCreate(objectMapper.convertValue(msgCb.getDispatch(), UserMsgData.class));
                case "FRIEND_ADD" -> processFriendAdd(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "FRIEND_DEL" -> processFriendDel(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "C2C_MSG_REJECT" -> processC2CMessageReject(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "C2C_MSG_RECEIVE" -> processC2CMessageReceive(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "GROUP_AT_MESSAGE_CREATE" -> processGroupAtMessageCreate(objectMapper.convertValue(msgCb.getDispatch(), GroupAtData.class));
                case "GROUP_ADD_ROBOT" -> processGroupAddRobot(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "GROUP_DEL_ROBOT" -> processGroupDelRobot(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "GROUP_MSG_RECEIVE" -> processGroupMessageReceive(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "GROUP_MSG_REJECT" -> processGroupMessageReject(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                case "SUBSCRIBE_MESSAGE_STATUS" -> processSubscribeMessageStatus(objectMapper.convertValue(msgCb.getDispatch(), BaseDispatchData.class));
                default -> {
                }
            }
        }
        return "success";
    }

    protected void processSubscribeMessageStatus(BaseDispatchData baseDispatchData) {
    }

    protected void processGroupMessageReject(BaseDispatchData baseDispatchData) {
    }

    protected void processGroupMessageReceive(BaseDispatchData baseDispatchData) {
    }

    protected void processGroupDelRobot(BaseDispatchData baseDispatchData) {
    }

    protected void processGroupAddRobot(BaseDispatchData baseDispatchData) {
    }

    protected void processC2CMessageReceive(BaseDispatchData baseDispatchData) {
    }

    protected void processC2CMessageReject(BaseDispatchData baseDispatchData) {
    }

    protected void processFriendDel(BaseDispatchData baseDispatchData) {
    }

    protected void processFriendAdd(BaseDispatchData baseDispatchData) {
    }

    protected void processC2CMessageCreate(UserMsgData userMsgData) throws BusinessException {
    }

    protected void processGroupAtMessageCreate(GroupAtData groupAtData) throws BusinessException {
    }
}
