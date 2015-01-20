package com.obiectumclaro.factronica.core.emac.access;



import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public abstract class BaseEaoBean {

    @PersistenceContext(unitName = "emac")
    protected EntityManager entityManager;

    protected <T> List<T> getResultListFromNamedQuery(final Class<T> type,
                                                      final String namedQuery, final Object... parameters) {
        TypedQuery<T> q = entityManager.createNamedQuery(namedQuery, type);
        if (parameters != null && parameters.length > 0) {
            int i = 1;
            for (Object parameter : parameters) {
                q.setParameter(i++, parameter);
            }
        }
        return q.getResultList();
    }

    protected <T> T getSingleResultFromNamedQuery(final Class<T> type,
                                                  final String namedQuery, final Object... parameters) {
        TypedQuery<T> q = entityManager.createNamedQuery(namedQuery, type);
        if (parameters != null && parameters.length > 0) {
            int i = 1;
            for (Object parameter : parameters) {
                q.setParameter(i++, parameter);
            }
        }
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected <T> T getSingleResultFromNamedQuery(final Class<T> type,
                                                  final String namedQuery, final Map<String, Object> parameters) {
        final TypedQuery<T> q = entityManager
                .createNamedQuery(namedQuery, type);
        if (parameters != null) {
            for (Entry<String, Object> p : parameters.entrySet()) {
                q.setParameter(p.getKey(), p.getValue());
            }
        }
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected <T> List<T> getResultListFromQuery(final Class<T> type,
                                                 final String query, final Map<String, Object> parameters) {
        TypedQuery<T> q = entityManager.createQuery(query, type);
        if (parameters != null) {
            for (Entry<String, Object> p : parameters.entrySet()) {
                q.setParameter(p.getKey(), p.getValue());
            }
        }
        return q.getResultList();
    }

    protected <T> T store(T entity) {
        T merged = merge(entity);
        entityManager.persist(merged);
        return merged;
    }

    protected <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
