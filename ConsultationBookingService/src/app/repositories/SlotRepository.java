package app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entities.ConsultationSlot;
import app.entities.SlotStatus;

@Repository
public interface SlotRepository extends JpaRepository<ConsultationSlot, Long> {

	List<ConsultationSlot> findByStatus(SlotStatus status);

	List<ConsultationSlot> findBySectionProfessorFacultyIdAndStatus(String facultyId, SlotStatus status);

	List<ConsultationSlot> findBySectionCourseCodeAndStatus(String code, SlotStatus status);

	List<ConsultationSlot> findBySectionIdAndStatus(Long sectionId, SlotStatus status);

	@Query("SELECT s FROM ConsultationSlot s "
			+ "WHERE s.section.professor.facultyId = :facultyId "
			+ "AND s.status <> :excludedStatus "
			+ "AND s.startTime > :windowStart "
			+ "AND s.startTime < :windowEnd")
	List<ConsultationSlot> checkOverlap(@Param("facultyId") String facultyId,
			@Param("excludedStatus") SlotStatus excludedStatus,
			@Param("windowStart") LocalDateTime windowStart,
			@Param("windowEnd") LocalDateTime windowEnd);

	List<ConsultationSlot> findByStatusAndStartTimeBefore(SlotStatus status, LocalDateTime cutoff);

	List<ConsultationSlot> findByReminderSentFalseAndStatusAndStartTimeBetween(SlotStatus status,
			LocalDateTime from, LocalDateTime to);

	boolean existsBySectionIdAndStatus(Long sectionId, SlotStatus status);

	boolean existsBySectionIdAndStatusNot(Long sectionId, SlotStatus status);

}
