package cn.jzyunqi.common.third.tencent.qq;

import cn.jzyunqi.common.third.tencent.qq.robot.TencentQQRobotApi;
import cn.jzyunqi.common.third.tencent.qq.token.TencentQQTokenApiProxy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Slf4j
public class TencentQQClient {

    @Resource
    private TencentQQTokenApiProxy token;

    @Resource
    private TencentQQRobotApi robot;
}
