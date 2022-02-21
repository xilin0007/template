package com.fxl.template.user.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fxl.frame.base.BaseEntity;

public class UserInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

    private String mobile;

    private String email;

    private String nickName;

    private Integer country;

    private Integer province;

    private Integer city;

    private String userImg;

    /**
     * JsonFormat 返回给前端时，日期自动转换成格式化的字符串，也可通过spring-mvc配置文件配置全局的格式化
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedDateOfConfinement;

    private Integer status;

    /**
     * JsonIgnore 设置后，此字段将忽略，不会返回到前端
     */
    @JsonIgnore
    private Date regTime;

    private String password;

    private Integer isSwitchPushMsg;

    private String openId;

    private Integer userType;

    private Integer gold;

    private Integer pregnantStage;

    private Integer pregnantWeek;

    private String sinaOpenId;

    private String qqOpenId;

    private String weixinOpenId;

    private String medicalNum;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg == null ? null : userImg.trim();
    }

    public Date getExpectedDateOfConfinement() {
        return expectedDateOfConfinement;
    }

    public void setExpectedDateOfConfinement(Date expectedDateOfConfinement) {
        this.expectedDateOfConfinement = expectedDateOfConfinement;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getIsSwitchPushMsg() {
        return isSwitchPushMsg;
    }

    public void setIsSwitchPushMsg(Integer isSwitchPushMsg) {
        this.isSwitchPushMsg = isSwitchPushMsg;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public Integer getPregnantStage() {
        return pregnantStage;
    }

    public void setPregnantStage(Integer pregnantStage) {
        this.pregnantStage = pregnantStage;
    }

    public Integer getPregnantWeek() {
        return pregnantWeek;
    }

    public void setPregnantWeek(Integer pregnantWeek) {
        this.pregnantWeek = pregnantWeek;
    }

    public String getSinaOpenId() {
        return sinaOpenId;
    }

    public void setSinaOpenId(String sinaOpenId) {
        this.sinaOpenId = sinaOpenId == null ? null : sinaOpenId.trim();
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId == null ? null : qqOpenId.trim();
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId == null ? null : weixinOpenId.trim();
    }

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum == null ? null : medicalNum.trim();
    }

//	@Override
//	public String toString() {
//		return "UserInfo [mobile=" + mobile + ", email=" + email
//				+ ", nickName=" + nickName + ", country=" + country
//				+ ", province=" + province + ", city=" + city + ", userImg="
//				+ userImg + ", expectedDateOfConfinement="
//				+ expectedDateOfConfinement + ", status=" + status
//				+ ", regTime=" + regTime + ", password=" + password
//				+ ", isSwitchPushMsg=" + isSwitchPushMsg + ", openId=" + openId
//				+ ", userType=" + userType + ", gold=" + gold
//				+ ", pregnantStage=" + pregnantStage + ", pregnantWeek="
//				+ pregnantWeek + ", sinaOpenId=" + sinaOpenId + ", qqOpenId="
//				+ qqOpenId + ", weixinOpenId=" + weixinOpenId + ", medicalNum="
//				+ medicalNum + "]";
//	}

	@Override
	public String toString() {
		return "UserInfo [nickName=" + nickName + ", status=" + status  + "]";
	}
    
}