/**
 * 
 */
package com.kayode.ifypm.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

//import org.apache.commons.io.FileUtils;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.crypto.hash.Sha256Hash;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.PagedList;

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
		PagedList<Project> list = new PagedList<Project>();
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
