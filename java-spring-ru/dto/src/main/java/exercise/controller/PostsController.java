package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/{id}")
    public PostDTO findPostById(@PathVariable Long id) {
        Optional<Post> maybePost = postRepository.findById(id);
        if (maybePost.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Post with id %d not found", id));
        }

        PostDTO post = toPostDTO(maybePost.get());
        List<Comment> comments = commentRepository.findByPostId(post.getId());

        for (Comment comment: comments) {
            CommentDTO commentDTO = toCommentDto(comment);
            post.getComments().add(commentDTO);
        }

        return post;
    }

    @GetMapping
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        Map<Long, PostDTO> idToPostDTOMap = new HashMap<>();
        for (Post post: posts) {
            idToPostDTOMap.put(post.getId(), toPostDTO(post));
        }

        List<Comment> comments = commentRepository.findAll();
        for (Comment comment: comments) {
            if (!idToPostDTOMap.containsKey(comment.getPostId())) {
                continue;
            }

            idToPostDTOMap.get(comment.getPostId()).getComments().add(toCommentDto(comment));
        }

        return idToPostDTOMap.values().stream().toList();
    }

    private PostDTO toPostDTO(Post post) {
        var postDto = new PostDTO();
        postDto.setTitle(post.getTitle());
        postDto.setBody(post.getBody());
        postDto.setId(post.getId());
        postDto.setComments(new ArrayList<>());
        return postDto;
    }

    private CommentDTO toCommentDto(Comment comment) {
        var commentDto = new CommentDTO();
        commentDto.setId(comment.getId());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }
}
// END
