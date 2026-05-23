package com.kayode.ifypm.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Chapter extends AbstractEntity {

    private int chapterNumber;
    private String title;
    private String filePath;
    private String fileName;

    @Enumerated(EnumType.STRING)
    private Status status = Status.SUBMITTED;

    @Column(columnDefinition = "TEXT")
    private String supervisorComment;

    private Long projectId;
    private Long studentId;
    private String studentName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @PrePersist
    private void onCreate() { createdDate = new Date(); }

    @PreUpdate
    private void onUpdate() { lastModifiedDate = new Date(); }

    public int getChapterNumber() { return chapterNumber; }
    public void setChapterNumber(int chapterNumber) { this.chapterNumber = chapterNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getSupervisorComment() { return supervisorComment; }
    public void setSupervisorComment(String supervisorComment) { this.supervisorComment = supervisorComment; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Date getCreatedDate() { return createdDate; }
    public Date getLastModifiedDate() { return lastModifiedDate; }
}
