package cn.mtjmtj7.bs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OutType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer outid;
	
	@Column(name = "outname")
	private String outname;

	public Integer getOutid() {
		return outid;
	}

	public void setOutid(Integer outid) {
		this.outid = outid;
	}

	public String getOutname() {
		return outname;
	}

	public void setOutname(String outname) {
		this.outname = outname;
	}

	@Override
	public String toString() {
		return "OutType [outid=" + outid + ", outname=" + outname + "]";
	}

	
}
