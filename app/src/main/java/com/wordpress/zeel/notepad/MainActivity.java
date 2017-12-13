package com.wordpress.zeel.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT= 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // this.setTitle("Notepad");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        Thread background= new Thread(){

            public void run(){
                try{
                    sleep(SPLASH_TIME_OUT);

                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                    finish();
                }
                catch (Exception e){

                }
            }
        };
        background.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
