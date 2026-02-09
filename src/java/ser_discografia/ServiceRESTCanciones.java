/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ser_discografia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
import java.util.HashMap;
import java.util.List;
import jpa_discografia.Albumes;
import jpa_discografia.AlbumesJpaController;
import jpa_discografia.CancionesJpaController;
import jpa_discografia.Canciones;
import jpa_discografia.exceptions.NonexistentEntityException;

/**
 *
 * @author ainar
 */
@Path("canciones")
public class ServiceRESTCanciones {

    private static final String PERSISTENCE_UNIT = "ApiRestDiscografiaPU";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        List<Canciones> lista;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            CancionesJpaController dao = new CancionesJpaController(emf);
            lista = dao.findCancionesEntities();
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

        Canciones cancion;
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            CancionesJpaController dao = new CancionesJpaController(emf);
            cancion = dao.findCanciones(id);

            if (cancion == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe canción con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                statusResul = Response.Status.OK;
                response = Response
                        .status(statusResul)
                        .entity(cancion)
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
    @Path("/interprete/{interprete}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByInterprete(@PathParam("interprete") String interprete) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;
        List<Canciones> lista = null;
        
        String intrepreteNormalizado = String.join("-", interprete.split("\\s"));

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            CancionesJpaController dao = new CancionesJpaController(emf);
            EntityManager em = dao.getEntityManager();
            
            TypedQuery<Canciones> consultaRegistros = 
                    em.createNamedQuery("Canciones.findByInterprete", Canciones.class);
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
    public Response post(Canciones cancion) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            CancionesJpaController dao = new CancionesJpaController(emf);

            Canciones cancionFound = null;
            if ((cancion.getIDsong() != 0) && (cancion.getIDsong() != null)) {
                cancionFound = dao.findCanciones(cancion.getIDsong());
            }

            if (cancionFound != null) {
                statusResul = Response.Status.FOUND;
                mensaje.put("mensaje", "Ya existe canción con ID " + cancion.getIDsong());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.create(cancion);
                statusResul = Response.Status.CREATED;
                mensaje.put("mensaje", "Canción " + cancion.getNombre() + " grabada");
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
    public Response put(Canciones cancion) {
        EntityManagerFactory emf = null;
        HashMap<String, String> mensaje = new HashMap<>();
        Response response;
        Response.Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            CancionesJpaController dao = new CancionesJpaController(emf);
            int id = cancion.getIDsong();
            Canciones cancionFound = dao.findCanciones(id);

            if (cancionFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe canción con ID " + cancion.getIDsong());
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                // Actualizar campos de la canción encontrada
                cancionFound.setInterprete(cancion.getInterprete());
                cancionFound.setNombre(cancion.getNombre());
                cancionFound.setArtistasinvitados(cancion.getArtistasinvitados());
                cancionFound.setNumerodepista(cancion.getNumerodepista());
                cancionFound.setDuracion(cancion.getDuracion());
                cancionFound.setSingle(cancion.getSingle());
                cancionFound.setDescripcion(cancion.getDescripcion());
                cancionFound.setCompositores(cancion.getCompositores());
                cancionFound.setProductores(cancion.getProductores());
                cancionFound.setSamples(cancion.getSamples());
                cancionFound.setAlbum(cancion.getAlbum());

                dao.edit(cancionFound);

                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Canción con ID " + cancion.getIDsong() + " actualizada");
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
        Response.Status statusResul;

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            CancionesJpaController dao = new CancionesJpaController(emf);
            Canciones cancionFound = dao.findCanciones(id);

            if (cancionFound == null) {
                statusResul = Response.Status.NOT_FOUND;
                mensaje.put("mensaje", "No existe canción con ID " + id);
                response = Response
                        .status(statusResul)
                        .entity(mensaje)
                        .build();
            } else {
                dao.destroy(id);
                statusResul = Response.Status.OK;
                mensaje.put("mensaje", "Canción con ID " + id + " eliminado");
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
