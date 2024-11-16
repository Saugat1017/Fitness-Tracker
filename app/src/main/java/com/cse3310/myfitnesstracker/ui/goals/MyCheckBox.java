package com.cse3310.myfitnesstracker.ui.goals;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.cse3310.myfitnesstracker.R;

public class MyCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

    public MyCheckBox(Context context, GoalsFragment parent, LinearLayout layout) {
        super(context);


        super.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Apply fade-out animation
                Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fadeout);

                // Set an animation listener
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Optional: Do something when the animation starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Remove the view after the animation ends
                        layout.removeView(MyCheckBox.this);
                        parent.finishGoals(); // Update goals count after removing the view
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // No action needed
                    }
                });

                // Start the animation
                this.startAnimation(fadeOut);
            }
        });
    }




}
