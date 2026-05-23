package com.kayode.ifypm.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Chapter;
import com.kayode.ifypm.model.Constants;
import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.Status;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.ChapterService;
import com.kayode.ifypm.service.ProjectService;
import com.kayode.ifypm.service.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("chapterBean")
@ViewScoped
public class ChapterBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ChapterBean.class);

    @Inject private ChapterService chapterService;
    @Inject private ProjectService projectService;
    @Inject private UserService userService;

    private User currentUser;
    private Project currentProject;
    private List<Chapter> chapters = new ArrayList<>();

    private int newChapterNumber = 1;
    private String newChapterTitle;
    private Chapter selectedChapter;

    // staged upload state
    private String stagedPathStr;
    private String stagedFileName;
    private String stagedContentType;
    private transient StreamedContent previewContent;
    private boolean previewReady;

    @PostConstruct
    public void init() {
        currentUser = userService.findUserByUserName(
            SecurityUtils.getSubject().getPrincipal().toString());
        if (currentUser.getProjectlId() != null) {
            currentProject = projectService.findProject(currentUser.getProjectlId());
        }
        loadChapters();
    }

    public void loadChapters() {
        chapters = currentProject != null
            ? chapterService.findByProjectId(currentProject.getId())
            : new ArrayList<>();
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (currentProject == null) {
            Messages.addGlobalError("You do not have an active project yet.");
            return;
        }
        try {
            // Delete previously staged file if any
            clearStagedFile();

            Path studentDir = Paths.get(Constants.UPLOAD_PATH_DIR, "students",
                                        String.valueOf(currentUser.getId()));
            Files.createDirectories(studentDir);

            String originalName = event.getFile().getFileName();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String savedName = timestamp + "_" + sanitize(originalName);
            Path dest = studentDir.resolve(savedName);

            try (InputStream in = event.getFile().getInputStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            stagedPathStr = dest.toString();
            stagedFileName = originalName;
            stagedContentType = detectContentType(originalName);
            previewContent = null; // rebuilt lazily in getter
            previewReady = true;
            newChapterTitle = null;
            newChapterNumber = 1;

            PrimeFaces.current().executeScript("PF('previewDlg').show();");
        } catch (IOException e) {
            LOG.error("File staging error", e);
            Messages.addGlobalError("Upload failed: " + e.getMessage());
        }
    }

    public void confirmChapter() {
        if (!previewReady || stagedPathStr == null) {
            Messages.addGlobalError("No file staged. Please choose a file first.");
            return;
        }
        if (newChapterTitle == null || newChapterTitle.isBlank()) {
            Messages.addGlobalError("Please enter a chapter title.");
            return;
        }
        try {
            Chapter chapter = new Chapter();
            chapter.setChapterNumber(newChapterNumber);
            chapter.setTitle(newChapterTitle.trim());
            chapter.setFileName(stagedFileName);
            chapter.setFilePath(stagedPathStr);
            chapter.setStatus(Status.SUBMITTED);
            chapter.setProjectId(currentProject.getId());
            chapter.setStudentId(currentUser.getId());
            chapter.setStudentName(currentUser.getFullName());
            chapterService.createChapter(chapter);

            Messages.addGlobalInfo("Chapter " + newChapterNumber
                + " — \"" + newChapterTitle.trim() + "\" submitted successfully.");
            clearStagedState();
            loadChapters();
            PrimeFaces.current().executeScript("PF('previewDlg').hide();");
        } catch (Exception e) {
            LOG.error("confirmChapter error", e);
            Messages.addGlobalError("Submission failed: " + e.getMessage());
        }
    }

    public void cancelUpload() {
        clearStagedFile();
        clearStagedState();
        PrimeFaces.current().executeScript("PF('previewDlg').hide();");
    }

    private void clearStagedFile() {
        if (stagedPathStr != null) {
            try { Files.deleteIfExists(Paths.get(stagedPathStr)); } catch (IOException ignored) {}
        }
    }

    private void clearStagedState() {
        stagedPathStr = null;
        stagedFileName = null;
        stagedContentType = null;
        previewContent = null;
        previewReady = false;
        newChapterTitle = null;
        newChapterNumber = 1;
    }

    private String detectContentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf"))  return "application/pdf";
        if (lower.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (lower.endsWith(".doc"))  return "application/msword";
        return "application/octet-stream";
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public boolean hasActiveProject() { return currentProject != null; }
    public boolean isPdfPreview() { return "application/pdf".equals(stagedContentType); }

    public StreamedContent getPreviewContent() {
        if (previewContent == null && stagedPathStr != null) {
            Path path = Paths.get(stagedPathStr);
            String ct = stagedContentType != null ? stagedContentType : "application/octet-stream";
            previewContent = DefaultStreamedContent.builder()
                .name(stagedFileName)
                .contentType(ct)
                .stream(() -> {
                    try { return Files.newInputStream(path); }
                    catch (IOException e) { return InputStream.nullInputStream(); }
                })
                .build();
        }
        return previewContent;
    }

    public User getCurrentUser() { return currentUser; }
    public Project getCurrentProject() { return currentProject; }
    public List<Chapter> getChapters() { return chapters; }
    public int getNewChapterNumber() { return newChapterNumber; }
    public void setNewChapterNumber(int n) { this.newChapterNumber = n; }
    public String getNewChapterTitle() { return newChapterTitle; }
    public void setNewChapterTitle(String t) { this.newChapterTitle = t; }
    public Chapter getSelectedChapter() { return selectedChapter; }
    public void setSelectedChapter(Chapter c) { this.selectedChapter = c; }
    public String getStagedFileName() { return stagedFileName; }
    public String getStagedContentType() { return stagedContentType; }
    public boolean isPreviewReady() { return previewReady; }
}
