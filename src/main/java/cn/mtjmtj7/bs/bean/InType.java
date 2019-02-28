package cn.mtjmtj7.bs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itid;
	
	@Column(name = "itname")
	private String itname;

	public Integer getItid() {
		return itid;
	}

	public void setItid(Integer itid) {
		this.itid = itid;
	}

	public String getItname() {
		return itname;
	}

	public void setItname(String itname) {
		this.itname = itname;
	}

	@Override
	public String toString() {
		return "InType [itid=" + itid + ", itname=" + itname + "]";
	}
	
	
}
