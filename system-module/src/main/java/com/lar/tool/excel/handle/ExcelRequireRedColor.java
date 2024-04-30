package com.lar.tool.excel.handle;

import cn.afterturn.easypoi.excel.entity.ExportParams;

import com.lar.tool.excel.ExcelHelper;
import com.lar.tool.excel.ExcelPostHandle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * excel必填字段的字体颜色修改为红色
 */
@Component
public class ExcelRequireRedColor implements ExcelPostHandle {
    @Override
    public void execute(ExcelHelper data, Map<String, Object> params) {
        Map<String, Object> extraParams = data.getExtraParams();
        ExportParams exportParams = data.getExportParams();
        Workbook workbook = data.getWorkbook();
        Sheet sheet = workbook.getSheet(exportParams.getSheetName());
        int rowLen = data.isComplexTable() ? 2 : 1;
        for (int i = 0; i < rowLen; i++) {
            Row headerRow = sheet.getRow(i);
            int lastCellNum = (int) headerRow.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell =    headerRow.getCell(j);
                if(!judgeRequired(cell)) continue;
                CellStyle originalCellStyle = cell.getCellStyle();
                Font font = workbook.createFont();
                font.setColor(IndexedColors.RED.getIndex());
                CellStyle redCellStyle = workbook.createCellStyle();
                redCellStyle.cloneStyleFrom(originalCellStyle); // 复制原有样式避免覆盖其他设置
                redCellStyle.setFont(font);
                cell.setCellStyle(redCellStyle);
            }
        }
    }

    private boolean judgeRequired(Cell cell) {
        String key = ((XSSFCell) cell).getRichStringCellValue().getString();
        if(StringUtils.isNotBlank(key)){

        }
        return false;
    }
}
