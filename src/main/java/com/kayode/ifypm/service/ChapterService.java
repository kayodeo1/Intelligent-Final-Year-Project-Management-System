package com.kayode.ifypm.service;

import java.util.List;

import com.kayode.ifypm.model.Chapter;
import com.kayode.ifypm.model.Status;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ChapterService {

    @PersistenceContext(unitName = "app")
    private EntityManager em;

    public void createChapter(Chapter c) {
        em.persist(c);
        em.flush();
    }

    public Chapter updateChapter(Chapter c) {
        return em.merge(c);
    }

    public Chapter findChapter(Long id) {
        return em.find(Chapter.class, id);
    }

    public List<Chapter> findByProjectId(Long projectId) {
        return em.createQuery(
            "SELECT c FROM Chapter c WHERE c.projectId = :pid ORDER BY c.chapterNumber ASC, c.createdDate DESC",
            Chapter.class)
            .setParameter("pid", projectId)
            .getResultList();
    }

    public List<Chapter> findByStudentId(Long studentId) {
        return em.createQuery(
            "SELECT c FROM Chapter c WHERE c.studentId = :sid ORDER BY c.chapterNumber ASC, c.createdDate DESC",
            Chapter.class)
            .setParameter("sid", studentId)
            .getResultList();
    }

    public List<Chapter> findBySupervisorStudents(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) return List.of();
        return em.createQuery(
            "SELECT c FROM Chapter c WHERE c.studentId IN :ids ORDER BY c.createdDate DESC",
            Chapter.class)
            .setParameter("ids", studentIds)
            .getResultList();
    }

    public List<Chapter> findRecentBySupervisorStudents(List<Long> studentIds, int limit) {
        if (studentIds == null || studentIds.isEmpty()) return List.of();
        return em.createQuery(
            "SELECT c FROM Chapter c WHERE c.studentId IN :ids ORDER BY c.createdDate DESC",
            Chapter.class)
            .setParameter("ids", studentIds)
            .setMaxResults(limit)
            .getResultList();
    }

    public long countPendingByStudentIds(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) return 0;
        return em.createQuery(
            "SELECT COUNT(c) FROM Chapter c WHERE c.studentId IN :ids AND c.status = :status",
            Long.class)
            .setParameter("ids", studentIds)
            .setParameter("status", Status.SUBMITTED)
            .getSingleResult();
    }
}
