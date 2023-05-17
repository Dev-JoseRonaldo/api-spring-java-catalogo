package com.joseronaldo.catalogo.resources;

import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

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
}
