package com.kayode.ifypm.bean;

import java.io.File;
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
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.Chapter;
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

    private static final String UPLOAD_DIR =
        System.getProperty("user.home") + File.separator + "fypms-uploads" + File.separator + "chapters" + File.separator;
   

    @Inject private ChapterService chapterService;
    @Inject private ProjectService projectService;
    @Inject private UserService userService;

    private User currentUser;
    private Project currentProject;
    private List<Chapter> chapters = new ArrayList<>();

    // new chapter form
    private int newChapterNumber = 1;
    private String newChapterTitle;
    private Chapter selectedChapter;

    @PostConstruct
    public void init() {
    	System.out.println(UPLOAD_DIR);
        currentUser = userService.findUserByUserName(
            SecurityUtils.getSubject().getPrincipal().toString());
        if (currentUser.getProjectlId() != null) {
            currentProject = projectService.findProject(currentUser.getProjectlId());
        }
        loadChapters();
    }

    public void loadChapters() {
        if (currentProject != null) {
            chapters = chapterService.findByProjectId(currentProject.getId());
        } else {
            chapters = new ArrayList<>();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (currentProject == null) {
            Messages.addGlobalError("You do not have an active project yet.");
            return;
        }
        if (event.getFile() == null) {
            Messages.addGlobalError("No file selected.");
            return;
        }
        if (newChapterTitle == null || newChapterTitle.isBlank()) {
            Messages.addGlobalError("Please enter a chapter title.");
            return;
        }
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR + currentProject.getId());
            Files.createDirectories(uploadPath);

            String originalName = event.getFile().getFileName();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String savedName = "ch" + newChapterNumber + "_" + timestamp + "_" + sanitize(originalName);
            Path destPath = uploadPath.resolve(savedName);

            try (InputStream in = event.getFile().getInputStream()) {
                Files.copy(in, destPath, StandardCopyOption.REPLACE_EXISTING);
            }

            Chapter chapter = new Chapter();
            chapter.setChapterNumber(newChapterNumber);
            chapter.setTitle(newChapterTitle.trim());
            chapter.setFileName(originalName);
            chapter.setFilePath(destPath.toString());
            chapter.setStatus(Status.SUBMITTED);
            chapter.setProjectId(currentProject.getId());
            chapter.setStudentId(currentUser.getId());
            chapter.setStudentName(currentUser.getFullName());
            chapterService.createChapter(chapter);

            Messages.addGlobalInfo("Chapter " + newChapterNumber + " — \"" + newChapterTitle + "\" uploaded successfully.");
            newChapterTitle = null;
            newChapterNumber = 1;
            loadChapters();
        } catch (IOException e) {
            LOG.error("Chapter upload error", e);
            Messages.addGlobalError("Upload failed: " + e.getMessage());
        }
    }

    public void viewChapter(Chapter c) {
        selectedChapter = c;
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public boolean hasActiveProject() {
        return currentProject != null;
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
}
