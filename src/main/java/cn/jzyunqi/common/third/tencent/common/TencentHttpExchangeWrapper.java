package cn.jzyunqi.common.third.tencent.common;

import cn.jzyunqi.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@Slf4j
@Aspect
@Order
public class TencentHttpExchangeWrapper {

    /**
     * 所有标记了@TencentHttpExchange的类下所有的方法
     */
    @Pointcut("within(@cn.jzyunqi.common.third.tencent.common.TencentHttpExchange *)")
    public void tencentHttpExchange() {
    }

    @Around(value = "tencentHttpExchange() ", argNames = "proceedingJoinPoint")
    public Object Around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("======tencentHttpExchange[{}] start=======", proceedingJoinPoint.getSignature().getName());
        Object resultObj;
        try {
            resultObj = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.debug("======tencentHttpExchange[{}] proceed throw exception=======", proceedingJoinPoint.getSignature().getName());
            throw new BusinessException("common_error_tencent_http_exchange_error", e);
        }
        log.debug("======tencentHttpExchange[{}] end=======", proceedingJoinPoint.getSignature().getName());
        return resultObj;
    }
}
