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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.kayode.ifypm.lazymodel.ProjectLazyDataModel;
import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ProjectService;
import com.kayode.ifypm.service.UserService;

/**
 * @author AAfolayan
 *
 */
@Named("projectBean")
@ViewScoped
public class ProjectBean implements Serializable {

    public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;
	private static Logger LOG = LoggerFactory.getLogger(ProjectBean.class);
	@Inject
	private UserService userService;
	@Inject
	private ProjectService projectService;
	private Project entry = new Project();
	private LazyDataModel<Project> lazyModel;
	
	private static final String PROJECT_MGT_URL = APP_BASE_NAME + "/online/project/list.xhtml?faces-redirect=true";
	private static final String PROJECT_CREATION_URL = APP_BASE_NAME + "/online/project/create.xhtml?faces-redirect=true";

	private List<Project> entries = new ArrayList<>();
																		
	
	private User user = new User();

	@PostConstruct
	public void init() {
		LOG.info("ProjectBean init!");
	}
	
	public void listProject() {
		LOG.info("listProject invoked!!!");
		try {
			//SecurityUtils.getSubject().checkRole("MDOPS");
			lazyModel = new ProjectLazyDataModel(projectService, QueryType.GET_ALL_PROJECT);
		} catch (Exception e) {
			Messages.addGlobalError("oops error encountered while fetching entries!");
			LOG.error("oops error encountered while fetching entries!", e.fillInStackTrace());
			e.printStackTrace(); //
		}
	}


	public void updateProject() {
		LOG.info("updateProject invoked!!!");
		try {
			//SecurityUtils.getSubject().checkRole("MDOPS");
			LOG.info("entry -> " + entry);
			projectService.updateProject(entry);
			Messages.addFlashGlobalInfo("Project update request processed successfully!");
			entry = new Project();
			Faces.redirect(PROJECT_MGT_URL);
		} catch (Exception e) {
			Messages.addGlobalError("Project update request failed!");
			LOG.error("Project update request failed!", e.fillInStackTrace());
			e.printStackTrace(); // TODO: logger.
			// return null;
		}
	}
	
	
		public void createProject() {
		LOG.info("createProject invoked!!!");
		try {
			//SecurityUtils.getSubject().checkRole("MDOPS");
			LOG.info("entry -> " + entry);
			projectService.createProject(entry);
			Messages.addFlashGlobalInfo("Project creation request processed successfully!");
			entry = new Project();
			Faces.redirect(PROJECT_MGT_URL);
		} catch (Exception e) {
			Messages.addGlobalError("Project creation failed!");
			LOG.error("Project creation failed!", e.fillInStackTrace());
			e.printStackTrace(); // TODO: logger.
			// return null;
		}
	}
	
	public void createNewProjectView() throws IOException {
		LOG.info("createNewProjectView invoked");
		this.entry=new Project();
	}
	
  public void createProjectView() throws IOException {
		LOG.info("createProjectView invoked");
		Faces.redirect(PROJECT_CREATION_URL);
	}
	
		public void displayProjectDialog(Project e) {
		LOG.info("displayProjectDialog invoked!");
		this.entry = e;
		LOG.info("entry selected:  id -> " + this.entry.getId());

	}
	
		public void prepare() {
		LOG.info("prepare method invoked!");
		Flash flash = Faces.getFlash();// FacesContext.getCurrentInstance().getExternalContext().getFlash();
		this.entry = (Project) flash.get("entry");
		LOG.info("selected Project retrieved >>> " + entry);
	}
	
		private User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		String username = String.valueOf(subject.getPrincipal());
		return userService.findByUsername(username);
	}
	
	
		/**
	 * @return the entry
	 */
	public Project getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Project entry) {
		this.entry = entry;
	}
	
		/**
	 * @return the entries
	 */
	public List<Project> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<Project> entries) {
		this.entries = entries;
	}
	
		/**
	 * @return the lazyModel
	 */
	public LazyDataModel<Project> getLazyModel() {
		return lazyModel;
	}

	/**
	 * @param lazyModel the lazyModel to set
	 */
	public void setLazyModel(LazyDataModel<Project> lazyModel) {
		this.lazyModel = lazyModel;
	}
	
	

}
