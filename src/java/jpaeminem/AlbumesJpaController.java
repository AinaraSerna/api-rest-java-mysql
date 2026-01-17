/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jpaeminem;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jpaeminem.exceptions.NonexistentEntityException;

/**
 *
 * @author ainar
 */
public class AlbumesJpaController implements Serializable {

    public AlbumesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Albumes albumes) {
        if (albumes.getCancionesCollection() == null) {
            albumes.setCancionesCollection(new ArrayList<Canciones>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Canciones> attachedCancionesCollection = new ArrayList<Canciones>();
            for (Canciones cancionesCollectionCancionesToAttach : albumes.getCancionesCollection()) {
                cancionesCollectionCancionesToAttach = em.getReference(cancionesCollectionCancionesToAttach.getClass(), cancionesCollectionCancionesToAttach.getIDsong());
                attachedCancionesCollection.add(cancionesCollectionCancionesToAttach);
            }
            albumes.setCancionesCollection(attachedCancionesCollection);
            em.persist(albumes);
            for (Canciones cancionesCollectionCanciones : albumes.getCancionesCollection()) {
                Albumes oldAlbumOfCancionesCollectionCanciones = cancionesCollectionCanciones.getAlbum();
                cancionesCollectionCanciones.setAlbum(albumes);
                cancionesCollectionCanciones = em.merge(cancionesCollectionCanciones);
                if (oldAlbumOfCancionesCollectionCanciones != null) {
                    oldAlbumOfCancionesCollectionCanciones.getCancionesCollection().remove(cancionesCollectionCanciones);
                    oldAlbumOfCancionesCollectionCanciones = em.merge(oldAlbumOfCancionesCollectionCanciones);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Albumes albumes) throws NonexistentEntityException, Exception {
        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();
            Albumes persistentAlbumes = em.find(Albumes.class, albumes.getIDalbum());
            Collection<Canciones> cancionesCollectionOld = persistentAlbumes.getCancionesCollection();
            Collection<Canciones> cancionesCollectionNew = albumes.getCancionesCollection();
            Collection<Canciones> attachedCancionesCollectionNew = new ArrayList<>();
            for (Canciones cancionesCollectionNewCancionesToAttach : cancionesCollectionNew) {
                cancionesCollectionNewCancionesToAttach = em.getReference(cancionesCollectionNewCancionesToAttach.getClass(), cancionesCollectionNewCancionesToAttach.getIDsong());
                attachedCancionesCollectionNew.add(cancionesCollectionNewCancionesToAttach);
            }
            cancionesCollectionNew = attachedCancionesCollectionNew;
            albumes.setCancionesCollection(cancionesCollectionNew);
            albumes = em.merge(albumes);
            for (Canciones cancionesCollectionOldCanciones : cancionesCollectionOld) {
                if (!cancionesCollectionNew.contains(cancionesCollectionOldCanciones)) {
                    cancionesCollectionOldCanciones.setAlbum(null);
                    cancionesCollectionOldCanciones = em.merge(cancionesCollectionOldCanciones);
                }
            }
            for (Canciones cancionesCollectionNewCanciones : cancionesCollectionNew) {
                if (!cancionesCollectionOld.contains(cancionesCollectionNewCanciones)) {
                    Albumes oldAlbumOfCancionesCollectionNewCanciones = cancionesCollectionNewCanciones.getAlbum();
                    cancionesCollectionNewCanciones.setAlbum(albumes);
                    cancionesCollectionNewCanciones = em.merge(cancionesCollectionNewCanciones);
                    if (oldAlbumOfCancionesCollectionNewCanciones != null && !oldAlbumOfCancionesCollectionNewCanciones.equals(albumes)) {
                        oldAlbumOfCancionesCollectionNewCanciones.getCancionesCollection().remove(cancionesCollectionNewCanciones);
                        oldAlbumOfCancionesCollectionNewCanciones = em.merge(oldAlbumOfCancionesCollectionNewCanciones);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = albumes.getIDalbum();
                if (findAlbumes(id) == null) {
                    throw new NonexistentEntityException("The albumes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Albumes albumes;
            try {
                albumes = em.getReference(Albumes.class, id);
                albumes.getIDalbum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The albumes with id " + id + " no longer exists.", enfe);
            }
            Collection<Canciones> cancionesCollection = albumes.getCancionesCollection();
            for (Canciones cancionesCollectionCanciones : cancionesCollection) {
                cancionesCollectionCanciones.setAlbum(null);
                cancionesCollectionCanciones = em.merge(cancionesCollectionCanciones);
            }
            em.remove(albumes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Albumes> findAlbumesEntities() {
        return findAlbumesEntities(true, -1, -1);
    }

    public List<Albumes> findAlbumesEntities(int maxResults, int firstResult) {
        return findAlbumesEntities(false, maxResults, firstResult);
    }

    private List<Albumes> findAlbumesEntities(boolean all, int maxResults, int firstResult) {
        try (EntityManager em = getEntityManager()) {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Albumes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        }
    }

    public Albumes findAlbumes(Integer id) {
        try (EntityManager em = getEntityManager()) {
            return em.find(Albumes.class, id);
        }
    }

    public int getAlbumesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Albumes> rt = cq.from(Albumes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
