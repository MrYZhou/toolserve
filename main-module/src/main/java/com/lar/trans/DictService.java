package com.lar.trans;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DictService {
    private final Map<String, Map<String, String>> dictType = new HashMap<>();

    // 比如字典是id转换名称,不需要加类别,因为雪花id之类生成策略已经保证分布式下也唯一。
    public void putDictItem(String key, String value) {
        putDictType("dictMan", new HashMap<>() {{
            put(key, value);
        }});
    }

    // 分类
    public void putDictType(String key, Map<String, String> item) {
        dictType.put(key, item);

    }

    public Map<String, String> getDict(String key) {
        return dictType.get(key);
    }
}
