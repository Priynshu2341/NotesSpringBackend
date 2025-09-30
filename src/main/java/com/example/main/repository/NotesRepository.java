package com.example.main.repository;

import com.example.main.model.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotesRepository extends MongoRepository<Notes, String> {


    List<Notes> findByUserId(String username);


    Page<Notes> findByUserId(String id , Pageable pageable);
}
