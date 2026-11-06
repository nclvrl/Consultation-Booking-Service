package app.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProfessorRequest {

	@NotBlank @Pattern(regexp = "\\d{6}", message = "must be 6 digits")
	private String facultyId;
	@NotBlank @Size(max = 100)
	private String name;
	@NotBlank @Email
	private String email;
	@NotBlank @Pattern(regexp = "\\+639\\d{9}", message = "must match +639XXXXXXXXX")
	private String phoneNumber;
	@NotBlank @Size(max = 100)
	private String department;

	public String getFacultyId() { return facultyId; }
	public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public String getDepartment() { return department; }
	public void setDepartment(String department) { this.department = department; }
}
