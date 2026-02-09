/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ser_discografia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.List;
import jpa_discografia.Albumes;
import jpa_discografia.AlbumesJpaController;
import jpa_discografia.CancionesJpaController;
import jpa_discografia.exceptions.NonexistentEntityException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ainar
 */
@Path("albumes")
public class ServiceRESTAlbumes {

    private static final String PERSISTENCE_UNIT = "ApiRestDiscografiaPU";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        List<Albumes> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            AlbumesJpaController dao = new AlbumesJpaController(emf);
            lista = dao.findAlbumesEntities();
            if (lista == null) {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOne(@PathParam("id") int id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        Albumes alb;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            AlbumesJpaController dao = new AlbumesJpaController(emf);
            alb = dao.findAlbumes(id);

            if (alb == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe álbum con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(alb)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @GET
    @Path("/numtracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumTracks() {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        String resultado = "{}";
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            AlbumesJpaController dao = new AlbumesJpaController(emf);
            EntityManager em = dao.getEntityManager();

            Query query
                    = em.createQuery("SELECT a.Nombre, size(a.cancionesCollection) as pistas FROM Albumes a");
            List<Object[]> lista = query.getResultList();

            if ((lista != null) && (!lista.isEmpty())) {
                JSONArray jsonArray = new JSONArray();
                for (Object[] obj : lista) {
                    JSONObject json = new JSONObject();
                    json.put("album", (String) obj[0]);
                    json.put("pistas", (Integer) obj[1]);
                    jsonArray.put(json);
                }

                resultado = jsonArray.toString();
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(resultado)
                        .build();
            } else {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            }
        } catch (JSONException ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @GET
    @Path("/fecha/{año}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByAño(@PathParam("fecha") int año) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        String resultado = "{}";
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            AlbumesJpaController dao = new AlbumesJpaController(emf);
            EntityManager em = dao.getEntityManager();

            Query query
                    = em.createQuery("SELECT a.Nombre, a.year(fecha_publicacion) as año"
                            + " FROM Albumes a WHERE year(fecha_publicacion)=" + año);
            List<Object[]> lista = query.getResultList();

            if ((lista != null) && (!lista.isEmpty())) {
                JSONArray jsonArray = new JSONArray();
                for (Object[] obj : lista) {
                    JSONObject json = new JSONObject();
                    json.put("Nombre", (String) obj[0]);
                    json.put("Año", (Integer) obj[1]);
                    jsonArray.put(json);
                }

                resultado = jsonArray.toString();
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(resultado)
                        .build();
            } else {
                statusResul = Response.Status.NO_CONTENT;
                response = Response
                        .status(statusResul)
                        .build();
            }
        } catch (JSONException ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }
    
    @GET
    @Path("/interprete/{interprete}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByInterprete(@PathParam("interprete") String interprete) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        List<Albumes> lista = null;
        
        String intrepreteNormalizado = String.join("-", interprete.split("\\s"));

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            CancionesJpaController dao = new CancionesJpaController(emf);
            EntityManager em = dao.getEntityManager();
            
            TypedQuery<Albumes> consultaRegistros = 
                    em.createNamedQuery("Albumes.findByInterprete", Albumes.class);
            lista = consultaRegistros.setParameter("interprete", intrepreteNormalizado).getResultList();

            if (lista != null && !lista.isEmpty()) {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(lista)
                        .build();
            } else {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existen canciones del intérprete " + interprete);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Albumes alb) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            AlbumesJpaController dao = new AlbumesJpaController(emf);

            Albumes albFound = null;
            if ((alb.getIDalbum() != 0) && (alb.getIDalbum() != null)) {
                albFound = dao.findAlbumes(alb.getIDalbum());
            }

            if (albFound != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe álbum con ID " + alb.getIDalbum());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(alb);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Empleado " + alb.getNombre() + " grabado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Albumes album) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            AlbumesJpaController dao = new AlbumesJpaController(emf);
            int id = album.getIDalbum();
            Albumes albumFound = dao.findAlbumes(id);

            if (albumFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe álbum con ID " + album.getIDalbum());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos del libro encontrado
                albumFound.setNombre(album.getNombre());
                albumFound.setCancionesCollection(album.getCancionesCollection());
                albumFound.setCuriosidades(album.getCuriosidades());
                albumFound.setDescripcion(album.getDescripcion());
                albumFound.setDuracion(album.getDuracion());
                albumFound.setFechapublicacion(album.getFechapublicacion());
                albumFound.setInterprete(album.getInterprete());

                dao.edit(albumFound);

                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Libro con ID " + album.getIDalbum() + " actualizado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (Exception ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            AlbumesJpaController dao = new AlbumesJpaController(emf);
            Albumes albumFound = dao.findAlbumes(id);

            if (albumFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe álbum con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "álbum con ID " + id + " eliminado");
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            }
        } catch (NonexistentEntityException ex) {
            statusResul = Response.Status.BAD_REQUEST;
            mensaje.put("mensaje", "Error al procesar la petición");
            response = Response
                    .status(statusResul)
                    .entity(mensaje)
                    .build();
        } finally {
            if (emf != null) {
                emf.close();
            }
        }
        return response;
    }
}
