package com.lar.trans;

import com.lar.common.util.CommonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.noear.snack.ONode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class DictAop {
    static Map<String, String> map = new HashMap<>() {{
        put("1", "书籍1");
        put("2", "书籍2");
    }};
    static Map<String, Map<String, String>> dictItem = new HashMap<>() {{
        put("book", map);
    }};

    @Around("@annotation(com.lar.trans.DictOne)")
    public Object transOne(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        try {
            DictHelper dictHelper = new DictHelper();
            dictHelper.initParserClass(joinPoint, "1");
            // 获取解析类中需要解析的字段
            ArrayList<DictValue> list = new ArrayList<>();
            Class<?> returnType = dictHelper.returnType;
            Class<?> dictParseClass = dictHelper.dictParseClass;
            Field[] declaredFields = dictParseClass.getDeclaredFields();
            proceed = joinPoint.proceed();

        } catch (Throwable e) {
            throw e;
        }
        return proceed;
    }

    @Around("@annotation(com.lar.trans.DictMany)")
    public Object transMany(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        try {
            DictHelper dictHelper = new DictHelper();
            dictHelper.initParserClass(joinPoint, "2");

            // 获取解析类中需要解析的字段
            Class<?> returnType = dictHelper.returnType;
            Class<?> dictParseClass = dictHelper.dictParseClass;
            Field[] declaredFields = dictParseClass.getDeclaredFields();
            proceed = joinPoint.proceed();
            ONode data = ONode.load(proceed);
            // todo 如果注解有写按注解上的
            List<?> list1 = data.select("$.data.records").toObjectList(dictParseClass);

            for (Field field : declaredFields) {
                DictValue annotation = field.getAnnotation(DictValue.class);
                if (annotation == null) {
                    continue;
                }
                String ref = annotation.ref();
                Map<String, String> dictMap = dictItem.get(ref);
                // 字段名
                String name = field.getName();
                // 获取方法名
                String getMethod = "get" + StringUtils.capitalize(name);
                String getMethodSet = "set" + StringUtils.capitalize(name);
                // 获取方法
                Method declaredMethod = dictParseClass.getDeclaredMethod(getMethod);
                Method declaredMethodSet = dictParseClass.getDeclaredMethod(getMethodSet, String.class);

                // 获取字典值,并且设置
                for (Object item : list1) {
                    String invoke = (String) declaredMethod.invoke(item);
                    String value = dictMap.get(invoke);
                    declaredMethodSet.invoke(item, value == null ? "" : value);
                }
            }
            // 设置数据
            data.select("$.data").set("records", ONode.load(list1));
            proceed = CommonUtil.toBean(data, returnType);

        } catch (Throwable e) {
            throw new Exception("解析失败");
        }
        return proceed;
    }

    static class DictHelper {
        ProceedingJoinPoint joinPoint;

        Class<?> dictParseClass;

        Class<?> returnType;


        public void initParserClass(ProceedingJoinPoint joinPoint, String type) throws NoSuchMethodException {
            this.joinPoint = joinPoint;
            Class<?> targetCls = joinPoint.getTarget().getClass();
            // 得到当前方法签名上面的解析类
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method targetMethod =
                    targetCls.getDeclaredMethod(
                            ms.getName(),
                            ms.getParameterTypes());
            Class<?> dictClass;
            // 获取解析类
            if ("1".equals(type)) {
                dictClass = targetMethod.getAnnotation(DictOne.class).value();
            } else {
                dictClass = targetMethod.getAnnotation(DictMany.class).value();
            }

            this.dictParseClass = dictClass;
            this.returnType = targetMethod.getReturnType();
        }

        public ProceedingJoinPoint getJoinPoint() {
            return joinPoint;
        }

        public void setJoinPoint(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }
    }

}
