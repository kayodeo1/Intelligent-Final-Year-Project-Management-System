package com.kayode.ifypm.bean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("adminStudentBean")
@ViewScoped
public class AdminStudentBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AdminStudentBean.class);

    @Inject private UserService userService;
    @Inject private ProposalService proposalService;
    @Inject private ProjectService projectService;

    private List<User> students = new ArrayList<>();
    private List<String> sessions = new ArrayList<>();
    private String selectedSession;
    private User newStudent = new User();
    private String newStudentPassword;
    private String uploadDefaultSession;

    // Detail view state
    private User selectedStudent;
    private List<Proposal> selectedStudentProposals = new ArrayList<>();
    private Project selectedStudentProject;
    private User selectedSupervisor;
    private List<User> selectedSupervisorStudents = new ArrayList<>();

    @PostConstruct
    public void init() {
        LOG.info("AdminStudentBean init");
        refreshSessions();
        students = userService.findAllByRole(Role.STUDENT);
    }

    private void refreshSessions() {
        sessions = userService.findDistinctSessions();
    }

    public void selectSession(String session) {
        this.selectedSession = session;
        loadStudents();
    }

    public void clearSessionFilter() {
        this.selectedSession = null;
        students = userService.findAllByRole(Role.STUDENT);
    }

    public void onSessionChange(AjaxBehaviorEvent event) {
        loadStudents();
    }

    public void loadStudents() {
        if (selectedSession == null || selectedSession.isBlank()) {
            students = userService.findAllByRole(Role.STUDENT);
        } else {
            students = userService.findStudentsBySession(selectedSession);
        }
    }

    public void saveStudent() {
        System.out.println("okayy... saving students");
        try {
            String matric = newStudent.getMatricNumber();
            if (matric == null || matric.isBlank()) {
                Messages.addGlobalError("Matric Number is required.");
                return;
            }
            String generatedUsername = matric.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (userService.usernameExists(generatedUsername)) {
                Messages.addGlobalError("A student with matric number '" + matric + "' already exists.");
                return;
            }
            newStudent.setUsername(generatedUsername);
            List<Role> roles = new ArrayList<>();
            roles.add(Role.STUDENT);
            newStudent.setRoles(roles);
            newStudent.setStatus(Status.ENABLED);
            String pwd = (newStudentPassword != null && !newStudentPassword.isBlank())
                    ? newStudentPassword
                    : (newStudent.getLastname() != null && !newStudent.getLastname().isBlank()
                        ? newStudent.getLastname().toLowerCase() : generatedUsername);
            newStudent.setPassword(pwd);
            userService.createUser(newStudent);
            Messages.addGlobalInfo("Student '" + newStudent.getFullName() + "' added successfully.");
            refreshSessions();
            loadStudents();
            newStudent = new User();
            newStudentPassword = null;
        } catch (Exception e) {
            LOG.error("Error saving student", e);
            Messages.addGlobalError("Failed to add student. Please check the details and try again.");
        }
    }

    public void onStudentSelect(SelectEvent<User> event) {
        Long id = event.getObject().getId();
        selectedStudent = userService.findUserWithSupervisor(id);
        selectedStudentProposals = proposalService.findAllByStudentId(id);
        selectedStudentProject = selectedStudent != null && selectedStudent.getProjectlId() != null
                ? projectService.findProject(selectedStudent.getProjectlId()) : null;
        selectedSupervisor = null;
        selectedSupervisorStudents = new ArrayList<>();
    }

    public void viewSupervisor() {
        if (selectedStudent != null && selectedStudent.getSupervisor() != null) {
            selectedSupervisor = selectedStudent.getSupervisor();
            selectedSupervisorStudents = userService.findStudentsBySupervisorId(selectedSupervisor.getId());
        }
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
                if (p.length > 5) u.setDepartment(p[5].trim());
                if (p.length > 6) u.setMatricNumber(p[6].trim());
                String sess = p.length > 7 && !p[7].trim().isEmpty() ? p[7].trim() : uploadDefaultSession;
                u.setSession(sess);
                List<Role> roles = new ArrayList<>();
                roles.add(Role.STUDENT);
                u.setRoles(roles);
                u.setStatus(Status.ENABLED);
                userService.createUser(u);
                created++;
            }
            Messages.addGlobalInfo(created + " student(s) imported" +
                    (skipped > 0 ? ", " + skipped + " row(s) skipped (duplicate username or bad format)." : "."));
            refreshSessions();
            loadStudents();
            uploadDefaultSession = null;
        } catch (Exception e) {
            LOG.error("CSV upload error", e);
            Messages.addGlobalError("Upload failed: " + e.getMessage());
        }
    }

    public List<User> getStudents() { return students; }
    public List<String> getSessions() { return sessions; }
    public String getSelectedSession() { return selectedSession; }
    public void setSelectedSession(String selectedSession) { this.selectedSession = selectedSession; }
    public User getNewStudent() { return newStudent; }
    public void setNewStudent(User newStudent) { this.newStudent = newStudent; }
    public String getNewStudentPassword() { return newStudentPassword; }
    public void setNewStudentPassword(String newStudentPassword) { this.newStudentPassword = newStudentPassword; }
    public String getUploadDefaultSession() { return uploadDefaultSession; }
    public void setUploadDefaultSession(String uploadDefaultSession) { this.uploadDefaultSession = uploadDefaultSession; }
    public User getSelectedStudent() { return selectedStudent; }
    public void setSelectedStudent(User selectedStudent) { this.selectedStudent = selectedStudent; }
    public List<Proposal> getSelectedStudentProposals() { return selectedStudentProposals; }
    public Project getSelectedStudentProject() { return selectedStudentProject; }
    public User getSelectedSupervisor() { return selectedSupervisor; }
    public List<User> getSelectedSupervisorStudents() { return selectedSupervisorStudents; }
}
