package com.joseronaldo.catalogo.services;

import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.entities.Category;
import com.joseronaldo.catalogo.repositories.CategoryRepository;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = categoryRepository.findAll(pageRequest);
        return list.map(x -> new CategoryDTO(x));
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
