package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.course.CourseCreateRequest;
import kz.soft.finalrpo.dto.course.CourseUpdateRequest;
import kz.soft.finalrpo.dto.course.CourseResponse;
import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.CourseMapper;
import kz.soft.finalrpo.repository.CourseRepository;
import kz.soft.finalrpo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void create_shouldCreateCourseAndReturnResponse() {
        // given
        Long teacherId = 2L;

        CourseCreateRequest request = new CourseCreateRequest();
        request.setTitle("Java Backend: Spring Boot");
        request.setDescription("REST API, JPA/Hibernate, Security");
        request.setTeacherId(teacherId);

        User teacher = new User();
        teacher.setId(teacherId);
        teacher.setEmail("aidana.teacher@gmail.com");

        Course savedCourse = new Course();
        savedCourse.setId(10L);
        savedCourse.setTitle(request.getTitle());
        savedCourse.setDescription(request.getDescription());
        savedCourse.setTeacher(teacher);

        when(userRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseResponse mappedResponse = mock(CourseResponse.class);
        when(courseMapper.toResponse(savedCourse)).thenReturn(mappedResponse);

        // when
        CourseResponse result = courseService.create(request);

        // then
        assertNotNull(result);
        assertSame(mappedResponse, result);

        // verify that we saved correct entity
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(captor.capture());

        Course courseToSave = captor.getValue();
        assertEquals(request.getTitle(), courseToSave.getTitle());
        assertEquals(request.getDescription(), courseToSave.getDescription());
        assertNotNull(courseToSave.getTeacher());
        assertEquals(teacherId, courseToSave.getTeacher().getId());

        verify(userRepository).findById(teacherId);
        verify(courseMapper).toResponse(savedCourse);
    }

    @Test
    void create_shouldThrowIfTeacherNotFound() {
        // given
        CourseCreateRequest request = new CourseCreateRequest();
        request.setTitle("Java Backend");
        request.setDescription("Spring Boot course");
        request.setTeacherId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> courseService.create(request));
        assertNotNull(ex.getMessage());

        verify(userRepository).findById(999L);
        verifyNoInteractions(courseRepository);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void getAll_shouldReturnMappedResponses() {
        // given
        User teacher = new User();
        teacher.setId(2L);
        teacher.setEmail("teacher@gmail.com");

        Course c1 = new Course();
        c1.setId(1L);
        c1.setTitle("Java Backend");
        c1.setDescription("Spring Boot course");
        c1.setTeacher(teacher);

        Course c2 = new Course();
        c2.setId(2L);
        c2.setTitle("DevOps");
        c2.setDescription("Docker and CI/CD");
        c2.setTeacher(teacher);

        when(courseRepository.findAll()).thenReturn(List.of(c1, c2));

        CourseResponse r1 = mock(CourseResponse.class);
        CourseResponse r2 = mock(CourseResponse.class);

        when(courseMapper.toResponse(c1)).thenReturn(r1);
        when(courseMapper.toResponse(c2)).thenReturn(r2);

        // when
        List<CourseResponse> result = courseService.getAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(r1, result.get(0));
        assertSame(r2, result.get(1));

        verify(courseRepository).findAll();
        verify(courseMapper).toResponse(c1);
        verify(courseMapper).toResponse(c2);
    }

    @Test
    void getById_shouldReturnMappedResponse() {
        // given
        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Java Backend");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseResponse mapped = mock(CourseResponse.class);
        when(courseMapper.toResponse(course)).thenReturn(mapped);

        // when
        CourseResponse result = courseService.getById(courseId);

        // then
        assertNotNull(result);
        assertSame(mapped, result);

        verify(courseRepository).findById(courseId);
        verify(courseMapper).toResponse(course);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        // given
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // when + then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> courseService.getById(courseId));
        assertNotNull(ex.getMessage());

        verify(courseRepository).findById(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void update_shouldUpdateFieldsAndReturnMappedResponse() {
        // given
        Long courseId = 1L;

        CourseUpdateRequest request = new CourseUpdateRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated description");

        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Old title");
        course.setDescription("Old description");

        Course saved = new Course();
        saved.setId(courseId);
        saved.setTitle("Updated title");
        saved.setDescription("Updated description");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(saved);

        CourseResponse mapped = mock(CourseResponse.class);
        when(courseMapper.toResponse(saved)).thenReturn(mapped);

        // when
        CourseResponse result = courseService.update(courseId, request);

        // then
        assertNotNull(result);
        assertSame(mapped, result);

        // entity updated before save
        assertEquals("Updated title", course.getTitle());
        assertEquals("Updated description", course.getDescription());

        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(course);
        verify(courseMapper).toResponse(saved);
    }

    @Test
    void update_shouldThrowIfNotFound() {
        // given
        Long courseId = 999L;

        CourseUpdateRequest request = new CourseUpdateRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated description");

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // when + then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> courseService.update(courseId, request));
        assertNotNull(ex.getMessage());

        verify(courseRepository).findById(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        // given
        Long courseId = 1L;

        // when
        courseService.delete(courseId);

        // then
        verify(courseRepository).deleteById(courseId);
        verifyNoMoreInteractions(courseRepository);
        verifyNoInteractions(courseMapper);
        verifyNoInteractions(userRepository);
    }
}
