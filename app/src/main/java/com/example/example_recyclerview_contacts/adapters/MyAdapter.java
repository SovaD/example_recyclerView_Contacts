package com.example.example_recyclerview_contacts.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.example_recyclerview_contacts.R;
import com.example.example_recyclerview_contacts.entities.Person;
import com.example.example_recyclerview_contacts.services.PeopleService;
import com.example.example_recyclerview_contacts.services.PeopleServiceImpl;
import com.example.example_recyclerview_contacts.services.Service;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    List<Person> people;

    public MyAdapter(Context context, List<Person> people) {
        this.context = context;
        this.people = people;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.activity_item, parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Person person = people.get(position);
        Service service = new Service();
        holder.tName.setText(service.capitalize(person.getName()));
        holder.tEmail.setText(person.getEmail());
        holder.tRelate.setText(service.capitalize(person.getRelate()));

        try{
            if (person.getImage().equals("unknown"))
                holder.imageView.setImageResource(R.drawable.unknown);
            else
                Glide.with(context)
                        .load(Uri.parse(person.getImage()))
                        .placeholder(R.drawable.unknown)
                        .into(holder.imageView);
        }
        catch (Exception e){
            Toast.makeText(context,"Cannot load an image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tName, tEmail, tRelate;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            tName = itemView.findViewById(R.id.tName);
            tEmail = itemView.findViewById(R.id.tEmail);
            tRelate = itemView.findViewById(R.id.tRelate);
        }
    }
}
