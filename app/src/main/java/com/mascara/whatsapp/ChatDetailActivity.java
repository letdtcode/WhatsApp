package com.mascara.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mascara.whatsapp.Adapters.ChatAdapter;
import com.mascara.whatsapp.Models.Messages;
import com.mascara.whatsapp.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    private ActivityChatDetailBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    //private ArrayList<String> messageReceiveIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recievedId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.txtUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.man_avatar).into(binding.profileImage);

        binding.imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final String senderRoom = senderId + recievedId;
        final String receiverRoom = recievedId + senderId;

//        messageReceiveIdList = new ArrayList<>();
////        firebaseDatabase.getReference().child("chats").child(receiverRoom).add(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                String key = snapshot.getKey();
////                messageReceiveIdList.add(key);
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
//        firebaseDatabase.getReference().child("chats")
//                .child(receiverRoom)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        messageReceiveIdList.clear();
//                        for (DataSnapshot keyReceiveSnapshot : snapshot.getChildren()) {
//                            String key = keyReceiveSnapshot.getKey();
//                            messageReceiveIdList.add(key);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        final ArrayList<Messages> messageList = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageList, this, recievedId);
        binding.chatRecycleView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycleView.setLayoutManager(layoutManager);

        firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageList.clear();
                                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                                    Messages messageModel = messageSnapshot.getValue(Messages.class);
                                    if (messageModel.getSttMessage() == 0) {
                                        messageModel.setMessage("Tin nhắn đã được thu hồi");
                                    }
                                    messageModel.setMessageId(messageSnapshot.getKey());
                                    messageList.add(messageModel);
                                }
                                chatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.txtMessage.getText().toString();
                final Messages messageModel = new Messages(senderId, message);
                messageModel.setTimestamp(new Date().getTime());
                messageModel.setSttMessage(1);
                binding.txtMessage.setText("");

                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseDatabase.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
//                                                messageReceiveIdList.clear();
//                                                firebaseDatabase.getReference().child("chats").child(receiverRoom).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        String key = snapshot.getKey();
//                                                        messageReceiveIdList.add(key);
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });
                                            }
                                        });
                            }
                        });
            }
        });
    }
}