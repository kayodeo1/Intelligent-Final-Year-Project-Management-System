package com.kayode.ifypm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class UserService {

	@PersistenceContext(unitName = "app")
	private EntityManager em;


	private static Logger LOG = LoggerFactory.getLogger(ProjectService.class);



	public User findUserByUserName(String username) {
		List<User> found = em.createQuery("select u from User u where u.username=:username", User.class)
				.setParameter("username", username).getResultList();
		return found.isEmpty() ? null : found.get(0);


	}
	
	public User update(User u) {
		return em.merge(u);
	}

	public void createUser(User u) {
		em.persist(u);
		em.flush();
	}

	public List<String> findDistinctSessions() {
		return em.createQuery(
				"SELECT DISTINCT u.session FROM User u WHERE u.session IS NOT NULL AND u.session <> '' ORDER BY u.session DESC",
				String.class).getResultList();
	}

	public List<User> findStudentsBySession(String session) {
		return em.createQuery(
				"SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.supervisor WHERE :role MEMBER OF u.roles AND u.session = :session ORDER BY u.lastname ASC",
				User.class)
				.setParameter("role", Role.STUDENT)
				.setParameter("session", session)
				.getResultList();
	}

	public boolean usernameExists(String username) {
		Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
				.setParameter("username", username).getSingleResult();
		return count > 0;
	}

	public PagedList<User> fetchUsersByRole(int first, int pageSize, Role role) {
		PagedList<User> result = new PagedList<>();
		TypedQuery<User> query = em.createQuery(
				"SELECT u FROM User u WHERE :role MEMBER OF u.roles ORDER BY u.lastname ASC", User.class)
				.setParameter("role", role)
				.setFirstResult(first)
				.setMaxResults(pageSize);
		result.setList(query.getResultList());
		result.setCount((int) countByRole(role));
		return result;
	}

	public long countStudentsBySupervisorId(Long supervisorId) {
		return em.createQuery(
				"SELECT COUNT(u) FROM User u WHERE u.supervisor.id = :id", Long.class)
				.setParameter("id", supervisorId)
				.getSingleResult();
	}

	public List<User> findStudentsBySupervisorId(Long supervisorId) {
		return em.createQuery(
				"SELECT u FROM User u WHERE u.supervisor.id = :id ORDER BY u.lastname ASC", User.class)
				.setParameter("id", supervisorId)
				.getResultList();
	}

	public User findUserWithSupervisor(Long id) {
		List<User> result = em.createQuery(
				"SELECT u FROM User u LEFT JOIN FETCH u.supervisor WHERE u.id = :id", User.class)
				.setParameter("id", id)
				.getResultList();
		return result.isEmpty() ? null : result.get(0);
	}

	public List<User> findAllByRole(Role role) {
		if (role == Role.STUDENT) {
			return em.createQuery(
					"SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.supervisor WHERE :role MEMBER OF u.roles ORDER BY u.lastname ASC", User.class)
					.setParameter("role", role)
					.getResultList();
		}
		return em.createQuery("SELECT u FROM User u WHERE :role MEMBER OF u.roles ORDER BY u.lastname ASC", User.class)
				.setParameter("role", role)
				.getResultList();
	}

	public long countByRole(Role role) {
		return em.createQuery("SELECT COUNT(u) FROM User u WHERE :role MEMBER OF u.roles", Long.class)
				.setParameter("role", role)
				.getSingleResult();
	}

}
