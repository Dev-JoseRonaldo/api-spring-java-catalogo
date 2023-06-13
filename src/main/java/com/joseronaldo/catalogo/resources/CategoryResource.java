package com.joseronaldo.catalogo.resources;

import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.repositories.CategoryRepository;
import com.joseronaldo.catalogo.services.CategoryService;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> list = categoryService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{categoryId}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long categoryId) {
        CategoryDTO result = categoryService.findById(categoryId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        dto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long categoryId, @RequestBody CategoryDTO dto){
        dto = categoryService.update(categoryId, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long categoryId){
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Id " + categoryId + " not found");
        }

        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }
}
