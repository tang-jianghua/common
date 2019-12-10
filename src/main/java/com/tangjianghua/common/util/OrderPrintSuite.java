package com.tangjianghua.common.util;


import com.tangjianghua.common.enums.ExceptionTypeEnum;
import com.tangjianghua.common.payload.OrderGoodsPayload;
import com.tangjianghua.common.payload.OrderPayload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPrintSuite extends PrintSuite {

	private static Logger log = LoggerFactory.getLogger(OrderPrintSuite.class);

	private ByteBuffer byteBuffer;

	private OrderPayload orderPayload;

	public OrderPrintSuite(ByteBuffer byteBuffer, OrderPayload orderPayload) {
		super(byteBuffer);
		this.byteBuffer = byteBuffer;
		this.orderPayload = orderPayload;
	}

	public byte[] generateTicketPayload() throws Exception {
		Byte printUsage = this.orderPayload.getPrintUsage();
		if (printUsage == null) {
			throw new MyRuntimeException(ExceptionTypeEnum.VO_EXCEPTION.getCode(),"未指定点餐小票的打印用途。");
		}
		Short usage = this.orderPayload.getUsage();
		//是否一刀一切
		int sliceGoods = this.orderPayload.getSliceGoods();
		if (printUsage == 2 || (printUsage == 1 && usage == 2)) {//如果明确指定分类打印或者是后厨打印机，并且全单打印
			byte[] bytes = printCategory(sliceGoods);
			if(sliceGoods == 1 && bytes.length >Constant.PAYLOAD_MAX_LENGTH){
				return  printCategory(0);
			}else{
				return bytes;
			}
		} else {
			return printAll();
		}
	}

	/**
	 * 全单打印
	 *
	 * @return
	 */
	private byte[] printAll() {
		log.info("开始打印前台菜品。。。。");

		//初始化打印小票格式
		initTicketFormat();

		//居中对齐
		setHorizontalAlign((byte) 0x01);
		//设置24号点阵
		setPrintMode((byte) 0x00);
		//字符倍宽倍高加粗
		setFontSize(false, true, true,false, false);
		int payloadLength = 0;
		this.byteBuffer.put(this.orderPayload.getTitle().getBytes());
		payloadLength = payloadLength+this.orderPayload.getTitle().getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		//设置24号点阵
		setPrintMode((byte) 0x00);
		//字符不放大
		setDoubleFontSize((byte) 0x00);
		//字符不加粗
//        bold((byte)0x00);
		//居右对齐
		setHorizontalAlign((byte) 0x02);
		setFontSize(false, true, false,false, false);
		this.byteBuffer.put(("桌位名称：").getBytes());
		payloadLength = payloadLength+"桌位名称：".getBytes().length;
		//字符加粗
		bold((byte) 0x01);
		this.byteBuffer.put(this.orderPayload.getTcName().getBytes());
		payloadLength = payloadLength+this.orderPayload.getTcName().getBytes().length;
		//设置行间距
		setLineSpace((byte) 0x12);

		//居左对齐
		setHorizontalAlign((byte) 0x00);
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;
		//字符不加粗
//        bold((byte)0x00);
		String merChantName = this.orderPayload.getMerName();
		if (StringUtils.isBlank(merChantName)) {
			merChantName = "";
		}
		this.byteBuffer.put("商户名称：".getBytes());
		payloadLength = payloadLength+"商户名称：".getBytes().length;
		this.byteBuffer.put(merChantName.getBytes());
		payloadLength = payloadLength+merChantName.getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		String serialNumStr = this.orderPayload.getSerialNum();
		int serialNumValueInt = Integer.parseInt(serialNumStr);
		if (serialNumValueInt < 10000) {
			serialNumStr = String.format("%04d", serialNumValueInt);
		}
		this.byteBuffer.put("流水号：".getBytes());
		payloadLength = payloadLength+"流水号：".getBytes().length;
		this.byteBuffer.put(serialNumStr.getBytes());
		payloadLength = payloadLength+serialNumStr.getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		String orderId = this.orderPayload.getOrderId();
		if (StringUtils.isNotBlank(orderId)) {
			this.byteBuffer.put("订单号：".getBytes());
			payloadLength = payloadLength+"订单号：".getBytes().length;
			this.byteBuffer.put(orderId.getBytes());
			payloadLength = payloadLength+orderId.getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
		}
		if (StringUtils.isNotBlank(orderId)) {
			this.byteBuffer.put("下单时间：".getBytes());
			payloadLength = payloadLength+"下单时间：".getBytes().length;
			String orderTs = this.orderPayload.getOrderTs();
			this.byteBuffer.put(orderTs.getBytes());
			payloadLength = payloadLength+orderTs.getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
		}

		String orderMemo = this.orderPayload.getOrderMemo();
		if (StringUtils.isNotBlank(orderMemo)) {
			this.byteBuffer.put("订单备注：".getBytes());
			payloadLength = payloadLength+"订单备注：".getBytes().length;
			this.byteBuffer.put(orderMemo.getBytes());
			payloadLength = payloadLength+orderMemo.getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
		}

		this.byteBuffer.put("——————消费明细——————\n".getBytes());
		payloadLength = payloadLength+"——————消费明细——————\n".getBytes().length;

		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("菜品", (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("菜品", (byte) 8).getBytes().length;
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("数量", (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("数量", (byte) 8).getBytes().length;
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("价格", (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("价格", (byte) 8).getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;
		setLineSpace((byte) 0x00);
		this.byteBuffer.put("————————————————\n".getBytes());
		payloadLength = payloadLength+"————————————————\n".getBytes().length;

		setLineSpace((byte) 0x12);

		int allNum = 0;
		BigDecimal finalAllAmt = new BigDecimal(0);

		BigDecimal finalShouldReceive = new BigDecimal(0);
		BigDecimal tempActualReceive;

		Long payAmt = this.orderPayload.getActualReceive();
		if (payAmt == null) {
			tempActualReceive = new BigDecimal(0);
		} else {
			tempActualReceive = new BigDecimal(payAmt);
		}
		BigDecimal actualReceive = new BigDecimal(0);

		Long disCount = this.orderPayload.getDiscount();
		if (disCount == null) {
			disCount = 0L;
		}
		BigDecimal disAmt = new BigDecimal(disCount);
		BigDecimal finalDisAmt = disAmt.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);

		List<OrderGoodsPayload> orderGoodsList = this.orderPayload.getOrderGoodsList();
		long allPrice = 0;
		if (orderGoodsList != null && !orderGoodsList.isEmpty()) {
			for (OrderGoodsPayload orderGoods : orderGoodsList) {
				setLineSpace((byte) 0x12);

				this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(orderGoods.getGdName(), (byte) 8).getBytes());
				payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr(orderGoods.getGdName(), (byte) 8).getBytes().length;
				setLeftMargin((byte) 0x02);
				bold((byte) 1);
				this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("*" + orderGoods.getGdNum(), (byte) 8).getBytes());
				payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("*" + orderGoods.getGdNum(), (byte) 8).getBytes().length;

				setDefaultLeftMargin();
				BigDecimal price = new BigDecimal(orderGoods.getGdAmt());
				BigDecimal finalPrice = price.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
				this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("¥" + finalPrice.toString(), (byte) 8).getBytes());
				payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("¥" + finalPrice.toString(), (byte) 8).getBytes().length;
				bold((byte) 0);
				this.byteBuffer.put("\n".getBytes());
				payloadLength = payloadLength+"\n".getBytes().length;

				allPrice = allPrice + orderGoods.getGdAmt() * orderGoods.getGdNum();
			}
			allNum = this.orderPayload.getAllGoodsNum();

			BigDecimal allPriceBD = new BigDecimal(allPrice);

			Long allAmt = this.orderPayload.getAllGoodsAmt();
			if (allAmt == null) {
				finalAllAmt = allPriceBD.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
			} else {
				finalAllAmt = new BigDecimal(allAmt).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
			}

			//合计应收
			Long shouldReceiveAmt = this.orderPayload.getShouldReceive();
			BigDecimal shouldReceive;
			if (shouldReceiveAmt == null) {
				shouldReceive = allPriceBD.subtract(new BigDecimal(disCount));//减去折扣
			} else {
				shouldReceive = new BigDecimal(shouldReceiveAmt);
			}
			finalShouldReceive = shouldReceive.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);

			//实收
			actualReceive = tempActualReceive.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);

		}

		this.byteBuffer.put("————————————————\n".getBytes());
		payloadLength = payloadLength+"————————————————\n".getBytes().length;
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("小   计：", (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("小   计：", (byte) 8).getBytes().length;
		bold((byte) 0x01);
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(String.valueOf(allNum), (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr(String.valueOf(allNum), (byte) 8).getBytes().length;
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("¥" + finalAllAmt.toString(), (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("¥" + finalAllAmt.toString(), (byte) 8).getBytes().length;
		bold((byte) 0x00);
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("优惠合计：", (byte) 16).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("优惠合计：", (byte) 16).getBytes().length;
		bold((byte) 0x01);
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("¥" + finalDisAmt.toString(), (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("¥" + finalDisAmt.toString(), (byte) 8).getBytes().length;
		bold((byte) 0x00);
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("合计应收：", (byte) 16).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("合计应收：", (byte) 16).getBytes().length;
		bold((byte) 0x01);
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("¥" + finalShouldReceive.toString(), (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("¥" + finalShouldReceive.toString(), (byte) 8).getBytes().length;
		bold((byte) 0x00);
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("合计实收：", (byte) 16).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("合计实收：", (byte) 16).getBytes().length;
		bold((byte) 0x01);
		this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("¥" + actualReceive.toString(), (byte) 8).getBytes());
		payloadLength = payloadLength+StringUtil.getLeftAbsWidthStr("¥" + actualReceive.toString(), (byte) 8).getBytes().length;
		bold((byte) 0x00);
		this.byteBuffer.put("\n".getBytes());
		payloadLength = payloadLength+"\n".getBytes().length;

		int payMode = this.orderPayload.getPayMode();

		if (payMode != 0) {//后支付

			String qrContent = this.orderPayload.getQrContent();

			this.byteBuffer.put("——————付款码———————\n".getBytes());
			payloadLength = payloadLength+"——————付款码———————\n".getBytes().length;
			qr(qrContent).qrMinCellWidth((byte) 0x08);
			this.byteBuffer.put("注：本卷为下单凭证\n\n".getBytes());
			payloadLength = payloadLength+"注：本卷为下单凭证\n\n".getBytes().length;

			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
		} else {//先支付
			this.byteBuffer.put("——————支付明细——————\n".getBytes());
			String tradeType, tradeNo, traderTime;
			tradeType = this.orderPayload.getTransType();//交易类型
			tradeNo = this.orderPayload.getTransOrderNo();//订单号
			traderTime = this.orderPayload.getTransTs();//交易时间

			if (StringUtils.isNotBlank(tradeType)) {
				this.byteBuffer.put("交易类型：".getBytes());
				payloadLength = payloadLength+"交易类型：".getBytes().length;
				this.byteBuffer.put(tradeType.getBytes());
				payloadLength = payloadLength+tradeType.getBytes().length;
				this.byteBuffer.put("\n".getBytes());
				payloadLength = payloadLength+"\n".getBytes().length;
			}

			if (StringUtils.isNotBlank(tradeNo)) {
				this.byteBuffer.put("订 单 号：".getBytes());
				payloadLength = payloadLength+"订 单 号：".getBytes().length;
				this.byteBuffer.put(tradeNo.getBytes());
				payloadLength = payloadLength+tradeNo.getBytes().length;
				this.byteBuffer.put("\n".getBytes());
				payloadLength = payloadLength+"\n".getBytes().length;
			}

			if (StringUtils.isNotBlank(traderTime)) {
				this.byteBuffer.put("交易时间：".getBytes());
				payloadLength = payloadLength+"交易时间：".getBytes().length;
				this.byteBuffer.put(traderTime.getBytes());
				payloadLength = payloadLength+traderTime.getBytes().length;
				this.byteBuffer.put("\n".getBytes());
				payloadLength = payloadLength+"\n".getBytes().length;
				this.byteBuffer.put("\n".getBytes());
				payloadLength = payloadLength+"\n".getBytes().length;
			}


			this.byteBuffer.put("注：本卷为交易凭证\n\n".getBytes());
			payloadLength = payloadLength+"注：本卷为交易凭证\n\n".getBytes().length;

			this.byteBuffer.put("\n".getBytes());
			payloadLength = payloadLength+"\n".getBytes().length;
			setFontSize(false, false, false,false, false);

		}

		initTicketFormat();
		log.info("报文实际字符字节长度："+payloadLength);
		return getPayload();

	}

	/**
	 * 分类打印
	 *
	 * @return
	 */
	private byte[] printCategory(int sliceGoods) throws Exception {
		log.info("开始打印后厨菜品。。。。");
		//初始化打印小票格式
		initTicketFormat();
		//居中对齐
		setHorizontalAlign((byte) 0x01);
		//设置24号点阵
		setPrintMode((byte) 0x00);
		//字符倍宽倍高加粗
		setFontSize(false, true, true,false, false);
		int payloadLength = 0;
		this.byteBuffer.put("点菜单".getBytes());
		payloadLength +="点菜单".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength +="\n".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength +="\n".getBytes().length;
		//恢复字符大小
		setFontSize(false,false,false,false,false);
		//居右对齐
		setHorizontalAlign((byte) 0x02);
		this.byteBuffer.put(("桌位名称：").getBytes());
		payloadLength +="桌位名称：".getBytes().length;
		setFontSize(false, true, true,true, false);
		this.byteBuffer.put(this.orderPayload.getTcName().getBytes());
		payloadLength +=this.orderPayload.getTcName().getBytes().length;
		setFontSize(false, false, false,false, false);
		this.byteBuffer.put("\n".getBytes());
		payloadLength +="\n".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength +="\n".getBytes().length;
		//居左对齐
		setHorizontalAlign((byte) 0x00);
		//设置行间距
		setLineSpace((byte) 0x12);
		String merChantName = this.orderPayload.getMerName()==null?"":this.orderPayload.getMerName();
		this.byteBuffer.put("商户名称：".getBytes());
		payloadLength +="商户名称：".getBytes().length;
		this.byteBuffer.put(merChantName.getBytes());
		payloadLength +=merChantName.getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength +="\n".getBytes().length;

		String orderId = this.orderPayload.getOrderId();
		if (StringUtils.isNotBlank(orderId)) {
			this.byteBuffer.put("下单时间：".getBytes());
			payloadLength +="下单时间：".getBytes().length;
			String orderTs = this.orderPayload.getOrderTs();
			this.byteBuffer.put(orderTs.getBytes());
			payloadLength +=orderTs.getBytes().length;
			this.byteBuffer.put("\n".getBytes());
			payloadLength +="\n".getBytes().length;
		}

		String orderMemo = this.orderPayload.getOrderMemo();
		if (StringUtils.isNotBlank(orderMemo)) {
			this.byteBuffer.put("订单备注：".getBytes());
			payloadLength +="订单备注：".getBytes().length;
			setFontSize(true, true, true,true, false);
			this.byteBuffer.put(orderMemo.getBytes());
			payloadLength +=orderMemo.getBytes().length;
			setFontSize(false, false, false,false, false);
			this.byteBuffer.put("\n\n".getBytes());
			payloadLength +="\n\n".getBytes().length;
		}

		Short usage = this.orderPayload.getUsage();
		if (usage == null || usage == -1) {
			log.error("请指定机具的打印用途。");
			throw new MyRuntimeException(ExceptionTypeEnum.VO_EXCEPTION.getCode(),"请指定机具的打印用途。");
		}

		//要打印的菜品
		List<OrderGoodsPayload> orderGoodsList = this.orderPayload.getOrderGoodsList();

		if (!orderGoodsList.isEmpty()) {//如果要打印的菜品不为空

			Map<String, List<OrderGoodsPayload>> classifyGoodsMap = orderGoodsList.stream().collect(Collectors.groupingBy(OrderGoodsPayload::getCateId));

			Iterator<String> classifyKeys = classifyGoodsMap.keySet().iterator();
			//分类打印要打印的分类列表
			List<String> categoryPrintList = this.orderPayload.getCategoryPrintList();

			//如果是共用打印机或者是后厨打印机，并且是全单打印
			if (usage == 0 || (usage == 2 && this.orderPayload.getPrintUsage() == 1)) {
				log.warn("分类打印未设置要打印的分类");
				while (classifyKeys.hasNext()) {
					setDefaultLineSpace();
					String key = classifyKeys.next();
					List<OrderGoodsPayload> tempOrderGoodsPayloadList = classifyGoodsMap.get(key);
					String cateName = tempOrderGoodsPayloadList.get(0).getCateName();
					printCategoryName(cateName);
					this.byteBuffer.put("\n".getBytes());
					payloadLength +="\n".getBytes().length;
					this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("菜品", (byte) 16).getBytes());
					payloadLength +=StringUtil.getLeftAbsWidthStr("菜品", (byte) 16).getBytes().length;
					this.byteBuffer.put("数量".getBytes());
					payloadLength +="数量".getBytes().length;
					setDefaultLeftMargin();
					this.byteBuffer.put("\n———————————————\n".getBytes());
					payloadLength +="\n———————————————\n".getBytes().length;

					int perCateGoodsNum = 0;
					setLineSpace((byte) 0x00);

					OrderGoodsPayload tempOrderGoodsPayload;
					if(!tempOrderGoodsPayloadList.isEmpty()){
						setLineSpace((byte)0x12);
						for (int k = 0; k < tempOrderGoodsPayloadList.size(); k++) {
							tempOrderGoodsPayload = tempOrderGoodsPayloadList.get(k);
							setFontSize(false, true, true,true, false);
							String goodsName = tempOrderGoodsPayload.getGdName();
							int goodsNum = tempOrderGoodsPayload.getGdNum();
							perCateGoodsNum += goodsNum;
							this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes());
							payloadLength +=StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes().length;
							this.byteBuffer.put("*".getBytes());
							payloadLength +="*".getBytes().length;
							this.byteBuffer.put(String.valueOf(goodsNum).getBytes());
							payloadLength +=String.valueOf(goodsNum).getBytes().length;
							this.byteBuffer.put("\n".getBytes());
							payloadLength +="\n".getBytes().length;
							if(!tempOrderGoodsPayload.getOrdermemo().isEmpty()){
								setFontSize(true,true,true,true,false);
								this.byteBuffer.put("菜品备注：".getBytes());
								payloadLength +="菜品备注：".getBytes().length;
								this.byteBuffer.put(tempOrderGoodsPayload.getOrdermemo().getBytes());
								payloadLength +=tempOrderGoodsPayload.getOrdermemo().getBytes().length;
								this.byteBuffer.put("\n".getBytes());
								payloadLength +="\n".getBytes().length;
							}
							if(k !=tempOrderGoodsPayloadList.size()-1){
								this.byteBuffer.put("\n".getBytes());
								payloadLength +="\n".getBytes().length;
							}
							if (sliceGoods == 1 && (k != tempOrderGoodsPayloadList.size() - 1)) {
								this.byteBuffer.put("———————————————\n".getBytes());
								payloadLength +="———————————————\n".getBytes().length;
							}
						}
						setFontSize(false, false, false,false, false);
						setLineSpace((byte)0x00);
					}
					this.byteBuffer.put("———————————————\n".getBytes());
					payloadLength +="———————————————\n".getBytes().length;
					this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes());
					payloadLength +=StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes().length;
					this.byteBuffer.put(String.valueOf(perCateGoodsNum).getBytes());
					payloadLength +=String.valueOf(perCateGoodsNum).getBytes().length;
					this.byteBuffer.put("\n\n\n".getBytes());
					payloadLength +="\n\n\n".getBytes().length;
				}
			} else {
				boolean isContainsCate = false;//打印分类列表是否包含要打印的菜品所对应的分类
				while (classifyKeys.hasNext()) {
					setDefaultLineSpace();
					String key = classifyKeys.next();
					List<OrderGoodsPayload> tempOrderGoodsPayloadList = classifyGoodsMap.get(key);
					String cateName = tempOrderGoodsPayloadList.get(0).getCateName();
					if (categoryPrintList.contains(cateName)) {
						isContainsCate = true;
						printCategoryName(cateName);
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
						this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("菜品", (byte) 16).getBytes());
						payloadLength +=StringUtil.getLeftAbsWidthStr("菜品" , (byte) 16).getBytes().length;
						this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("数量", (byte) 16).getBytes());
						payloadLength +=StringUtil.getLeftAbsWidthStr("数量" , (byte) 16).getBytes().length;
						setLineSpace((byte) 0x01);
						setDefaultLeftMargin();
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
						this.byteBuffer.put("———————————————".getBytes());
						payloadLength +="———————————————".getBytes().length;
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;


						int perCateGoodsNum = 0;
						OrderGoodsPayload tempOrderGoodsPayload;
						for (int k = 0; k < tempOrderGoodsPayloadList.size(); k++) {
							tempOrderGoodsPayload = tempOrderGoodsPayloadList.get(k);
							setFontSize(false, true, true,false, false);
							setLineSpace((byte) 0x00);
							String goodsName = tempOrderGoodsPayload.getGdName();
							int goodsNum = tempOrderGoodsPayload.getGdNum();
							perCateGoodsNum += goodsNum;
							this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes());
							payloadLength +=StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes().length;
							bold((byte) 1);
							this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("*" + goodsNum, (byte) 8).getBytes());
							payloadLength +=StringUtil.getLeftAbsWidthStr("*" + goodsNum, (byte) 8).getBytes().length;
							bold((byte) 0);
							setDefaultLeftMargin();
							setFontSize(false, false, false,false, false);
							this.byteBuffer.put((byte) 0x0A);
							if (sliceGoods == 1 && (k != tempOrderGoodsPayloadList.size() - 1)) {
								this.byteBuffer.put("———————————————\n".getBytes());
								payloadLength +="———————————————\n".getBytes().length;
							}
						}
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
						this.byteBuffer.put("———————————————".getBytes());
						payloadLength +="———————————————".getBytes().length;
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
						this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes());
						payloadLength +=StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes().length;
						this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(" " + perCateGoodsNum, (byte) 16).getBytes());
						payloadLength +=StringUtil.getLeftAbsWidthStr(" " + perCateGoodsNum, (byte) 16).getBytes().length;
						setDefaultLeftMargin();

						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
						this.byteBuffer.put("\n".getBytes());
						payloadLength +="\n".getBytes().length;
					}
				}
				if (!isContainsCate) {
					this.byteBuffer.clear();
					return null;
				}
			}
		}

		this.byteBuffer.put("\n\n\n\n\n\n".getBytes());
		payloadLength +="\n\n\n\n\n\n".getBytes().length;
		log.info("报文实际字符字节长度："+payloadLength);
		return getPayload();
	}

	private byte[] getPayload() {
		int writePos = this.byteBuffer.position();
		byte[] data = new byte[writePos];
		this.byteBuffer.flip();
		this.byteBuffer.get(data, 0, writePos);
		this.byteBuffer.clear();
		return data;
	}

}
