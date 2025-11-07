package edu.upc.dsa.services;

import edu.upc.dsa.bibliotecaMANAGERImpl;
import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;

import io.swagger.annotations.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Api(value = "/biblioteca", description = "Endpoint para gestionar la biblioteca rural")
@Path("/biblioteca")
public class bibliotecaService {

    private final bibliotecaMANAGERImpl manager;

    public bibliotecaService() {
        this.manager = bibliotecaMANAGERImpl.getInstance();

        // Inicialización mínima por si está vacío
        if (this.manager.getLectores().isEmpty()) {

            // Añadir lectores
            Lector lector1 = new Lector("L001", "Juan", "García", "11111111A", "1990-05-12", "Madrid", "Calle A");
            Lector lector2 = new Lector("L002", "Ana", "Pérez", "22222222B", "1992-03-10", "Barcelona", "Calle B");
            this.manager.getLectores().add(lector1);
            this.manager.getLectores().add(lector2);

            // Añadir libros al almacén
            Libro libro1 = new Libro("B001", "ISBN001", "El Quijote", "Anaya", 2005, 1, "Cervantes", "Novela");
            Libro libro2 = new Libro("B002", "ISBN002", "El Hobbit", "Minotauro", 2012, 3, "Tolkien", "Fantasía");
            Libro libro3 = new Libro("B003", "ISBN003", "Cien años de soledad", "Sudamericana", 1998, 2, "García Márquez", "Realismo mágico");

            this.manager.almacenarLibro(libro1);
            this.manager.almacenarLibro(libro2);
            this.manager.almacenarLibro(libro3);

            // Catalogar los libros (para crear el catálogo inicial)
            this.manager.catalogarLibro();
            this.manager.catalogarLibro();
            this.manager.catalogarLibro();

            // Añadir un préstamo inicial para probar la consulta
            try {
                this.manager.prestarLibro("L001", "ISBN001");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ============================================================
    // GET: Consultar todos los lectores
    // ============================================================
    @GET
    @ApiOperation(value = "Obtener todos los lectores registrados", notes = "Devuelve una lista de lectores")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Lector.class, responseContainer = "List")
    })
    @Path("/lectores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLectores() {
        List<Lector> lectores = this.manager.getLectores();
        GenericEntity<List<Lector>> entity = new GenericEntity<List<Lector>>(lectores) {};
        return Response.status(200).entity(entity).build();
    }

    // ============================================================
    // POST: Crear un nuevo préstamo
    // ============================================================
    @POST
    @ApiOperation(
            value = "Crear un nuevo préstamo",
            notes = "Crea un préstamo si el lector y el libro existen y hay ejemplares disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Préstamo creado correctamente"),
            @ApiResponse(code = 400, message = "Datos inválidos o libro sin ejemplares disponibles"),
            @ApiResponse(code = 404, message = "Lector o libro no encontrado")
    })
    @Path("/prestamos/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response prestarLibro(Prestamo request) {
        if (request == null || request.getIdLector() == null || request.getIsbnLibro() == null) {
            return Response.status(400).entity("Petición inválida").build();
        }

        try {
            this.manager.prestarLibro(request.getIdLector(), request.getIsbnLibro());
            return Response.status(201).entity("Préstamo creado correctamente").build();
        }
        catch (LectorNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
        catch (LibroNoEncontradoException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
        catch (StockInsuficienteException e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    // ============================================================
    // POST: Añadir un nuevo lector
    // ============================================================
    @POST
    @ApiOperation(value = "Añadir un nuevo lector", notes = "Si el lector ya existe, se actualizan sus datos")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Lector añadido o actualizado correctamente"),
            @ApiResponse(code = 400, message = "Datos del lector inválidos")
    })
    @Path("/lectores/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLector(Lector lector) {
        if (lector == null || lector.getId() == null) {
            return Response.status(400).entity("Datos inválidos").build();
        }

        this.manager.añadirLector(lector);
        return Response.status(201).entity("Lector añadido o actualizado correctamente").build();
    }

    // ============================================================
    // POST: Almacenar libro (apilarlo)
    // ============================================================
    @POST
    @ApiOperation(value = "Almacenar libro", notes = "Apila el libro en el último montón disponible o crea uno nuevo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Libro almacenado correctamente")
    })
    @Path("/libros/almacenar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response almacenarLibro(Libro libro) {
        if (libro == null || libro.getId() == null) {
            return Response.status(400).entity("Datos del libro inválidos").build();
        }

        this.manager.almacenarLibro(libro);
        return Response.status(201).entity("Libro almacenado correctamente").build();
    }

    // ============================================================
    // PUT: Catalogar el siguiente libro pendiente
    // ============================================================
    @PUT
    @ApiOperation(value = "Catalogar un libro", notes = "Saca el siguiente libro del primer montón y lo añade al catálogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Libro catalogado correctamente"),
            @ApiResponse(code = 404, message = "No hay libros pendientes de catalogar")
    })
    @Path("/libros/catalogar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response catalogarLibro() {
        if (this.manager.getMontones().isEmpty()) {
            return Response.status(404).entity("No hay libros pendientes de catalogar").build();
        }

        this.manager.catalogarLibro();
        return Response.status(200).entity("Libro catalogado correctamente").build();
    }

    // ============================================================
    // GET: Consultar préstamos de un lector
    // ============================================================
    @GET
    @ApiOperation(value = "Consultar préstamos de un lector", notes = "Devuelve todos los préstamos asociados a un lector")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Prestamo.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Lector no encontrado")
    })
    @Path("/lectores/{id}/prestamos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrestamosDeLector(@PathParam("id") String idLector) {
        boolean existe = false;
        for (Lector l : this.manager.getLectores()) {
            if (l.getId().equals(idLector)) {
                existe = true;
                break;
            }
        }

        if (!existe) {
            return Response.status(404).entity("Lector no encontrado").build();
        }

        List<Prestamo> prestamos = this.manager.consultarPrestamosDeLector(idLector);
        GenericEntity<List<Prestamo>> entity = new GenericEntity<List<Prestamo>>(prestamos) {};
        return Response.status(200).entity(entity).build();
    }
}
