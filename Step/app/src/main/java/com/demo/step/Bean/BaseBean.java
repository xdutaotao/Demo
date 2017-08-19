package com.demo.step.Bean;

import android.text.TextUtils;

public class BaseBean<T>{
	private T data;
	private String msg;
	private String result;

	public boolean isError() {
		//if (TextUtils.equals("MSG00000", CODE) || TextUtils.equals("00000", CODE)){
		if(TextUtils.isEmpty(msg)){
			return true;
		}
		if (TextUtils.equals(msg, "200")){
			return false;
		}else{
			return true;
		}
	}

	public String errorMsg(){
		return result;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}