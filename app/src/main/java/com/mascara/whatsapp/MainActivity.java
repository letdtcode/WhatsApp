package com.mascara.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.mascara.whatsapp.Adapters.FragmentsAdapter;
import com.mascara.whatsapp.Fragments.CallsFragment;
import com.mascara.whatsapp.Fragments.ChatsFragment;
import com.mascara.whatsapp.Fragments.StatusFragment;
import com.mascara.whatsapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private FragmentsAdapter fragmentsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        intial();
    }

    private void intial() {
        fragmentsAdapter = new FragmentsAdapter(MainActivity.this);
        fragmentsAdapter.addFragment(new ChatsFragment(),"Chats");
        fragmentsAdapter.addFragment(new StatusFragment(),"Status");
        fragmentsAdapter.addFragment(new CallsFragment(),"Calls");

        binding.viewPager.setAdapter(fragmentsAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Chat");
                break;
                case 1: tab.setText("Status");
                break;
                case 2: tab.setText("Calls");
                break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}