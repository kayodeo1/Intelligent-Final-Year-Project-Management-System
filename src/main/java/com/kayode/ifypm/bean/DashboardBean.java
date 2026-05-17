package com.kayode.ifypm.bean;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {
	private static Logger LOG = LoggerFactory.getLogger(DashboardBean.class);
	@Inject
	UserService userService;
	
	private static final long serialVersionUID = 1L;
	public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;
	private User currentUser;
	
	@PostConstruct
	public void init() {
		Subject subject = SecurityUtils.getSubject();
		setCurrentUser(userService.findUserByUserName(subject.getPrincipal().toString()));
		System.out.println(currentUser.getProposalStatus());
		
		
		
	}
	
	
	
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
