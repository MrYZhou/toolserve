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
            Class<?> returnType = dictHelper.returnType;
            Class<?> dictParseClass = dictHelper.dictParseClass;
            Field[] declaredFields = dictParseClass.getDeclaredFields();
            proceed = joinPoint.proceed();
            ONode data = ONode.load(proceed);
            String key = dictHelper.key;
            Object item = data.select("$." + key).toObject(dictParseClass);
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
                String invoke = (String) declaredMethod.invoke(item);
                String value = dictMap.get(invoke);
                declaredMethodSet.invoke(item, value == null ? "" : value);
            }
            // 设置数据
            data.set("data", ONode.load(item));
            proceed = CommonUtil.toBean(data, returnType);

        } catch (Throwable e) {
            throw new Exception("解析失败");
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
            String key = dictHelper.key;
            Field[] declaredFields = dictParseClass.getDeclaredFields();
            proceed = joinPoint.proceed();
            ONode data = ONode.load(proceed);
            List<?> list1 = data.select("$." + key).toObjectList(dictParseClass);

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
            int index = -1;
            for (int i = key.length() - 1; i > 0; i--) {
                char c = key.charAt(i);
                if (c == '.') {
                    index = i;
                }
            }
            String path = key.substring(0, index);
            String dataKey = key.substring(index + 1);

            data.select("$." + path).set(dataKey, ONode.load(list1));
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

        String key;

        public void initParserClass(ProceedingJoinPoint joinPoint, String type) throws NoSuchMethodException, InstantiationException, IllegalAccessException {
            this.joinPoint = joinPoint;
            Class<?> targetCls = joinPoint.getTarget().getClass();
            // 得到当前方法签名上面的解析类
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
            Class<?> dictClass;
            String key;

            // 获取解析类
            if ("1".equals(type)) {
                DictOne annotation = targetMethod.getAnnotation(DictOne.class);
                key = annotation.key();
                dictClass = annotation.value();
            } else {
                DictMany annotation = targetMethod.getAnnotation(DictMany.class);
                key = annotation.key();
                dictClass = annotation.value();
            }
            this.key = key;
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
