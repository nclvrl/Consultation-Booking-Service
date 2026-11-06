package app.rest.dto;

import java.time.LocalDateTime;

import app.entities.Appointment;

public class AppointmentResponse {

	private Long id;
	private Long slotId;
	private String studentId;
	private String topic;
	private String status;
	private LocalDateTime bookedAt;

	public static AppointmentResponse from(Appointment a) {
		AppointmentResponse r = new AppointmentResponse();
		r.id = a.getId();
		r.slotId = a.getSlot().getId();
		r.studentId = a.getStudent().getStudentId();
		r.topic = a.getTopic();
		r.status = a.getStatus().name();
		r.bookedAt = a.getBookedAt();
		return r;
	}

	public Long getId() { return id; }
	public Long getSlotId() { return slotId; }
	public String getStudentId() { return studentId; }
	public String getTopic() { return topic; }
	public String getStatus() { return status; }
	public LocalDateTime getBookedAt() { return bookedAt; }
}
