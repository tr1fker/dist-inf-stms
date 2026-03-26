package edu.lab.cdi;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Arrays;
import java.io.Serializable;

@Logged
@Interceptor
public class LoggedInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        long start = System.currentTimeMillis();
        String cls = ctx.getMethod().getDeclaringClass().getSimpleName();
        String method = ctx.getMethod().getName();
        Object[] params = ctx.getParameters();
        System.out.println("[LOGGED] -> " + cls + "." + method + "(" + Arrays.toString(params) + ")");
        try {
            Object result = ctx.proceed();
            long took = System.currentTimeMillis() - start;
            System.out.println("[LOGGED] <- " + cls + "." + method + " took " + took + "ms");
            return result;
        } catch (Exception e) {
            long took = System.currentTimeMillis() - start;
            System.out.println("[LOGGED] !! " + cls + "." + method + " failed after " + took + "ms: " + e);
            throw e;
        }
    }
}
