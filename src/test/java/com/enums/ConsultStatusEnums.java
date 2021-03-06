package com.enums;

public enum ConsultStatusEnums {
	not_take(0, "未认领"), 
	have_take(1, "已认领"), 
	not_reply(2, "未回复"), 
	have_reply(3, "已回复"), 
	wait_deal(4, "待处理"), 
	have_end(5, "已结束"),
	have_refuse(6, "已拒绝");
	
	private int value;
	private String name;

	private ConsultStatusEnums(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static String getStatusName(int statusValue) {
		ConsultStatusEnums[] statusEnums = ConsultStatusEnums.values();
		String statusName = "";
		for (ConsultStatusEnums status : statusEnums) {
			if (status.value == statusValue) {
				statusName = status.name;
				break;
			}
		}
		return statusName;
	}
}
