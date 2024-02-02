package coms309.animal;

import coms309.animal.Animal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

public class AnimalController {

    // Note that there is only ONE instance of AnimalController in
    // Springboot system.
    HashMap<String, Animal> animalList = new HashMap<>();

    // CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the Animal in the list and returns it in JSON format
    // This controller takes no input.
    // Springboot automatically converts the list to JSON format
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/Animal")
    public HashMap<String, Animal> getAllAnimals() {
        return animalList;
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/Animal")
    public String createAnimal(@RequestBody Animal animal) {
        System.out.println(animal);
        animalList.put(animal.getName(), animal);
        return "New animal " + animal.getName() + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/Animal/{name}")
    public Animal getAnimal(@PathVariable String name) {
        Animal a = animalList.get(name);
        return a;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/Animal/{name}")
    public Animal updateAnimal(@PathVariable String name, @RequestBody Animal a) {
        animalList.replace(name, a);
        return animalList.get(name);
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method

    @DeleteMapping("/Animal/{name}")
    public HashMap<String, Animal> deleteAnimal(@PathVariable String name) {
        animalList.remove(name);
        return animalList;
    }

}
