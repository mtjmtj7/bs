package cn.mtjmtj7.bs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.mtjmtj7.bs.intercepter.AdminIntercepter;
import cn.mtjmtj7.bs.intercepter.UserIntercepter;

@Configuration
public class UserConfig extends WebMvcConfigurerAdapter {

	/**
	 * user拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		 registry.addInterceptor(new UserIntercepter()).addPathPatterns("/user/**");
	}

}
