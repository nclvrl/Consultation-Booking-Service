package app.components;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class DataInitializer {

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private ProfessorRepository professorRepo;

	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	private SectionRepository sectionRepo;

	@Autowired
	private SlotRepository slotRepo;

	@Autowired
	private AppointmentRepository appointmentRepo;

	@PostConstruct
	public void init() {
		if (professorRepo.count() > 0 || courseRepo.count() > 0 || studentRepo.count() > 0) {
			return;
		}

		// 3 professors
		Professor p1 = addProfessor("100001", "Dr. Maria Santos", "msantos@ateneo.edu", "+639181234567", "Computer Science");
		Professor p2 = addProfessor("100002", "Dr. Six Seven", "sseven@ateneo.edu", "+639191234567", "Computer Science");
		Professor p3 = addProfessor("100003", "Prof. Ana Lim", "alim@ateneo.edu", "+639201234567", "Information Systems");

		// 4 courses
		Course c1 = addCourse("CSCI 180.05", "Special Topics in Languages: Enterprise Systems Programming");
		Course c2 = addCourse("CSCI 70", "Structure and Interpretation of Programming Languages");
		Course c3 = addCourse("CSCI 30", "Data Structures and Algorithms");
		Course c4 = addCourse("CSCI 60", "Computer Networks and Data Communications");

		// 6 students
		Student st1 = addStudent("230001", "Juan Dela Cruz", "juan@student.ateneo.edu", "+639171234501");
		Student st2 = addStudent("230002", "Maria Clara", "mclara@student.ateneo.edu", "+639171234502");
		Student st3 = addStudent("230003", "Pedro Penduko", "ppenduko@student.ateneo.edu", "+639171234503");
		Student st4 = addStudent("230004", "Gabriela Silang", "gsilang@student.ateneo.edu", "+639171234504");
		Student st5 = addStudent("230005", "Andres Bonifacio", "abonifacio@student.ateneo.edu", "+639171234505");
		Student st6 = addStudent("230006", "Teresa Magbanua", "tmagbanua@student.ateneo.edu", "+639171234506");

		// 6 sections
		Section s1 = addSection(c1, p1, "O", "TF 3:30-5:00 PM");
		Section s2 = addSection(c1, p2, "E", "MTh 2:00-3:30 PM");
		Section s3 = addSection(c2, p1, "B", "MTh 9:30-11:00 AM");
		addSection(c2, p3, "L", "TF 11:00-12:30 PM");
		Section s5 = addSection(c3, p2, "D", "MTh 12:30-2:00 PM");
		addSection(c4, p3, "J", "TF 8:00-9:30 AM");

		// 4 sample slots
		// 1) OPEN with 1 booking
		ConsultationSlot slot1 = addSlot(s1, daysFromNow(2, 14), 30, "CTC 215", 2, 5, SlotStatus.OPEN);
		// 2) CONFIRMED (min of 2 reached)
		ConsultationSlot slot2 = addSlot(s2, daysFromNow(3, 10), 30, "CTC 301", 2, 4, SlotStatus.CONFIRMED);
		// 3) OPEN with no bookings
		addSlot(s3, daysFromNow(4, 15), 60, "CTC 215", 2, 3, SlotStatus.OPEN);
		// 4) Far in the future
		addSlot(s5, daysFromNow(90, 13), 45, "BEL 204", 2, 6, SlotStatus.OPEN);

		// Appointments matching those states
		addAppointment(slot1, st1, "Springboot help");
		addAppointment(slot2, st2, "Project proposal feedback");
		addAppointment(slot2, st3, "Entity relationship diagram");
	}

	private LocalDateTime daysFromNow(int days, int hour) {
		return LocalDateTime.now().plusDays(days).withHour(hour).withMinute(0).withSecond(0).withNano(0);
	}

	private Professor addProfessor(String facultyId, String name, String email, String phoneNumber, String department) {
		Professor p = new Professor();
		p.setFacultyId(facultyId);
		p.setName(name);
		p.setEmail(email);
		p.setPhoneNumber(phoneNumber);
		p.setDepartment(department);
		return professorRepo.save(p);
	}

	private Course addCourse(String code, String title) {
		Course c = new Course();
		c.setCode(code);
		c.setTitle(title);
		return courseRepo.save(c);
	}

	private Student addStudent(String studentId, String name, String email, String phoneNumber) {
		Student s = new Student();
		s.setStudentId(studentId);
		s.setName(name);
		s.setEmail(email);
		s.setPhoneNumber(phoneNumber);
		return studentRepo.save(s);
	}

	private Section addSection(Course course, Professor professor, String sectionCode, String schedule) {
		Section s = new Section();
		s.setCourse(course);
		s.setProfessor(professor);
		s.setSectionCode(sectionCode);
		s.setSchedule(schedule);
		return sectionRepo.save(s);
	}

	private ConsultationSlot addSlot(Section section, LocalDateTime startTime, int durationMinutes, String location,
			int minStudents, int maxStudents, SlotStatus status) {
		ConsultationSlot slot = new ConsultationSlot();
		slot.setSection(section);
		slot.setStartTime(startTime);
		slot.setDurationMinutes(durationMinutes);
		slot.setLocation(location);
		slot.setMinStudents(minStudents);
		slot.setMaxStudents(maxStudents);
		slot.setStatus(status);
		slot.setReminderSent(false);
		return slotRepo.save(slot);
	}

	private Appointment addAppointment(ConsultationSlot slot, Student student, String topic) {
		Appointment a = new Appointment();
		a.setSlot(slot);
		a.setStudent(student);
		a.setTopic(topic);
		a.setStatus(AppointmentStatus.BOOKED);
		a.setBookedAt(LocalDateTime.now());
		return appointmentRepo.save(a);
	}

}
