package com.example.alphagame;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Fragment2 extends Fragment implements View.OnClickListener{

    private static final String TAG = "FragmentLogs";

    static boolean Flag = false;
    ImageView imageView1, imageView2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"Fragment2: onStart");

        imageView1 = (ImageView)getActivity().findViewById(R.id.imageViewLeft_F2);
        imageView2 = (ImageView)getActivity().findViewById(R.id.imageViewRight_F2);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        Flag = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"Fragment2: onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Fragment2: onDestroy");
        Flag = false;

    }

    @Override
    public void onClick(View view) {

        Fragment fragment = null;

        if(!Fragment1.Flag){
            switch (view.getId()) {
                case R.id.imageViewLeft_F2:
                    Animation animWin = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
                    imageView1.startAnimation(animWin);

                    MainActivity.playSoundlose(view);
                    fragment = new Fragment1();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.setCustomAnimations(R.animator.frag_from_right_to_centre, R.animator.frag_from_centre_to_left);
                    ft.replace(R.id.fragment_place, fragment);
                    ft.commit();
                    break;
                case R.id.imageViewRight_F2:
                    MainActivity.playSoundwin(view);
                    Animation animLose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                    imageView2.startAnimation(animLose);
                    //Log.d("huilo", MyService.thread.getState().toString());
//                fragment = new Fragment1();
                    break;
            }
        }
    }
}
