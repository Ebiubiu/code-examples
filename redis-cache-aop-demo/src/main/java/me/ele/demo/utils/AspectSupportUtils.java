package me.ele.demo.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * aop切面专用的支持类
 *
 * @author daiderong
 */
public class AspectSupportUtils {


    /**
     * 取得真正实现类的方法
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getTargetCalssMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取目标方法
        Method method = methodSignature.getMethod();
        //获取真正的的实现类
        Class implClass = joinPoint.getTarget().getClass();
        //获取真正的实现类的方法
        return implClass.getMethod(method.getName(), method.getParameterTypes());
    }

    /**
     * 解析el表达式的key
     *
     * @param key    注解的key
     * @param method 调用的方法
     * @param args   参数列表
     * @return 解析得到最后的key
     */
    public static String parseElKey(String key, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer descoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = descoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(key);
        return expression.getValue(context, String.class);
    }

    //    /**
//     * 解析key
//     *
//     * @param originKey
//     * @param paramName
//     * @param arguments
//     * @return
//     */
//    private String parseKey(String originKey, String[] paramName, Object[] arguments) {
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression(originKey);
//        EvaluationContext context = new StandardEvaluationContext();
//        for (int i = 0; i < arguments.length; i++) {
//            context.setVariable(paramName[i], arguments[i]);
//        }
//        return expression.getValue(context, String.class);
//    }
}
