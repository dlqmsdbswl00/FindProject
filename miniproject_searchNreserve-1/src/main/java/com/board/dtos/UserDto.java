package com.board.dtos;

public class UserDto {

   private String email;
   private String name;
   private String password;
   
   private String useraccesstoken; // 접근을 위한 토큰
   private String userrefreshtoken; // 갱신을 위한 토큰
   private int userseqno; // 사용자 일련 번호
   
   private String address;
   private int birth;
   private String phone;
   private String money;
   public UserDto() {
      super();
      // TODO Auto-generated constructor stub
   }
public UserDto(String email, String name, String password, String useraccesstoken, String userrefreshtoken,
		int userseqno, String address, int birth, String phone, String money) {
	super();
	this.email = email;
	this.name = name;
	this.password = password;
	this.useraccesstoken = useraccesstoken;
	this.userrefreshtoken = userrefreshtoken;
	this.userseqno = userseqno;
	this.address = address;
	this.birth = birth;
	this.phone = phone;
	this.money = money;
}
@Override
public String toString() {
	return "UserDto [email=" + email + ", name=" + name + ", password=" + password + ", useraccesstoken="
			+ useraccesstoken + ", userrefreshtoken=" + userrefreshtoken + ", userseqno=" + userseqno + ", address="
			+ address + ", birth=" + birth + ", phone=" + phone + ", money=" + money + "]";
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getUseraccesstoken() {
	return useraccesstoken;
}
public void setUseraccesstoken(String useraccesstoken) {
	this.useraccesstoken = useraccesstoken;
}
public String getUserrefreshtoken() {
	return userrefreshtoken;
}
public void setUserrefreshtoken(String userrefreshtoken) {
	this.userrefreshtoken = userrefreshtoken;
}
public int getUserseqno() {
	return userseqno;
}
public void setUserseqno(int userseqno) {
	this.userseqno = userseqno;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public int getBirth() {
	return birth;
}
public void setBirth(int birth) {
	this.birth = birth;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getMoney() {
	return money;
}
public void setMoney(String money) {
	this.money = money;
}
   
}