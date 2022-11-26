package com.az.backend;

import com.az.backend.mapper.UserMapper;
import com.az.backend.pojo.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

	@Resource
	private UserMapper userMapper;

	@Test
	void contextLoads() {
		System.out.println(("----- selectAll method test ------"));
		List<User> userList = userMapper.selectList(null);
		Assert.assertEquals(5, userList.size());
		userList.forEach(System.out::println);

	}

}
