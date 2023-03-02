package com.mascara.whatsapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mascara.whatsapp.Adapters.UserAdapter;
import com.mascara.whatsapp.Models.Users;
import com.mascara.whatsapp.R;
import com.mascara.whatsapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Users> userList;
    private UserAdapter adapter;
    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(getLayoutInflater(), container, false);
        userList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getUsers();
        return binding.getRoot();
    }

    private void getUsers() {
        binding.chatRecycleView.setHasFixedSize(true);
        binding.chatRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter(userList, getContext());
        binding.chatRecycleView.setAdapter(adapter);


        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    user.setUserId(dataSnapshot.getKey());
                    if (!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}