package cn.mtjmtj7.bs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.mtjmtj7.bs.bean.OutType;

public interface OuttypeDao extends JpaRepository<OutType, Integer>{
	/**
	 * 批量删除
	 */
	@Modifying
	@Query("delete from OutType u where u.outid in (?1)")
	public void deleteOutCatalogBatch(List<Integer> list);
}
