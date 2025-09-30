package com.example.main.service;

import com.example.main.model.Notes;
import com.example.main.model.User;
import com.example.main.repository.NotesRepository;
import com.example.main.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final UserRepository userRepository;
    private final NotesRepository notesRepository;

    public NoteService(UserRepository userRepository, NotesRepository notesRepository) {
        this.userRepository = userRepository;
        this.notesRepository = notesRepository;
    }

    public List<Notes> findNoteForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));


        return notesRepository.findByUserId(existingUser.getId());

    }

    public Notes addNote(Notes notes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username not found"));
        notes.setDate(LocalDateTime.now());
        notes.setUser(existingUser);
        notes.setDescription(notes.getDescription());
        notes.setTitle(notes.getTitle());
        notes.setId(null);
        notesRepository.save(notes);
        return notes;
    }

    public Notes updateNote(String id, String title, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User existingUser = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User Not Exist"));
        Notes existingNote = notesRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));

        if (!existingNote.getUser().getId().equals(existingUser.getId())) {
            throw new RuntimeException("You cannot update someone else's note");
        }

        existingNote.setDescription(description);
        existingNote.setTitle(title);
        existingNote.setDate(LocalDateTime.now());

        return notesRepository.save(existingNote);
    }

    public void deleteNote(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userRepository.findByUsername(userName).orElseThrow(() -> new RuntimeException("User not found"));
        Notes note = notesRepository.findById(id).orElseThrow(() -> new RuntimeException("Note Does not Exist"));
        if (!note.getUser().getId().equals(existingUser.getId())) {
            throw new RuntimeException("You cannot delete someone else's note");
        }
        notesRepository.deleteById(id);
    }

    public Page<Notes> getNote(int page, int size, String sortBy) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userRepository.findByUsername(userName).orElseThrow(()-> new RuntimeException("User Not Found"));
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());
        return notesRepository.findByUserId(existingUser.getId(),pageable);

    }
}
