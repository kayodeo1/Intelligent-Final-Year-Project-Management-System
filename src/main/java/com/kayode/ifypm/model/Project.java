/**
 * 
 */
package com.kayode.ifypm.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.kayode.ifypm.model.AbstractEntity;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author AAfolayan
 */
@Entity
public class Project extends AbstractEntity {

@Temporal(TemporalType.TIMESTAMP)
private Date createdDate;
@Temporal(TemporalType.TIMESTAMP)
private Date lastModifiedDate;
private Long proposalId;
private Long supervisorId;
private String startDate;
private String endDate;
private ProgressStatus progressStatus;


/**
 * @return the endDate
 */
public String getEndDate(){
	return endDate;
}
/**
 * @param endDate the endDate to set
 */
public void setEndDate(String endDate) {
	this.endDate = endDate;
}/**
 * @return the progressStatus
 */
public ProgressStatus getProgressStatus(){
	return progressStatus;
}
/**
 * @param progressStatus the progressStatus to set
 */
public void setProgressStatus(ProgressStatus progressStatus) {
	this.progressStatus = progressStatus;
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
 * @return the proposalId
 */
public Long getProposalId(){
	return proposalId;
}
/**
 * @param proposalId the proposalId to set
 */
public void setProposalId(Long proposalId) {
	this.proposalId = proposalId;
}/**
 * @return the startDate
 */
public String getStartDate(){
	return startDate;
}
/**
 * @param startDate the startDate to set
 */
public void setStartDate(String startDate) {
	this.startDate = startDate;
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