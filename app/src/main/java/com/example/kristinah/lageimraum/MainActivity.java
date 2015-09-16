package com.example.kristinah.lageimraum;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Timestamp;

import static android.hardware.Sensor.TYPE_LINEAR_ACCELERATION;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_ORIENTATION;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    Sensor orientation;
    Sensor acceleration;
    Sensor compass;
    SensorManager am;
    SensorManager om;
    SensorManager cm;
    TextView text;
    TextView textAcc;
    TextView machText;
    String Text = "";
    String Text2 = "";
    String Text3 = "";
    Button button;
    Button mach;
    int lymax1;
    int lymax2;
    int lymin;
    int pos;
    int pos2;
    int counter = 0;
    int c = 0;
    MediaPlayer player;
    int time;
    int time2;
    int time3;

    int list[] = new int[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        om=(SensorManager)getSystemService(SENSOR_SERVICE);
        orientation=om.getDefaultSensor(TYPE_ORIENTATION);
        om.registerListener(this, orientation, SensorManager.SENSOR_ORIENTATION);

        am = (SensorManager)getSystemService(SENSOR_SERVICE);
        acceleration = am.getDefaultSensor(TYPE_LINEAR_ACCELERATION);
        am.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_NORMAL);

        cm = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass = cm.getDefaultSensor(TYPE_MAGNETIC_FIELD);
        cm.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME);

        text=(TextView)findViewById(R.id.text);
        textAcc = (TextView) findViewById(R.id.textAcc);
        button = (Button) findViewById(R.id.button);
        mach = (Button) findViewById(R.id.mach);
        machText=(TextView)findViewById(R.id.machText);

    }


    public void setTextToZero(View view){
        Text = "";
        Text2= "";
        Text3= "";

        lymax1 = 0;
        lymax2 = 0;
        lymin = 500;
        pos = 5000;
        pos2 = 5000;
        counter = 0;

        machText.setText("Bitte Daten auswerten, um Auswertung zu sehen");
    }

    public void writeExternalStorage(View view){

        String state;
        state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/MyBurpeeApp");
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            File file1 = new File(Dir, "Daten_Lage_X_Achse.txt");
            File file2 = new File(Dir, "Daten_Lage_Y_Achse.txt");
            File file3 = new File(Dir, "Daten_Lage_Z_Achse.txt");


            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                fileOutputStream.write(Text.getBytes());
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(Text2.getBytes());
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                fileOutputStream.write(Text3.getBytes());
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
            else{
            Toast.makeText(getApplicationContext(), "SD-Card not found", Toast.LENGTH_LONG).show();
        }


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {


       if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){

           text.setText("Drehung auf Y-Achse: " + (int) event.values[1]*(-1) );

           int wert = (int) (event.values[1]*(-1));

           Text += (int)event.values[1]*(-1);
           Text += ", ";


           if ((wert >= 120) && (c == 0)) {
               c = 1;}

           if ((wert <= 40) && (c ==1)){
               c = 2;}

           if((wert >= 120) && (c == 2)) {
               c =3;}

           if ((wert>90) && (wert<110) && (c ==3)){
               counter += 1;
               machText.setText("Du hast " + counter + " Burpees gemacht! :-) ");
               c =0;
               player=MediaPlayer.create(MainActivity.this,R.raw.iphone);
               player.start();

           }



       }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            textAcc.setText("Beschleunigung auf Z-Achse:" + (int)event.values[2] + "\nBeschleunigung auf Y-Achse: " + (int)event.values[1]);

            Text2 += (int)event.values[2]*5+100;
            Text2 += ",";

            Text3 += (int)event.values[1]*5+100;
            Text3 += ",";



        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
