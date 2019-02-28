package com.syswin.pipeline.enums;

public enum UrlEnums {
	AcountUrl(1, "/manage/account/list"),
	UserLogUrl(2, "/manage/userlog/list"),
	PublisherUrl(3, "/manage/publisher/list"),
	TransactionUrl(4, "/manage/transaction/list");

	public Integer code;
	public String url;

	UrlEnums(Integer code, String url) {
		this.code = code;
		this.url = url;
	}

	/**
	 * * 日志管理：
	 * *  xxx 时间内
	 * * 新增   人数 跳转到人具体登录
	 * * 活跃   人数
	 * * 出版社  发布  订阅
	 * * 交易   充值
	 */
	public String getName() {
		String name = "";
		switch (this.code) {
			case 1:
				name = "新增用户统计";
				break;
			case 2:
				name = "用户操作记录";
				break;
			case 3:
				name = "出版社统计";
				break;
			case 4:
				name = "用户交易记录";
				break;
		}
		return name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
