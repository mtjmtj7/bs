package cn.mtjmtj7.bs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.mtjmtj7.bs.bean.InType;

public interface IntypeDao extends JpaRepository<InType, Integer>{
	
	/**
	 * 批量删除
	 */
	@Modifying
	@Query("delete from InType u where u.itid in (?1)")
	public void deleteInCatalogBatch(List<Integer> list);
}
