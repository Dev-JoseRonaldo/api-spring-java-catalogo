package com.joseronaldo.catalogo.repositories;

import com.joseronaldo.catalogo.entities.Product;
import com.joseronaldo.catalogo.services.exceptions.ResourceNotFoundException;
import com.joseronaldo.catalogo.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalProduct;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProduct = 25;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct + 1, product.getId());
    }

    @Test
    public void returnOptionalProductNonEmptyWhenExistingId() {

        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void returnOptionalProductEmptyWhenNonExistingId() {

        Optional<Product> result = repository.findById(nonExistingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Disabled("Test disabled because delete by id no longer throws any exceptions")
    @Test
    public void deleteShouldThrowEmptyResultResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }
}