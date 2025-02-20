package com.postgresql.ytdemo2.Controller;

import com.postgresql.ytdemo2.model.Dog;
import com.postgresql.ytdemo2.repo.PetRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
public class restPetController {
    @Autowired
    PetRepo repo;

    @PostMapping("/insertPet")
//whether we click on this endpoint(from the home page or manually type it in this method needs to happen
    public void addPet(@RequestBody Dog dog) {

        repo.save(dog);
        //Dog myDog=repo.save(dog);
        //return myDog;

    }


    //THIS NEEDS RETURN TYPE "FINDBYID" BUT THE UPDATE METHOD NEEDS RETURN TYPE "OPTIONAL<DOG>" AS THE RETURN TYPE, IF WANT BOTH TO WORK THEN CHANGE BOTH TO "DOG" AS THE RETURN TYPE
    //@GetMapping("/getPetById")
    //public Dog GetPetId(@RequestParam long id) {

    //    return repo.findById(id);// looks like "Dog findById(long id);" in PetRepo interface allows this method to return a object and not a optional if the type is set to "dog" and not the default "Optional<dog>"
        // while in the controller example we return a optional using "Optional<Dog>" for the "IdResults" method as the type and "Dog findById(long id);"
    //}       // in the PetRepo interface doesnt overide this


    @GetMapping("/getAll")
    public List<Dog> getAll() {

        return repo.findAll();

    }


    @GetMapping("/getSpecific")
    public List<Dog> filter() {

        return repo.filterPostman(0, 5, "terrier", "grey", "male");//manual input of values because not inputting the values into the form

    }


    @GetMapping("/deletePetById")
    public String deletePet(@RequestParam long id) {
        List<Dog> dogs = repo.findAll();
        for (Dog dog : dogs) {
            if (dog.getId() == id) {
                repo.deleteById(id);
                return "deleteSuccess";
            }
        }

        return "deleteFailed";

    }


    //if (repo.existsById(id)) {
    //    repo.deleteById(id);
    //    return "deleteSuccess";
    //}

    //return "deleteFailed";

    //}



    //COME BACK TO APPARENTLY,HAVE CREATED ANOTHER REPOSITORY BUT STILL NOT WORKING
    //@PostMapping("/CreateUser")
    //public String addUser(@RequestBody User user) {

    //    repo.save(user);
    //    return "saved User";

    //}



    @PutMapping("/updatePetById")
    public Dog updatePetById(@RequestBody Dog dog, @RequestParam Long id ){


        Optional<Dog> existingDog=repo.findById(id);
        //r........ that is a Optional so its a r........ that contains another .........
        if(!existingDog.isEmpty()) {
            existingDog.get().setBreed(dog.getBreed());
            existingDog.get().setAge(dog.getAge());
            existingDog.get().setColour(dog.getColour());
            existingDog.get().setSex(dog.getSex());

            return repo.save(existingDog.get());

        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dog not found with id: " + id);
        }


    }










}
