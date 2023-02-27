package com.mascara.whatsapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mascara.whatsapp.Models.Messages;
import com.mascara.whatsapp.databinding.ChatRecieverItemBinding;
import com.mascara.whatsapp.databinding.ChatSenderItemBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<Messages> messagesList;
    private Context context;

    private final int SENDER_VIEW_TYPE = 1;
    private final int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<Messages> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
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
