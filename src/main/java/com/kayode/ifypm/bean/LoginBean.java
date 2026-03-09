package com.kayode.ifypm.bean;

import java.io.Serializable;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Constants;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

@Named("loginBean")
@ViewScoped
public class LoginBean implements Serializable {
	private static Logger LOG = LoggerFactory.getLogger(LoginBean.class);

	private static final long serialVersionUID = 1L;
    public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;

	private static final String DASHBOARD_URL = APP_BASE_NAME+"/online/template/dashboard.xhtml?faces-redirect=true";
	public void logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		LOG.info("User logged out successfully");
		try {
			Faces.redirect(APP_BASE_NAME + "/online/login.xhtml?faces-redirect=true");
		} catch (Exception e) {
			LOG.error("Redirection failed after logout: " + e.getMessage(), e);
			Messages.addFlashGlobalError("An error occurred while logging out. Please try again.");
		}
	}
	

	public void login() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			LOG.info("User is already authenticated");

		} else {
			try {
				subject.login(new UsernamePasswordToken(username, password));
				System.out.println("Login successful for user: " + username);
				Faces.redirect(DASHBOARD_URL);
			} catch (org.apache.shiro.authc.AuthenticationException e) {
				LOG.info("Login failed for user: " + username + ". Reason: " + e.getMessage());
				Messages.addFlashGlobalError("Invalid username or password. Please try again.");
			}
		}

		
	}
	
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
