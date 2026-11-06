package app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Course;
import app.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

	boolean existsByStudentId(String studentId);

	boolean existsByEmail(String email);

	Optional<Student> findByStudentId(String studentId);

}
