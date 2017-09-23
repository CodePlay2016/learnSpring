package com.hufangquan.learnSpring.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LogAspect {

    /**
     * 正常返回时调用
     * @param
     */
    @AfterReturning("execution(* com.hufangquan.learnSpring.aop.*.*(..))")
    public void logAfterReturning(JoinPoint jp){
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        System.out.print("正常返回Log:调用"+className+"的"+methodName+"方法,参数为：");
        for (Object obj : jp.getArgs()) {
            System.out.print(obj.toString());
        }
        System.out.println("");
    }
    /**
     * 抛出异常返回时调用
     * @param jp 调用点信息
     * @param ex 异常信息
     */
    @AfterThrowing(pointcut="execution(* com.hufangquan.learnSpring.aop.*.*(..))",throwing="ex")
    public void logAfterThrowing(JoinPoint jp,Exception ex){
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        System.out.print("异常Log:调用"+className+"的"+methodName+"方法,参数为：");
        for (Object obj : jp.getArgs()) {
            System.out.print(obj.toString());
        }
        System.out.println("抛出异常:"+ex.getMessage()+"!");
    }
}
