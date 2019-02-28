package com.syswin.pipeline.utils;

/**
 * @author:lhz
 * @date:2018/12/18 11:32
 */
public class MessageUtil {


	/**
	 * 发送帮助信息
	 *
	 * @return
	 */
//	public static String sendHelp() {
//		StringBuilder stringBuilder = new StringBuilder("请输入指令序号：\n");
//		stringBuilder.append("1、Piper引导\n");
//		stringBuilder.append("2、创建出版社\n");
//		stringBuilder.append("3、订阅出版社\n");
//		stringBuilder.append("4、取消订阅出版社\n");
//		stringBuilder.append("5、发布文章");
//		return stringBuilder.toString();
//	}


	/**
	 * 发送新手帮忙信息
	 *
	 * @return
	 */
	public static String sendNewOrgTip() {
		StringBuilder stringBuilder = new StringBuilder("Piper组织管理\n");
		stringBuilder.append("1、查看订阅列表（www.baidu.com）\n");
		stringBuilder.append("2、添加订阅者（www.baidu.com）\n");
//		stringBuilder.append("PipeLine地址为：a_pipeline@systoontest.com，请好友们发邮件。");
		return stringBuilder.toString();
	}

	/**
	 * 发送新手帮忙信息
	 *
	 * @return
	 */
	public static String shareNewTip(String temail) {
		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append("您可创建自己的出版社，分享文章,订阅别人的出版社\n");
//		stringBuilder.append("您可分享文章给订阅了您的所有读者，获得秘票。\n");
//		stringBuilder.append("您可阅读您订阅的出版社的文章。\n\n");
//
//		stringBuilder.append("温馨提示1：将本地址分享给好友。\n");
//		stringBuilder.append("温馨提示2：创建出版社，可获得自己出版社的订阅指令。\n");
//		stringBuilder.append("分享以下地址：将开始您与读者们的 美好阅读时光...\n");

		stringBuilder.append(temail);
		return stringBuilder.toString();
	}

	/**
	 * 发送帮助信息
	 *
	 * @return
	 */
	public static String sendCreateHelpTip(String tip) {
		StringBuilder stringBuilder = new StringBuilder("温馨提示\n");
		stringBuilder.append(tip);
		return stringBuilder.toString();
	}

	/**
	 * 发送创建出版社提示信息
	 *
	 * @return
	 */
	public static String sendCreatePublisher(String mail, String name) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("出版社< " + name + " >创建成功，订阅地址为：");
		return stringBuilder.toString();
	}

	/**
	 * 发送订阅出版社提示信息
	 *
	 * @return
	 */
	public static String sendOrderPublisher() {
		StringBuilder stringBuilder = new StringBuilder("你已订阅该出版社\n");

		return stringBuilder.toString();
	}

	/**
	 * 发送取消订阅出版社提示信息
	 *
	 * @return
	 */
	public static String sendCancelOrderPublisher() {
		StringBuilder stringBuilder = new StringBuilder("你尚未订阅该出版社\n");

		return stringBuilder.toString();
	}
}
