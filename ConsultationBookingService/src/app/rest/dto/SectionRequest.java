package app.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SectionRequest {

	private Long courseId;
	@NotBlank
	private String facultyId;
	@NotBlank @Size(max = 3)
	private String sectionCode;
	@Size(max = 100)
	private String schedule;

	public Long getCourseId() { return courseId; }
	public void setCourseId(Long courseId) { this.courseId = courseId; }
	public String getFacultyId() { return facultyId; }
	public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
	public String getSectionCode() { return sectionCode; }
	public void setSectionCode(String sectionCode) { this.sectionCode = sectionCode; }
	public String getSchedule() { return schedule; }
	public void setSchedule(String schedule) { this.schedule = schedule; }
}