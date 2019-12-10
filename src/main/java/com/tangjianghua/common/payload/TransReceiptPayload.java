package com.tangjianghua.common.payload;

public class TransReceiptPayload {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(Long orderAmt) {
        this.orderAmt = orderAmt;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getActualPayAmt() {
        return actualPayAmt;
    }

    public void setActualPayAmt(Long actualPayAmt) {
        this.actualPayAmt = actualPayAmt;
    }

    public String getTransTs() {
        return transTs;
    }

    public void setTransTs(String transTs) {
        this.transTs = transTs;
    }

    /**
     * 小票标题
     */
    private String title;

    /**
     * 商户名称
     */
    private String merName;

    /**
     * 商户号
     */
    private String merId;

    public Byte getTransType() {
        return transType;
    }

    public void setTransType(Byte transType) {
        this.transType = transType;
    }

    /**
     * 交易类型（0：微信支付 1：支付宝 2：云闪付）
     */
    private Byte transType;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单金额
     */
    private Long orderAmt;

    /**
     * 商户优惠
     */
    private Long discount;

    /**
     * 支付金额
     */
    private Long actualPayAmt;

    /**
     * 交易时间
     */
    private String transTs;
}
