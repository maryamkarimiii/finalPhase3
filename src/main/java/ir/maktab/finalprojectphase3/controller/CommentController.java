package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.CommentDTO;
import ir.maktab.finalprojectphase3.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("add/comments")
    public void addNewComment(@Valid @RequestBody CommentDTO commentDTO) {
        commentService.save(commentDTO);
    }

    @GetMapping("get_comments/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.findById(id);
        return ResponseEntity.ok().body(commentDTO);
    }
}
