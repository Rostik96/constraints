package dev.rost.constraints.persist.repository;

import dev.rost.constraints.persist.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityRepository extends JpaRepository<Person, UUID> {
}
