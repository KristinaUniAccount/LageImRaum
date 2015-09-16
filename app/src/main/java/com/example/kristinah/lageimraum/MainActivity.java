package com.example.kristinah.lageimraum;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import static android.hardware.Sensor.TYPE_LINEAR_ACCELERATION;
import static android.hardware.Sensor.TYPE_ORIENTATION;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    Sensor orientation;
    Sensor acceleration;
    SensorManager am;
    SensorManager om;
    TextView text;
    TextView textAcc;
    TextView machText;
    String Text = "";
    String Text2 = "";
    String Text3 = "";
    String Text4 = "";
    Button button;
    Button mach;
    int lymax1;
    int lymax2;
    int lymin;
    int pos;
    int pos2;
    int counter;

    int list[] = new int[2];

    int arschi;
    int b;

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
        Text4= "";
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
            File file1 = new File(Dir, "DatenBurpee_Lage.txt");
            File file2 = new File(Dir, "DatenBurpee_Acc_Z.txt");
            File file3 = new File(Dir, "DatenBurpee_Acc_Y.txt");


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

           //text.setText("Drehung auf Y-Achse: " + (int) event.values[1]*(-1) );


           new Thread(new Runnable() {
               @Override
               public void run() {
                   for (int i = 0; i <2; i++){
                       list[i] = (int) event.values[1];
                       try {
                           Thread.sleep(500);
                       } catch (InterruptedException e) {
                       }
                   }
                   text.setText("array: " + list[0] + "," + list[1]);

               }
           }).start();


           /**Text += (int)event.values[1] *(-1);
            Text += "\n";

           Text4 +=(int)event.values[1] *(-1);
           Text4 += ",";**/

       }

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            textAcc.setText("Beschleunigung auf Z-Achse:" + (int)event.values[2] + "\nBeschleunigung auf Y-Achse: " + (int)event.values[1]);

            Text2 += (int)event.values[2]*5+100;
            Text2 += "\n";
            Text3 += (int)event.values[1]*5+100;
            Text3 += "\n";


        }


    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void auswerten(View view) {
        String[] liste = Text4.split(",");
        Text4 = "";
        lymax1 = 0;
        lymax2 = 0;
        lymin = 500;
        pos = 5000;
        pos2 = 5000;
        counter = 0;

        for(int i = 0; i< liste.length; i++){

            int v = Math.abs(Integer.parseInt(liste[i]));

            if(v >= 120 &&  i<pos){
                lymax1 = v;
                if(v>lymax1){
                    lymax1 = v;
                }
            }
            else if(v <= 40){
                lymin = v;

                if(i < pos){
                    pos =i;}

                if(v<lymin){
                    lymin = v;
                }
            }

            else if(v >=120 && i > pos){
                lymax2 = v;

                if(i < pos2){
                pos2 =i;}

                if(v>lymax2){
                    lymax2 = v;
                }
            }

            else if(v>90 && v<110 && i>pos2){
                counter+=1;

                lymax1=0;
                lymax2=0;
                lymin=0;
                pos=5000;
                pos2=5000;
            }

        }
        machText.setText("Du hast " + counter + " Burpees gemacht! :-) ");


    }



}

