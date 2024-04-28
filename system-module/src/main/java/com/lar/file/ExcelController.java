package com.lar.file;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.WriteHandler;
import com.lar.vo.AppResult;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;

// 导出excel
@RestController
@RequestMapping(value = "/excel")
public class ExcelController {
    // 导出模板
    @RequestMapping(value = "/export")
    @ResponseBody
    public AppResult<?> export(String fileVersionId) {
        String filePath = "./excel.xlsx";
        String sheetName = "Sheet1";
        int columnIndex = 0;
        String comment = "这是一个批注示例";

        try {
            this.addCommentToHeader(filePath, sheetName, columnIndex, comment);
            System.out.println("批注添加成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void addCommentToHeader(String filePath, String sheetName, int columnIndex, String comment) throws IOException {
        Workbook workbook = new XSSFWorkbook(filePath);
        Sheet sheet = workbook.getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        Cell headerCell = headerRow.getCell(columnIndex);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        Comment headerComment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(comment);
        headerComment.setString(str);
        headerCell.setCellComment((XSSFComment) headerComment);
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

}
