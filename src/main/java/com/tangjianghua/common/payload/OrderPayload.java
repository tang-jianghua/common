package com.tangjianghua.common.payload;

import java.util.List;

public class OrderPayload {

    /**
     * 1: 全单打印 2：分类打印
     * 必填
     */
    private Byte printUsage;
    /**
     * 商户付费模式
     * 必填
     */
    private Integer payMode;
    /**
     * 是否一刀一切（默认是0，不一刀一切）
     */
    private Short sliceGoods = 0;
    /**
     * 打印机用途
     */
    private Short usage = -1;
    /**
     * 小票标题
     */
    private String title;
    /**
     * 桌位号
     */
    private String tcNo;
    /**
     * 桌位名称
     */
    private String tcName = "";
    /**
     * 商户名称
     */
    private String merName = "";
    /**
     * 流水号
     */
    private String serialNum;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 下单时间
     */
    private String orderTs;
    /**
     * 订单备注
     */
    private String orderMemo = "";
    /**
     * 菜品List
     */
    private List<OrderGoodsPayload> orderGoodsList;
    /**
     * 菜品总数量
     */
    private Integer allGoodsNum;
    /**
     * 菜品总价
     */
    private Long allGoodsAmt;
    /**
     * 优惠合计
     */
    private Long discount;
    /**
     * 合计应收
     */
    private Long shouldReceive;
    /**
     * 合计实收
     */
    private Long actualReceive;
    /**
     * 交易类型
     */
    private String transType;
    /**
     * 交易订单号
     */
    private String transOrderNo;
    /**
     * 交易时间
     */
    private String transTs;
    /**
     * 二维码内容
     */
    private String qrContent;
    /**
     * 分类打印的菜品分类名称
     */
    private List<String> categoryPrintList;

    /**
     * 退菜原因
     */
    private String returnReason;

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public Short getUsage() {
        return usage;
    }

    public void setUsage(Short usage) {
        this.usage = usage;
    }

    public Short getSliceGoods() {
        return sliceGoods;
    }

    public void setSliceGoods(Short sliceGoods) {
        this.sliceGoods = sliceGoods;
    }

    public List<String> getCategoryPrintList() {
        return categoryPrintList;
    }

    public void setCategoryPrintList(List<String> categoryPrintList) {
        this.categoryPrintList = categoryPrintList;
    }

    public Byte getPrintUsage() {
        return printUsage;
    }

    public void setPrintUsage(Byte printUsage) {
        this.printUsage = printUsage;
    }

    public String getTcName() {
        return tcName;
    }

    public void setTcName(String tcName) {
        this.tcName = tcName;
    }

    public Integer getPayMode() {
        return payMode;
    }

    public void setPayMode(Integer payMode) {
        this.payMode = payMode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTcNo() {
        return tcNo;
    }

    public void setTcNo(String tcNo) {
        this.tcNo = tcNo;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(String orderTs) {
        this.orderTs = orderTs;
    }

    public String getOrderMemo() {
        return orderMemo;
    }

    public void setOrderMemo(String orderMemo) {
        this.orderMemo = orderMemo;
    }

    public List<OrderGoodsPayload> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<OrderGoodsPayload> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public Integer getAllGoodsNum() {
        return allGoodsNum;
    }

    public void setAllGoodsNum(Integer allGoodsNum) {
        this.allGoodsNum = allGoodsNum;
    }

    public Long getAllGoodsAmt() {
        return allGoodsAmt;
    }

    public void setAllGoodsAmt(Long allGoodsAmt) {
        this.allGoodsAmt = allGoodsAmt;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getShouldReceive() {
        return shouldReceive;
    }

    public void setShouldReceive(Long shouldReceive) {
        this.shouldReceive = shouldReceive;
    }

    public Long getActualReceive() {
        return actualReceive;
    }

    public void setActualReceive(Long actualReceive) {
        this.actualReceive = actualReceive;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getTransTs() {
        return transTs;
    }

    public void setTransTs(String transTs) {
        this.transTs = transTs;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }


    @Override
    public String toString() {
        return "OrderPayload{" + "printUsage=" + printUsage + ", payMode=" + payMode + ", sliceGoods=" + sliceGoods + ", title='" + title + '\'' + ", tcNo='" + tcNo + '\'' + ", tcName='" + tcName + '\'' + ", merName='" + merName + '\'' + ", serialNum='" + serialNum + '\'' + ", orderId='" + orderId + '\'' + ", orderTs='" + orderTs + '\'' + ", orderMemo='" + orderMemo + '\'' + ", orderGoodsList=" + orderGoodsList + ", allGoodsNum=" + allGoodsNum + ", allGoodsAmt=" + allGoodsAmt + ", discount=" + discount + ", shouldReceive=" + shouldReceive + ", actualReceive=" + actualReceive + ", transType='" + transType + '\'' + ", transOrderNo='" + transOrderNo + '\'' + ", transTs='" + transTs + '\'' + ", qrContent='" + qrContent + '\'' + ", categoryPrintList=" + categoryPrintList + '}';
    }
}