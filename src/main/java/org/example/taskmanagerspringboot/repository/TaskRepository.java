package org.example.taskmanagerspringboot.repository;

import org.example.taskmanagerspringboot.Entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByTitleContaining(String title, Pageable pageable);
}
