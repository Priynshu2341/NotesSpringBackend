package com.example.main.service;

import com.example.main.model.Notes;
import com.example.main.model.User;
import com.example.main.repository.NotesRepository;
import com.example.main.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final NotesRepository notesRepository;

    public NoteService(UserRepository userRepository, AuthenticationManager authenticationManager, NotesRepository notesRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.notesRepository = notesRepository;
    }

    public List<Notes> findNoteForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        return notesRepository.findByUser(existingUser);

    }

    public Notes addNote(Notes notes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
        notes.setDate(LocalDateTime.now());

        notes.setUser(existingUser);
        notesRepository.save(notes);
        return notes;
    }
}
