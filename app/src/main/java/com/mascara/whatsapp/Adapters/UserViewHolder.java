package com.mascara.whatsapp.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.mascara.whatsapp.Models.Users;
import com.mascara.whatsapp.R;
import com.mascara.whatsapp.databinding.UserItemBinding;
import com.squareup.picasso.Picasso;

public class UserViewHolder extends RecyclerView.ViewHolder {
    UserItemBinding binding;
    public UserViewHolder(UserItemBinding userItemBinding) {
        super(userItemBinding.getRoot());
        binding = userItemBinding;
    }

    public void setDataForUserItem(Users user){
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.man_avatar).into(binding.profileImage);
        binding.txtUsername.setText(user.getUserName());
    }
}
