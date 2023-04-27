package com.example.controleestoquees;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.Timer;
import java.util.TimerTask;

public class SearchWatcher implements TextWatcher {
    private Timer typingTimer = new Timer();
    private Long timerDelay = 750L;
    private Runnable hook;

    public SearchWatcher(Runnable hook) {
        this.hook = hook;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            typingTimer.cancel();
            typingTimer.purge();
        } catch (IllegalStateException ex) {
            // Not an error
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        typingTimer = new Timer();
        typingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                hook.run();
            }
        }, timerDelay);
    }
}
