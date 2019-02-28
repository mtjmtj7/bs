package cn.mtjmtj7.bs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;

@Entity
public class PayOut {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer outid;
	
	@Column(name = "outname")
	private String outname;

	@Column(name = "money")
	private String money;
	
	@Column(name = "outtypeid")
	private String outtypeid;
	
	@Column(name="accountid")
	private int accountid;
	
	@Column(name = "createtime")
	private String createtime;
	
	@Column(name = "updatetime")
	private String updatetime;
	
	@Column(name = "remark")
	private String remark;

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

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getOuttypeid() {
		return outtypeid;
	}

	public void setOuttypeid(String outtypeid) {
		this.outtypeid = outtypeid;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "PayOut [outid=" + outid + ", outname=" + outname + ", money=" + money + ", outtypeid=" + outtypeid
				+ ", accountid=" + accountid + ", createtime=" + createtime + ", updatetime=" + updatetime + ", remark="
				+ remark + "]";
	}
	
	//结果集
	public PayOut() {
		// TODO Auto-generated constructor stub
	}

	public PayOut(Integer outid, String outname, String money, int accountid, String createtime) {
		super();
		this.outid = outid;
		this.outname = outname;
		this.money = money;
		this.accountid = accountid;
		this.createtime = createtime;
	}
	
}
