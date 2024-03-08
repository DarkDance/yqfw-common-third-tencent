package cn.jzyunqi.common.model.tree;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.utils.TreeUtilPlus;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

class TreePathStrategyTest {

    TreePathStrategy<TreePathFeatureIns, Long> treePathStrategy = new TreePathStrategy<>(4);

    AtomicLong atomicLong = new AtomicLong(100);

    //根节点的父节点，这是一个虚拟节点，因此框架会将id设为0，深度为-1，路径为/
    TreePathFeatureIns defaultParent = TreePathFeatureIns.builder().build();

    @Test
    void rootMoveDown() {
        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(2L).sequence(5).parentId(0L).depth(0).path("/2/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingBottom(dropToNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, defaultParent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(1L).sequence(6).parentId(0L).depth(0).path("/1/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(child.getId()).sequence(child.getSequence()).parentId(child.getParentId()).depth(dropToNode.getDepth() + child.getDepth())
                .path("/1/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void rootMoveUp() {

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(2L).sequence(5).parentId(0L).depth(0).path("/2/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);

        //移动根节点到指定兄弟节点上方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), true, defaultParent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(1L).sequence(4).parentId(0L).depth(0).path("/1/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(child.getId()).sequence(child.getSequence()).parentId(child.getParentId()).depth(dropToNode.getDepth() + child.getDepth())
                .path("/1/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void rootMoveInOtherDown() {

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childDepth = child.getDepth();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(2L).sequence(5).parentId(0L).depth(0).path("/2/").build();

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(3L).sequence(5).parentId(parent.getId()).depth(1).path("/2/3/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingBottom(dropToNode);

        //移动根节点到其它节点里
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, parent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("parentNode : " + parent);
        System.out.println("    dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("    dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(1L).sequence(6).parentId(2L).depth(1).path("/2/1/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(dropToNode.getDepth() + childDepth)
                .path("/2/1/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void rootMoveInOtherUp() {

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childDepth = child.getDepth();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(2L).sequence(5).parentId(0L).depth(0).path("/2/").build();

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(3L).sequence(5).parentId(parent.getId()).depth(1).path("/2/3/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到其它节点里
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), true, parent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("parentNode : " + parent);
        System.out.println("    dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("    dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(1L).sequence(4).parentId(2L).depth(1).path("/2/1/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(dropToNode.getDepth() + childDepth)
                .path("/2/1/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void nodeMoveDown() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(2L).sequence(1).parentId(parent.getId()).depth(1).path("/1/2/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childDepth = child.getDepth();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(3L).sequence(5).parentId(parent.getId()).depth(1).path("/1/3/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingBottom(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, parent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(2L).sequence(6).parentId(parent.getId()).depth(1).path("/1/2/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(childDepth).path("/1/2/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void nodeMoveUp() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(2L).sequence(1).parentId(parent.getId()).depth(1).path("/1/2/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childDepth = child.getDepth();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(3L).sequence(5).parentId(parent.getId()).depth(1).path("/1/3/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), true, parent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(2L).sequence(4).parentId(parent.getId()).depth(1).path("/1/2/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(childDepth).path("/1/2/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void nodeMoveRoot() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(2L).sequence(1).parentId(parent.getId()).depth(1).path("/1/2/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(3L).sequence(5).parentId(0L).depth(0).path("/3/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), true, defaultParent, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(2L).sequence(4).parentId(0L).depth(0).path("/2/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(1).path("/2/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void nodeMoveNode() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();

        TreePathFeatureIns dragNode = TreePathFeatureIns.builder().id(2L).sequence(1).parentId(parent.getId()).depth(1).path("/1/2/").build();
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);
        TreePathFeatureIns child = dragNodeChildList.get(0);
        Long childId = child.getId();
        Integer childSequence = child.getSequence();
        Long childParent = child.getParentId();

        TreePathFeatureIns parent2 = TreePathFeatureIns.builder().id(3L).sequence(2).parentId(0L).depth(0).path("/3/").build();
        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(4L).sequence(5).parentId(parent2.getId()).depth(1).path("/3/4/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), true, parent2, dropToNodeSiblingList);
        } catch (BusinessException e) {
            System.out.println("move failed!!  " + e.getCode());
        }

        System.out.println("dragNode : " + dragNode);
        dragNodeChildList.forEach(treePathFeatureIns -> System.out.println("    dragNodeChildList : " + treePathFeatureIns));
        System.out.println("dropToNode : " + dropToNode);
        dropToNodeSiblingList.forEach(treePathFeatureIns -> System.out.println("dropToNodeSiblingList : " + treePathFeatureIns));

        TreePathFeatureIns expect = TreePathFeatureIns.builder().id(2L).sequence(4).parentId(3L).depth(1).path("/3/2/").build();
        Assertions.assertEquals(expect, dragNode);

        TreePathFeatureIns expect2 = TreePathFeatureIns.builder().id(childId).sequence(childSequence).parentId(childParent).depth(2).path("/3/2/" + child.getId() + "/").build();
        Assertions.assertEquals(expect2, child);
    }

    @Test
    void nodeMoveError() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> childList = getChild(parent);
        TreePathFeatureIns dragNode = childList.get(0);
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns parent2 = TreePathFeatureIns.builder().id(2L).sequence(2).parentId(0L).depth(2).path("/2/").build();
        TreePathFeatureIns sub = TreePathFeatureIns.builder().id(3L).sequence(1).parentId(2L).depth(3).path("/2/3/").build();
        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(4L).sequence(1).parentId(3L).depth(4).path("/2/3/4/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, sub, dropToNodeSiblingList);
            Assertions.fail();
        } catch (BusinessException e) {
            Assertions.assertEquals("common_error_max_depth_limit", e.getCode());
        }
    }

    @Test
    void nodeMoveError1() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> childList = getChild(parent);
        TreePathFeatureIns dragNode = childList.get(0);
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns parent2 = TreePathFeatureIns.builder().id(2L).sequence(2).parentId(0L).depth(0).path("/2/").build();
        TreePathFeatureIns sub = TreePathFeatureIns.builder().id(3L).sequence(1).parentId(2L).depth(1).path("/2/3/").build();
        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(4L).sequence(1).parentId(3L).depth(2).path("/2/3/4/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, dragNode, dropToNodeSiblingList);
            Assertions.fail();
        } catch (BusinessException e) {
            Assertions.assertEquals("common_error_parent_cant_be_self", e.getCode());
        }
    }

    @Test
    void nodeMoveError2() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> childList = getChild(parent);
        TreePathFeatureIns dragNode = childList.get(0);
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns a = dragNodeChildList.get(0);
        List<TreePathFeatureIns> b = getSiblingTop(a);
        TreePathFeatureIns dropToNode = b.get(0);
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, a, dropToNodeSiblingList);
            Assertions.fail();
        } catch (BusinessException e) {
            Assertions.assertEquals("common_error_parent_cant_be_child", e.getCode());
        }
    }

    @Test
    void nodeMoveError3() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> childList = getChild(parent);
        TreePathFeatureIns dragNode = childList.get(0);
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        TreePathFeatureIns dropToNode = TreePathFeatureIns.builder().id(2L).sequence(2).parentId(0L).depth(0).path("/2/").build();
        List<TreePathFeatureIns> dropToNodeSiblingList = getSiblingTop(dropToNode);
        dropToNodeSiblingList.get(0).setParentId(1L);

        //移动根节点到指定兄弟节点下方
        try {
            treePathStrategy.treeMove(dragNode, dragNodeChildList, dropToNode.getSequence(), false, dropToNode, dropToNodeSiblingList);
            Assertions.fail();
        } catch (BusinessException e) {
            Assertions.assertEquals("common_error_sibling_parent_not_match", e.getCode());
        }
    }

    @Test
    void hasCycleTest() {
        TreePathFeatureIns parent = TreePathFeatureIns.builder().id(1L).sequence(1).parentId(0L).depth(0).path("/1/").build();
        List<TreePathFeatureIns> childList = getChild(parent);
        TreePathFeatureIns dragNode = childList.get(0);
        List<TreePathFeatureIns> dragNodeChildList = getChild(dragNode);

        List<TreePathFeatureIns> list = new ArrayList<>();
        list.add(parent);
        list.addAll(childList);
        list.addAll(dragNodeChildList);

        Assertions.assertFalse(TreeUtilPlus.hasCycle(list));

        TreePathFeatureIns cycleNode = childList.get(0);
        parent.setParentId(cycleNode.getId());

        Assertions.assertTrue(TreeUtilPlus.hasCycle(list));
    }

    @NotNull
    private List<TreePathFeatureIns> getChild(TreePathFeatureIns node) {
        List<TreePathFeatureIns> childList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Long id = atomicLong.getAndIncrement();
            childList.add(TreePathFeatureIns.builder().id(id).sequence(i + 1).parentId(node.getId()).depth(node.getDepth() + 1).path(node.getPath() + String.format("%d/", id)).build());
        }
        return childList;
    }

    @NotNull
    private List<TreePathFeatureIns> getSiblingBottom(TreePathFeatureIns node) {
        String parentPath = node.getPath().replace(String.format("/%d/", node.getId()), "");
        List<TreePathFeatureIns> childList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Long id = atomicLong.getAndIncrement();
            childList.add(TreePathFeatureIns.builder().id(id).sequence(node.getSequence() + i + 1).parentId(node.getParentId()).depth(node.getDepth()).path(parentPath + String.format("/%d/", id)).build());
        }
        return childList;
    }

    @NotNull
    private List<TreePathFeatureIns> getSiblingTop(TreePathFeatureIns node) {
        String parentPath = node.getPath().replace(String.format("/%d/", node.getId()), "");
        List<TreePathFeatureIns> childList = new ArrayList<>();

        for (int i = 4; i > 0; i--) {
            Long id = atomicLong.getAndIncrement();
            childList.add(TreePathFeatureIns.builder().id(id).sequence(node.getSequence() - i).parentId(node.getParentId()).depth(node.getDepth()).path(parentPath + String.format("/%d/", id)).build());
        }
        return childList;
    }

}