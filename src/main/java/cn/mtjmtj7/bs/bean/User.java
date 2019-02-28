package cn.mtjmtj7.bs.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity

public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uid;
	
	@Column(name = "username") //数据库的字段名 不区分大小写
	private String username;
	
	@Column(name = "userpass")
	private String userpass;
	
	@Column(name = "usernickname")
	private String usernickname;
	
	@Column(name = "usertype")
	private String usertype;
	
	@Column(name = "userimg")
	private String userimg;
	
	@Column(name = "userregisttime")
	private String userregisttime;
	
	@Column(name = "lastlogintime")
	private String lastlogintime;

	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getUserimg() {
		return userimg;
	}
	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}
	public String getUsernickname() {
		return usernickname;
	}
	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}
	public String getUserregisttime() {
		return userregisttime;
	}
	public void setUserregisttime(String userregisttime) {
		this.userregisttime = userregisttime;
	}
	public String getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public User(Integer uid, String username, String usernickname, String usertype, String userimg,
			String userregisttime, String lastlogintime) {
		super();
		this.uid = uid;
		this.username = username;
		this.usernickname = usernickname;
		this.usertype = usertype;
		this.userimg = userimg;
		this.userregisttime = userregisttime;
		this.lastlogintime = lastlogintime;
	}
	public User() {
		super();
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", userpass=" + userpass + ", usernickname="
				+ usernickname + ", usertype=" + usertype + ", userimg=" + userimg + ", userregisttime="
				+ userregisttime + ", lastlogintime=" + lastlogintime + "]";
	}
	
	
	
}
