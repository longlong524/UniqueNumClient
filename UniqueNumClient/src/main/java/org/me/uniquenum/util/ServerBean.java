package org.me.uniquenum.util;

public class ServerBean {
	private String  url;
	private String secretKey;
	public ServerBean(String  url,
			String secretKey){
		this.url=url;
		this.secretKey=secretKey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	
	
}
