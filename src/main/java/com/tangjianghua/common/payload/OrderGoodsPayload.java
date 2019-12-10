package com.tangjianghua.common.payload;

public class OrderGoodsPayload {

    /**
     * 菜品分类Id
     */
    private String cateId;
    /**
     * 菜品分类名称
     */
    private String cateName;
    /**
     * 菜品名称
     */
    private String gdName;
    /**
     * 菜品数量
     */
    private Integer gdNum;
    /**
     * 菜品单价
     */
    private Long gdAmt;

    /**
     * 单一种类菜品点餐备注
     */
    private String ordermemo;

    public String getOrdermemo() {
        return ordermemo;
    }

    public void setOrdermemo(String ordermemo) {
        this.ordermemo = ordermemo;
    }

    public String getGdName() {
        return gdName;
    }

    public void setGdName(String gdName) {
        this.gdName = gdName;
    }

    public Integer getGdNum() {
        return gdNum;
    }

    public void setGdNum(Integer gdNum) {
        this.gdNum = gdNum;
    }

    public Long getGdAmt() {
        return gdAmt;
    }

    public void setGdAmt(Long gdAmt) {
        this.gdAmt = gdAmt;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}