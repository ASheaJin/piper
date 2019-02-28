package com.syswin.pipeline.enums;

/**
 * @author:lhz
 * @date:2018/12/17 10:20
 */
public final class PeriodEnums {

	public class MenuList {
		public static final int HELP = 1;             //help帮助
		public static final int CREATPUBLISH = 2;   //创造出版社
		public static final int ORDERPUBLISH = 3;   //订阅出版社
		public static final int UNORDERPUBLISH = 4;   //取消订阅出版社
		public static final int PUBLISHARTICLE = 5;   //发布文章

	}

	public class CREATEPUBLISHPROCESS {
		public static final int INPUTTEMAIL = 0; //第一步：输入出版社邮箱号提示
		public static final int INPUTTITLE = 1;     //第二步：输入出版社的名称
	}

	public static void main(String[] args) {
	}
}
