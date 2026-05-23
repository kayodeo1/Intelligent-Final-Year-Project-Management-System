package com.kayode.ifypm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.lazymodel.ProposalLazyDataModel;
import com.kayode.ifypm.model.Chapter;
import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.model.ProgressStatus;
import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.Status;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ChapterService;
import com.kayode.ifypm.service.ProjectService;
import com.kayode.ifypm.service.ProposalService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("supervisorBean")
@ViewScoped
public class SupervisorBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;
    private static final Logger LOG = LoggerFactory.getLogger(SupervisorBean.class);

    @Inject private ProposalService proposalService;
    @Inject private UserService userService;
    @Inject private ProjectService projectService;
    @Inject private ChapterService chapterService;

    // Current logged-in supervisor
    private User currentUser;

    // Dashboard stats
    private long myStudentCount;
    private long pendingProposalCount;
    private long approvedProposalCount;
    private long rejectedProposalCount;
    private long flaggedProposalCount;
    private long activeProjectCount;
    private List<Proposal> recentProposals = new ArrayList<>();
    private List<Chapter> recentChapters = new ArrayList<>();
    private List<Long> myStudentIds = new ArrayList<>();
    private List<User> myStudents = new ArrayList<>();

    // Proposals list
    private LazyDataModel<Proposal> lazyModel;
    private Proposal entry = new Proposal();
    private String rejectComment;

    // Chapters list + selected
    private List<Chapter> chapters = new ArrayList<>();
    private Chapter selectedChapter;
    private String chapterRejectComment;

    @PostConstruct
    public void init() {
        LOG.info("SupervisorBean init");
        Subject subject = SecurityUtils.getSubject();
        currentUser = userService.findUserByUserName(subject.getPrincipal().toString());
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        Long svId = currentUser.getId();
        myStudentCount = userService.countStudentsBySupervisorId(svId);
        pendingProposalCount = proposalService.countPendingBySupervisorId(svId);
        approvedProposalCount = proposalService.countApprovedBySupervisorId(svId);
        rejectedProposalCount = proposalService.countRejectedBySupervisorId(svId);
        flaggedProposalCount = proposalService.countFlaggedBySupervisorId(svId);
        activeProjectCount = projectService.countBySupervisorId(svId);

        List<User> allStudents = userService.findStudentsBySupervisorId(svId);
        myStudentIds = allStudents.stream().map(User::getId).collect(Collectors.toList());
        myStudents = allStudents.size() > 5 ? allStudents.subList(0, 5) : allStudents;
        recentProposals = proposalService.findRecentByStudentIds(myStudentIds, 5);
        recentChapters = chapterService.findRecentBySupervisorStudents(myStudentIds, 5);
    }

    // ── Called from proposals page ──────────────────────────────────────────────

    public void listProposal() {
        LOG.info("listProposal");
        try {
            SecurityUtils.getSubject().checkRole("SUPERVISOR");
            setLazyModel(new ProposalLazyDataModel(proposalService, QueryType.GET_ALL_SUBMITED_PROPOSAL, currentUser.getId()));
        } catch (Exception e) {
            Messages.addGlobalError("Error fetching proposals.");
            LOG.error("listProposal error", e);
        }
    }

    public void displayProposalDialog(Proposal p) {
        entry = p;
        rejectComment = null;
    }

    public void approveProposal() {
        try {
            Proposal full = proposalService.findProposalWithStudent(entry.getId());
            if (full == null) { Messages.addGlobalError("Proposal not found."); return; }
            full.setStatus(Status.APPROVED);
            full.setSupervisorComment(rejectComment != null && !rejectComment.isBlank() ? rejectComment : null);
            proposalService.updateProposal(full);

            User student = full.getStudent();
            Project project = new Project();
            project.setProposalId(full.getId());
            project.setSupervisorId(currentUser.getId());
            project.setProgressStatus(ProgressStatus.IN_PROGRESS);
            projectService.createProject(project);

            student.setProjectlId(project.getId());
            student.setProposalStatus(Status.APPROVED);
            student.setProjectStatus(Status.APPROVED);
            userService.update(student);

            Messages.addGlobalInfo("Proposal approved — project started for " + student.getFullName() + ".");
            entry = new Proposal();
            rejectComment = null;
            listProposal();
            loadDashboardStats();
        } catch (Exception e) {
            LOG.error("approveProposal error", e);
            Messages.addGlobalError("Failed to approve proposal: " + e.getMessage());
        }
    }

    public void rejectProposal() {
        try {
            if (rejectComment == null || rejectComment.isBlank()) {
                Messages.addGlobalError("Please provide a reason for rejection.");
                return;
            }
            Proposal full = proposalService.findProposalWithStudent(entry.getId());
            if (full == null) { Messages.addGlobalError("Proposal not found."); return; }
            full.setStatus(Status.REJECTED);
            full.setSupervisorComment(rejectComment);
            proposalService.updateProposal(full);

            User student = full.getStudent();
            student.setProposalStatus(Status.REJECTED);
            userService.update(student);

            Messages.addGlobalInfo("Proposal rejected. Student has been notified.");
            entry = new Proposal();
            rejectComment = null;
            listProposal();
            loadDashboardStats();
        } catch (Exception e) {
            LOG.error("rejectProposal error", e);
            Messages.addGlobalError("Failed to reject proposal: " + e.getMessage());
        }
    }

    // ── Chapters ──────────────────────────────────────────────────────────────

    public void loadChapters() {
        if (myStudentIds.isEmpty()) {
            List<User> students = userService.findStudentsBySupervisorId(currentUser.getId());
            myStudentIds = students.stream().map(User::getId).collect(Collectors.toList());
        }
        chapters = chapterService.findBySupervisorStudents(myStudentIds);
    }

    public void viewChapter(Chapter c) {
        selectedChapter = c;
        chapterRejectComment = null;
    }

    public void approveChapter() {
        try {
            selectedChapter.setStatus(Status.APPROVED);
            if (chapterRejectComment != null && !chapterRejectComment.isBlank()) {
                selectedChapter.setSupervisorComment(chapterRejectComment);
            }
            chapterService.updateChapter(selectedChapter);
            Messages.addGlobalInfo("Chapter " + selectedChapter.getChapterNumber() + " approved.");
            selectedChapter = null;
            chapterRejectComment = null;
            loadChapters();
        } catch (Exception e) {
            LOG.error("approveChapter error", e);
            Messages.addGlobalError("Failed to approve chapter.");
        }
    }

    public void rejectChapter() {
        try {
            if (chapterRejectComment == null || chapterRejectComment.isBlank()) {
                Messages.addGlobalError("Please provide feedback for the student.");
                return;
            }
            selectedChapter.setStatus(Status.REJECTED);
            selectedChapter.setSupervisorComment(chapterRejectComment);
            chapterService.updateChapter(selectedChapter);
            Messages.addGlobalInfo("Chapter returned to student with feedback.");
            selectedChapter = null;
            chapterRejectComment = null;
            loadChapters();
        } catch (Exception e) {
            LOG.error("rejectChapter error", e);
            Messages.addGlobalError("Failed to return chapter.");
        }
    }

    public String compareSimilar() {
        if (entry == null || entry.getMostSimilarProjectId() == null) return "";
        Faces.getFlash().put("entry", entry);
        Proposal sim = proposalService.findProposal(entry.getMostSimilarProjectId());
        Faces.getFlash().put("similarProposal", sim);
        Faces.getFlash().put("similarityScore", entry.getSimilarityScore());
        return APP_BASE_NAME + "/online/proposal/compare.xhtml?faces-redirect=true";
    }

    // ── Chapter preview helpers ───────────────────────────────────────────────

    public boolean isChapterPdf() {
        return selectedChapter != null
            && selectedChapter.getFileName() != null
            && selectedChapter.getFileName().toLowerCase().endsWith(".pdf");
    }

    public String getChapterFileUrl() {
        if (selectedChapter == null) return "";
        return "/chapter-file?id=" + selectedChapter.getId();
    }

    // Getters / setters
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User u) { this.currentUser = u; }
    public long getMyStudentCount() { return myStudentCount; }
    public long getPendingProposalCount() { return pendingProposalCount; }
    public long getApprovedProposalCount() { return approvedProposalCount; }
    public long getRejectedProposalCount() { return rejectedProposalCount; }
    public long getFlaggedProposalCount() { return flaggedProposalCount; }
    public long getActiveProjectCount() { return activeProjectCount; }
    public List<Proposal> getRecentProposals() { return recentProposals; }
    public List<Chapter> getRecentChapters() { return recentChapters; }
    public List<User> getMyStudents() { return myStudents; }
    public LazyDataModel<Proposal> getLazyModel() { return lazyModel; }
    public void setLazyModel(LazyDataModel<Proposal> m) { this.lazyModel = m; }
    public Proposal getEntry() { return entry; }
    public void setEntry(Proposal e) { this.entry = e; }
    public String getRejectComment() { return rejectComment; }
    public void setRejectComment(String s) { this.rejectComment = s; }
    public List<Chapter> getChapters() { return chapters; }
    public Chapter getSelectedChapter() { return selectedChapter; }
    public void setSelectedChapter(Chapter c) { this.selectedChapter = c; }
    public String getChapterRejectComment() { return chapterRejectComment; }
    public void setChapterRejectComment(String s) { this.chapterRejectComment = s; }
}
