package cn.mtjmtj7.bs.util;

import java.util.ArrayList;
import java.util.List;

public class MyUitl {
	private static MyUitl instance = null;
	private MyUitl() {
	}
	public synchronized static MyUitl getInstance() {
		if(instance==null){
			instance=new MyUitl();
		}
		return instance;
	}
	/**
	 * ID字符串->List
	 * @param id字符串 如 1-2-3-4-5-6
	 * @return List<Integer>
	 */
	public List<Integer> IDStringToList(String ids){
		String id[] = ids.split("[-]");
		List<Integer> idList = new ArrayList<>();
		for (String str : id) {
			idList.add(new Integer(str));
		}
		return idList;
	}
}
