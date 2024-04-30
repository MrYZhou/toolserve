package com.lar.tool.excel.handle;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

import com.lar.tool.excel.ExcelHelper;
import com.lar.tool.excel.ExcelPostHandle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * excel添加数据下拉校验
 * 下拉选择（单选）、单选框组、开关导入EXCEL内生成【数据验证】下拉框
 */
@Component
public class ExcelDataValidation implements ExcelPostHandle {
    String sheetName;
    /**
     * workbook增加数据验证
     *
     * @param workbook    工作簿
     * @param sheetName   工作表名称
     * @param columnIndex 列索引,从0开始
     * @param options     校验数据项
     */
    public void addDataValidation(Workbook workbook, String sheetName, Integer columnIndex, String[] options) {
        Sheet sheet = workbook.getSheet(sheetName);
        // 设置范围
        CellRangeAddressList addressList = new CellRangeAddressList(1, sheet.getLastRowNum(), columnIndex, columnIndex);
        // 添加数据验证助手
        DataValidationHelper helper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(options);
        DataValidation validation = helper.createValidation(constraint, addressList);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    @Override
    public void execute(ExcelHelper data, Map<String, Object> params) {
//        JnpfKeyConsts.SWITCH
        Workbook workbook = data.getWorkbook();
        List<ExcelExportEntity> list = data.getEntities();
        ExportParams exportParams = data.getExportParams();
        this.sheetName = exportParams.getSheetName();
        addDataValidation(workbook,sheetName,0,new String[]{"1","2"});
        for (ExcelExportEntity export : list) {

        }
    }
}
