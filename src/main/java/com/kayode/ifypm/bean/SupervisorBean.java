package com.kayode.ifypm.bean;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.lazymodel.ProposalLazyDataModel;
import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ProposalService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("supervisorBean")
@ViewScoped
public class SupervisorBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;
	private static Logger LOG = LoggerFactory.getLogger(ProposalBean.class);
//	@Inject
//	private UserService userService;
	@Inject
	private ProposalService proposalService;
	private Proposal entry = new Proposal();
	private LazyDataModel<Proposal> lazyModel;
	private User currentUser;
	@Inject
	UserService userService;

	@PostConstruct
	public void init() {
		LOG.info("ProposalBean init!");
		Subject subject = SecurityUtils.getSubject();
		this.setCurrentUser(userService.findUserByUserName(subject.getPrincipal().toString()));
		
	}
	
	
	public void listProposal() {
		LOG.info("listProposal invoked!!!");
		try {
			SecurityUtils.getSubject().checkRole("SUPERVISOR");
			setLazyModel(new ProposalLazyDataModel(proposalService, QueryType.GET_ALL_SUBMITED_PROPOSAL,currentUser.getId()));
		} catch (Exception e) {
			Messages.addGlobalError("oops error encountered while fetching entries!");
			LOG.error("oops error encountered while fetching entries!", e.fillInStackTrace());
			e.printStackTrace(); //
		}
	}


	public LazyDataModel<Proposal> getLazyModel() {
		return lazyModel;
	}


	public void setLazyModel(LazyDataModel<Proposal> lazyModel) {
		this.lazyModel = lazyModel;
	}


	public Proposal getEntry() {
		return entry;
	}


	public void setEntry(Proposal entry) {
		this.entry = entry;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}


	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
