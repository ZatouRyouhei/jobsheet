package db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author ryouhei
 */
public class SuperDb<T> {
    protected Class<T> entityClass;
    
    @PersistenceContext
    protected EntityManager em;
    
    protected SuperDb(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected void create(T entity) {
        em.persist(entity);
    }
    
    protected void edit(T entity) {
        em.merge(entity);
    }
    
    protected void remove(T entity) {
        em.remove(em.merge(entity));
    }
    
    protected T find(Object id) {
        return em.find(entityClass, id);
    }
    
    protected List<T> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    protected List<T> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    protected int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
