package kz.soft.finalrpo.repository;

import kz.soft.finalrpo.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByCourseId(Long courseId);
}
