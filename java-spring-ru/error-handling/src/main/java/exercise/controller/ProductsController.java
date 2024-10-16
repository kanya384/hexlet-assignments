package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BEGIN
    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(String.format("Product with id %d not found", id)));
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product data) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %d not found", id)));
        product.setPrice(data.getPrice());
        product.setTitle(data.getTitle());
        productRepository.save(product);
        return product;
    }
    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
