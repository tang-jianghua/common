package com.tangjianghua.common.util;

import com.tangjianghua.common.payload.OrderGoodsPayload;
import com.tangjianghua.common.payload.OrderPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderReturnPayloadUtil extends PrintSuite {

	private static Logger log = LoggerFactory.getLogger(OrderReturnPayloadUtil.class);

	private ByteBuffer byteBuffer;

	private OrderPayload orderPayload;

	public OrderReturnPayloadUtil(ByteBuffer byteBuffer, OrderPayload orderPayload) {
		super(byteBuffer);
		this.byteBuffer = byteBuffer;
		this.orderPayload = orderPayload;
	}

	public byte[] getPayload() {
		//初始化打印小票格式
		initTicketFormat();
		//居中对齐
		setHorizontalAlign((byte) 0x01);
		//字符24点阵倍宽倍高
		setFontSize(false,true,true,false,false);
		int payloadLength = 0;
		this.byteBuffer.put("退菜单".getBytes());
		payloadLength += "退菜单".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;
		//恢复字符大小
		setFontSize(false,false,false,false,false);
		//居左对齐
		setHorizontalAlign((byte) 0x00);
		this.byteBuffer.put("桌位名称：".getBytes());
		payloadLength += "桌位名称：".getBytes().length;
		//字符倍宽倍高加粗
		setFontSize(false,true,true,true,false);
		this.byteBuffer.put(this.orderPayload.getTcName().getBytes());
		payloadLength += this.orderPayload.getTcName().getBytes().length;
		//恢复字符大小
		setFontSize(false,false,false,false,false);
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;
		setLineSpace((byte) 0x12);
		String merChantName = this.orderPayload.getMerName() == null ? "" : this.orderPayload.getMerName();
		this.byteBuffer.put("商户名称：".getBytes());
		payloadLength += "商户名称：".getBytes().length;
		this.byteBuffer.put(merChantName.getBytes());
		payloadLength += merChantName.getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;

		String orderTs = this.orderPayload.getOrderTs() == null ? "" : this.orderPayload.getOrderTs();
		this.byteBuffer.put("下单时间：".getBytes());
		payloadLength += "下单时间：".getBytes().length;
		this.byteBuffer.put(orderTs.getBytes());
		payloadLength += orderTs.getBytes().length;
		this.byteBuffer.put("\n".getBytes());
		payloadLength += "\n".getBytes().length;

		String orderMemo = this.orderPayload.getOrderMemo() == null ? "" : this.orderPayload.getOrderMemo();
		this.byteBuffer.put("订单备注：".getBytes());
		payloadLength += "订单备注：".getBytes().length;
		setFontSize(true,true,true,true,false);
		this.byteBuffer.put(orderMemo.getBytes());
		payloadLength += orderMemo.getBytes().length;
		setFontSize(false,false,false,false,false);
		this.byteBuffer.put("\n\n".getBytes());
		payloadLength += "\n\n".getBytes().length;

		//要打印的菜品
		List<OrderGoodsPayload> orderGoodsList = this.orderPayload.getOrderGoodsList();

		if (!orderGoodsList.isEmpty()) {//如果要打印的菜品不为空

			Map<String, List<OrderGoodsPayload>> classifyGoodsMap = orderGoodsList.stream().collect(Collectors.groupingBy(OrderGoodsPayload::getCateName));

			Iterator<Map.Entry<String, List<OrderGoodsPayload>>> iterator = classifyGoodsMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, List<OrderGoodsPayload>> next = iterator.next();
				String cateName = next.getKey();
				printCategoryName(cateName);
				this.byteBuffer.put("\n".getBytes());
				payloadLength += "\n".getBytes().length;
				this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("菜品", (byte) 16).getBytes());
				payloadLength += StringUtil.getLeftAbsWidthStr("菜品", (byte) 16).getBytes().length;
				this.byteBuffer.put("数量".getBytes());
				payloadLength += "数量".getBytes().length;
				setDefaultLeftMargin();
				this.byteBuffer.put("\n———————————————\n".getBytes());
				payloadLength += "\n———————————————\n".getBytes().length;

				int perCateGoodsNum = 0;
				List<OrderGoodsPayload> tempOrderGoodsPayloadList = next.getValue();
				OrderGoodsPayload tempOrderGoodsPayload;
				if(!tempOrderGoodsPayloadList.isEmpty()){
					setLineSpace((byte) 0x06);
					setFontSize(false,true,true,true,false);
					for (int k = 0; k < tempOrderGoodsPayloadList.size(); k++) {
						tempOrderGoodsPayload = tempOrderGoodsPayloadList.get(k);
						String goodsName = tempOrderGoodsPayload.getGdName();
						int goodsNum = tempOrderGoodsPayload.getGdNum();
						perCateGoodsNum += goodsNum;
						this.byteBuffer.put(StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes());
						payloadLength += StringUtil.getLeftAbsWidthStr(goodsName, (byte) 8).getBytes().length;
						this.byteBuffer.put(("-" + goodsNum).getBytes());
						payloadLength += ("-" + goodsNum).getBytes().length;
						this.byteBuffer.put("\n".getBytes());
						payloadLength += "\n".getBytes().length;
					}
					setDefaultLineSpace();
					setFontSize(false,false,false,false,false);
				}

				this.byteBuffer.put("———————————————\n".getBytes());
				payloadLength += "———————————————\n".getBytes().length;
				this.byteBuffer.put(StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes());
				payloadLength += StringUtil.getLeftAbsWidthStr("小   计：", (byte) 16).getBytes().length;
				this.byteBuffer.put(("-"+perCateGoodsNum).getBytes());
				payloadLength +=("-"+perCateGoodsNum).getBytes().length;
				this.byteBuffer.put("\n\n\n".getBytes());
				payloadLength += "\n\n\n".getBytes().length;
			}
		}
		this.byteBuffer.put("———————————————\n".getBytes());
		payloadLength += "———————————————\n".getBytes().length;
		this.byteBuffer.put("退菜原因：".getBytes());
		payloadLength += "退菜原因：".getBytes().length;

		String returnReason = this.orderPayload.getReturnReason() == null ? "" : this.orderPayload.getReturnReason();
		this.byteBuffer.put(returnReason.getBytes());
		payloadLength += returnReason.getBytes().length;
		this.byteBuffer.put("\n\n\n\n\n\n\n\n".getBytes());
		payloadLength += "\n\n\n\n\n\n\n\n".getBytes().length;
		log.info("报文实际字符字节长度：" + payloadLength);
		int writePos = this.byteBuffer.position();
		byte[] data = new byte[writePos];
		this.byteBuffer.flip();
		this.byteBuffer.get(data, 0, writePos);
		this.byteBuffer.clear();
		return data;
	}
}
