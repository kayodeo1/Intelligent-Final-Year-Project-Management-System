/**
 * 
 */
package com.kayode.ifypm.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.PagedList;

/**
 * @author AAfolayan
 *
 */
@Stateless
public class ProposalService {

	@PersistenceContext(unitName = "app")
	private EntityManager em;

	private static Logger LOG = LoggerFactory.getLogger(ProposalService.class);

	public void createProposal(Proposal e) {
		em.persist(e);
		em.flush();

	}

	public List<Proposal> findSimilarProposals(Proposal e) {
		float[] embedding = e.getEmbedding();

		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < embedding.length; i++) {
			sb.append(embedding[i]);
			if (i < embedding.length - 1)
				sb.append(",");
		}
		sb.append("]");
		String vectorLiteral = sb.toString();

		String sql = "SELECT * FROM proposal " + "WHERE id != " + e.getId() + " " + "AND embedding IS NOT NULL "
				+ "ORDER BY embedding <=> CAST('" + vectorLiteral + "' AS vector) " + "LIMIT 5";

		return em.createNativeQuery(sql, Proposal.class).getResultList();
	}

	public Proposal updateProposal(Proposal e) {
		return em.merge(e);
	}

	public Proposal findProposal(Long id) {
		return em.find(Proposal.class, id);
	}

	public PagedList<Proposal> fetchProposal(int first, int pageSize) {
		PagedList<Proposal> list = new PagedList<Proposal>();
		TypedQuery<Proposal> query = em.createQuery("select s from Proposal s order by s.createdDate desc",
				Proposal.class);
		query.setFirstResult(first).setMaxResults(pageSize);
		List<Proposal> res = query.getResultList();
		list.setList(res);

		Number count = fetchProposalCount();
		list.setCount(count.intValue());

		return list;
	}
	public PagedList<Proposal> fetchStudentProposal(int first, int pageSize, Long studentId) {
		PagedList<Proposal> list = new PagedList<Proposal>();
		TypedQuery<Proposal> query = em.createQuery("select s from Proposal s WHERE s.studentId =:id order by s.createdDate desc",
				Proposal.class);
		query.setParameter("id", studentId);
		query.setFirstResult(first).setMaxResults(pageSize);
		List<Proposal> res = query.getResultList();
		list.setList(res);

		Number count = fetchProposalCount();
		list.setCount(count.intValue());

		return list;
	}


	public Number fetchProposalCount() {
		TypedQuery<Number> query = em.createQuery("select count(s.id) from Proposal s", Number.class);
		Number res = query.getSingleResult();
		return res;
	}

}
