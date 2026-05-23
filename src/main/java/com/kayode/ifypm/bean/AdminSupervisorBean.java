package com.kayode.ifypm.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.Status;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ProjectService;
import com.kayode.ifypm.service.ProposalService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("adminSupervisorBean")
@ViewScoped
public class AdminSupervisorBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AdminSupervisorBean.class);

    @Inject private UserService userService;
    @Inject private ProposalService proposalService;
    @Inject private ProjectService projectService;

    private List<User> supervisors = new ArrayList<>();
    private long supervisorCount;
    private Map<Long, Long> studentCounts = new HashMap<>();

    private User newSupervisor = new User();
    private String newSupervisorPassword;
    private String uploadDefaultDepartment;

    // Detail view state
    private User selectedSupervisor;
    private List<User> selectedSupervisorStudents = new ArrayList<>();
    private User selectedStudent;
    private List<Proposal> selectedStudentProposals = new ArrayList<>();
    private Project selectedStudentProject;

    @PostConstruct
    public void init() {
        LOG.info("AdminSupervisorBean init");
        reload();
    }

    private void reload() {
        supervisors = userService.findAllByRole(Role.SUPERVISOR);
        supervisorCount = supervisors.size();
        studentCounts.clear();
        for (User sv : supervisors) {
            studentCounts.put(sv.getId(), userService.countStudentsBySupervisorId(sv.getId()));
        }
    }

    public long getStudentCount(Long supervisorId) {
        return studentCounts.getOrDefault(supervisorId, 0L);
    }

    public void saveSupervisor() {
        try {
            if (userService.usernameExists(newSupervisor.getUsername())) {
                Messages.addGlobalError("Username '" + newSupervisor.getUsername() + "' is already taken.");
                return;
            }
            List<Role> roles = new ArrayList<>();
            roles.add(Role.SUPERVISOR);
            newSupervisor.setRoles(roles);
            newSupervisor.setStatus(Status.ENABLED);
            String pwd = (newSupervisorPassword != null && !newSupervisorPassword.isBlank())
                    ? newSupervisorPassword : newSupervisor.getUsername();
            newSupervisor.setPassword(pwd);
            userService.createUser(newSupervisor);
            Messages.addGlobalInfo("Supervisor '" + newSupervisor.getFullName() + "' added successfully.");
            reload();
            newSupervisor = new User();
            newSupervisorPassword = null;
        } catch (Exception e) {
            LOG.error("Error saving supervisor", e);
            Messages.addGlobalError("Failed to add supervisor. Please check the details and try again.");
        }
    }

    public void onSupervisorSelect(SelectEvent<User> event) {
        selectedSupervisor = event.getObject();
        selectedSupervisorStudents = userService.findStudentsBySupervisorId(selectedSupervisor.getId());
        selectedStudent = null;
        selectedStudentProposals = new ArrayList<>();
        selectedStudentProject = null;
    }

    public void onSupervisorStudentSelect(SelectEvent<User> event) {
        Long id = event.getObject().getId();
        selectedStudent = userService.findUserWithSupervisor(id);
        selectedStudentProposals = proposalService.findAllByStudentId(id);
        selectedStudentProject = selectedStudent != null && selectedStudent.getProjectlId() != null
                ? projectService.findProject(selectedStudent.getProjectlId()) : null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (event.getFile() == null) return;
        int created = 0;
        int skipped = 0;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(event.getFile().getInputStream()))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 4) { skipped++; continue; }
                User u = new User();
                u.setFirstname(p[0].trim());
                u.setLastname(p[1].trim());
                u.setEmail(p[2].trim());
                u.setUsername(p[3].trim().toLowerCase());
                if (userService.usernameExists(u.getUsername())) { skipped++; continue; }
                String pwd = p.length > 4 && !p[4].trim().isEmpty() ? p[4].trim() : u.getUsername();
                u.setPassword(pwd);
                String dept = p.length > 5 && !p[5].trim().isEmpty() ? p[5].trim() : uploadDefaultDepartment;
                if (dept != null) u.setDepartment(dept);
                List<Role> roles = new ArrayList<>();
                roles.add(Role.SUPERVISOR);
                u.setRoles(roles);
                u.setStatus(Status.ENABLED);
                userService.createUser(u);
                created++;
            }
            Messages.addGlobalInfo(created + " supervisor(s) imported" +
                    (skipped > 0 ? ", " + skipped + " row(s) skipped (duplicate username or bad format)." : "."));
            reload();
            uploadDefaultDepartment = null;
        } catch (Exception e) {
            LOG.error("CSV upload error", e);
            Messages.addGlobalError("Upload failed: " + e.getMessage());
        }
    }

    public List<User> getSupervisors() { return supervisors; }
    public long getSupervisorCount() { return supervisorCount; }
    public User getNewSupervisor() { return newSupervisor; }
    public void setNewSupervisor(User s) { this.newSupervisor = s; }
    public String getNewSupervisorPassword() { return newSupervisorPassword; }
    public void setNewSupervisorPassword(String p) { this.newSupervisorPassword = p; }
    public String getUploadDefaultDepartment() { return uploadDefaultDepartment; }
    public void setUploadDefaultDepartment(String d) { this.uploadDefaultDepartment = d; }
    public User getSelectedSupervisor() { return selectedSupervisor; }
    public void setSelectedSupervisor(User sv) { this.selectedSupervisor = sv; }
    public List<User> getSelectedSupervisorStudents() { return selectedSupervisorStudents; }
    public User getSelectedStudent() { return selectedStudent; }
    public void setSelectedStudent(User s) { this.selectedStudent = s; }
    public List<Proposal> getSelectedStudentProposals() { return selectedStudentProposals; }
    public Project getSelectedStudentProject() { return selectedStudentProject; }
}
