package com.joseronaldo.catalogo.resources;

import com.joseronaldo.catalogo.dto.ProductDTO;
import com.joseronaldo.catalogo.repositories.ProductRepository;
import com.joseronaldo.catalogo.services.ProductService;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
        @RequestParam(value = "direction", defaultValue = "ASC") String direction,
        @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
    ) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);


        Page<ProductDTO> list = productService.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long productId) {
        ProductDTO result = productService.findById(productId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
        dto = productService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{productId}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long productId, @RequestBody ProductDTO dto){
        dto = productService.update(productId, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long productId){
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Id " + productId + " not found");
        }

        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
