package cn.mtjmtj7.bs.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import cn.mtjmtj7.bs.bean.User;
import cn.mtjmtj7.bs.service.UserService;
import cn.mtjmtj7.bs.util.Md5Token;

@Controller
@SessionAttributes(value= {"userNow","adminNow"})
public class IndexController {

	@Autowired
	private UserService userService;
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("test")
	public String test() {
		return "test";
	}
	/**
	 * 首页
	 * 
	 * @return
	 */

	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("login")
	public String login(User user,Model model) {
		User userNow = null;
		boolean flag = false;
		//加密登陆密码
		user.setUserpass(Md5Token.getInstance().getShortToken(user.getUserpass()));
		userNow = userService.login(user);
		if(userNow != null) {
			flag = true;
		}
		
		if(flag) {
			//登陆成功
			if(userNow.getUsertype().equals("0")) {
				//管理员
				model.addAttribute("adminNow", userNow);
				return "redirect:admin/index";
			}
			else {
				//普通用户登陆成功
				model.addAttribute("userNow", userNow);
				//设置登录时间
				userNow.setLastlogintime(sf.format(new Date()));
				userService.updateUser(userNow);
				return "redirect:user/index";
			}
		}
		else {
			//登陆失败
			model.addAttribute("user", user);
			model.addAttribute("message", "用户名或密码错误");
			return "index";
		}
	}
	
	@RequestMapping("toLogin")
	public String toLogin() {
		return "toLogin";
	}
	/**
	 * 注销
	 */
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.setMaxInactiveInterval(1);
		return "index";
	}
}
