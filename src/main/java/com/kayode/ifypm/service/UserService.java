package com.kayode.ifypm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserService {
	
	@PersistenceContext(unitName = "app")
	private EntityManager em;


	private static Logger LOG = LoggerFactory.getLogger(ProjectService.class);
	
	
	
	public User findUserByUserName(String username) {
		List<User> found = em.createQuery("select u from User u where u.username=:username", User.class)
				.setParameter("username", username).getResultList();
		return found.isEmpty() ? null : found.get(0);
		
		
	}

}
