package kz.soft.finalrpo.repository;

import kz.soft.finalrpo.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    List<Enrollment> findAllByCourseId(Long courseId);

    List<Enrollment> findAllByStudentId(Long studentId);

    Optional<Enrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    boolean existsByStudentIdAndCourseId(Long userId, Long courseId);

}
