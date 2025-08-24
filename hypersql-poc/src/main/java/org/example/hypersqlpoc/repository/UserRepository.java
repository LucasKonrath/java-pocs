package org.example.hypersqlpoc.repository;

import org.example.hypersqlpoc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    @Query("SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    List<User> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    long countAllUsers();
}
