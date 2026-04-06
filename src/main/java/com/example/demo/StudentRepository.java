package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT COUNT(s) FROM Student s WHERE s.status = :status")
    long countStudentsByStatus(@Param("status") String status);

    @Query("SELECT s.course, COUNT(s) FROM Student s WHERE s.course IS NOT NULL GROUP BY s.course ORDER BY COUNT(s) DESC")
    List<Object[]> getCountByCourse();
}