package ap.test.com.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            splashTread = new Thread(){
                @Override
                public void run(){
                    try{
                        synchronized(this){
                            wait(5000);

                        }
                    }catch(InterruptedException e){
                        Log.v("메인","에러났다 : " + e.toString());
                    }
                    finish();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, StartActivity.class);
                    startActivity(intent);
            }
        };
        splashTread.start();
    }
}
