package org.dxer.app;

import java.util.Date;

public class HouseInfo {
	private String bigTitle; // 标题
	private Date postDate; // 发布时间
	private String price; // 租金
	private String paymentType;// 付款方式
	private String houseType; // 户型
	private String houseState; // 装修情况
	private String floor; // 所属楼层
	private String house; // 出租间
	private String area; // 所在区域
	private String address;// 地址
	private String contacts; // 联系人
	private String contactsType; // 联系人种类（经纪人、个体）
	private String phoneNumber; // 联系方式
	private String peizhi; // 配置
	private String description; // 描述

	public String getBigTitle() {
		return bigTitle;
	}

	public void setBigTitle(String bigTitle) {
		this.bigTitle = bigTitle;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String rental) {
		this.price = rental;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public String getHouseState() {
		return houseState;
	}

	public void setHouseState(String houseState) {
		this.houseState = houseState;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsType() {
		return contactsType;
	}

	public void setContactsType(String contactsType) {
		this.contactsType = contactsType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPeizhi() {
		return peizhi;
	}

	public void setPeizhi(String peizhi) {
		this.peizhi = peizhi;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(bigTitle).append(", |");
		ret.append(price).append(", |");
		ret.append(paymentType).append(", |");
		ret.append(houseType).append(", |");
		ret.append(floor).append(", |");
		ret.append(house).append(", |");
		ret.append(house).append(", |");
		ret.append(area).append(", |");
		ret.append(address).append(", |");
		ret.append(contacts).append(", |");
		ret.append(description);
		return ret.toString();
	}
}
