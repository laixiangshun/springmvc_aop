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
 * ע�⣺

@Before �C Ŀ�귽��ִ��ǰִ��

@After �C Ŀ�귽��ִ�к�ִ��

@AfterReturning �C Ŀ�귽�����غ�ִ�У���������쳣��ִ��

@AfterThrowing �C �쳣ʱִ��

@Around �C ��ִ����������������ͬʱҲִ���������

 * SpringMVC���Ҫʹ��AOPע�⣬���뽫<aop:aspectj-autoproxy proxy-target-class="true"/>
 * @author lailai
 *
 */
@Controller
@RequestMapping("/aop")
public class SpringController {

	@Autowired
	private UserService userService;
	
	
	@Log(name="���ڷ���aop2����")
	@ResponseBody
	@RequestMapping(value="/aop2")
	public String aop2(String name) throws Exception{
		Thread.sleep(1000L);
		User user=new User();
		user.setName(name);
		String result=userService.save(user);
		return result;
	}
	//@Log(name="���ڷ���aop3����")
	@ResponseBody
	@RequestMapping(value="/aop.delete")
	public String aop3(HttpServletResponse response)throws Exception{
		this.userService.delete();
		String message="ɾ��ʧ��";
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
