package com.fxl.template.user.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Data，设置后将字段生成set、get、toString等方法
 * Accessors，设置后，set方法将改为build模式，非void模式
 */
@Data
@Accessors(chain = true)
public class VOUserInfo {
	private Integer id;
	
	private String nick_name;
	
	private String user_img;

    private Integer pregnant_stage;

    private Integer pregnant_week;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
