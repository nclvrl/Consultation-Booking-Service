package app.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "appointment_table")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "slot_id", nullable = false)
	@NotNull
	private ConsultationSlot slot;

	@ManyToOne(optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	@NotNull
	private Student student;

	@Column(nullable = false, length = 255)
	@NotBlank
	@Size(max = 255)
	private String topic;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private AppointmentStatus status;

	@Column(name = "booked_at", nullable = false)
	private LocalDateTime bookedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ConsultationSlot getSlot() {
		return slot;
	}

	public void setSlot(ConsultationSlot slot) {
		this.slot = slot;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

}