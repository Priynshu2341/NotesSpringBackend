package com.example.main.repository;

import com.example.main.model.Notes;
import com.example.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {


   List<Notes> findByUser(User user);
}
