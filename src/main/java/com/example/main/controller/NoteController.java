package com.example.main.controller;

import com.example.main.model.Notes;
import com.example.main.service.NoteService;
import org.springframework.data.domain.Page;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(
            @PathVariable String id,
            @RequestBody Notes notes
    ){
        Notes updatedNotes = noteService.updateNote(id,notes.getTitle(), notes.getDescription());
        return ResponseEntity.ok(updatedNotes);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id){
        try {
            noteService.deleteNote(id);
          return   ResponseEntity.ok("Note deleted");
        }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/page/find")
    public ResponseEntity<?> getPageableNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy
    ){
         Page<Notes> note = noteService.getNote(page,size,sortBy);
         return ResponseEntity.ok().body(note);
    }
}
