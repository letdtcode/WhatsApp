package com.mascara.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mascara.whatsapp.ChatDetailActivity;
import com.mascara.whatsapp.Models.Users;
import com.mascara.whatsapp.R;
import com.mascara.whatsapp.databinding.UserItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private ArrayList<Users> usersList;
    private Context context;

    public UserAdapter(ArrayList<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = usersList.get(position);
        holder.setDataForUserItem(user);

        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid() + user.getUserId())
                                .orderByChild("timestamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (snapshot.hasChildren()) {
                                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                holder.binding.txtLastMessage.setText(snapshot1.child("message").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", user.getUserId());
                intent.putExtra("profilePic", user.getProfilePic());
                intent.putExtra("userName", user.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
