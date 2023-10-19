package dev.rost.constraints.persist;

import dev.rost.constraints.persist.basetest.AbstractPersistTest;
import dev.rost.constraints.persist.entity.Person;
import dev.rost.constraints.persist.repository.EntityRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;


@TestMethodOrder(OrderAnnotation.class)
class ValidationConstraintsTest extends AbstractPersistTest {

    @Autowired
    EntityRepository repository;

    @Autowired
    EntityManager entityManager;


    @Test
    @Order(1)
    void whenCreateWithNullName_thenThrows() {
        var entity = new Person(null);

        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(entity));
    }


    @Test
    @Order(2)
    void whenReadWithNullName_thenNoThrows() {
        var entity = new Person("Rasta");
        var id = repository.saveAndFlush(entity).getId();

        entityManager.createNativeQuery(
                        "update person set name = null where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        entityManager.refresh(entity);

        assertDoesNotThrow(() -> repository.findById(id));
        assertNull(repository.findById(id).orElseThrow().getName());
    }


    @Test
    @Order(3)
    void whenUpdateWithNullName_thenThrows() {
        var entity = new Person("Rasta");
        var savedId = repository.saveAndFlush(entity).getId();

        var firstUpdatable = new Person(savedId, "Thomson");
        repository.saveAndFlush(firstUpdatable);

        var secondUpdatable = new Person(savedId, null);
        assertThrows(ConstraintViolationException.class, () -> repository.saveAndFlush(secondUpdatable));
    }


    @Test
    @Order(4)
    void whenDeleteWithNullName_thenNoThrows() {
        var entity = new Person("Rasta");
        var savedId = repository.saveAndFlush(entity).getId();

        var deletable = new Person(savedId, null);

        assertDoesNotThrow(() -> repository.delete(deletable));
    }
}
