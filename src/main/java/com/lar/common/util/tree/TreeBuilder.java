package com.lar.common.util.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TreeBuilder<T extends TreeNode> {

    public List<T> buildTree(List<T> nodes) {
        Map<String, T> nodeMap = new HashMap<>();
        List<T> rootNodes = new ArrayList<>();

        // 第一次遍历：将所有节点存入map
        for (T node : nodes) {
            nodeMap.put(node.getId(), node);
        }

        // 第二次遍历：建立父子关系
        for (T node : nodes) {
            String parentId = node.getParentId();
            if (Objects.equals(parentId, "0")) {
                rootNodes.add(node);  // 根节点
            } else {
                T parent = nodeMap.get(parentId);
                if (parent != null) {
                    parent.addChild(node);
                }
            }
        }
        // 对所有层级的节点按sort排序
        sortTree(rootNodes);
        return rootNodes;
    }

    private void sortTree(List<T> nodes) {
        if (nodes == null) return;

        // 当前层级排序
        nodes.sort(Comparator.comparingInt(T::getSort));

        // 递归排序子节点
        for (T node : nodes) {
            sortTree((List<T>) node.getChildren());
        }
    }
}
