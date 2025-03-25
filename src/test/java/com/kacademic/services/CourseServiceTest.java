package com.kacademic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kacademic.dto.course.CourseRequestDTO;
import com.kacademic.repositories.CourseRepository;

public class CourseServiceTest {

    @Mock
    private CourseRepository courseR;

    @InjectMocks
    private CourseService courseS;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given valid course data, when creating course, then should be sucessful")
    void givenValidData_whenCreateCourse_thenSuccess() throws InterruptedException, ExecutionException {

        // Arrange
        CourseRequestDTO data = new CourseRequestDTO("Ciência da Computação", "04", "Dor e Sofrimento");

        // Act
        CompletableFuture<String> result = this.courseS.createAsync(data);
        String response = result.get();

        // Assert
        assertEquals("Created Course", response);

        verify(courseR, times(1)).save(any());

    }

}
