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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "consultation_slot_table")
public class ConsultationSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "section_id", nullable = false)
	@NotNull
	private Section section;

	@Column(name = "start_time", nullable = false)
	@NotNull
	private LocalDateTime startTime;

	@Column(name = "duration_minutes", nullable = false)
	@Min(15)
	@Max(120)
	private int durationMinutes;

	@Column(nullable = false, length = 100)
	@NotBlank
	@Size(max = 100)
	private String location;

	@Column(name = "min_students", nullable = false)
	@Min(2)
	private int minStudents = 2;

	@Column(name = "max_students", nullable = false)
	@Min(2)
	@Max(10)
	private int maxStudents;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private SlotStatus status = SlotStatus.OPEN;

	@Column(name = "reminder_sent", nullable = false)
	private boolean reminderSent = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMinStudents() {
		return minStudents;
	}

	public void setMinStudents(int minStudents) {
		this.minStudents = minStudents;
	}

	public int getMaxStudents() {
		return maxStudents;
	}

	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	public SlotStatus getStatus() {
		return status;
	}

	public void setStatus(SlotStatus status) {
		this.status = status;
	}

	public boolean isReminderSent() {
		return reminderSent;
	}

	public void setReminderSent(boolean reminderSent) {
		this.reminderSent = reminderSent;
	}

}
