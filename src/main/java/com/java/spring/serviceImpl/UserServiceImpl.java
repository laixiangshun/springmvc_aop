package com.java.spring.serviceImpl;

import org.springframework.stereotype.Service;

import com.java.spring.annotation.Log;
import com.java.spring.annotation.ServiceLog;
import com.java.spring.entity.User;
import com.java.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Override
	//@Log(name="�����ڷ��ʱ����û�����")
	public String save(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getName());
		return user.getName();
	}

	//@Log(name="������ɾ���û�")
	@ServiceLog(name="����service��ɾ���û�����")
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		throw new NullPointerException();
	}

	@Log(name="service������û�")
	@Override
	public String update(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getName());
		return user.getName();
	}

}
