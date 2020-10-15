package com.example.loginapp;

import android.content.Intent;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class UserInfo extends AppCompatActivity {

private static final String TAG="UserInfo";

private SectionPageAdapter mSectionPageAdapter;

private ViewPager mViewPager;

    private FirebaseAuth firebaseAuth1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Log.d(TAG,"onCreate Starting");

        mSectionPageAdapter=new SectionPageAdapter(getSupportFragmentManager());

        mViewPager=(ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        firebaseAuth1=FirebaseAuth.getInstance();

        if(firebaseAuth1.getCurrentUser()==null){
            finish();

            Intent gotologinactivity=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(gotologinactivity);
        }
    }

private void setupViewPager(ViewPager viewPager){

        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(),"Signup as Service Station");
    adapter.addFragment(new Tab2Fragment(),"Signup as Vehicle Owner");

    viewPager.setAdapter(adapter);


}


}
