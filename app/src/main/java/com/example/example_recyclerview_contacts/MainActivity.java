package com.example.example_recyclerview_contacts;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_recyclerview_contacts.adapters.MyAdapter;
import com.example.example_recyclerview_contacts.entities.Person;
import com.example.example_recyclerview_contacts.services.PeopleService;
import com.example.example_recyclerview_contacts.services.PeopleServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton btnAdd;
    PeopleService service = new PeopleServiceImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        fillData();
        linkAdapterToRV(service.getAll());
    }
    void linkAdapterToRV(List<Person> data){
        MyAdapter adapter = new MyAdapter(getApplicationContext(),data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    void print(){
        for(Person p: service.getAll()){
            Log.i("keyyy",service.capitalize(p.getName())+"\t"+p.getEmail()+"\t"+service.capitalize(p.getRelate()));
        }
    } void print( List<Person> people){
        for(Person p: people){
            Log.i("keyyy",service.capitalize(p.getName())+"\t"+p.getEmail()+"\t"+service.capitalize(p.getRelate()));
        }
    }
    void fillData()
    {
        List<Person> people = new ArrayList<>(Arrays.asList(
                new Person("tom","tom@gmail.com","unknown","friend"),
                new Person("sam","sam@gmail.com","unknown","friend"),
                new Person("alice","alice@gmail.com","unknown","family"),
                new Person("kate","kate@gmail.com","unknown","work"),
                new Person("peter","peter@gmail.com","unknown","work"),
                new Person("oliver","oliver@gmail.com","unknown","other")
        ));
        service.setAll(people);
    }
}