package com.tangjianghua.common.util;

import java.nio.ByteBuffer;

public class PrintSuite {

    private ByteBuffer byteBuffer;

    private PrintSuite(){}

    public PrintSuite(ByteBuffer byteBuffer){
        this.byteBuffer=byteBuffer;
    }

    /**
     * 打印分类名称
     */
    public void printCategoryName(String cateName) {
        int lineDotMatrixNum = 384;
        int cateNameDotMatrixNum = calDotMatrix(cateName);
        int equalDotMatrixNum = lineDotMatrixNum - cateNameDotMatrixNum;
        int equalCharsNum = equalDotMatrixNum / 12;
        for (int k = 0; k < equalCharsNum / 2; k++) {
            this.byteBuffer.put("=".getBytes());
        }
        this.byteBuffer.put(cateName.getBytes());
        for (int k = 0; k < equalCharsNum / 2; k++) {
            this.byteBuffer.put("=".getBytes());
        }
    }

    /**
     * 计算一个字符串所占的点阵数
     * <p>
     * 一行384点.
     * 24点阵英文字体 宽度 16点.
     * 24点阵中文字体24点.
     * 数字和字符12点阵宽度
     *
     * @param str
     * @return
     */
    public int calDotMatrix(String str) {
        if (str != null) {
            int dotMatrixNum = 0;
            char[] chars = str.toCharArray();
            for (char perChar : chars) {
                if ((perChar >= 'A' && perChar <= 'Z') || (perChar >= 'a' && perChar <= 'z')) {
                    dotMatrixNum = dotMatrixNum + 16;
                } else if (isChinese(perChar)) {
                    dotMatrixNum = dotMatrixNum + 24;
                } else {
                    dotMatrixNum = dotMatrixNum + 12;
                }
            }
            return dotMatrixNum;
        }
        return 0;
    }

    /***
     * 判断字符是否为中文
     * @param ch 需要判断的字符
     * @return 中文返回true，非中文返回false
     */
    public boolean isChinese(char ch) {
        //获取此字符的UniCodeBlock
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        //  GENERAL_PUNCTUATION 判断中文的“号
        //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
        //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 设置打印模式
     * 0：24点阵
     * 1：16点阵
     * @param align
     * @return
     */
    public PrintSuite setPrintMode(byte align) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x21);
        this.byteBuffer.put(align);
        return this;
    }

    /**
     * 设置水平对齐方式
     * 0：左对齐
     * 1：中间对齐
     * 2：右对齐
     *
     * @return
     */
    public PrintSuite setHorizontalAlign(byte align) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x61);
        this.byteBuffer.put(align);
        return this;
    }

    /**
     * 设置字符倍宽倍高
     *
     * @param flag 1：字符不倍宽不倍高 2：字符宽度高度都放大两倍
     * @return
     */
    public PrintSuite setDoubleFontSize(byte flag) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x57);
        this.byteBuffer.put(flag);
        return this;
    }

    /**
     * 是否加粗
     *
     * @param flag 0：不加粗 1：加粗
     */
    public PrintSuite bold(byte flag) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x45);
        this.byteBuffer.put(flag);

        return this;
    }

    /**
     * 初始化打印机
     * @return
     */
    public PrintSuite initPrinter(){
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x40);

        return this;
    }


    /**
     * 设置字体大小下划线，优点：占字节少
     *
     * @param spaceLattice true:16点阵字号 false:24点阵字号
     * @param high true:倍高 false:取消倍高
     * @param width true:倍宽 false:取消倍宽
     * @param bold true:加粗 false:取消加粗
     * @param underLine true:下划线 false:取消下划线
     * @return
     */
    public PrintSuite setFontSize(boolean spaceLattice,boolean high,boolean width,boolean bold,boolean underLine){
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x21);
        //拼装指令字节
        StringBuilder stringBuilder =new StringBuilder();
        //第0位 0--24点阵字号；1--16点阵字号
        stringBuilder.append(spaceLattice?"1":"0");
        //第1,2位未定义
        stringBuilder.append("00");
        //第3位 0--取消加粗模式；1--选择加粗模式
        stringBuilder.append(bold?"1":"0");
        //第4位 0--取消倍高模式；1--选择倍高模式
        stringBuilder.append(high?"1":"0");
        //第5位 0--取消倍宽模式；1--选择倍宽模式
        stringBuilder.append(width?"1":"0");
        //第6位未定义
        stringBuilder.append("0");
        //第7位 0--取消下划线模式；1--选择下划线模式
        stringBuilder.append(underLine?"1":"0");
        stringBuilder.reverse();
        this.byteBuffer.put((byte)Integer.parseInt(stringBuilder.toString(),2));
        return this;
    }

    /**
     * 设置字体大小
     * n值可取：0，1，48，49
     *
     * @param fontSize 0：选择24点阵字号 1：选择16点阵字号
     * @return
     */
    public PrintSuite setSpaceLattice(byte fontSize) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x4D);
        this.byteBuffer.put(fontSize);
        return this;
    }

    /**
     * 设置默认行间距
     *
     * @return
     */
    public PrintSuite setDefaultLineSpace() {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x32);
        return this;
    }

    /**
     * 设置行间距
     *
     * @param lineSpace
     * @return
     */
    public PrintSuite setLineSpace(byte lineSpace) {
        this.byteBuffer.put((byte) 0x1B);
        this.byteBuffer.put((byte) 0x33);
        this.byteBuffer.put(lineSpace);
        return this;
    }

    /**
     * 设置左边距
     *
     * @return
     */
    public PrintSuite setLeftMargin(byte margin) {
        this.byteBuffer.put((byte) 0x1D);
        this.byteBuffer.put((byte) 0x4C);
        this.byteBuffer.put((byte) 0x00);
        this.byteBuffer.put(margin);

        return this;
    }

    /**
     * 设置默认左边距
     *
     * @return
     */
    public PrintSuite setDefaultLeftMargin() {
        this.byteBuffer.put((byte) 0x1D);
        this.byteBuffer.put((byte) 0x4C);
        this.byteBuffer.put((byte) 0x00);
        this.byteBuffer.put((byte) 0x00);

        return this;
    }

    /**
     * 设置二维码最小模块单元宽度
     * 宽度值只能为2，4，8
     *
     * @return
     */
    public PrintSuite qrMinCellWidth(byte width) {
        this.byteBuffer.put((byte) 0x1F);
        this.byteBuffer.put((byte) 0x15);
        this.byteBuffer.put(width);
        return this;
    }

    /**
     * 设置UTF-8 编码
     *
     * @return
     */
    public PrintSuite setEncodeUTF8() {
        this.byteBuffer.put((byte)0x1C);
        this.byteBuffer.put((byte)0x43);
        this.byteBuffer.put((byte)255);
        return this;
    }

    /**
     * 设置二维码
     *
     * @param content
     * @return
     */
    public PrintSuite qr(String content) {
        if (content == null) {
            content = "";
        }
        this.byteBuffer.put((byte) 0x1F);
        this.byteBuffer.put((byte) 0x11);
        this.byteBuffer.put((byte) 0x00);

        int length = content.length();
        byte lengthByte = ByteUtil.getByteOfInt(length);
        this.byteBuffer.put(lengthByte);
        this.byteBuffer.put(content.getBytes());

        return this;
    }

    /**
     * 初始化小票格式
     *
     */
    public PrintSuite initTicketFormat(){

        //设置编码格式UTF-8
        setEncodeUTF8();

        //设置左边距为0
        setDefaultLeftMargin();

        //设置居左对齐
        setHorizontalAlign((byte)0x00);

        //设置字号24点阵
        setSpaceLattice((byte)0x00);

        //设置字符不放大
        setDoubleFontSize((byte)0x01);

        //设置字符不加粗
        bold((byte)0x00);

        return this;
    }
}
