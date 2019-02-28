package cn.mtjmtj7.bs.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.mtjmtj7.bs.bean.InType;
import cn.mtjmtj7.bs.bean.PayIn;
import cn.mtjmtj7.bs.bean.PayOut;

public interface PayInDao extends JpaRepository<PayIn, Integer>{

	/**
	 * 查询所有收入
	 */
	@Query("select p from PayIn p where p.accountid = ?1")
	public Page<PayIn> findByAccountid(int accountid,Pageable pageable);
	/**
	 * 批量删除
	 */
	@Modifying
	@Query("delete from PayIn p where p.inid in (?1)")
	public void deleteBatch(List<Integer> list);
	/**
	 * 查询当年所有收入金额
	 */
	@Query("select new PayIn(p.inid,p.inname, p.money, p.accountid, p.createtime) from PayIn p where p.accountid = ?1 and p.createtime like ?2")
	public List<PayIn> getAllMoney(int accountid,String year);
	
	/**
	 * 按分类查询某人收入
	 */
	@Query("select new PayIn(p.inid, p.inname,p.money, p.accountid, p.createtime) from PayIn p where p.accountid = ?2 and p.intypeid = ?1")
	public List<PayIn> getAllPayInMoneyByIntypeID(String intypeid, int accountid);
}
