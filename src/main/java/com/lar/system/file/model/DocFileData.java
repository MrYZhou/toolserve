package com.lar.system.file.model;

import lombok.Data;

@Data
public class DocFileData {
    /**
     * office365 文件id
     */
    private String itemId;
    /**
     * 文件类型：excel.word
     */
    private String fileType;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 上级文档目录id
     */
    private String parentId;
    /**
     * 操作类型：0-新增上传；1-更新上传
     */
    private Integer operateType = 0;
    /**
     * 本地文件的id
     */
    private String localFileId;

    private String keyword;
    /**
     * 文件路径
     */
    private String filePath;
}
