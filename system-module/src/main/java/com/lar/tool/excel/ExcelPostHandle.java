package com.lar.tool.excel;

import java.util.Map;

public interface ExcelPostHandle {
     void execute(ExcelHelper data, Map<String, Object> params);
}
