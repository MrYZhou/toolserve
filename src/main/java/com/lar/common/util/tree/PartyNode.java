package com.lar.common.util.tree;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartyNode implements TreeNode {
    @JSONField(name = "fid")
    private String id;
    @JSONField(name = "fparentId")
    private String parentId;
    @JSONField(name = "fgroupName")
    private String fullName;
    @JSONField(name = "fsort")
    private int sort;
    @JSONField(name = "fstatus")
    private String status;
    private List<TreeNode> children = new ArrayList<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public int getSort() {
        return sort;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public void addChild(TreeNode child) {
        this.children.add(child);
    }
}
