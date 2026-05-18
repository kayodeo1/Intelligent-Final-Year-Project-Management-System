/**
 *
 */
package com.kayode.ifypm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.Status;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

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
			if (i < embedding.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		String vectorLiteral = sb.toString();

		String sql = "SELECT * FROM proposal " + "WHERE id != " + e.getId() + " " + "AND embedding IS NOT NULL AND status != '"+ Status.DRAFT.ordinal() + "' "
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
		PagedList<Proposal> list = new PagedList<>();
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
		PagedList<Proposal> list = new PagedList<>();
		TypedQuery<Proposal> query = em.createQuery(
		        "SELECT p FROM Proposal p WHERE p.student.id = :id ORDER BY p.createdDate DESC",
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
