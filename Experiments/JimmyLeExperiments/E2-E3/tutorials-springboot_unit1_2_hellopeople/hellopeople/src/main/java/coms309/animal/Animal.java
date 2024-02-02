package coms309.animal;

public class Animal {
    private String name;

    private String specie;

    private String sound;

    private String color;

    public Animal(){

    }

    public Animal(String name, String specie, String sound, String color){
        this.name = name;
        this.specie = specie;
        this.sound = sound;
        this.color = color;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecie() {
        return this.specie;
    }

    public void setSpecie(String specie) { this.specie = specie;}

    public String getSound()  { return this.sound; }

    public void setSound(String sound) { this.sound = sound; }

    public String getColor() { return this.color; }

    public void setColor(String color) { this.color = color; }

    @Override
    public String toString() {
        return name + " "
                + specie + " "
                + sound + " "
                + color;
    }
}
