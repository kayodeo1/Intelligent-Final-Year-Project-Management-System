package com.kayode.ifypm.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.lazymodel.ProposalLazyDataModel;
import com.kayode.ifypm.lazymodel.UserLazyDataModel;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ProjectService;
import com.kayode.ifypm.service.ProposalService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("adminBean")
@ViewScoped
public class AdminBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(AdminBean.class);

	@Inject
	private UserService userService;
	@Inject
	private ProposalService proposalService;
	@Inject
	private ProjectService projectService;
	private LazyDataModel<Proposal> lazyModel;

	private LazyDataModel<User> studentsModel;
	private LazyDataModel<User> supervisorsModel;

	private long studentCount;
	private long supervisorCount;
	private long proposalCount;
	private long projectCount;
	private User newStudent;

	private Map<Long, Long> supervisorStudentCounts = new HashMap<>();

	@PostConstruct
	public void init() {
		LOG.info("AdminBean init");
		studentCount = userService.countByRole(Role.STUDENT);
		supervisorCount = userService.countByRole(Role.SUPERVISOR);
		proposalCount = proposalService.fetchProposalCount().longValue();
		projectCount = projectService.fetchProjectCount().longValue();
		studentsModel = new UserLazyDataModel(userService, Role.STUDENT);
		supervisorsModel = new UserLazyDataModel(userService, Role.SUPERVISOR);
		newStudent = new User();
		List<User> sups = userService.findAllByRole(Role.SUPERVISOR);
		for (User sv : sups) {
			supervisorStudentCounts.put(sv.getId(), userService.countStudentsBySupervisorId(sv.getId()));
		}
	}
	public void listStudentsProposal() {
		LOG.info("listProposal invoked!!!");
		try {
			SecurityUtils.getSubject().checkRole("ADMIN");
			setLazyModel(new ProposalLazyDataModel(proposalService, QueryType.GET_ALL_PROPOSAL));
		} catch (Exception e) {
			Messages.addGlobalError("oops error encountered while fetching entries!");
			LOG.error("oops error encountered while fetching entries!", e.fillInStackTrace());
			e.printStackTrace(); //
		}
	}

	public long getSupervisorStudentCount(Long supervisorId) {
		return supervisorStudentCounts.getOrDefault(supervisorId, 0L);
	}

	public void saveStudent() {
		userService.createUser(newStudent);

	}

	public LazyDataModel<User> getStudentsModel() {
		return studentsModel;
	}

	public LazyDataModel<User> getSupervisorsModel() {
		return supervisorsModel;
	}

	public long getStudentCount() {
		return studentCount;
	}

	public long getSupervisorCount() {
		return supervisorCount;
	}

	public long getProposalCount() {
		return proposalCount;
	}

	public long getProjectCount() {
		return projectCount;
	}

	public User getNewStudent() {
		return newStudent;
	}

	public void setNewStudent(User newStudent) {
		this.newStudent = newStudent;
	}
	public LazyDataModel<Proposal> getLazyModel() {
		return lazyModel;
	}
	public void setLazyModel(LazyDataModel<Proposal> lazyModel) {
		this.lazyModel = lazyModel;
	}

}
