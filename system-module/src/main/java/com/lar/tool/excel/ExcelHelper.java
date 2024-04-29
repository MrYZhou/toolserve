package com.lar.tool.excel;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Scope("prototype")
@Component
public class ExcelHelper {
    boolean initPre = false;
    boolean initPost = false;
    // excel参数
    ExportParams exportParams;
    // 导出字段信息
    List<ExcelExportEntity> entities;
    @Autowired
    private ApplicationContext applicationContext;
    private Workbook workbook;
    private List<ExcelFunction> preHandleFunctions = new ArrayList<>();
    private List<ExcelFunction> postHandleFunctions = new ArrayList<>();
    private Map<String, Object> extraParams;

    public void init(ExportParams exportParams, List<ExcelExportEntity> entities) {
        this.exportParams = exportParams;
        this.entities = entities;
    }

    public void init(Workbook workbook, ExportParams exportParams, List<ExcelExportEntity> entities) {
        this.workbook = workbook;
        this.exportParams = exportParams;
        this.entities = entities;
    }

    public void init(Workbook workbook, ExportParams exportParams, List<ExcelExportEntity> entities, Map<String, Object> extraParams) {
        this.workbook = workbook;
        this.exportParams = exportParams;
        this.entities = entities;
        this.extraParams = extraParams;
    }

    private void initPreHandle() {
        Collection<ExcelPreHandle> handles = applicationContext.getBeansOfType(ExcelPreHandle.class).values();
        handles.forEach(handle -> addPreHandle(handle::execute));
        this.initPre = true;
    }

    private void initPostHandle() {
        Collection<ExcelPostHandle> handles = applicationContext.getBeansOfType(ExcelPostHandle.class).values();
        handles.forEach(handle -> addPostHandle(handle::execute));
        this.initPost = true;
    }

    public void doPreHandle() {
        if (!initPre) {
            this.initPreHandle();
        }
        preHandleFunctions.forEach(item -> item.apply(this, this.extraParams));
    }

    public void doPostHandle() {
        if (!initPost) {
            this.initPostHandle();
        }
        postHandleFunctions.forEach(item -> item.apply(this, new HashMap<>()));
    }

    public void addPreHandle(ExcelFunction... functions) {
        Collections.addAll(preHandleFunctions, functions);
    }

    public void addPostHandle(ExcelFunction... functions) {
        Collections.addAll(postHandleFunctions, functions);
    }
}
