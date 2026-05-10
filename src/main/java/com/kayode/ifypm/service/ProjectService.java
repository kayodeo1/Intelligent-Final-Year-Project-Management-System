/**
 *
 */
package com.kayode.ifypm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Project;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * @author AAfolayan
 *
 */
@Stateless
public class ProjectService {

	@PersistenceContext(unitName = "app")
	private EntityManager em;


	private static Logger LOG = LoggerFactory.getLogger(ProjectService.class);

	public void createProject(Project e){
		em.persist(e);
		em.flush();
	}

	public Project updateProject(Project e){
		return em.merge(e);
	}

	public Project findProject(Long id){
		return em.find(Project.class, id);
	}

	public PagedList<Project> fetchProject(int first, int pageSize){
		PagedList<Project> list = new PagedList<>();
		TypedQuery<Project> query = em.createQuery(
				"select s from Project s order by s.createdDate desc",
				Project.class);
		query.setFirstResult(first).setMaxResults(pageSize);
		List<Project> res = query.getResultList();
		list.setList(res);

		Number count = fetchProjectCount();
		list.setCount(count.intValue());

		return list;
	}

		public Number fetchProjectCount()
			{
		TypedQuery<Number> query = em.createQuery(
				"select count(s.id) from Project s",
				Number.class);
		Number res = query.getSingleResult();
		return res;
	}

}
