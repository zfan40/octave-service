package com.musixise.musixisebox.aop;

import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.service.UserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by zhaowei on 2018/4/1.
 */
@Aspect
@Component
public class AppAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @Pointcut("@annotation(com.musixise.musixisebox.aop.AppMethod)")
    public void loginMethodPointcut() {}

    @Around("loginMethodPointcut() && @annotation(appMethod)")
    public Object Interception(ProceedingJoinPoint point, AppMethod appMethod) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        Object result = null;
        //需要检测登录
        if (appMethod.isLogin()) {
            String accessToken = request.getParameter("access_token");
            if (StringUtils.hasText(accessToken)) {
                //check
                if (!islogin(accessToken)) {
                    return  new ResponseData(ExceptionMsg.NEED_LOGIN);
                } else {
                    Object[] args = point.getArgs();
                    args[0] = userService.getUserIdByToken(accessToken);;
                    return point.proceed(args);
                }
            } else {
                return  new ResponseData(ExceptionMsg.NEED_LOGIN);
            }
        }

        try {
            if (result == null) {
                result = point.proceed();
            }
        } catch (Throwable e) {
            //exception
        }

        return result;
    }

    private void injectUid(Long uid) {

    }

    private Boolean islogin(String accessToken) {
        //检测 access_token 是否存在
        Long userId = userService.getUserIdByToken(accessToken);
        return userId > 0;
    }

    @Before("within(com.musixise.musixisebox..*)")
    public void addBeforeLogger(JoinPoint joinPoint) {
        logger.info("run " + getInvokeName(joinPoint) + " begin");
        logger.info(joinPoint.getSignature().toString());
        logger.info(parseParames(joinPoint.getArgs()));
    }

    @AfterReturning("within(com.musixise.musixisebox..*)")
    public void addAfterReturningLogger(JoinPoint joinPoint) {
        logger.info("run " + getInvokeName(joinPoint) + " end");
    }

    @AfterThrowing(pointcut = "within(com.musixise.musixisebox..*) && @annotation(appMethod)", throwing = "ex")
    public void addAfterThrowingLogger(JoinPoint joinPoint, AppMethod appMethod, Exception ex) {
        logger.error("run " + getInvokeName(joinPoint) + " EXCEPTION", ex);
    }

    private String parseParames(Object[] parames) {
        if (null == parames || parames.length <= 0) {
            return "";
        }
        StringBuffer param = new StringBuffer("get params[{}] ");
        for (Object obj : parames) {
            param.append(ToStringBuilder.reflectionToString(obj)).append("  ");
        }
        return param.toString();
    }


    private String getInvokeName(JoinPoint joinPoint) {
        //正在被通知的方法相关信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取被拦截的方法名
        String methodName = method.getName();

        return String.format("%s.%s",signature.getDeclaringTypeName(), methodName);
    }

}