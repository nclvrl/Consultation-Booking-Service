package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "student_table")
public class Student {

	@Id
	@Column(name = "student_id", nullable = false, length = 6)
	@NotBlank
	@Pattern(regexp = "\\d{6}")
	private String studentId;

	@Column(nullable = false, length = 100)
	@NotBlank
	@Size(max = 100)
	private String name;

	@Column(nullable = false, unique = true)
	@NotBlank
	@Email
	private String email;

	@Column(name = "phone_number", nullable = false, length = 13)
	@NotBlank
	@Pattern(regexp = "\\+639\\d{9}")
	private String phoneNumber;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}