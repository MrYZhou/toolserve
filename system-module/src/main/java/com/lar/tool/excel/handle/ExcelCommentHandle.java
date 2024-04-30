package com.lar.tool.excel.handle;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.ICommentHandler;

import com.lar.tool.excel.ExcelHelper;
import com.lar.tool.excel.ExcelPreHandle;
import org.apache.commons.collections4.CollectionUtils;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * excel添加批注
 */
@Component
public class ExcelCommentHandle implements ExcelPreHandle {
    String sheetName;
    Map<String,String> commentMap = new HashMap<>();

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
        ExportParams exportParams = data.getExportParams();

        params.put("sheetName", sheetName);
        exportParams.setCommentHandler(new CommentHandle());
        this.sheetName = exportParams.getSheetName();
        for (ExcelExportEntity export : list) {
            this.addComment(export);
        }
    }

    public void addComment(ExcelExportEntity export){
        if(!CollectionUtils.isEmpty(export.getList())){
            export.getList().forEach(this::addComment);
        }
        // 输出的标题
        String name = export.getName();
        Pattern pattern = Pattern.compile("(.*)\\((.*?)\\)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            commentMap.put(name,matcher.group(2));
        }
    }
    class CommentHandle implements ICommentHandler {
        @Override
        public String getComment(String name) {
            return commentMap.get(name);
        }
    }
}
