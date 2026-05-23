/**
 *
 */
package com.kayode.ifypm.model;

import java.util.Date;
/**
 * @author Afolayana
 *
 */
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
//@NamedQueries({
//		@NamedQuery(name = "User.find", query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
//		@NamedQuery(name = "User.list", query = "SELECT u FROM User u where u.status=:status") })
// @Table(name = "user", schema = "public2")
@Table(name = "USER_")
public class User extends AbstractEntity {


	@NotNull
	@Column(unique = true, name = "username")
	private String username;
	private String department;
	private Long projectlId;
	private String session;        // academic session, e.g. "2023/2024"
	private String matricNumber;   // e.g. "CSC/2022/045"


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supervisor_username", referencedColumnName = "username")
	private User supervisor;  // null if this user IS a supervisor

	@OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
	private List<User> supervisedStudents;  // empty if this user is a student
	@Enumerated(EnumType.STRING)
	private Status projectStatus = Status.NOT_STARTED;
	private Status proposalStatus = Status.NOT_STARTED;
	
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	private List<Proposal> proposals;

	// @NotNull
	@Column(name = "password")
	private String password;
	private String firstname;
	private String lastname;
	@Enumerated(EnumType.STRING)
	private Status status;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)

	@CollectionTable(
		    name = "user_roles",
		    joinColumns = { @JoinColumn(name = "username",    // join by username, not userid
		                   referencedColumnName = "username") }
		)
	@Column(name = "role")
	private List<Role> roles;



	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;
	@NotNull
	@Column(unique = true, name = "email")
	private String email;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	public static String extractInitials(String fullName) {
		String[] words = fullName.split("\\s+");
		StringBuilder initials = new StringBuilder();
		int count = 0;

		for (String word : words) {
			if (word.length() > 0 && !isTitleWord(word) && count < 2) {
				initials.append(Character.toUpperCase(word.charAt(0)));
				count++;
			}
		}

		return initials.toString();
	}

	private static boolean isTitleWord(String word) {
		String lowerWord = word.toLowerCase();
		return lowerWord.equals("mr") || lowerWord.equals("mrs") || lowerWord.equals("ms") || lowerWord.equals("dr")
				|| lowerWord.equals("prof");
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastName(String lastName) {
		this.lastname = lastName;
	}

	public Long getProjectlId() {
		return projectlId;
	}

	public void setProjectlId(Long projectlId) {
		this.projectlId = projectlId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the projectStatus
	 */
	public Status getProjectStatus() {
		return projectStatus;
	}

	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(Status projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Status getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(Status proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public String getFullName() {
		return firstname + " " + lastname;
	}

	public User getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(User supervisor) {
		this.supervisor = supervisor;
	}

	public List<User> getSupervisedStudents() {
		return supervisedStudents;
	}

	public void setSupervisedStudents(List<User> supervisedStudents) {
		this.supervisedStudents = supervisedStudents;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getMatricNumber() {
		return matricNumber;
	}

	public void setMatricNumber(String matricNumber) {
		this.matricNumber = matricNumber;
	}

}