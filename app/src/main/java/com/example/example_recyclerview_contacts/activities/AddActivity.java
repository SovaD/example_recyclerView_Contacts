package com.example.example_recyclerview_contacts.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.example_recyclerview_contacts.R;
import com.example.example_recyclerview_contacts.entities.Person;
import com.example.example_recyclerview_contacts.services.PeopleService;
import com.example.example_recyclerview_contacts.services.PeopleServiceImpl;
import com.example.example_recyclerview_contacts.services.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private static final int LOAD_IMAGE_REQ = 2;
    EditText tName, tEmail;
    Spinner spinner;
    ImageButton btnImage, btnAdd;

    List<String> relates = new ArrayList<>(Arrays.asList("Family", "Friend", "Work", "Other"));
    ArrayAdapter<String> spinnerAdapter;

    List<Person> people;
    String imagePath = "unknown";
    PeopleService peopleService = new PeopleServiceImpl();
    Person person;
    Service service = new Service();
    boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tName = findViewById(R.id.tName);
        tEmail = findViewById(R.id.tEmail);
        btnAdd = findViewById(R.id.btnAdd);
        btnImage = findViewById(R.id.btnImage);
        spinner = findViewById(R.id.spRelate);

        spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, relates);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        people = (List<Person>) getIntent().getSerializableExtra("key");

        peopleService.clearAll();
        peopleService.setAll(people);
        if (getIntent().hasExtra("person")) {
            person = (Person) getIntent().getSerializableExtra("person");
            fillContent();
            isNew = false;
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNew)
                    createPerson();
                else editPerson();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load image from gallery
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,LOAD_IMAGE_REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_REQ)
            if (resultCode==RESULT_OK){
                imagePath = data.getData().toString();
                btnImage.setImageURI(Uri.parse(imagePath));
            }
    }

    private void editPerson() {

        String name = tName.getText().toString().toLowerCase();
        String email = tEmail.getText().toString().toLowerCase();
        String image = imagePath;
        String relate = spinner.getSelectedItem().toString().toLowerCase();
        Person newPerson = new Person(name, email, image, relate);
        boolean result = peopleService.edit(person, newPerson);
        if (result) {
            Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("key", (Serializable) peopleService.getAll());
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, "Data not updated", Toast.LENGTH_SHORT).show();
    }

    private void fillContent() {
        tName.setText(service.capitalize(person.getName()));
        tEmail.setText(person.getEmail());
        spinner.setSelection(relates.indexOf(service.capitalize(person.getRelate())));
        btnImage.setImageURI(Uri.parse(person.getImage()));
    }

    private void createPerson() {

        if (checkData()) {
            String name = tName.getText().toString().toLowerCase();
            String email = tEmail.getText().toString().toLowerCase();
            String image = imagePath;
            String relate = spinner.getSelectedItem().toString().toLowerCase();
            Person newPerson = new Person(name, email, image, relate);
            people.add(newPerson);

            Intent intent = new Intent();
            intent.putExtra("key", (Serializable) people);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean checkData() {
        String name = tName.getText().toString();
        String email = tEmail.getText().toString();

        if (name.isEmpty() || email.isEmpty()) {
            return false;
        }
        if (peopleService.get(email) == null)
            return true;
        Toast.makeText(getApplicationContext(),
                "The email already exists",
                Toast.LENGTH_SHORT).show();
        return false;
    }
}