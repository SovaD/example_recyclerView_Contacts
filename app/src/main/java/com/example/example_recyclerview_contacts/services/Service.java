package com.example.example_recyclerview_contacts.services;

public class Service {
    public String capitalize(String text) {
        String[] words = text.split(" ");
        String result="";
        for (String word: words){
            result+= word.substring(0,1).toUpperCase()+word.substring(1).toLowerCase()+" ";
        }
        return result.trim();
    }
}
