package app.rest.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.components.DirectoryService;
import app.entities.Professor;
import app.rest.dto.ProfessorRequest;

@Component
@Path("/api/professors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfessorController {

	@Autowired
	private DirectoryService directory;

	@POST
	public Response create(@Valid ProfessorRequest request) {
		Professor p = directory.createProfessor(request);
		return Response.status(Response.Status.CREATED).entity(p).build();
	}

	@GET
	public List<Professor> list() {
		return directory.getAllProfessors();
	}

	@GET
	@Path("/{facultyId}")
	public Professor get(@PathParam("facultyId") String facultyId) {
		return directory.getProfessor(facultyId);
	}

	@DELETE
	@Path("/{facultyId}")
	public Map<String, Boolean> delete(@PathParam("facultyId") String facultyId) {
		directory.deleteProfessor(facultyId);
		return Collections.singletonMap("deleted", true);
	}
}
