package com.java.spring.service;

import com.java.spring.entity.User;

public interface UserService {

	public String save(User user);
	
	public void delete();
	
	public String update(User user);
}
