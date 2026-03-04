/**
 * 
 */
package com.kayode.ifypm.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.Flash;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.lazymodel.ProposalLazyDataModel;
import com.kayode.ifypm.model.Proposal;
//import com.kayode.ifypm.model.Role;
//import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ProposalService;
//import com.kayode.ifypm.service.UserService;

/**
 * @author AAfolayan
 *
 */
@Named("proposalBean")
@ViewScoped
public class ProposalBean implements Serializable {

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

	private static final String PROPOSAL_MGT_URL = APP_BASE_NAME + "/online/proposal/list.xhtml?faces-redirect=true";
	private static final String PROPOSAL_CREATION_URL = APP_BASE_NAME
			+ "/online/proposal/create.xhtml?faces-redirect=true";

	private List<Proposal> entries = new ArrayList<>();
	private String title;
	private String methodology;
	private String problemStatement;
	private String aimsAndObjectives;
	private String objective1;
	private String objective2;
	private String objective3;

//	private User user = new User();

	@PostConstruct
	public void init() {
		LOG.info("ProposalBean init!");
	}

	public void submit() {
		String proposal = "Title: " + title + "\n\n" + "Methodology: " + methodology + "\n\n" + "Problem Statement: "
				+ problemStatement + "\n\n" + "Aims and Objectives: " + aimsAndObjectives + "\n\n" + "Objective 1: "
				+ objective1 + "\n\n" + "Objective 2: " + objective2 + "\n\n" + "Objective 3: " + objective3;
		
		System.out.println("proposal -> " + proposal);

	}

	public void listProposal() {
		LOG.info("listProposal invoked!!!");
		try {
			// SecurityUtils.getSubject().checkRole("MDOPS");
			lazyModel = new ProposalLazyDataModel(proposalService, QueryType.GET_ALL_PROPOSAL);
		} catch (Exception e) {
			Messages.addGlobalError("oops error encountered while fetching entries!");
			LOG.error("oops error encountered while fetching entries!", e.fillInStackTrace());
			e.printStackTrace(); //
		}
	}

	public void updateProposal() {
		LOG.info("updateProposal invoked!!!");
		try {
			// SecurityUtils.getSubject().checkRole("MDOPS");
			LOG.info("entry -> " + entry);
			proposalService.updateProposal(entry);
			Messages.addFlashGlobalInfo("Proposal update request processed successfully!");
			entry = new Proposal();
			Faces.redirect(PROPOSAL_MGT_URL);
		} catch (Exception e) {
			Messages.addGlobalError("Proposal update request failed!");
			LOG.error("Proposal update request failed!", e.fillInStackTrace());
			e.printStackTrace(); // TODO: logger.
			// return null;
		}
	}

	public void createProposal() {
		LOG.info("createProposal invoked!!!");
		try {
			// SecurityUtils.getSubject().checkRole("MDOPS");
			LOG.info("entry -> " + entry);
			proposalService.createProposal(entry);
			Messages.addFlashGlobalInfo("Proposal creation request processed successfully!");
			entry = new Proposal();
			Faces.redirect(PROPOSAL_MGT_URL);
		} catch (Exception e) {
			Messages.addGlobalError("Proposal creation failed!");
			LOG.error("Proposal creation failed!", e.fillInStackTrace());
			e.printStackTrace(); // TODO: logger.
			// return null;
		}
	}

	public void createNewProposalView() throws IOException {
		LOG.info("createNewProposalView invoked");
		this.entry = new Proposal();
	}

	public void createProposalView() throws IOException {
		LOG.info("createProposalView invoked");
		Faces.redirect(PROPOSAL_CREATION_URL);
	}

	public void displayProposalDialog(Proposal e) {
		LOG.info("displayProposalDialog invoked!");
		this.entry = e;
		LOG.info("entry selected:  id -> " + this.entry.getId());

	}

	public void prepare() {
		LOG.info("prepare method invoked!");
		Flash flash = Faces.getFlash();// FacesContext.getCurrentInstance().getExternalContext().getFlash();
		this.entry = (Proposal) flash.get("entry");
		LOG.info("selected Proposal retrieved >>> " + entry);
	}

//		private User getCurrentUser() {
//		Subject subject = SecurityUtils.getSubject();
//		String username = String.valueOf(subject.getPrincipal());
//		return userService.findByUsername(username);
//	}

	/**
	 * @return the entry
	 */
	public Proposal getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Proposal entry) {
		this.entry = entry;
	}

	/**
	 * @return the entries
	 */
	public List<Proposal> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<Proposal> entries) {
		this.entries = entries;
	}

	/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Proposal> getLazyModel() {
		return lazyModel;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Proposal> lazyModel) {
		this.lazyModel = lazyModel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the methodology
	 */
	public String getMethodology() {
		return methodology;
	}

	/**
	 * @param methodology the methodology to set
	 */
	public void setMethodology(String methodology) {
		this.methodology = methodology;
	}

	/**
	 * @return the problemStatement
	 */
	public String getProblemStatement() {
		return problemStatement;
	}

	/**
	 * @param problemStatement the problemStatement to set
	 */
	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
	}

	public String getObjective1() {
		return objective1;
	}

	public void setObjective1(String objective1) {
		this.objective1 = objective1;
	}

	public String getObjective2() {
		return objective2;
	}

	public void setObjective2(String objective2) {
		this.objective2 = objective2;
	}

	public String getObjective3() {
		return objective3;
	}

	public void setObjective3(String objective3) {
		this.objective3 = objective3;
	}

	/**
	 * @return the aimsAndObjectives
	 */
	public String getAimsAndObjectives() {
		return aimsAndObjectives;
	}

	/**
	 * @param aimsAndObjectives the aimsAndObjectives to set
	 */
	public void setAimsAndObjectives(String aimsAndObjectives) {
		this.aimsAndObjectives = aimsAndObjectives;
	}

}
