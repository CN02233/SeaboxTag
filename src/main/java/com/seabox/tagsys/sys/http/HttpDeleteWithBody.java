/**
 * 
 */
package com.seabox.tagsys.sys.http;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Description
 * @author Lenovo
 * @create date 2016年1月4日
 * @version 0.0.1
 */
public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase{
	public static final String METHOD_NAME="DELETE";
	/**
	 * 
	 */
	public HttpDeleteWithBody() {
		super();
	}
	public HttpDeleteWithBody(final URI uri){
		super();
		setURI(uri);
	}
	public HttpDeleteWithBody(final String uri){
		super();
		setURI(URI.create(uri));
	}
	/* (non-Javadoc)
	 * @see org.apache.http.client.methods.HttpRequestBase#getMethod()
	 */
	@Override
	public String getMethod() {
		return METHOD_NAME;
	}
	
}
