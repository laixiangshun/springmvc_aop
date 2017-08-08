package com.java.spring.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.java.spring.annotation.Log;
import com.java.spring.annotation.ServiceLog;
import com.java.spring.entity.User;
import com.java.spring.service.UserService;
/**
 * 注解：

@Before – 目标方法执行前执行

@After – 目标方法执行后执行

@AfterReturning – 目标方法返回后执行，如果发生异常不执行

@AfterThrowing – 异常时执行

@Around – 在执行上面其他操作的同时也执行这个方法

 * SpringMVC如果要使用AOP注解，必须将<aop:aspectj-autoproxy proxy-target-class="true"/>
 * @author lailai
 *
 */
@Controller
@RequestMapping("/aop")
public class SpringController {

	@Autowired
	private UserService userService;
	
	
	@Log(name="你在访问aop2方法")
	@ResponseBody
	@RequestMapping(value="/aop2")
	public String aop2(String name) throws Exception{
		Thread.sleep(1000L);
		User user=new User();
		user.setName(name);
		String result=userService.save(user);
		return result;
	}
	//@Log(name="你在访问aop3方法")
	@ResponseBody
	@RequestMapping(value="/aop.delete")
	public String aop3(HttpServletResponse response)throws Exception{
		this.userService.delete();
		String message="删除失败";
		return message;
	}
	@ResponseBody
	@RequestMapping(value="/aop3")
	public String aop4(String name)throws Exception{
		User user=new User();
		user.setName(name);
		String result=userService.update(user);
		return result;
	}
}
