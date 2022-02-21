package com.enums;

public enum ConsultStatusEnum {
	not_take(0, "未认领"), 
	have_take(1, "已认领"), 
	not_reply(2, "未回复"), 
	have_reply(3, "已回复"), 
	wait_deal(4, "待处理"), 
	have_end(5, "已结束"),
	have_refuse(6, "已拒绝");
	
	private int value;
	private String name;

	private ConsultStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public static String getStatusName(int statusValue) {
		ConsultStatusEnum[] statusEnums = ConsultStatusEnum.values();
		String statusName = "";
		for (ConsultStatusEnum status : statusEnums) {
			if (status.value == statusValue) {
				statusName = status.name;
				break;
			}
		}
		return statusName;
	}

	public static void main(String[] args) {
		//枚举valueOf方法 "not_take" = not_take.name() 效果相等
		ConsultStatusEnum notTake = ConsultStatusEnum.valueOf("not_take");
		ConsultStatusEnum notTake1 = ConsultStatusEnum.valueOf(not_take.name());
		System.out.println(not_take.name);
	}
}
