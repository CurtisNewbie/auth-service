package com.curtisnewbie.service.auth.messaging.helper;

import com.curtisnewbie.common.trace.TUser;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Advice for saving operate_log
 *
 * @author yongjie.zhuang
 * @see LogOperation
 */
@Slf4j
@Aspect
public class OperateLogAdvice {

    private static final String ANONYMOUS_NAME = "anonymous";
    private static final int ANONYMOUS_ID = 0;
    private static final int MAX_PARAM_LENGTH = 950;

    @Autowired
    private AuthMessageDispatcher messageDispatcher;

    @Around("@annotation(logOperation)")
    public Object logOperation(ProceedingJoinPoint pjp, LogOperation logOperation) throws Throwable {
        try {
            doAsyncOperationLog(pjp, logOperation);
        } catch (Exception e) {
            log.error("Unable to log operation", e);
        }
        return pjp.proceed();
    }

    private void doAsyncOperationLog(ProceedingJoinPoint pjp, LogOperation logOperation) throws InvalidAuthenticationException {
        if (logOperation == null || !logOperation.enabled())
            return;

        OperateLogVo v = new OperateLogVo();
        v.setOperateName(logOperation.name());
        v.setOperateDesc(logOperation.description());
        v.setOperateTime(LocalDateTime.now());
        v.setOperateParam(toParamString(pjp.getArgs()));

        String username = ANONYMOUS_NAME;
        int userId = ANONYMOUS_ID;

        final Optional<TUser> opt = TraceUtils.nullableTUser();
        if (opt.isPresent()) {
            final TUser tu = opt.get();
            username = tu.getUsername();
            userId = tu.getUserId();
        }
        v.setUsername(username);
        v.setUserId(userId);
        messageDispatcher.dispatchOperateLog(v);
    }

    private String toParamString(Object[] args) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            Object a = args[i];
            if (a == null) {
                continue;
            }
            // skip the injected object from spring
            if (isExcluded(a))
                continue;

            if (i > 0 && sb.length() > 0)
                sb.append(",");
            sb.append(stripOffClassName(a));
        }

        // cut it if necessary
        if (sb.length() > MAX_PARAM_LENGTH) {
            sb.setLength(MAX_PARAM_LENGTH);
            sb.append("...");
        }
        sb.append("]");
        return sb.toString();
    }

    private String stripOffClassName(Object o) {
        String ts = o.toString();
        String clzName = o.getClass().getCanonicalName();
        int i;
        // Object#toString() with outer class's name
        if ((i = ts.indexOf(clzName)) != -1)
            return o.getClass().getSimpleName() + ts.substring(i + clzName.length());

        // Object#toString() with inner class's name
        int j;
        if ((j = ts.lastIndexOf("$")) != -1) {
            // replace the $ with .
            String rts = ts.substring(0, j) + "." + ts.substring(j + 1);
            if ((i = rts.indexOf(clzName)) != -1)
                return o.getClass().getSimpleName() + rts.substring(i + clzName.length());
        }
        return ts;
    }

    private boolean isExcluded(Object o) {
        return o.getClass().getCanonicalName().startsWith("org.springframework");
    }

}
