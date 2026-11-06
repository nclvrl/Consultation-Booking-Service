package app.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AppointmentRequest {

	private Long slotId;
	@NotNull
	private String studentId;
	@NotBlank @Size(max = 255)
	private String topic;

	public Long getSlotId() { return slotId; }
	public void setSlotId(Long slotId) { this.slotId = slotId; }
	public String getStudentId() { return studentId; }
	public void setStudentId(String studentId) { this.studentId = studentId; }
	public String getTopic() { return topic; }
	public void setTopic(String topic) { this.topic = topic; }
}
