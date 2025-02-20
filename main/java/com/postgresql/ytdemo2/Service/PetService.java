package com.postgresql.ytdemo2.Service;

import com.postgresql.ytdemo2.model.Dog;
import com.postgresql.ytdemo2.repo.PetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepo repo;

    public String prepareAddPetForm(Model model) {
        model.addAttribute("request", new Dog());//just binds just the fields to this object

        return "saveDog";//once reach this line and the empty form appears from this line we then go to the "SavePetForm" method
    }


    public String prepareSavePetForm(Dog dog) {

        repo.save(dog);

        return "index";

    }


    public String prepareResultFromId(Long id, Model model) {
        System.out.println(id);

        Optional<Dog> dog = repo.findById(id);// doesnt mean l.... v are available unless they spef... returned to this method  which they are not here as dog is returned

        //System.out.println(id); // we can print this stuff because the database returned info to the method above but if we dont return the above method call them info wont be passed to the controller method but
        //System.out.println(dog.get().colour); //even if we did we still wouldnt see database details because it returns straight to the controller method. you would need another layer to see details again
        // but even if we did return then it wouldnt return the database details it would return a template

        if (dog.isPresent()) {
            model.addAttribute("dog", dog.get());//"dog.get()" just returns a object which is stored in the Optional container, and then we are reassigning this object to another variable called "dog" again
            return "dogIdResults"; // This page will display the dog's details
        }
        else {
            return "incorrectId"; //  error message with the option of being Redirect back to search page
        }

    }
        public String prepareFilterResults(int minAge, int maxAge, String breed, String colour, String sex, Model model) {
            List<Dog> dogs = repo.filterResults(minAge, maxAge, breed, colour, sex);
            System.out.println(dogs.size() + "-----------");
            model.addAttribute("dogs", dogs);
            return "dogFilterResults";
        }


        public String prepareDelete( long id){
            List<Dog> dogs = repo.findAll();
            for (Dog dog : dogs) {
                if (dog.getId() == id) {
                    repo.deleteById(id);
                    return "deleteSuccess";
                }
            }
            return "deleteFailed";


        }





    public String prepareUpdateByIdForm(Model model){
        model.addAttribute("request",new Dog());
        return "updateById";//basically we are returning a string which is like a reference to a form to the previous st... .....

    }


    public String prepareUpdateById(Dog dog,long id) {

        Optional<Dog> existingDog = repo.findById(id);
        if (existingDog.isPresent()){
            Dog dogToUpdate=existingDog.get();

            dogToUpdate.setBreed(dog.getBreed());
            dogToUpdate.setAge(dog.getAge());
            dogToUpdate.setColour(dog.getColour());
            dogToUpdate.setSex(dog.getSex());

            repo.save(dogToUpdate);
            return "updateSuccess";

        }
        else {
            return "updateFailed";
        }



    }






}






















