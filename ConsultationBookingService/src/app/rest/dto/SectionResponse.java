package app.rest.dto;

import app.entities.Section;

public class SectionResponse {

	private Long id;
	private Long courseId;
	private String courseCode;
	private String sectionCode;
	private String facultyId;
	private String schedule;

	public static SectionResponse from(Section s) {
		SectionResponse r = new SectionResponse();
		r.id = s.getId();
		r.courseId = s.getCourse().getId();
		r.courseCode = s.getCourse().getCode();
		r.sectionCode = s.getSectionCode();
		r.facultyId = s.getProfessor().getFacultyId();
		r.schedule = s.getSchedule();
		return r;
	}

	public Long getId() { return id; }
	public Long getCourseId() { return courseId; }
	public String getCourseCode() { return courseCode; }
	public String getSectionCode() { return sectionCode; }
	public String getFacultyId() { return facultyId; }
	public String getSchedule() { return schedule; }
}
