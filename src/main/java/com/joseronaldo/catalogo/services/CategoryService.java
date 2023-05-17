package com.joseronaldo.catalogo.services;

import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.entities.Category;
import com.joseronaldo.catalogo.repositories.CategoryRepository;
import com.joseronaldo.catalogo.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long categoryId){
        Optional<Category> obj = categoryRepository.findById(categoryId);
        Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return new CategoryDTO(entity);
    }
}
