package exercise.controller;

import exercise.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping
    public List<ProductDTO> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    private ProductDTO findById(@PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no product with id = " + id));
        return productMapper.map(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ProductDTO create(@RequestBody ProductCreateDTO createDTO) {
        var product = productMapper.map(createDTO);
        product = productRepository.save(product);
        return productMapper.map(product);
    }

    @PutMapping("/{id}")
    private ProductDTO update(@PathVariable Long id, @RequestBody ProductUpdateDTO data) {
        var maybeProduct = productRepository.findById(id);
        if (maybeProduct.isEmpty()) {
            throw new ResourceNotFoundException("no product with id = " + id);
        }

        var product = maybeProduct.get();

        productMapper.update(data, product);

        productRepository.save(product);

        return productMapper.map(product);
    }
    // END
}
