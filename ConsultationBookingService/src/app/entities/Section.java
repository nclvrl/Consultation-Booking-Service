package app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "section_table", uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "section_code"}))
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "course_id", nullable = false)
	@NotNull
	private Course course;

	@ManyToOne(optional = false)
	@JoinColumn(name = "faculty_id", nullable = false)
	@NotNull
	private Professor professor;

	@Column(name = "section_code", nullable = false, length = 20)
	@NotBlank
	@Size(max = 20)
	private String sectionCode;

	@Column(nullable = false, length = 100)
	@Size(max = 100)
	private String schedule;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

}