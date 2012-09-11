package org.sse.service.base;

import java.io.Serializable;

import com.google.gson.Gson;

/**
 * @author dux(duxionggis@126.com)
 */
public class Poi implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id = "";
	private String name = "";
	private String kind = "";
	private String phone = "";
	private String address = "";
	private String remark = "";
	private String vertex = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public String toString() {
		return new Gson().toJson(this);
	}

}
