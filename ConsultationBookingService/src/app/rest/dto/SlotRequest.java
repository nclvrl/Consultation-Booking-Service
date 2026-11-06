package app.rest.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SlotRequest {

	@NotNull
	private Long sectionId;
	private String facultyId;
	@NotNull @Future
	private LocalDateTime startTime;
	@Min(15) @Max(120)
	private int durationMinutes;
	@NotBlank @Size(max = 100)
	private String location;
	@Min(2)
	private int minStudents = 2;
	@Min(2) @Max(10)
	private int maxStudents;

	public Long getSectionId() { return sectionId; }
	public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
	public String getFacultyId() { return facultyId; }
	public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
	public LocalDateTime getStartTime() { return startTime; }
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
	public int getDurationMinutes() { return durationMinutes; }
	public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }
	public int getMinStudents() { return minStudents; }
	public void setMinStudents(int minStudents) { this.minStudents = minStudents; }
	public int getMaxStudents() { return maxStudents; }
	public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }
}
