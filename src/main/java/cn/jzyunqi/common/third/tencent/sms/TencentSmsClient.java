package cn.jzyunqi.common.third.tencent.sms;

import cn.jzyunqi.common.third.tencent.sms.send.TencentSmsSenderApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@Slf4j
public class TencentSmsClient {

    @Resource
    public TencentSmsSenderApi sender;

}
