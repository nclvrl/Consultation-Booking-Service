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
import app.entities.Course;
import app.rest.dto.CourseRequest;

@Component
@Path("/api/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseController {

	@Autowired
	private DirectoryService directory;

	@POST
	public Response create(@Valid CourseRequest request) {
		Course c = directory.createCourse(request);
		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	@GET
	public List<Course> list() {
		return directory.getAllCourses();
	}

	@GET
	@Path("/{id}")
	public Course get(@PathParam("id") Long id) {
		return directory.getCourse(id);
	}

	@DELETE
	@Path("/{id}")
	public Map<String, Boolean> delete(@PathParam("id") Long id) {
		directory.deleteCourse(id);
		return Collections.singletonMap("deleted", true);
	}
}
