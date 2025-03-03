package com.example.example_recyclerview_contacts.services;

import com.example.example_recyclerview_contacts.entities.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PeopleServiceImpl implements PeopleService {

    List<Person> people;

    public PeopleServiceImpl() {
        people = new ArrayList<>();
    }

    @Override
    public boolean add(Person person) {
        if (get(person.getEmail()) != null)
            return false;
        people.add(person);
        return true;
    }

    @Override
    public boolean edit(Person person, Person editedPerson) {
        if (!person.getEmail().equals(editedPerson.getEmail()) && get(editedPerson.getEmail()) != null)
            return false;
        for (Person p : people) {
            if (p.getEmail().equals(person.getEmail())) {
                p.setImage(editedPerson.getImage());
                p.setName(editedPerson.getName());
                p.setEmail(editedPerson.getEmail());
                p.setRelate(editedPerson.getRelate());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String email) {
        Person p = get(email);
        if (p != null) {
            people.remove(p);
            return true;
        }
        return false;
    }

    @Override
    public Person get(String email) {
        for (Person p : people) {
            if (p.getEmail().equals(email))
                return p;
        }
        return null;
    }

    @Override
    public List<Person> getAll() {
        return people;
    }

    @Override
    public List<Person> getAll(String par) {
        return people.stream().filter(p ->
                p.getEmail().contains(par)
                        || p.getName().contains(par)
                        || p.getRelate().contains(par)).collect(Collectors.toList());
    }

    @Override
    public void setAll(List<Person> people) {
        this.people.addAll(people);
    }

    @Override
    public void clearAll() {
        people.clear();
    }

    @Override
    public int size() {
        return people.size();
    }

    @Override
    public String capitalize(String text) {
        String[] words = text.split(" ");
        String result="";
        for (String word: words){
            result+= word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase()+" ";
        }
        return result.trim();
    }
}
