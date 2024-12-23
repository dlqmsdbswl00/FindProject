package com.board.dtos;

import java.time.LocalDate;

public class ExpenseDto {

	private String email;
	private LocalDate date; // 소비 날짜
	private String expenseType; // 소비 항목 (커피, 담배, 택시 등)
	private int amount; // 소비 금액
	private String imageUrl; // 소비 항목의 이미지 URL (PNG)

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ExpenseDto [email=" + email + ", date=" + date + ", expenseType=" + expenseType + ", amount=" + amount
				+ ", imageUrl=" + imageUrl + "]";
	}

}
