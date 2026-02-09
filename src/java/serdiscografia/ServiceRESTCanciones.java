/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sereminem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import jpaeminem.CancionesJpaController;
import jpaeminem.Canciones;

/**
 *
 * @author ainar
 */
@Path("canciones")
public class ServiceRESTCanciones {

    private static final String PERSISTENCE_UNIT = "ApiRest2PU";

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

        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            CancionesJpaController dao = new CancionesJpaController(emf);
            EntityManager em = dao.getEntityManager();
            
            TypedQuery<Canciones> consultaRegistros = 
                    em.createNamedQuery("Canciones.findByInterprete", Canciones.class);
            lista = consultaRegistros.setParameter("interprete", interprete).getResultList();

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

}
