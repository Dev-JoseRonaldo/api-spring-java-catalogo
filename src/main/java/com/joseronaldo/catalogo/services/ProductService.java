package com.joseronaldo.catalogo.services;


import com.joseronaldo.catalogo.dto.CategoryDTO;
import com.joseronaldo.catalogo.dto.ProductDTO;
import com.joseronaldo.catalogo.entities.Category;
import com.joseronaldo.catalogo.entities.Product;
import com.joseronaldo.catalogo.repositories.CategoryRepository;
import com.joseronaldo.catalogo.repositories.ProductRepository;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long categoryId){
        Optional<Product> obj = productRepository.findById(categoryId);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoEntity(dto, entity);
        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long productId, ProductDTO dto) {
        try {
            Product entity = productRepository.getOne(productId);
            copyDtoEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + productId + " not found");
        }

    }

    public void delete(Long categoryId) {
//        try {
        productRepository.deleteById(categoryId);
//        }
//        catch (EmptyResultDataAccessException e) {
//            throw new ResourceNotFoundException("Id  + categoryId +  not found");
//        }
//        catch (DataIntegrityViolationException e) {
//            throw new DatabaseException("Integrity violation");
//        }
    }

    private void copyDtoEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
