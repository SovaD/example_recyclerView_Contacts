package com.example.example_recyclerview_contacts.entities;public class Person {
    String name;
    String email;
    String image;
    String relate;

    public Person() {
    }

    public Person(String name, String email, String image, String relate) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.relate = relate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRelate(String relate) {
        this.relate = relate;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getRelate() {
        return relate;
    }
}
