package cn.mtjmtj7.bs.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PayIn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inid;
	
	@Column(name = "inname")
	private String inname;

	@Column(name = "money")
	private String money;
	
	@Column(name = "intypeid")
	private String intypeid;
	
	@Column(name="accountid")
	private int accountid;
	
	@Column(name = "createtime")
	private String createtime;
	
	@Column(name = "updatetime")
	private String updatetime;
	
	@Column(name = "remark")
	private String remark;
	
	
	public Integer getInid() {
		return inid;
	}

	public void setInid(Integer inid) {
		this.inid = inid;
	}

	public String getInname() {
		return inname;
	}

	public void setInname(String inname) {
		this.inname = inname;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}



	public String getIntypeid() {
		return intypeid;
	}

	public void setIntypeid(String intypeid) {
		this.intypeid = intypeid;
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

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	@Override
	public String toString() {
		return "PayIn [inid=" + inid + ", inname=" + inname + ", money=" + money + ", intypeid=" + intypeid
				+ ", accountid=" + accountid + ", createtime=" + createtime + ", updatetime=" + updatetime + ", remark="
				+ remark + "]";
	}
	
	//结果集构造方法
	public PayIn() {
		// TODO Auto-generated constructor stub
	}

	public PayIn(Integer inid, String inname, String money, int accountid, String createtime) {
		super();
		this.inid = inid;
		this.inname = inname;
		this.money = money;
		this.accountid = accountid;
		this.createtime = createtime;
	}

}
