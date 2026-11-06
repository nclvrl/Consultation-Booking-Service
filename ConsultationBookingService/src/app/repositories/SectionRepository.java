package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

	boolean existsByCourseIdAndSectionCode(Long courseId, String sectionCode);

	List<Section> findByCourseId(Long courseId);

	List<Section> findByProfessorFacultyId(String facultyId);

	boolean existsByCourseId(Long courseId);

	boolean existsByProfessorFacultyId(String facultyId);

}