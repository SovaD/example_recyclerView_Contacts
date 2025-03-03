package com.example.example_recyclerview_contacts.services;

import android.os.Parcelable;

import com.example.example_recyclerview_contacts.entities.Person;

import java.io.Serializable;
import java.util.List;

public interface PeopleService {
    boolean add(Person person);
    boolean edit(Person person, Person editedPerson);
    boolean delete(String email);
    Person get(String email);
    List<Person> getAll();
    List<Person> getAll(String par);
    void setAll(List<Person> people);
    void clearAll();
    int size();

    String capitalize(String text);
}
