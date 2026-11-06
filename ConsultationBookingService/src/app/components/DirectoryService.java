package app.components;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.entities.Appointment;
import app.entities.AppointmentStatus;
import app.entities.ConsultationSlot;
import app.entities.Course;
import app.entities.Professor;
import app.entities.Section;
import app.entities.SlotStatus;
import app.entities.Student;
import app.repositories.AppointmentRepository;
import app.repositories.CourseRepository;
import app.repositories.ProfessorRepository;
import app.repositories.SectionRepository;
import app.repositories.SlotRepository;
import app.repositories.StudentRepository;
import app.rest.dto.AppointmentRequest;
import app.rest.dto.CourseRequest;
import app.rest.dto.ProfessorRequest;
import app.rest.dto.SectionRequest;
import app.rest.dto.SlotRequest;
import app.rest.dto.StudentRequest;
import app.rest.errorhandlers.BadRequestException;
import app.rest.errorhandlers.ConflictException;
import app.rest.errorhandlers.NotFoundException;


@Component
@Transactional
public class DirectoryService {

	@Autowired private StudentRepository studentRepo;
	@Autowired private ProfessorRepository professorRepo;
	@Autowired private CourseRepository courseRepo;
	@Autowired private SectionRepository sectionRepo;
	@Autowired private SlotRepository slotRepo;
	@Autowired private AppointmentRepository appointmentRepo;
	@Autowired private NotificationService notifier;


	// STUDENTS ------------------------------------------------------------

	public Student createStudent(StudentRequest r) {
		if (studentRepo.existsByStudentId(r.getStudentId()))
			throw new ConflictException("Student " + r.getStudentId() + " already exists");
		if (studentRepo.existsByEmail(r.getEmail()))
			throw new ConflictException("Email " + r.getEmail() + " already in use");

		Student s = new Student();
		s.setStudentId(r.getStudentId());
		s.setName(r.getName());
		s.setEmail(r.getEmail());
		s.setPhoneNumber(r.getPhoneNumber());
		return studentRepo.save(s);
	}

	public List<Student> getAllStudents() {
		return studentRepo.findAll();
	}

	public Student getStudent(String studentId) {
		return studentRepo.findByStudentId(studentId)
				.orElseThrow(() -> new NotFoundException("Student " + studentId + " not found"));
	}

	public void deleteStudent(String studentId) {
		Student s = getStudent(studentId);

		// cancel booked appointments because student is gone
		for (Appointment a : appointmentRepo.findByStudentStudentIdAndStatus(s.getStudentId(), AppointmentStatus.BOOKED)) {
			cancelInternal(a);
		}
		// delete appointment rows to enable student deletion (gets blocked because of FK reference)
		appointmentRepo.deleteAll(appointmentRepo.findByStudentStudentId(s.getStudentId()));
		studentRepo.delete(s);
	}

	// PROFESSORS ------------------------------------------------------------

	public Professor createProfessor(ProfessorRequest r) {
		if (professorRepo.existsByFacultyId(r.getFacultyId()))
			throw new ConflictException("Professor " + r.getFacultyId() + " already exists");
		if (professorRepo.existsByEmail(r.getEmail()))
			throw new ConflictException("Email " + r.getEmail() + " already in use");

		Professor p = new Professor();
		p.setFacultyId(r.getFacultyId());
		p.setName(r.getName());
		p.setEmail(r.getEmail());
		p.setPhoneNumber(r.getPhoneNumber());
		p.setDepartment(r.getDepartment());
		return professorRepo.save(p);
	}

	public List<Professor> getAllProfessors() {
		return professorRepo.findAll();
	}

	public Professor getProfessor(String facultyId) {
		return professorRepo.findByFacultyId(facultyId)
				.orElseThrow(() -> new NotFoundException("Professor " + facultyId + " not found"));
	}

	public void deleteProfessor(String facultyId) {
		Professor p = getProfessor(facultyId);
		if (sectionRepo.existsByProfessorFacultyId(p.getFacultyId()))
			throw new ConflictException("Professor " + facultyId + " still teaches sections");
		professorRepo.delete(p);
	}

	// COURSES ------------------------------------------------------------

	public Course createCourse(CourseRequest r) {
		if (courseRepo.existsByCode(r.getCode()))
			throw new ConflictException("Course " + r.getCode() + " already exists");

		Course c = new Course();
		c.setCode(r.getCode());
		c.setTitle(r.getTitle());
		return courseRepo.save(c);
	}

	public List<Course> getAllCourses() {
		return courseRepo.findAll();
	}

	public Course getCourse(Long id) {
		return courseRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Course " + id + " not found"));
	}

	public void deleteCourse(Long id) {
		Course c = getCourse(id);
		if (sectionRepo.existsByCourseId(id))
			throw new ConflictException("Course " + id + " still has sections");
		courseRepo.delete(c);
	}

	// SECTIONS ------------------------------------------------------------

	public Section createSection(Long courseId, SectionRequest r) {
		Course c = getCourse(courseId);
		Professor p = getProfessor(r.getFacultyId());

		if (sectionRepo.existsByCourseIdAndSectionCode(courseId, r.getSectionCode()))
			throw new ConflictException("Section " + r.getSectionCode() + " already exists for this course");

		Section s = new Section();
		s.setCourse(c);
		s.setProfessor(p);
		s.setSectionCode(r.getSectionCode());
		s.setSchedule(r.getSchedule());
		return sectionRepo.save(s);
	}

	public List<Section> getAllSections(Long courseId) {
		getCourse(courseId);
		return sectionRepo.findByCourseId(courseId);
	}

	public Section getSection(Long courseId, Long sectionId) {
		Section s = sectionRepo.findById(sectionId)
				.orElseThrow(() -> new NotFoundException("Section " + sectionId + " not found"));
		if (!s.getCourse().getId().equals(courseId))
			throw new NotFoundException("Section " + sectionId + " not found in course " + courseId);
		return s;
	}

	public void deleteSection(Long courseId, Long sectionId) {
		Section s = getSection(courseId, sectionId);
		if (slotRepo.existsBySectionIdAndStatusNot(sectionId, SlotStatus.CANCELLED))
			throw new ConflictException("Section " + sectionId + " still has non-cancelled slots");
		sectionRepo.delete(s);
	}

	// CONSULTATION SLOTS ------------------------------------------------------------

	public ConsultationSlot createSlot(SlotRequest r) {
		Section section = sectionRepo.findById(r.getSectionId())
				.orElseThrow(() -> new NotFoundException("Section " + r.getSectionId() + " not found"));

		if (r.getMaxStudents() < r.getMinStudents())
			throw new BadRequestException("maxStudents must be >= minStudents");

		// check for overlap with the professor's other non-cancelled slots
		LocalDateTime start = r.getStartTime();
		LocalDateTime end = start.plusMinutes(r.getDurationMinutes());
		List<ConsultationSlot> existing = slotRepo
				.findBySectionProfessorFacultyIdAndStatusNot(section.getProfessor().getFacultyId(), SlotStatus.CANCELLED);
		for (ConsultationSlot other : existing) {
			LocalDateTime otherEnd = other.getStartTime().plusMinutes(other.getDurationMinutes());
			if (start.isBefore(otherEnd) && end.isAfter(other.getStartTime()))
				throw new ConflictException("Overlaps with another slot of this professor");
		}

		ConsultationSlot slot = new ConsultationSlot();
		slot.setSection(section);
		slot.setStartTime(start);
		slot.setDurationMinutes(r.getDurationMinutes());
		slot.setLocation(r.getLocation());
		slot.setMinStudents(r.getMinStudents());
		slot.setMaxStudents(r.getMaxStudents());
		slot.setStatus(SlotStatus.OPEN);
		slot.setReminderSent(false);
		return slotRepo.save(slot);
	}

	public List<ConsultationSlot> findSlots(String status) {
		if (status == null || status.isBlank())
			return slotRepo.findAll();
		try {
			return slotRepo.findByStatus(SlotStatus.valueOf(status.toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Invalid status: " + status);
		}
	}

	public ConsultationSlot getSlot(Long id) {
		return slotRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Slot " + id + " not found"));
	}

	public int getBookedCount(Long slotId) {
		return (int) appointmentRepo.countBySlotIdAndStatus(slotId, AppointmentStatus.BOOKED);
	}

	public void deleteSlot(Long id) {
		ConsultationSlot slot = getSlot(id);

		for (Appointment a : appointmentRepo.findBySlotIdAndStatus(id, AppointmentStatus.BOOKED)) {
			a.setStatus(AppointmentStatus.CANCELLED);
			appointmentRepo.save(a);
			notifier.sendSms(a.getStudent().getPhoneNumber(),
					"The consultation for " + describe(slot) + " has been cancelled.");
		}
		appointmentRepo.deleteAll(appointmentRepo.findBySlotId(id));
		slotRepo.delete(slot);
	}

	// APPOINTMENTS ------------------------------------------------------------

	public Appointment bookAppointment(Long slotId, AppointmentRequest r) {
		ConsultationSlot slot = getSlot(slotId);
		Student student = studentRepo.findById(r.getStudentId())
				.orElseThrow(() -> new NotFoundException("Student " + r.getStudentId() + " not found"));

		if (slot.getStatus() != SlotStatus.OPEN && slot.getStatus() != SlotStatus.CONFIRMED)
			throw new ConflictException("Slot is not open for booking");
		if (!slot.getStartTime().isAfter(LocalDateTime.now()))
			throw new BadRequestException("Slot has already started");
		if (appointmentRepo.existsBySlotIdAndStudentStudentIdAndStatus(slotId, student.getStudentId(), AppointmentStatus.BOOKED))
			throw new ConflictException("Student already booked this slot");
		if (getBookedCount(slotId) >= slot.getMaxStudents())
			throw new ConflictException("Slot is full");

		Appointment a = new Appointment();
		a.setSlot(slot);
		a.setStudent(student);
		a.setTopic(r.getTopic());
		a.setStatus(AppointmentStatus.BOOKED);
		a.setBookedAt(LocalDateTime.now());
		a = appointmentRepo.save(a);

		int booked = getBookedCount(slotId);
		Professor prof = slot.getSection().getProfessor();

		if (booked == slot.getMinStudents() && slot.getStatus() == SlotStatus.OPEN) {
			slot.setStatus(SlotStatus.CONFIRMED);
			slotRepo.save(slot);
			String msg = "Your consultation for " + describe(slot) + " is pushing through.";
			for (Appointment b : appointmentRepo.findBySlotIdAndStatus(slotId, AppointmentStatus.BOOKED)) {
				notifier.sendSms(b.getStudent().getPhoneNumber(), msg);
			}
			notifier.sendSms(prof.getPhoneNumber(),
					"Your consultation for " + describe(slot) + " is confirmed (" + booked + " students).");
		} else if (slot.getStatus() == SlotStatus.CONFIRMED) {
			notifier.sendSms(student.getPhoneNumber(),
					"Your booking for " + describe(slot) + " is confirmed.");
		}
		return a;
	}

	public List<Appointment> findAppointments(Long slotId) {
		getSlot(slotId);
		return appointmentRepo.findBySlotId(slotId);
	}

	public Appointment getAppointment(Long slotId, Long appointmentId) {
		Appointment a = appointmentRepo.findById(appointmentId)
				.orElseThrow(() -> new NotFoundException("Appointment " + appointmentId + " not found"));
		if (!a.getSlot().getId().equals(slotId))
			throw new NotFoundException("Appointment " + appointmentId + " not found in slot " + slotId);
		return a;
	}

	public void cancelAppointment(Long slotId, Long appointmentId) {
		cancelInternal(getAppointment(slotId, appointmentId));
	}

	//	cancelAppointment and deleteStudent both use the method below
	private void cancelInternal(Appointment a) {
		if (a.getStatus() == AppointmentStatus.CANCELLED)
			return;

		a.setStatus(AppointmentStatus.CANCELLED);
		appointmentRepo.save(a);

		ConsultationSlot slot = a.getSlot();
		int booked = getBookedCount(slot.getId());

		if (slot.getStatus() == SlotStatus.CONFIRMED
				&& booked < slot.getMinStudents()
				&& slot.getStartTime().isAfter(LocalDateTime.now().plusHours(24))) {

			slot.setStatus(SlotStatus.OPEN);
			slotRepo.save(slot);

			String msg = "The consultation for " + describe(slot)
					+ " is no longer pushing through unless another student books.";
			for (Appointment b : appointmentRepo.findBySlotIdAndStatus(slot.getId(), AppointmentStatus.BOOKED)) {
				notifier.sendSms(b.getStudent().getPhoneNumber(), msg);
			}
			notifier.sendSms(slot.getSection().getProfessor().getPhoneNumber(), msg);
		}
	}

	private String describe(ConsultationSlot slot) {
		return slot.getSection().getCourse().getCode() + "-" + slot.getSection().getSectionCode()
				+ " on " + slot.getStartTime() + " at " + slot.getLocation();
	}
}
