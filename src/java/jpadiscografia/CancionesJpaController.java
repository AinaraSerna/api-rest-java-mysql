/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaeminem;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jpaeminem.exceptions.NonexistentEntityException;

/**
 *
 * @author ainar
 */
public class CancionesJpaController implements Serializable {

    public CancionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Canciones canciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Albumes album = canciones.getAlbum();
            if (album != null) {
                album = em.getReference(album.getClass(), album.getIDalbum());
                canciones.setAlbum(album);
            }
            em.persist(canciones);
            if (album != null) {
                album.getCancionesCollection().add(canciones);
                album = em.merge(album);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Canciones canciones) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Canciones persistentCanciones = em.find(Canciones.class, canciones.getIDsong());
            Albumes albumOld = persistentCanciones.getAlbum();
            Albumes albumNew = canciones.getAlbum();
            if (albumNew != null) {
                albumNew = em.getReference(albumNew.getClass(), albumNew.getIDalbum());
                canciones.setAlbum(albumNew);
            }
            canciones = em.merge(canciones);
            if (albumOld != null && !albumOld.equals(albumNew)) {
                albumOld.getCancionesCollection().remove(canciones);
                albumOld = em.merge(albumOld);
            }
            if (albumNew != null && !albumNew.equals(albumOld)) {
                albumNew.getCancionesCollection().add(canciones);
                albumNew = em.merge(albumNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = canciones.getIDsong();
                if (findCanciones(id) == null) {
                    throw new NonexistentEntityException("The canciones with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Canciones canciones;
            try {
                canciones = em.getReference(Canciones.class, id);
                canciones.getIDsong();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The canciones with id " + id + " no longer exists.", enfe);
            }
            Albumes album = canciones.getAlbum();
            if (album != null) {
                album.getCancionesCollection().remove(canciones);
                album = em.merge(album);
            }
            em.remove(canciones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Canciones> findCancionesEntities() {
        return findCancionesEntities(true, -1, -1);
    }

    public List<Canciones> findCancionesEntities(int maxResults, int firstResult) {
        return findCancionesEntities(false, maxResults, firstResult);
    }

    private List<Canciones> findCancionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Canciones.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Canciones findCanciones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Canciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getCancionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Canciones> rt = cq.from(Canciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
