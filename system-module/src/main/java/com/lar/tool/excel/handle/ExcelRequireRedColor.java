package com.lar.tool.excel.handle;

import com.lar.tool.excel.ExcelHelper;
import com.lar.tool.excel.ExcelPreHandle;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * excel必填字段的字体颜色修改为红色
 */
@Component
public class ExcelRequireRedColor implements ExcelPreHandle {
    @Override
    public void execute(ExcelHelper data, Map<String, Object> params) {

    }
}
