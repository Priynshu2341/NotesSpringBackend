package com.example.main.controller;

import com.example.main.model.Notes;
import com.example.main.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {



    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping("/add")
    public Notes addNote(@RequestBody Notes note){
        return noteService.addNote(note);
    }
    @GetMapping("/find")
    public List<Notes> getAllNotes(){
        return noteService.findNoteForLoggedInUser();
    }
}
