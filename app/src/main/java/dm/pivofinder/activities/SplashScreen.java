package dm.pivofinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import dm.pivofinder.R;



public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread sleepThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this, Home.class);
                    startActivity(intent);
                }
            }
        };
        sleepThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}