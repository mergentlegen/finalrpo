package kz.soft.finalrpo.repository;

import kz.soft.finalrpo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCourseId(Long courseId);
    List<Review> findAllByAuthorId(Long authorId);

    boolean existsByAuthorIdAndCourseId(Long authorId, Long courseId);


}
