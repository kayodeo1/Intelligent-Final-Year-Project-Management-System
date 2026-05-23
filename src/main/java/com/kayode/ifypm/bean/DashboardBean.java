package com.kayode.ifypm.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Chapter;
import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ChapterService;
import com.kayode.ifypm.service.ProposalService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String APP_BASE_NAME = Constants.APP_BASE_NAME;
    private static final Logger LOG = LoggerFactory.getLogger(DashboardBean.class);

    @Inject UserService userService;
    @Inject ChapterService chapterService;
    @Inject ProposalService proposalService;

    private User currentUser;
    private List<Chapter> chapters = new ArrayList<>();
    private Proposal latestProposal;

    @PostConstruct
    public void init() {
        Subject subject = SecurityUtils.getSubject();
        currentUser = userService.findUserByUserName(subject.getPrincipal().toString());

        if (currentUser.getProjectlId() != null) {
            chapters = chapterService.findByProjectId(currentUser.getProjectlId());
        }

        List<Proposal> proposals = proposalService.findAllByStudentId(currentUser.getId());
        if (!proposals.isEmpty()) {
            latestProposal = proposals.get(0);
        }
    }

    // ── Null-safe helpers ──────────────────────────────────────────────────────

    public String getSupervisorName() {
        if (currentUser == null) return "—";
        User sv = currentUser.getSupervisor();
        return sv != null ? sv.getFullName() : "Not assigned yet";
    }

    public String getSupervisorDepartment() {
        if (currentUser == null) return "";
        User sv = currentUser.getSupervisor();
        return sv != null && sv.getDepartment() != null ? sv.getDepartment() : "";
    }

    public boolean hasSupervisor() {
        return currentUser != null && currentUser.getSupervisor() != null;
    }

    public boolean hasProject() {
        return currentUser != null && currentUser.getProjectlId() != null;
    }

    public double getSimilarityScore() {
        if (latestProposal == null) return 0.0;
        Double s = latestProposal.getSimilarityScore();
        return s != null ? s : 0.0;
    }

    public String getSimilarityClass() {
        double s = getSimilarityScore();
        if (s >= 0.80) return "high";
        if (s >= 0.60) return "medium";
        return "low";
    }

    public String getSimilarityHint() {
        double s = getSimilarityScore();
        if (s == 0.0) return "No proposal submitted yet";
        if (s >= 0.80) return "High similarity — review required";
        if (s >= 0.60) return "Moderate similarity — proceed with care";
        return "Distinct topic — clear to proceed";
    }

    public long getApprovedChapterCount() {
        return chapters.stream().filter(c -> "APPROVED".equals(String.valueOf(c.getStatus()))).count();
    }

    // ── Getters / setters ─────────────────────────────────────────────────────

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }
    public List<Chapter> getChapters() { return chapters; }
    public Proposal getLatestProposal() { return latestProposal; }
}
