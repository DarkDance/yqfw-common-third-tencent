package cn.jzyunqi.common.third.tencent.qq.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuth;
import cn.jzyunqi.common.third.tencent.qq.TencentQQAuthHelper;
import cn.jzyunqi.common.third.tencent.qq.TencentQQClient;
import cn.jzyunqi.common.third.tencent.qq.callback.model.MsgCb;
import cn.jzyunqi.common.third.tencent.qq.callback.model.VerifyData;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        String[] keyPair = DigestUtilPlus.Ed25519.generateKey(seed.getBytes(StringUtilPlus.UTF_8), 0, Boolean.FALSE);
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

        return "success";
    }
}
