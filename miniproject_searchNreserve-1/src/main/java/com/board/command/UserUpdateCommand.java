package com.board.command;


public class UserUpdateCommand {

   private String email;
   
   private String name;
   
   private String address;
   private String birth;
   
   
   private String phone;
      
   public UserUpdateCommand() {
      super();
   }

   public UserUpdateCommand(String email, String name, String address, String birth, String phone) {
      super();
      this.email = email;
      this.name = name;
      this.address = address;
      this.birth = birth;
      this.phone = phone;
   }

   @Override
   public String toString() {
      return "UserUpdateCommand [email=" + email + ", name=" + name + ", address=" + address + ", birth=" + birth
            + ", phone=" + phone + "]";
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

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getBirth() {
      return birth;
   }

   public void setBirth(String birth) {
      this.birth = birth;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   
   
   
   
   
}