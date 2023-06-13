package com.joseronaldo.catalogo.services;

import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.entities.Category;
import com.joseronaldo.catalogo.repositories.CategoryRepository;
import com.joseronaldo.catalogo.services.exceptions.DatabaseException;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long categoryId, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getOne(categoryId);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + categoryId + " not found");
        }

    }

    public void delete(Long categoryId) {
//        try {
        categoryRepository.deleteById(categoryId);
//        }
//        catch (EmptyResultDataAccessException e) {
//            throw new ResourceNotFoundException("Id  + categoryId +  not found");
//        }
//        catch (DataIntegrityViolationException e) {
//            throw new DatabaseException("Integrity violation");
//        }
    }
}
