package org.sse.geoc;

import java.io.Serializable;

/**
 * @author dux(duxionggis@126.com)
 */
public class District implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cityCode;
	private String provinceCode;
	private String province;
	private String city;
	private String county;

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

}
