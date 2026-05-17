/**
 *
 */
package com.kayode.ifypm.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * @author AAfolayan
 */
@Entity
public class Proposal extends AbstractEntity {

@Temporal(TemporalType.TIMESTAMP)
private Date createdDate;
@Temporal(TemporalType.TIMESTAMP)
private Date lastModifiedDate;
@Column(columnDefinition = "TEXT")
private String title;
@Column(columnDefinition = "TEXT")
private String problemStatement;
@Column(columnDefinition = "TEXT")
private String objectives;
@Column(columnDefinition = "TEXT")
private String methodology;
private Status status;
@Column(columnDefinition = "TEXT")
private String supervisorComment;
private double similarityScore;
private Long mostSimilarProposalId;
private Long mostSimilarProjectId;
private String submittedAt;

@Column(name = "embedding", columnDefinition = "vector(768)")
@Convert(converter = VectorConverter.class)
private float[] embedding;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "student", referencedColumnName = "username")
private User student; 

/**
 * @return the problemStatement
 */
public String getProblemStatement(){
	return problemStatement;
}
/**
 * @param problemStatement the problemStatement to set
 */
public void setProblemStatement(String problemStatement) {
	this.problemStatement = problemStatement;
}/**
 * @return the mostSimilarProjectId
 */
public Long getMostSimilarProjectId(){
	return mostSimilarProjectId;
}
/**
 * @param mostSimilarProjectId the mostSimilarProjectId to set
 */
public void setMostSimilarProjectId(Long mostSimilarProjectId) {
	this.mostSimilarProjectId = mostSimilarProjectId;
}/**
 * @return the supervisorComment
 */
public String getSupervisorComment(){
	return supervisorComment;
}
/**
 * @param supervisorComment the supervisorComment to set
 */
public void setSupervisorComment(String supervisorComment) {
	this.supervisorComment = supervisorComment;
}/**
 * @return the objectives
 */
public String getObjectives(){
	return objectives;
}
/**
 * @param objectives the objectives to set
 */
public void setObjectives(String objectives) {
	this.objectives = objectives;
}/**
 * @return the mostSimilarProposalId
 */
public Long getMostSimilarProposalId(){
	return mostSimilarProposalId;
}
/**
 * @param mostSimilarProposalId the mostSimilarProposalId to set
 */
public void setMostSimilarProposalId(Long mostSimilarProposalId) {
	this.mostSimilarProposalId = mostSimilarProposalId;
}/**
 * @return the title
 */
public String getTitle(){
	return title;
}
/**
 * @param title the title to set
 */
public void setTitle(String title) {
	this.title = title;
}/**
 * @return the submittedAt
 */
public String getSubmittedAt(){
	return submittedAt;
}
/**
 * @param submittedAt the submittedAt to set
 */
public void setSubmittedAt(String submittedAt) {
	this.submittedAt = submittedAt;
}/**
 * @return the methodology
 */
public String getMethodology(){
	return methodology;
}
/**
 * @param methodology the methodology to set
 */
public void setMethodology(String methodology) {
	this.methodology = methodology;
}/**
 * @return the status
 */
public Status getStatus(){
	return status;
}
/**
 * @param status the status to set
 */
public void setStatus(Status status) {
	this.status = status;
}/**
 * @return the similarityScore
 */
public double getSimilarityScore(){
	return similarityScore;
}
/**
 * @param similarityScore the similarityScore to set
 */
public void setSimilarityScore(double similarityScore) {
	this.similarityScore = similarityScore;
}
@PrePersist
private void onCreate() {
	createdDate = new Date();
}
@PreUpdate
private void onUpdate() {
	lastModifiedDate = new Date();
}
/**
 * @return the embedding
 */
public float[] getEmbedding() {
	return embedding;
}
/**
 * @param embedding the embedding to set
 */
public void setEmbedding(float[] embedding) {
	this.embedding = embedding;
}
/**
 * @return the createdDate
 */
public Date getCreatedDate() {
	return createdDate;
}
/**
 * @param createdDate the createdDate to set
 */
public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}

public User getStudent() {
	return student;
}
public void setStudent(User student) {
	this.student = student;
}







}