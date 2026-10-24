package com.troy.ats.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Uniform entry/exit logging for every controller and service, so we get coverage
 * without scattering log statements through 30 classes.
 *
 * Levels: controllers at INFO (one line in, one line out with duration), services at
 * DEBUG. Exceptions are NOT logged here - GlobalExceptionHandler already logs them with
 * the right severity, and duplicating would double every stacktrace.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /** Anything slower than this is worth noticing even on a happy path. */
    private static final long SLOW_MS = 1000;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllers() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void services() {
    }

    @Around("controllers()")
    public Object logController(ProceedingJoinPoint pjp) throws Throwable {
        String target = pjp.getSignature().getDeclaringType().getSimpleName() + "." + pjp.getSignature().getName();

        long start = System.nanoTime();
        log.info("-> {}({})", target, describeArgs(pjp.getArgs()));
        try {
            Object result = pjp.proceed();
            long ms = (System.nanoTime() - start) / 1_000_000;
            if (ms >= SLOW_MS) {
                log.warn("<- {} completed in {}ms (slow)", target, ms);
            } else {
                log.info("<- {} completed in {}ms", target, ms);
            }
            return result;
        } catch (Throwable ex) {
            // Let it propagate to GlobalExceptionHandler, which owns the error logging.
            long ms = (System.nanoTime() - start) / 1_000_000;
            log.info("<- {} failed after {}ms with {}", target, ms, ex.getClass().getSimpleName());
            throw ex;
        }
    }

    @Around("services()")
    public Object logService(ProceedingJoinPoint pjp) throws Throwable {
        if (!log.isDebugEnabled()) {
            return pjp.proceed();
        }

        String target = pjp.getSignature().getDeclaringType().getSimpleName() + "." + pjp.getSignature().getName();

        long start = System.nanoTime();
        log.debug("  -> {}({})", target, describeArgs(pjp.getArgs()));
        try {
            Object result = pjp.proceed();
            log.debug("  <- {} in {}ms", target, (System.nanoTime() - start) / 1_000_000);
            return result;
        } catch (Throwable ex) {
            log.debug("  <- {} threw {}", target, ex.getClass().getSimpleName());
            throw ex;
        }
    }

    /**
     * Renders arguments without ever serialising a DTO. Request objects like LoginRequest
     * carry raw passwords, so complex types are reduced to their class name - only
     * identifiers and scalars are shown by value.
     */
    private String describeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return Arrays.stream(args).map(this::describe).collect(Collectors.joining(", "));
    }

    private String describe(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg instanceof MultipartFile file) {
            return "file(" + file.getOriginalFilename() + ", " + file.getSize() + "B)";
        }
        if (arg instanceof UUID || arg instanceof Number || arg instanceof Boolean || arg instanceof Enum<?>) {
            return String.valueOf(arg);
        }
        if (arg instanceof CharSequence cs) {
            // Search terms and path segments are safe and useful; cap the length.
            return cs.length() > 40 ? "\"" + cs.subSequence(0, 40) + "...\"" : "\"" + cs + "\"";
        }
        if (arg instanceof Collection<?> c) {
            return arg.getClass().getSimpleName() + "[" + c.size() + "]";
        }
        if (arg instanceof Map<?, ?> m) {
            return arg.getClass().getSimpleName() + "{" + m.size() + "}";
        }
        // Anything else (DTOs, entities, Pageable, servlet objects) - type only, never contents.
        return arg.getClass().getSimpleName();
    }
}
