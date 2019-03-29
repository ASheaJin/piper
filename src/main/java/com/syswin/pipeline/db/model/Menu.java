package com.syswin.pipeline.db.model;

public class Menu {
    private Long menuId;

    private Long parentId;

    private String name;

    private Byte isLeaf;

    private Integer sort;

    private Byte status;

    private Integer createTime;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Byte isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}