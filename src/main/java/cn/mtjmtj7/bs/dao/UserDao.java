package cn.mtjmtj7.bs.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.mtjmtj7.bs.bean.User;

public interface UserDao extends JpaRepository<User, Integer>{

	/**
	 * 登陆验证
	 */
	public User findByUsernameAndUserpassAndUsertype(String userName,String userPass,String userType);
	/**
	 * 查询普通用户
	 */
	@Query(value = "select new User(u.uid,u.username,u.usernickname,u.usertype,u.userimg,u.userregisttime,u.lastlogintime) from User u where u.usertype = ?1")
	public Page<User> findByUsertype(String userType,Pageable pageable);
	/**
	 * 批量删除
	 */
	@Modifying
	@Query("delete from User u where u.uid in (?1)")
	public void deleteBatch(List<Integer> list);
	/**
	 * 用户唯一
	 */
	public User findByUsername(String username);
	/**
	 * 用户昵称唯一
	 */
	public User findByUsernickname(String username);
}
