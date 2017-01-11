package com.seabox.tagsys.sys;
/**
 * 
 * Description 基础异常类
 * @author shibh
 * @create date 2015年12月19日
 * @version 0.0.1
 */
public class BaseException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 异常代码
	 */
	private String expCode;
	/**
	 * 异常信息
	 */
	private String expMessage;
	/**
	 * 异常原因
	 */
	private Throwable e;
	
	public BaseException(){
		
	}
	public BaseException(String expMessage){
		this.expMessage=expMessage;
	}
	public BaseException(String expCode,Throwable e){
		this.expCode=expCode;
		this.e=e;
	}
	public BaseException(String expCode,String expMessage){
		this.expCode=expCode;
		this.expMessage=expMessage;
	}
	
	public BaseException(String expCode, String expMessage, Throwable e) {
		super();
		this.expCode = expCode;
		this.expMessage = expMessage;
		this.e = e;
	}
	public String getexpCode() {
		return expCode;
	}
	

	public void setexpCode(String expCode) {
		this.expCode = expCode;
	}
	public String getexpMessage() {
		return expMessage;
	}
	public void setexpMessage(String expMessage) {
		this.expMessage = expMessage;
	}
	public Throwable getE() {
		return e;
	}
	public void setE(Throwable e) {
		this.e = e;
	}
	
	
}
