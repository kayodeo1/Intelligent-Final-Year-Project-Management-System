package com.kayode.ifypm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("assignmentBean")
@ViewScoped
public class AssignmentBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(AssignmentBean.class);

    @Inject
    private UserService userService;

    private List<User> students = new ArrayList<>();
    private List<User> supervisors = new ArrayList<>();
    private List<String> sessions = new ArrayList<>();
    private String selectedSession;

    // studentId → supervisorId (null = unassigned)
    private Map<Long, Long> selectedSupervisorIds = new HashMap<>();

    // for bulk assign
    private Long bulkSupervisorId;

    @PostConstruct
    public void init() {
        sessions = userService.findDistinctSessions();
        supervisors = userService.findAllByRole(Role.SUPERVISOR);
    }

    public void loadStudents() {
        if (selectedSession == null || selectedSession.isBlank()) {
            students = userService.findAllByRole(Role.STUDENT);
        } else {
            students = userService.findStudentsBySession(selectedSession);
        }
        selectedSupervisorIds.clear();
        for (User s : students) {
            selectedSupervisorIds.put(s.getId(),
                s.getSupervisor() != null ? s.getSupervisor().getId() : null);
        }
    }

    public void saveAssignment(Long studentId) {
        try {
            Long svId = selectedSupervisorIds.get(studentId);
            User student = findStudentById(studentId);
            if (student == null) {
                Messages.addGlobalError("Student not found.");
                return;
            }
            if (svId == null) {
                student.setSupervisor(null);
                userService.update(student);
                Messages.addGlobalInfo(student.getFullName() + " — supervisor removed.");
            } else {
                User supervisor = findSupervisorById(svId);
                if (supervisor == null) {
                    Messages.addGlobalError("Selected supervisor not found.");
                    return;
                }
                student.setSupervisor(supervisor);
                userService.update(student);
                Messages.addGlobalInfo(student.getFullName() + " assigned to " + supervisor.getFullName() + ".");
            }
            loadStudents();
        } catch (Exception e) {
            LOG.error("Error saving assignment", e);
            Messages.addGlobalError("Failed to save assignment. Please try again.");
        }
    }

    public void bulkAssign() {
        if (bulkSupervisorId == null) {
            Messages.addGlobalError("Please select a supervisor for bulk assignment.");
            return;
        }
        User supervisor = findSupervisorById(bulkSupervisorId);
        if (supervisor == null) {
            Messages.addGlobalError("Selected supervisor not found.");
            return;
        }
        int count = 0;
        try {
            for (User s : students) {
                if (s.getSupervisor() == null) {
                    s.setSupervisor(supervisor);
                    userService.update(s);
                    selectedSupervisorIds.put(s.getId(), bulkSupervisorId);
                    count++;
                }
            }
            Messages.addGlobalInfo(count + " unassigned student(s) assigned to " + supervisor.getFullName() + ".");
            loadStudents();
        } catch (Exception e) {
            LOG.error("Error bulk assigning", e);
            Messages.addGlobalError("Bulk assignment failed: " + e.getMessage());
        }
    }

    private User findStudentById(Long id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    private User findSupervisorById(Long id) {
        return supervisors.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean isUnassigned(User s) {
        return s.getSupervisor() == null;
    }

    public long countUnassigned() {
        return students.stream().filter(s -> s.getSupervisor() == null).count();
    }

    // Getters / setters
    public List<User> getStudents() { return students; }
    public List<User> getSupervisors() { return supervisors; }
    public List<String> getSessions() { return sessions; }
    public String getSelectedSession() { return selectedSession; }
    public void setSelectedSession(String s) { this.selectedSession = s; }
    public Map<Long, Long> getSelectedSupervisorIds() { return selectedSupervisorIds; }
    public void setSelectedSupervisorIds(Map<Long, Long> m) { this.selectedSupervisorIds = m; }
    public Long getBulkSupervisorId() { return bulkSupervisorId; }
    public void setBulkSupervisorId(Long id) { this.bulkSupervisorId = id; }
}
