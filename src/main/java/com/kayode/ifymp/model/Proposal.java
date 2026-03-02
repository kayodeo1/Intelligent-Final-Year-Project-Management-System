/**
 * 
 */
package com.kayode.ifymp.model;


import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.kayode.ifymp.model.AbstractEntity;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author AAfolayan
 */
@Entity
public class Proposal extends AbstractEntity {

@Temporal(TemporalType.TIMESTAMP)
private Date createdDate;
@Temporal(TemporalType.TIMESTAMP)
private Date lastModifiedDate;
private Long studentId;
private Long supervisorId;
private String title;
private String problemStatement;
private String objectives;
private String methodology;
private status status;
private String supervisorComment;
private String similarityScore;
private Long mostSimilarProposalId;
private Long mostSimilarProjectId;
private String submittedAt;


/**
 * @return the studentId
 */
public Long getStudentId(){
	return studentId;
}
/**
 * @param studentId the studentId to set
 */
public void setStudentId(Long studentId) {
	this.studentId = studentId;
}/**
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
 * @return the supervisorId
 */
public Long getSupervisorId(){
	return supervisorId;
}
/**
 * @param supervisorId the supervisorId to set
 */
public void setSupervisorId(Long supervisorId) {
	this.supervisorId = supervisorId;
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
public enum getStatus(){
	return status;
}
/**
 * @param status the status to set
 */
public void setStatus(enum status) {
	this.status = status;
}/**
 * @return the similarityScore
 */
public String getSimilarityScore(){
	return similarityScore;
}
/**
 * @param similarityScore the similarityScore to set
 */
public void setSimilarityScore(String similarityScore) {
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

}