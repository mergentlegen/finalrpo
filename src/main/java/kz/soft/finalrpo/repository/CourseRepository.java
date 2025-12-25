package kz.soft.finalrpo.repository;

import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);
}
