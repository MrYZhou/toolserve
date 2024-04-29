package com.lar.tool.excel.handle;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.lar.tool.excel.ExcelHelper;
import com.lar.tool.excel.ExcelPreHandle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * excel添加批注
 */
@Component
public class ExcelCommentHandle implements ExcelPreHandle {

    /**
     * workbook增加标题批注
     *
     * @param workbook    工作簿
     * @param sheetName   工作表名称
     * @param columnIndex 列索引,从0开始
     * @param comment     批注内容
     */
    public void addComment(Workbook workbook, String sheetName, int columnIndex, String comment) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row headerRow = sheet.getRow(0);
        Cell headerCell = headerRow.getCell(columnIndex);
        // 判断是否存在批注
        Comment cellComment = headerCell.getCellComment();
        if (!Objects.isNull(cellComment)) {
            headerCell.setCellComment(null);
        }
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        Comment headerComment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(comment);
        headerComment.setString(str);
        headerCell.setCellComment(headerComment);
    }

    @Override
    public void execute(ExcelHelper data, Map<String, Object> params) {
        List<ExcelExportEntity> list = data.getEntities();

        System.out.println(111);
    }
}
