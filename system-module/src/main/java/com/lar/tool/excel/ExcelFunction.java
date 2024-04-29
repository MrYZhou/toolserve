package com.lar.tool.excel;



import java.util.Map;

@FunctionalInterface
public interface ExcelFunction {
    void apply(ExcelHelper compute, Map<String, Object> params);
}
