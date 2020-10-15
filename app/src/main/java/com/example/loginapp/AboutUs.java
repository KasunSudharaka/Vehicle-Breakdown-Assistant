package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {


    private Animation animfadein;
    private Animation animfadeout;
    private Animation animslideup;
    private Animation animslidedown;
   private Animation FabOpenNew;

    private LinearLayout aboutPanel,teamLayout;
    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        animfadein= AnimationUtils.loadAnimation(this,R.anim.fade);
        animfadeout= AnimationUtils.loadAnimation(this,R.anim.fadeout);
        animslideup= AnimationUtils.loadAnimation(this,R.anim.slideup);
        animslidedown= AnimationUtils.loadAnimation(this,R.anim.slidedown);
        FabOpenNew= AnimationUtils.loadAnimation(this,R.anim.fab_open_new);
        aboutPanel=(LinearLayout)findViewById(R.id.aboutpanel);
        teamLayout=(LinearLayout)findViewById(R.id.teamlayout);
        aboutText=(TextView)findViewById(R.id.aboutText);




        fadeinAnim();
        slideUp();
fabopenNew();

    }


    public void fadeinAnim(){



            aboutPanel.startAnimation(animfadein);
        }

    public void fadeoutAnim(){


            aboutPanel.startAnimation(animfadeout);
        }


    public void slideUp(){


            teamLayout.startAnimation(animslideup);
        }


    public void slideDown(){



            aboutText.startAnimation(animslidedown);
        }


    public void fabopenNew(){



            aboutText.startAnimation(FabOpenNew);
        }
}
