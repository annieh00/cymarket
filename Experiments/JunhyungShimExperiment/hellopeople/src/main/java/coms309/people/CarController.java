package coms309.people;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
    Car cc;
    @PostMapping("car/create")
    public String createCar(Car c){
        cc = c;
        return c.getBrandName() + ": " + c.getModelName() + " HP: " + c.getHorsePower();
    }

    @GetMapping("car/view")
    public Car getCar(){
        return cc;
    }

}
