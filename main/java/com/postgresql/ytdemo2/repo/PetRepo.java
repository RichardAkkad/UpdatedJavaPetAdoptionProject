package com.postgresql.ytdemo2.repo;

import com.postgresql.ytdemo2.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PetRepo extends JpaRepository<Dog,Long> {


    Optional<Dog> findById(long id);//need becos want a object sent via postman


     List<Dog> findByBreedContainingIgnoreCase(String breed);


    //i think because of the "@Query" we have to have the code below in this repository that extends jpa repository
    @Query("SELECT d FROM Dog d WHERE d.age BETWEEN :minAge AND :maxAge AND d.breed = :breed AND d.colour = :colour AND d.sex = :sex")
    //@Query(value = "SELECT * FROM dogs WHERE age BETWEEN :minAge AND :maxAge AND breed = :breed AND colour = :colour AND sex = :sex",nativeQuery = true)
    List<Dog> filterPostman(@Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("breed") String breed, @Param("colour") String colour, @Param("sex") String sex);

    @Query("SELECT d FROM Dog d WHERE d.age BETWEEN :minAge AND :maxAge AND d.breed = :breed AND d.colour = :colour AND d.sex = :sex")
    List<Dog> filterResults(@Param("minAge") int minAge, @Param("maxAge") int maxAge, @Param("breed") String breed, @Param("colour") String colour, @Param("sex") String sex);





}
