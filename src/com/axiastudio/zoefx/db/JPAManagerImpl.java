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
public class JPAManagerImpl<E> implements Manager<E> {

    private Class entityClass;
    private EntityManager entityManager;

    public JPAManagerImpl(EntityManager em, Class<E> klass) {
        entityClass = klass;
        entityManager = em;
    }

    @Override
    public E commit(E entity) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        E merged = em.merge(entity);
        em.getTransaction().commit();
        return merged;
    }

    @Override
    public void delete(E entity) {

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
    public E get(Long id) {
        return null;
    }

    @Override
    public List<E> getAll() {
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<E> query = em.createQuery(cq);
        List<E> store = query.getResultList();
        return store;
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }
}
