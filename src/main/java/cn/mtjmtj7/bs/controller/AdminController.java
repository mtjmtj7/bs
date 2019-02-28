package cn.mtjmtj7.bs.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.mtjmtj7.bs.bean.InType;
import cn.mtjmtj7.bs.bean.OutType;
import cn.mtjmtj7.bs.bean.User;
import cn.mtjmtj7.bs.dao.IntypeDao;
import cn.mtjmtj7.bs.service.UserService;
import cn.mtjmtj7.bs.util.Md5Token;
import cn.mtjmtj7.bs.util.MyUitl;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private UserService userService;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	ThreadLocal<User>  admin = new ThreadLocal<>();
	@ModelAttribute
	public void initUser (HttpSession session) {
		Object obj = session.getAttribute("adminNow");
		if(obj instanceof User) {
			User adminNow = (User)obj;
			admin.set(adminNow);
		}
	}
	
	// 管理员首页
	@RequestMapping("index")
	public String index(Model model) {
		model.addAttribute("adminNow", admin.get());
		return "admin/admin";
	}

	// 修改信息
	@RequestMapping("update/{uid}")
	public String test(@PathVariable int uid, Model model) {
		User user = userService.findById(uid);
		model.addAttribute("user", user);
		return "admin/update";
	}

	// 上传头像
	@RequestMapping("uploadImg")
	@ResponseBody
	public String uploadImg(MultipartFile file, HttpServletRequest request) {
		User adminNow =  admin.get();
		Map<String, String> map = new HashMap<String, String>();
		if (!file.isEmpty()) {
			userService.savaImg(request, adminNow, file);
			// 更新数据库
			userService.updateUser(adminNow);
			map.put("msg", "yes");
			map.put("code", "1");
			map.put("uri", adminNow.getUserimg());
		} else {
			map.put("msg", "no");
			map.put("code", "0");
		}
		return JSONObject.toJSONString(map);
	}

	// 更新信息
	@RequestMapping("adminUpdate")
	@ResponseBody
	public String updateUser( @RequestParam String usernickname, @RequestParam String userpass) {
		User adminNow =  admin.get();
		if (!usernickname.equals("") && usernickname != null)
			adminNow.setUsernickname(usernickname);
		if (!userpass.equals("") && userpass != null)
			adminNow.setUserpass(Md5Token.getInstance().getShortToken(userpass));
		User u = userService.updateUser(adminNow);
		String msg;
		if (u != null) {
			msg = "更新成功，下次登录生效！";
		} else {
			msg = "更新失败！请重试。";
		}
		return msg;
	}

	/**
	 * 注册用户管理
	 */
	// 跳转用户管理页面
	@RequestMapping("userManage")
	public String userManage(Model model) {
		return "admin/userManage";
	}

	// 用户管理：查询所有普通用户
	@RequestMapping("userGetAll")
	@ResponseBody
	public String usreGetAll(ModelMap map, int page, int limit) {
		Page<User> pageList = userService.findByType(page, limit);
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", pageList.getTotalElements());
		map.put("data", pageList.getContent());
		return JSONObject.toJSONString(map);
	}

	// 删除用户
	@RequestMapping("userDelete")
	@ResponseBody
	public String userDelete(int uid) {
		userService.userDelete(uid);
		return "删除成功！";
	}

	// 修改用户
	@RequestMapping("userUpdate")
	@ResponseBody
	public String userUpdate(int uid, String pass, String usernickname) {
		User user = userService.findById(uid);
		if (!pass.equals("") && pass != null) {
			user.setUserpass(Md5Token.getInstance().getShortToken(pass));
		}
		if (!usernickname.equals("") && usernickname != null) {
			User userCheck = userService.findByUsernickname(usernickname);
			if (userCheck != null) {
				return "该昵称已存在！";
			} else {
				user.setUsernickname(usernickname);
			}
		}
		User uu = userService.updateUser(user);
		if (uu != null) {
			return "修改成功！";
		}
		return "修改失败";
	}

	// 跳转用户查询页面
	@RequestMapping("userSearch")
	public String userSearch() {
		return "admin/userSearch";
	}

	// 跳转用户添加
	@RequestMapping("userAdd")
	public String userAdd() {
		return "admin/userAdd";
	}

	// 添加一个用户
	@RequestMapping("userAddOne")
	@ResponseBody
	public String userAddOne(ModelMap map, User user) {
		// 密码
		user.setUserpass(Md5Token.getInstance().getShortToken(user.getUserpass()));
		// 设置注册时间
		Date nowDate = new Date();
		user.setUserregisttime(sf.format(nowDate));
		// 普通用户1
		user.setUsertype("1");
		// 头像
		user.setUserimg("head.jpg");
		userService.save(user);
		return "添加成功！";
	}

	// 批量删除
	@RequestMapping("userDeleteMany")
	@ResponseBody
	public String userDeleteMany(String ids) {
		userService.deleteBatch(MyUitl.getInstance().IDStringToList(ids));
		return "删除成功！";
	}

	// 跳转数据分析
	@RequestMapping("userAnalysis")
	public String userAnalysis() {
		return "admin/userAnalysis";
	}

	// 判断用户唯一
	@RequestMapping("userCheckOne")
	@ResponseBody
	public String userCheckOne(String username) {
		User user = userService.findByUsername(username);
		if (user != null) {
			return "yes";
		} else {
			return "no";
		}
	}

	/**
	 * 分类管理
	 */
	// 收入分类
	@RequestMapping("inCatalogManage")
	public String inCatalogManage() {
		return "admin/inCatalogManage";
	}

	// 查询收入分类
	@RequestMapping("getInCatalog")
	@ResponseBody
	public String getInCatalog(ModelMap map) {
		List<InType> inTypeList = userService.findAllIntype();
		map.put("data", inTypeList);
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", inTypeList.size());
		return JSONObject.toJSONString(map);
	}

	// 删除单个收入分类
	@RequestMapping("deleteOneInCatalog")
	@ResponseBody
	public String deleteOneInCatalog(int itid) {
		userService.deleteOneInCatalog(itid);
		return "删除成功！";
	}

	// 批量删除收入分类
	@RequestMapping("inCatalogDeleteMany")
	@ResponseBody
	public String inCatalogDeleteMany(String ids) {
		userService.deleteInCatalogBatch(MyUitl.getInstance().IDStringToList(ids));
		return "删除成功！";
	}

	// 支出分类
	@RequestMapping("outCatalogManage")
	public String outCatalogManage() {
		return "admin/outCatalogManage";
	}

	// 查询收入分类
	@RequestMapping("getOutCatalog")
	@ResponseBody
	public String getOutCatalog(ModelMap map) {
		List<OutType> outTypeList = userService.findAllOuttype();
		map.put("data", outTypeList);
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", outTypeList.size());
		return JSONObject.toJSONString(map);
	}

	// 删除单个支出分类
	@RequestMapping("deleteOneOutCatalog")
	@ResponseBody
	public String deleteOneOutCatalog(int outid) {
		userService.deleteOneOutCatalog(outid);
		return "删除成功！";
	}

	// 批量删除支出分类
	@RequestMapping("outCatalogDeleteMany")
	@ResponseBody
	public String outCatalogDeleteMany(String ids) {
		userService.deleteOutCatalogBatch(MyUitl.getInstance().IDStringToList(ids));
		return "删除成功！";
	}

	// 分类跳转
	@RequestMapping("addCatalog")
	public String addCatalog() {
		return "admin/addCatalog";
	}

	// 添加分类
	@RequestMapping("catalogAddOne")
	@ResponseBody
	public String catalogAddOne(String catalogName, String catalogType) {
		boolean flag = false;
		if(catalogType.equals("in")) {
			//添加收入分类
			InType in = new InType();
			in.setItname(catalogName);
			userService.addOneInType(in);
			flag = true;
		}
		else {
			//添加支出分类
			OutType out = new OutType();
			out.setOutname(catalogName);
			userService.addOneOutType(out);
			flag = true;
		}
		if(flag) {
			return "添加成功！";
		}
		return "添加失败！";
	}
	
	
	
}
