package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

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

    // BEGIN
    @GetMapping
    public List<Product> getProducts(@RequestParam(value = "min") Optional<Integer> minPrice,
                                     @RequestParam(value = "max") Optional<Integer> maxPrice) {
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            return productRepository.findByPriceBetweenOrderByPriceAsc(minPrice.get(), maxPrice.get());
        } else if (minPrice.isPresent()) {
            return productRepository.findByPriceGreaterThanEqualOrderByPriceAsc(minPrice.get());
        } else if (maxPrice.isPresent()) {
            return productRepository.findByPriceLessThanEqualOrderByPriceAsc(maxPrice.get());
        }
        return productRepository.findAll(Sort.by(Sort.Order.asc("price")));
    }
    
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
