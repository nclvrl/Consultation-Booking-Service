package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, String> {

	boolean existsByFacultyId(String facultyId);

	boolean existsByEmail(String email);

}
