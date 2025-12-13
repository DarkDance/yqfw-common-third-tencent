package cn.jzyunqi.common.third.tencent.qq.callback;

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

import java.util.HashMap;
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
    @RequestMapping(consumes = "text/xml", produces = "text/xml")
    @ResponseBody
    public Object userMessageCallback(@PathVariable String appId, @RequestBody MsgCb msgCb, @RequestBody String msgCbStr, @RequestHeader Map<String, String[]> headers) {
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
        //回调地址验证
        if (13 == msgCb.getOpCode()) {
            try {
                // 解析验证数据
                VerifyData verifyData = objectMapper.convertValue(msgCb.getDispatch(), VerifyData.class);

                // 处理密钥种子：如果长度不足32字节，则重复填充；如果超过32字节，则截取前32字节
                String seed = tencentQQAuth.getClientSecret();
                while (seed.length() < 32) {
                    seed = seed + seed;
                }
                seed = seed.substring(0, 32);
                byte[] seedBytes = seed.getBytes(StringUtilPlus.UTF_8);

                // 从seed生成密钥对
                String[] keyPair = DigestUtilPlus.Ed25519.generateKey(seedBytes, 0, Boolean.TRUE);
                byte[] privateKey = DigestUtilPlus.Base64.decodeBase64(keyPair[1]);

                // 构造待签名消息
                String message = verifyData.getEventTimestamp().toString() + verifyData.getPlainToken();

                // 使用私钥种子进行签名
                String signatureHex = DigestUtilPlus.Ed25519.sign(message.getBytes(java.nio.charset.StandardCharsets.UTF_8), privateKey, Boolean.FALSE);

                // 构造响应
                VerifyData rsp = new VerifyData();
                rsp.setSignature(signatureHex);
                rsp.setPlainToken(verifyData.getPlainToken());
                return rsp;
            } catch (Exception e) {
                log.error("处理回调地址验证失败", e);
                return "failed";
            }
        }

        return "success";
    }
}
