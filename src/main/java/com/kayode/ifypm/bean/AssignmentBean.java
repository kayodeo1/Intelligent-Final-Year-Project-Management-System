package com.kayode.ifypm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    // studentId → supervisorId as String (null or "" = unassigned)
    // String avoids JSF type-erasure: Map<Long,Long> loses generics at runtime and
    // JSF puts submitted String values in, causing ClassCastException on read-back.
    private Map<Long, String> selectedSupervisorIds = new HashMap<>();

    // for random bulk assign
    private List<Long> selectedSupervisorsForRandom = new ArrayList<>();

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
                s.getSupervisor() != null ? String.valueOf(s.getSupervisor().getId()) : null);
        }
    }

    public void saveAssignment(Long studentId) {
        try {
            String svIdStr = selectedSupervisorIds.get(studentId);
            Long svId = (svIdStr == null || svIdStr.isBlank()) ? null : Long.parseLong(svIdStr);
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

    public void randomAssign() {
        if (selectedSupervisorsForRandom == null || selectedSupervisorsForRandom.isEmpty()) {
            Messages.addGlobalError("Please select at least one supervisor.");
            return;
        }
        // Use selectedSupervisorIds map (populated during loadStudents) — avoids lazy-load issues
        List<User> unassigned = students.stream()
            .filter(s -> { String v = selectedSupervisorIds.get(s.getId()); return v == null || v.isBlank(); })
            .collect(java.util.stream.Collectors.toList());
        if (unassigned.isEmpty()) {
            Messages.addGlobalInfo("All students in this session are already assigned.");
            return;
        }
        List<User> targets = supervisors.stream()
            .filter(sv -> selectedSupervisorsForRandom.contains(sv.getId()))
            .collect(java.util.stream.Collectors.toList());
        if (targets.isEmpty()) {
            Messages.addGlobalError("None of the selected supervisors were found.");
            return;
        }
        Random rng = new Random();
        Collections.shuffle(unassigned, rng);   // randomise student order
        Collections.shuffle(targets, rng);       // randomise which supervisor is "first"
        int svCount = targets.size();
        try {
            for (int i = 0; i < unassigned.size(); i++) {
                User student = unassigned.get(i);
                User supervisor = targets.get(i % svCount);
                student.setSupervisor(supervisor);
                userService.update(student);
            }
            Messages.addGlobalInfo(unassigned.size() + " student(s) randomly distributed across "
                + svCount + " supervisor(s).");
            selectedSupervisorsForRandom.clear();
            loadStudents();
        } catch (Exception e) {
            LOG.error("Random assign error", e);
            Messages.addGlobalError("Random assignment failed: " + e.getMessage());
        }
    }

    public void deassignAll() {
        if (students.isEmpty()) {
            Messages.addGlobalError("No students loaded. Select a session first.");
            return;
        }
        int count = 0;
        try {
            for (User s : students) {
                String v = selectedSupervisorIds.get(s.getId());
                if (v != null && !v.isBlank()) {
                    s.setSupervisor(null);
                    userService.update(s);
                    count++;
                }
            }
            if (count == 0) {
                Messages.addGlobalInfo("No students were assigned — nothing to deassign.");
            } else {
                Messages.addGlobalInfo(count + " student(s) deassigned from their supervisors.");
            }
            loadStudents();
        } catch (Exception e) {
            LOG.error("deassignAll error", e);
            Messages.addGlobalError("Failed to deassign students: " + e.getMessage());
        }
    }

    private User findStudentById(Long id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    private User findSupervisorById(Long id) {
        return supervisors.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean isUnassigned(User s) {
        String v = selectedSupervisorIds.get(s.getId());
        return v == null || v.isBlank();
    }

    public long countUnassigned() {
        return students.stream()
            .filter(s -> { String v = selectedSupervisorIds.get(s.getId()); return v == null || v.isBlank(); })
            .count();
    }

    public long countAssigned() {
        return students.stream()
            .filter(s -> { String v = selectedSupervisorIds.get(s.getId()); return v != null && !v.isBlank(); })
            .count();
    }

    // Getters / setters
    public List<User> getStudents() { return students; }
    public List<User> getSupervisors() { return supervisors; }
    public List<String> getSessions() { return sessions; }
    public String getSelectedSession() { return selectedSession; }
    public void setSelectedSession(String s) { this.selectedSession = s; }
    public Map<Long, String> getSelectedSupervisorIds() { return selectedSupervisorIds; }
    public void setSelectedSupervisorIds(Map<Long, String> m) { this.selectedSupervisorIds = m; }
    public List<Long> getSelectedSupervisorsForRandom() { return selectedSupervisorsForRandom; }
    public void setSelectedSupervisorsForRandom(List<Long> ids) { this.selectedSupervisorsForRandom = ids; }
}
