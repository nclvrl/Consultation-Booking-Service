package app.rest.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import app.rest.dto.SectionRequest;
import app.rest.dto.SectionResponse;

@Component
@Path("/api/courses/{courseId}/sections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SectionController {

	@Autowired
	private DirectoryService directory;

	@POST
	public Response create(@PathParam("courseId") Long courseId, @Valid SectionRequest request) {
		SectionResponse body = SectionResponse.from(directory.createSection(courseId, request));
		return Response.status(Response.Status.CREATED).entity(body).build();
	}

	@GET
	public List<SectionResponse> list(@PathParam("courseId") Long courseId) {
		return directory.getAllSections(courseId).stream()
				.map(SectionResponse::from)
				.collect(Collectors.toList());
	}

	@GET
	@Path("/{sectionId}")
	public SectionResponse get(@PathParam("courseId") Long courseId,
	                           @PathParam("sectionId") Long sectionId) {
		return SectionResponse.from(directory.getSection(courseId, sectionId));
	}

	@DELETE
	@Path("/{sectionId}")
	public Map<String, Boolean> delete(@PathParam("courseId") Long courseId,
	                                   @PathParam("sectionId") Long sectionId) {
		directory.deleteSection(courseId, sectionId);
		return Collections.singletonMap("deleted", true);
	}
}
