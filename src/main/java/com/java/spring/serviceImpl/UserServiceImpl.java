package com.java.spring.serviceImpl;

import org.springframework.stereotype.Service;

import com.java.spring.annotation.Log;
import com.java.spring.annotation.ServiceLog;
import com.java.spring.entity.User;
import com.java.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	//@Log(name="你正在访问保存用户操作")
	public String save(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getName());
		return user.getName();
	}

	//@Log(name="你正在删除用户")
	@ServiceLog(name="你在service层删除用户操作")
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		throw new NullPointerException();
	}

	@Log(name="service层更新用户")
	@Override
	public String update(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getName());
		return user.getName();
	}

}
