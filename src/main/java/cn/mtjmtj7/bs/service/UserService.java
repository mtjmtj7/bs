package cn.mtjmtj7.bs.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.mtjmtj7.bs.bean.InType;
import cn.mtjmtj7.bs.bean.OutType;
import cn.mtjmtj7.bs.bean.PayIn;
import cn.mtjmtj7.bs.bean.PayOut;
import cn.mtjmtj7.bs.bean.User;
import cn.mtjmtj7.bs.dao.IntypeDao;
import cn.mtjmtj7.bs.dao.OuttypeDao;
import cn.mtjmtj7.bs.dao.PayInDao;
import cn.mtjmtj7.bs.dao.PayOutDao;
import cn.mtjmtj7.bs.dao.UserDao;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private IntypeDao inTypeDao;
	@Autowired
	private PayInDao payInDao;
	@Autowired
	private OuttypeDao outTypeDao;
	@Autowired
	private PayOutDao payOutDao;
	/**
	 * 登陆验证
	 */
	public User login(User user) {
		 return userDao.findByUsernameAndUserpassAndUsertype(user.getUsername(), user.getUserpass(), user.getUsertype());
	} 
	/**
	 * 查询修改
	 */
	public User findById(int uid) {
		return userDao.getOne(uid);
	}
	/**
	 * 保存图片
	 */
	public void savaImg(HttpServletRequest request,User user,MultipartFile file) {
		String path = request.getServletContext().getRealPath("img/");
		File filepath  = new File(path);
		if(!filepath.exists()) {
			filepath.mkdirs();
		}
		//上传文件原始文件名
		String filename = file.getOriginalFilename();
		String[] arr = filename.split("\\.");
		String ext = arr[arr.length-1];
		String newFileName = new Date().getTime()+"."+ext;//修改上传图片名字
		//创建上传后文件的路径和文件名
		File name = new File(filepath,newFileName);
		File oldFile = new File(filepath,user.getUserimg());
		try {
			if(oldFile.exists()) {
				oldFile.delete();
			}
			file.transferTo(name);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUserimg(newFileName);
	}
	/**
	 * 更新用户
	 * @return 
	 */
	public User updateUser(User user) {
		return userDao.save(user);
	}
	
	/**
	 * 查询普通用户
	 */
	public Page<User> findByType(int page, int size){
		Pageable pageable = new PageRequest(page-1, size, Sort.Direction.ASC,"uid");
		Page<User> pageList = userDao.findByUsertype("1",pageable);
		return pageList;
	}
	/**
	 * 删除用户
	 */
	public void userDelete(int uid) {
		userDao.deleteById(uid);
	}
	/**
	 * 添加用户
	 */
	public void save(User user) {
		userDao.save(user);
	}
	/**
	 * 批量删除
	 */
	public void deleteBatch(List<Integer> list) {
		userDao.deleteBatch(list);
	}
	/**
	 * 用户唯一
	 */
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	/**
	 * 用户昵称唯一
	 */
	public User findByUsernickname(String usernickname) {
		return userDao.findByUsernickname(usernickname);
	}
	/**
	 * 查询所有分类
	 */
	public List<InType> findAllIntype(){
		return inTypeDao.findAll();
	}
	/**
	 * 添加一个收入
	 */
	public void addOnePayIn(PayIn payIn) {
		payInDao.save(payIn);
	}
	/**
	 * 按ID查询所有收入
	 */
	public Page<PayIn> findAllPayInByAccountId(int accountid,int page,int size){
		Pageable pageable = new PageRequest(page-1, size, Sort.Direction.ASC,"accountid");
		Page<PayIn> pageList = payInDao.findByAccountid(accountid,pageable);
		return pageList;
	}
	/**
	 * 删除一条收入
	 */
	public void payInDeleteOne(int inid) {
		payInDao.deleteById(inid);
	}
	/**
	 * findPayInById
	 */
	public PayIn findPayInById(int inid) {
		return payInDao.getOne(inid);
	}
	/**
	 * 更新收入记录
	 */
	public void updatePayIn(PayIn p) {
		payInDao.save(p);
	}
	/**
	 * 批量删除收入记录
	 */
	public void deletePayInBatch(List<Integer> list) {
		payInDao.deleteBatch(list);
	}
	/**
	 * 查询所有支出分类
	 */
	public List<OutType> findAllOuttype() {
		return outTypeDao.findAll();
	}
	/**
	 * 添加支出记录
	 */
	public void addOneExpenditure(PayOut p) {
		payOutDao.save(p);
	}
	
	/**
	 * 按ID查询所有支出
	 */
	public Page<PayOut> findAllPayOutByAccountId(int accountid,int page,int size){
		Pageable pageable = new PageRequest(page-1, size, Sort.Direction.ASC,"accountid");
		Page<PayOut> pageList = payOutDao.findByAccountid(accountid,pageable);
		return pageList;
	}
	/**
	 * findPayOutById
	 * @param outid
	 * @return PayOut
	 */
	public PayOut findPayOutById(int outid) {
		return payOutDao.getOne(outid);
	}
	/**
	 * 更新支出记录
	 */
	public void updatePayOut(PayOut p) {
		payOutDao.save(p);
	}
	/**
	 * 删除一条支出
	 */
	public void payOutDeleteOne(int outid) {
		payOutDao.deleteById(outid);
	}
	/**
	 * 批量删除支出记录
	 */
	public void deletePayOutBatch(List<Integer> list) {
		payOutDao.deleteBatch(list);
	}
	/**
	 * 当年总支出金额
	 */
	public List<PayOut> getAllPayOutMoney(int accountid, String year){
		return payOutDao.getAllMoney(accountid, year);
	}
	/**
	 * 当年总收入金额
	 */
	public List<PayIn> getAllPayInMoney(int accountid, String year){
		return payInDao.getAllMoney(accountid, year);
	}
	
	/**
	 * 按月查询支出
	 */
	public List<PayOut> getAllPayOutMoneyByOuttypeID(String outtypeid, int accountid){
		return payOutDao.getAllPayOutMoneyByOuttypeID(outtypeid, accountid);
	}
	/**
	 * 按月查询收入
	 */
	public List<PayIn> getAllPayInMoneyByIntypeID(String intypeid, int accountid){
		return payInDao.getAllPayInMoneyByIntypeID(intypeid, accountid);
	}
	/**
	 * 删除单个分类
	 */
	public void deleteOneInCatalog(int itid) {
		inTypeDao.deleteById(itid);
	}
	/**
	 * 批量删除分类
	 */
	public void deleteInCatalogBatch(List<Integer> list) {
		inTypeDao.deleteInCatalogBatch(list);
	}
	//
	/**
	 * 删除单个支出分类
	 */
	public void deleteOneOutCatalog(int itid) {
		outTypeDao.deleteById(itid);
	}
	/**
	 * 批量删除支出分类
	 */
	public void deleteOutCatalogBatch(List<Integer> list) {
		outTypeDao.deleteOutCatalogBatch(list);
	}
	/**
	 * 添加一个收入分类
	 */
	public void addOneInType(InType in) {
		inTypeDao.save(in);
	}
	/**
	 * 添加一个支出分类
	 */
	public void addOneOutType(OutType out) {
		outTypeDao.save(out);
	}
}
