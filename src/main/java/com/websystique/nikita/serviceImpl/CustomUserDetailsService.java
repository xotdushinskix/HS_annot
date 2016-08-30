package com.websystique.nikita.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.websystique.nikita.dao.UserDao;
import com.websystique.nikita.model.Role;
import com.websystique.nikita.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String login)
			throws UsernameNotFoundException {
		User user = null;
		try {
			user = userDao.getUserByLogin(login);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(user == null){
			throw new UsernameNotFoundException("Username not found");
		}
			return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
				 user.getPosition().equals("Active"), true, true, true, getGrantedAuthorities(user));
	}

	
	private List<GrantedAuthority> getGrantedAuthorities(User user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role : user.getRoles()){
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleType()));
		}
		return authorities;
	}
	
}