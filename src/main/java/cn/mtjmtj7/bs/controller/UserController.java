package cn.mtjmtj7.bs.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.mtjmtj7.bs.bean.InType;
import cn.mtjmtj7.bs.bean.OutType;
import cn.mtjmtj7.bs.bean.PayIn;
import cn.mtjmtj7.bs.bean.PayOut;
import cn.mtjmtj7.bs.bean.User;
import cn.mtjmtj7.bs.service.UserService;
import cn.mtjmtj7.bs.util.Md5Token;
import cn.mtjmtj7.bs.util.MyUitl;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String year = String.valueOf(new Date().getYear() + 1900);

	ThreadLocal<User>  user = new ThreadLocal<>();
	@ModelAttribute
	public void initUser (HttpSession session) {
		Object obj = session.getAttribute("userNow");
		if(obj instanceof User) {
			User userNow = (User)obj;
			user.set(userNow);
		}
	}
	// 用户首页
	@RequestMapping("index")
	public String userIndex(Model model) {
		model.addAttribute("userNow", user.get());
		return "user/index";
	}

	// 修改信息
	@RequestMapping("update/{uid}")
	public String update(@PathVariable int uid, Model model) {
		User user = userService.findById(uid);
		model.addAttribute("user", user);
		return "user/update";
	}

	// 上传头像
	@RequestMapping("uploadImg")
	@ResponseBody
	public String uploadImg(MultipartFile file, HttpServletRequest request) {
		User userNow = user.get();
		Map<String, String> map = new HashMap<String, String>();
		if (!file.isEmpty()) {
			userService.savaImg(request, userNow, file);
			// 更新数据库
			userService.updateUser(userNow);
			map.put("msg", "yes");
			map.put("code", "1");
			map.put("uri", userNow.getUserimg());
		} else {
			map.put("msg", "no");
			map.put("code", "0");
		}
		return JSONObject.toJSONString(map);
	}

	// 更新信息
	@RequestMapping("userUpdate")
	@ResponseBody
	public String updateUser(@RequestParam String usernickname, @RequestParam String userpass) {
		User userNow = user.get();
		if (!usernickname.equals("") && usernickname != null)
			userNow.setUsernickname(usernickname);
		if (!userpass.equals("") && userpass != null)
			userNow.setUserpass(Md5Token.getInstance().getShortToken(userpass));
		User u = userService.updateUser(userNow);
		String msg;
		if (u != null) {
			msg = "更新成功，下次登录生效！";
		} else {
			msg = "更新失败！请重试。";
		}
		return msg;
	}

	// 收入信息汇总
	// 跳转
	@RequestMapping("incomeManage")
	public String incomeManage() {
		return "user/incomeManage";
	}

	@RequestMapping("addIncome")
	public String addIncome(Model model) {
		// 查询所有分类
		List<InType> inTypeList = userService.findAllIntype();
		model.addAttribute("inTypeList", inTypeList);
		return "user/addIncome";
	}

	// 添加收入
	@RequestMapping("addOneIncome")
	@ResponseBody
	public String addOneIncome(@RequestBody PayIn payIn, ModelMap map) {
		// 创建时间
		payIn.setCreatetime(sf.format(new Date()));
		payIn.setUpdatetime(sf.format(new Date()));
		userService.addOnePayIn(payIn);
		map.put("msg", "添加成功");
		return JSONObject.toJSONString(map, SerializerFeature.IgnoreNonFieldGetter);
	}

	// 查询收入
	@RequestMapping("payInGet")
	@ResponseBody
	public String payInGet(ModelMap model, ModelMap map, int page, int limit) {
		User userNow = user.get();
		int uid = userNow.getUid();
		Page<PayIn> pageList = userService.findAllPayInByAccountId(uid, page, limit);
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", pageList.getTotalElements());
		map.put("data", pageList.getContent());
		return JSONObject.toJSONString(map);
	}

	// 删除一个收入
	@RequestMapping("payinDelete")
	@ResponseBody
	public String payinDelete(int inid) {
		userService.payInDeleteOne(inid);
		return "删除成功！";
	}

	// 修改收入
	@RequestMapping("updatePayIn/{inid}")
	public String updatePayIn(@PathVariable int inid, Model model) {
		PayIn payIn = userService.findPayInById(inid);
		List<InType> inTypeList = userService.findAllIntype();
		model.addAttribute("inTypeList", inTypeList);
		model.addAttribute("payIn", payIn);
		return "user/updatePayIn";
	}

	// 更新收入
	@RequestMapping("updateOneIncome")
	@ResponseBody
	public String updateOneIncome(@RequestBody PayIn payIn, ModelMap map) {
		PayIn p = userService.findPayInById(payIn.getInid());
		// 处理数据
		// 名字
		if (!payIn.getInname().equals("") || payIn.getInname() != null) {
			p.setInname(payIn.getInname());
		}
		// 金额
		if (!payIn.getMoney().equals("") || payIn.getMoney() != null) {
			p.setMoney(payIn.getMoney());
		}
		// 分类
		p.setIntypeid(payIn.getIntypeid());
		// 备注
		if (!payIn.getRemark().equals("") || payIn.getRemark() != null) {
			p.setRemark(payIn.getRemark());
		}
		// 修改时间
		p.setUpdatetime(sf.format(new Date()));
		userService.updatePayIn(p);
		map.put("msg", "更新成功！");
		return JSONObject.toJSONString(map, SerializerFeature.IgnoreNonFieldGetter);
	}

	// 删除多个收入记录
	@RequestMapping("payInDeleteMany")
	@ResponseBody
	public String userDeleteMany(String ids) {
		userService.deletePayInBatch(MyUitl.getInstance().IDStringToList(ids));
		return "删除成功！";
	}

	/**
	 * 支出管理
	 */
	// 添加支出记录 跳转
	@RequestMapping("addExpenditure")
	public String addExpenditure(Model model) {
		// 查询所有支出分类
		List<OutType> outTypeList = userService.findAllOuttype();
		model.addAttribute("outTypeList", outTypeList);
		return "user/addExpenditure";
	}

	// 添加支出
	@RequestMapping("addOneExpenditure")
	@ResponseBody
	public String addOneExpenditure(@RequestBody PayOut payOut, ModelMap map) {
		// 创建时间
		payOut.setCreatetime(sf.format(new Date()));
		payOut.setUpdatetime(sf.format(new Date()));
		userService.addOneExpenditure(payOut);
		map.put("msg", "添加成功");
		return JSONObject.toJSONString(map, SerializerFeature.IgnoreNonFieldGetter);
	}

	// 支出管理
	@RequestMapping("expenditureManage")
	public String expenditureManage() {
		return "user/expenditureManage";
	}

	// 查询所有支出记录
	@RequestMapping("payOutGet")
	@ResponseBody
	public String payOutGet(ModelMap model, ModelMap map, int page, int limit) {
		User userNow = user.get();
		int uid = userNow.getUid();
		Page<PayOut> pageList = userService.findAllPayOutByAccountId(uid, page, limit);
		map.put("code", 0);
		map.put("msg", "");
		map.put("count", pageList.getTotalElements());
		map.put("data", pageList.getContent());
		return JSONObject.toJSONString(map);
	}

	// 修改支出
	@RequestMapping("updatePayOut/{outid}")
	public String updatePayOut(@PathVariable int outid, Model model) {
		PayOut payOut = userService.findPayOutById(outid);
		List<OutType> outTypeList = userService.findAllOuttype();
		model.addAttribute("outTypeList", outTypeList);
		model.addAttribute("payOut", payOut);
		return "user/updatePayOut";
	}

	/// 修改支出记录
	@RequestMapping("updateOneExpenditure")
	@ResponseBody
	public String updateOneExpenditure(@RequestBody PayOut payOut, ModelMap map) {
		PayOut p = userService.findPayOutById(payOut.getOutid());
		// 处理数据
		// 名字
		if (!payOut.getOutname().equals("") || payOut.getOutname() != null) {
			p.setOutname(payOut.getOutname());
		}
		// 金额
		if (!payOut.getMoney().equals("") || payOut.getMoney() != null) {
			p.setMoney(payOut.getMoney());
		}
		// 分类
		p.setOuttypeid(payOut.getOuttypeid());
		// 备注
		if (!payOut.getRemark().equals("") || payOut.getRemark() != null) {
			p.setRemark(payOut.getRemark());
		}
		// 修改时间
		p.setUpdatetime(sf.format(new Date()));
		userService.updatePayOut(p);
		map.put("msg", "更新成功！");
		return JSONObject.toJSONString(map, SerializerFeature.IgnoreNonFieldGetter);
	}

	// 删除一条支出
	@RequestMapping("payoutDelete")
	@ResponseBody
	public String payoutDelete(int outid) {
		userService.payOutDeleteOne(outid);
		return "删除成功！";
	}

	// 删除多个支出
	@RequestMapping("payOutDeleteMany")
	@ResponseBody
	public String payOutDeleteMany(String ids) {
		userService.deletePayOutBatch(MyUitl.getInstance().IDStringToList(ids));
		return "删除成功！";
	}

	/**
	 * 个人账目分析
	 */
	@RequestMapping("personAlanalysis")
	public String personAlanalysis(HttpSession session, ModelMap map) {
		// 获取当年支出
		User userNow = user.get();
		int accountid = userNow.getUid();
		List<PayOut> allPayOutMoneyList = userService.getAllPayOutMoney(accountid, year + "%");
		int allPayOutMoney = 0;
		for (PayOut p : allPayOutMoneyList) {
			allPayOutMoney += new Integer(p.getMoney());
		}
		map.put("allPayOutMoney", allPayOutMoney);
		// 获取当年收入
		List<PayIn> allPayInMoneyList = userService.getAllPayInMoney(accountid, year + "%");
		int allPayInMoney = 0;
		for (PayIn p : allPayInMoneyList) {
			allPayInMoney += new Integer(p.getMoney());
		}
		map.put("allPayInMoney", allPayInMoney);
		// 查询当年每月支出情况
		List<Integer> everyMonthPayOutMoneyList = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			String month;
			if (i < 10) {
				month = year + "-0" + i + "%";
			} else {
				month = year + "-" + i + "%";
			}
			List<PayOut> payOutList = userService.getAllPayOutMoney(accountid, month);
			int sum = 0;
			for (PayOut p : payOutList) {
				sum += new Integer(p.getMoney());
			}
			everyMonthPayOutMoneyList.add(sum);
		}
		map.put("everyMonthPayOutMoneyList", everyMonthPayOutMoneyList);
		// 查询当年每月收入情况
		List<Integer> everyMonthPayInMoneyList = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			String month;
			if (i < 10) {
				month = year + "-0" + i + "%";
			} else {
				month = year + "-" + i + "%";
			}
			List<PayIn> payInList = userService.getAllPayInMoney(accountid, month);
			int sum = 0;
			for (PayIn p : payInList) {
				sum += new Integer(p.getMoney());
			}
			everyMonthPayInMoneyList.add(sum);
		}
		map.put("everyMonthPayInMoneyList", everyMonthPayInMoneyList);
		// 支出分类占比
		List<OutType> outTypeList = userService.findAllOuttype();
		List<String> outTypeCatalog = new ArrayList<>();
		Map<String, Integer> payOutMapByCatalog = new HashMap<>();
		for (OutType o : outTypeList) {
			// 分类
			outTypeCatalog.add(o.getOutname());
			// 按ID查询汇总
			List<PayOut> payOutListByCatalog = userService.getAllPayOutMoneyByOuttypeID(String.valueOf(o.getOutid()),
					accountid);
			int sumByCatalog = 0;
			for (PayOut p : payOutListByCatalog) {
				sumByCatalog += new Integer(p.getMoney());
			}
			payOutMapByCatalog.put(o.getOutname(), sumByCatalog);
		}
		map.put("outTypeCatalog", outTypeCatalog);
		map.put("payOutMapByCatalog", payOutMapByCatalog);
		// 收入分类占比
		List<InType> InTypeList = userService.findAllIntype();
		List<String> inTypeCatalog = new ArrayList<>();
		Map<String, Integer> payInMapByCatalog = new HashMap<>();
		for (InType o : InTypeList) {
			// 分类
			inTypeCatalog.add(o.getItname());
			// 按ID查询汇总
			List<PayIn> payInListByCatalog = userService.getAllPayInMoneyByIntypeID(String.valueOf(o.getItid()),
					accountid);
			int sumByCatalog = 0;
			for (PayIn p : payInListByCatalog) {
				sumByCatalog += new Integer(p.getMoney());
			}
			payInMapByCatalog.put(o.getItname(), sumByCatalog);
		}
		map.put("inTypeCatalog", inTypeCatalog);
		map.put("payInMapByCatalog", payInMapByCatalog);
		return "user/personAlanalysis";
	}
	//查询收入跳转
	@RequestMapping("searchIncome")
	public String toSearchPayIn() {
		return "user/searchPayIn";
	}

}
