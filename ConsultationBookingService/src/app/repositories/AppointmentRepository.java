package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Appointment;
import app.entities.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	long countBySlotIdAndStatus(Long slotId, AppointmentStatus status);

	boolean existsBySlotIdAndStudentStudentIdAndStatus(Long slotId, String studentId, AppointmentStatus status);

	default boolean checkDoubleBooking(Long slotId, String studentId, AppointmentStatus status) {
		return existsBySlotIdAndStudentStudentIdAndStatus(slotId, studentId, status);
	}

	List<Appointment> findBySlotIdAndStatus(Long slotId, AppointmentStatus status);

	List<Appointment> findByStudentStudentIdAndStatus(String studentId, AppointmentStatus status);

	List<Appointment> findBySlotId(Long slotId);

}
