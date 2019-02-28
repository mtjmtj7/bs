package cn.mtjmtj7.bs.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.mtjmtj7.bs.bean.PayOut;

public interface PayOutDao extends JpaRepository<PayOut, Integer>{
	/**
	 * 查询所有支出
	 */
	@Query("select p from PayOut p where p.accountid = ?1")
	public Page<PayOut> findByAccountid(int accountid,Pageable pageable);
	/**
	 * 批量删除
	 */
	@Modifying
	@Query("delete from PayOut p where p.outid in (?1)")
	public void deleteBatch(List<Integer> list);
	/**
	 * 查询当年所有支出金额
	 */
	@Query("select new PayOut(p.outid, p.outname,p.money, p.accountid, p.createtime) from PayOut p where p.accountid = ?1 and p.createtime like ?2")
	public List<PayOut> getAllMoney(int accountid,String year);
	/**
	 * 按分类查询某人支出
	 */
	@Query("select new PayOut(p.outid, p.outname,p.money, p.accountid, p.createtime) from PayOut p where p.accountid = ?2 and p.outtypeid = ?1")
	public List<PayOut> getAllPayOutMoneyByOuttypeID(String outtypeid, int accountid);
}
