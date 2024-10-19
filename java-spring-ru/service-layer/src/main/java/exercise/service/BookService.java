package exercise.service;

import exercise.dto.*;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.model.Author;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorService;

    @Autowired
    private BookMapper bookMapper;

    public List<BookDTO> getAll() {
        var Books = bookRepository.findAll();
        return Books.stream().map(bookMapper::map).toList();
    }

    public BookDTO create(BookCreateDTO data) {
        authorService.findById(data.getAuthorId());
        var Book = bookMapper.map(data);
        bookRepository.save(Book);
        return bookMapper.map(Book);
    }

    public BookDTO findById(Long id) {
        var Book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        return bookMapper.map(Book);
    }

    public BookDTO update(Long id, BookUpdateDTO data) {
        var Book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        bookMapper.update(data, Book);

        bookRepository.save(Book);

        return bookMapper.map(Book);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
    // END
}
