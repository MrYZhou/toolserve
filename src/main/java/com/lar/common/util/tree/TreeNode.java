package com.lar.common.util.tree;

import java.util.List;

public interface TreeNode {
    String getId();
    String getParentId();

    /**
     * 名称
     * @return
     */
    String getFullName();

    /**
     * 启用
     * @return
     */
    String getStatus();

    /**
     * 排序
     * @return
     */
    int getSort();
    List<? extends TreeNode> getChildren();
    void addChild(TreeNode child);
}
