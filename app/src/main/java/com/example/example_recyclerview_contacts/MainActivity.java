package com.example.example_recyclerview_contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.example_recyclerview_contacts.activities.AddActivity;
import com.example.example_recyclerview_contacts.adapters.MyAdapter;
import com.example.example_recyclerview_contacts.adapters.OnClickListener;
import com.example.example_recyclerview_contacts.entities.Person;
import com.example.example_recyclerview_contacts.services.PeopleService;
import com.example.example_recyclerview_contacts.services.PeopleServiceImpl;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_PEOPLE_REQ = 1;
    SearchView searchView;
    RecyclerView recyclerView;
    ImageButton btnAdd;
    PeopleService service = new PeopleServiceImpl();
    MyAdapter adapter;
    List<Person> people = new ArrayList<>();

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
        people = service.getAll();
        linkAdapterToRV(people);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("key", (Serializable) people);
                startActivityForResult(intent, UPDATE_PEOPLE_REQ);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Person> data = service.getAll(newText.toLowerCase());
                linkAdapterToRV(data);
                return false;
            }
        });
        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                Person deletedCourse = people.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                people.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, deletedCourse.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        people.add(position, deletedCourse);
                        // below line is to notify item is
                        // added to our adapter class.
                        adapter.notifyItemInserted(position);

                    }
                }).show();
            }

            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_PEOPLE_REQ)
            if (resultCode == RESULT_OK) {
                people = (List<Person>) data.getSerializableExtra("key");
                service.clearAll();
                service.setAll(people);
                linkAdapterToRV(people);
            }
    }

    void linkAdapterToRV(List<Person> data) {
        adapter = new MyAdapter(getApplicationContext(), data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("key", (Serializable) people);
                intent.putExtra("person", people.get(position));
                startActivityForResult(intent, UPDATE_PEOPLE_REQ);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    void print() {
        for (Person p : service.getAll()) {
            Log.i("keyyy", service.capitalize(p.getName()) + "\t" + p.getEmail() + "\t" + service.capitalize(p.getRelate()));
        }
    }

    void print(List<Person> people) {
        for (Person p : people) {
            Log.i("keyyy", service.capitalize(p.getName()) + "\t" + p.getEmail() + "\t" + service.capitalize(p.getRelate()));
        }
    }

    void fillData() {
        List<Person> people = new ArrayList<>(Arrays.asList(
                new Person("tom", "tom@gmail.com", "unknown", "friend"),
                new Person("sam", "sam@gmail.com", "unknown", "friend"),
                new Person("alice", "alice@gmail.com", "unknown", "family"),
                new Person("kate", "kate@gmail.com", "unknown", "work"),
                new Person("peter", "peter@gmail.com", "unknown", "work"),
                new Person("oliver", "oliver@gmail.com", "unknown", "other")
        ));
        service.setAll(people);
    }
}