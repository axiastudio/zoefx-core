package com.axiastudio.zoefx.db;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:50
 */
public class JPAManagerImpl<T> implements Manager<T> {

    private Class entityClass;
    private EntityManager entityManager;

    public JPAManagerImpl(EntityManager em, Class<T> klass) {
        entityClass = klass;
        entityManager = em;
    }

    @Override
    public T commit(T entity) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        T merged = em.merge(entity);
        em.getTransaction().commit();
        return merged;
    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void truncate(){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM " + entityClass.getCanonicalName() + " e").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public T get(Long id) {
        return null;
    }

    @Override
    public List<T> getAll() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> query = em.createQuery(cq);
        List<T> store = query.getResultList();
        return store;
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }
}
