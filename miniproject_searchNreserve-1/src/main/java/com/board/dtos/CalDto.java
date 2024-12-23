package com.board.dtos;

import java.util.Date;

import lombok.Data;

//@Data
public class CalDto {

	private int seq;
	private String email;
	private String title;
	private String content;
	private String mdate;
	private Date regdate;
	private String ykiho;
	private String yadmNm;
	private int useMoney;

	public CalDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CalDto(int seq, String email, String title, String content, String mdate, Date regdate, String ykiho,
			String yadmNm, int useMoney) {
		super();
		this.seq = seq;
		this.email = email;
		this.title = title;
		this.content = content;
		this.mdate = mdate;
		this.regdate = regdate;
		this.ykiho = ykiho;
		this.yadmNm = yadmNm;
		this.useMoney = useMoney;
	}

	@Override
	public String toString() {
		return "CalDto [seq=" + seq + ", email=" + email + ", title=" + title + ", content=" + content + ", mdate="
				+ mdate + ", regdate=" + regdate + ", ykiho=" + ykiho + ", yadmNm=" + yadmNm + ", useMoney=" + useMoney
				+ "]";
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public String getYkiho() {
		return ykiho;
	}

	public void setYkiho(String ykiho) {
		this.ykiho = ykiho;
	}

	public String getYadmNm() {
		return yadmNm;
	}

	public void setYadmNm(String yadmNm) {
		this.yadmNm = yadmNm;
	}

	public int getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(int useMoney) {
		this.useMoney = useMoney;
	}

}