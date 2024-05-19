package com.mobilefastcharger.batterycharginganimationscreen.chargingshow.flashing.light.interfaces;

import android.content.Context;
import android.view.View;

public class DoubleTapListener implements View.OnClickListener {

    private final DoubleTapCallback listener;
    public int counter = 0;
    public boolean isRunning = false;
    public int resetInTime = 500;

    public DoubleTapListener(Context context) {
        this.listener = (DoubleTapCallback) context;
    }

    public void onClick(View view) {
        this.listener.onSingleClick(view);
        if (this.isRunning && this.counter == 1) {
            this.listener.onDoubleClick(view);
        }
        this.counter++;
        if (!this.isRunning) {
            this.isRunning = true;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(DoubleTapListener.this.resetInTime);
                        boolean unused = DoubleTapListener.this.isRunning = false;
                        int unused2 = DoubleTapListener.this.counter = 0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
