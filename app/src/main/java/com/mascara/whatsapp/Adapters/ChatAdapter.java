package com.mascara.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mascara.whatsapp.Models.Messages;
import com.mascara.whatsapp.databinding.ChatRecieverItemBinding;
import com.mascara.whatsapp.databinding.ChatSenderItemBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<Messages> messagesList;
    private Context context;
    private String recId;
    //private ArrayList<String> messReceiveIdList;

    private final int SENDER_VIEW_TYPE = 1;
    private final int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<Messages> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    public ChatAdapter(ArrayList<Messages> messagesList, Context context, String recId) {
        this.messagesList = messagesList;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            ChatSenderItemBinding binding = ChatSenderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SenderViewHolder(binding);
        } else {
            ChatRecieverItemBinding binding = ChatRecieverItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecieverViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        }
        return RECEIVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messagesList.get(position);
        //String messReceiverId = messReceiveIdList.get(position);

        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder)holder).binding.layoutClick.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete this message")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                    String receiverRoom = recId + FirebaseAuth.getInstance().getUid();
                                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats");
                                    chatRef.child(senderRoom).child(message.getMessageId())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    HashMap<String, Object> obj = new HashMap<>();
                                                    obj.put("sttMessage",0);
                                                    chatRef.child(senderRoom).child(message.getMessageId()).updateChildren(obj);

                                                    int timeStampOfMessDeleted = message.getSttMessage();
                                                    //chatRef.child(receiverRoom). -- dang so sanh thoi gian de xac dinh ID mesage
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
//                                    chatRef.child(receiverRoom).child(messReceiverId)
//                                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                    HashMap<String, Object> obj = new HashMap<>();
//                                                    obj.put("sttMessage",0);
//                                                    chatRef.child(receiverRoom).child(messReceiverId).updateChildren(obj);
//                                                }
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                                }
//                                            });
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return false;
                }
            });
        }
        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder)holder).binding.txtSenderText.setText(message.getMessage());
        } else {

            ((RecieverViewHolder)holder).binding.txtRecieverText.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {

        private ChatRecieverItemBinding binding;

        public RecieverViewHolder(@NonNull ChatRecieverItemBinding chatRecieverItemBinding) {
            super(chatRecieverItemBinding.getRoot());
            binding = chatRecieverItemBinding;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        private ChatSenderItemBinding binding;

        public SenderViewHolder(@NonNull ChatSenderItemBinding chatSenderItemBinding) {
            super(chatSenderItemBinding.getRoot());
            binding = chatSenderItemBinding;

        }
    }
}
