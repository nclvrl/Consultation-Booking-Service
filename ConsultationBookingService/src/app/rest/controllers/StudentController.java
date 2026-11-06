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
import app.entities.Student;
import app.rest.dto.StudentRequest;

@Component
@Path("/api/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentController {

	@Autowired
	private DirectoryService directory;

	@POST
	public Response create(@Valid StudentRequest request) {
		Student s = directory.createStudent(request);
		return Response.status(Response.Status.CREATED).entity(s).build();
	}

	@GET
	public List<Student> list() {
		return directory.getAllStudents();
	}

	@GET
	@Path("/{studentId}")
	public Student get(@PathParam("studentId") String studentId) {
		return directory.getStudent(studentId);
	}

	@DELETE
	@Path("/{studentId}")
	public Map<String, Boolean> delete(@PathParam("studentId") String studentId) {
		directory.deleteStudent(studentId);
		return Collections.singletonMap("deleted", true);
	}
}
