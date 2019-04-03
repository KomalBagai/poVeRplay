package com.jskgmail.cricview;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    FirebaseDatabase database ;
    int mAzimuth,stopp;
    long curTime,difftime,curTime1,difftime1;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    TextView strt,stop;
    EditText t1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
     //   mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       // txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        //database = FirebaseDatabase.getInstance();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
      //  database = FirebaseDatabase.getInstance();

t1=findViewById(R.id.textView5);

        strt=findViewById(R.id.textView2);

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setText(t1.getText().toString()+";");
                start();
stopp=0;

            }
        });
        stop=findViewById(R.id.textView10);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();stopp=1;
            }
        });
      //  Intent s=new Intent(this,MyService.class);
       // startService(s);
        //startActivity(new Intent(MainActivity.this,MonitoringActivity.class));

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stopp==0) {


            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometerSet = true;
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(rMat, orientation);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }

            mAzimuth = Math.round(mAzimuth);
            //  compass_img.setRotation(-mAzimuth);
            t1.setText(t1.getText().toString() + ","+mAzimuth);


            int where = 1;

            if (mAzimuth >= 350 || mAzimuth <= 10)
                where = 1;
            if (mAzimuth < 350 && mAzimuth > 280)
                where = 2;
            if (mAzimuth <= 280 && mAzimuth > 260)
                where = 3;
            if (mAzimuth <= 260 && mAzimuth > 190)
                where = 4;
            // if (mAzimuth <= 190 && mAzimuth > 170)
            //     where = 5;
            if (mAzimuth <= 170 && mAzimuth > 100)
                where = 5
                        ;
            // if (mAzimuth <= 100 && mAzimuth > 80)
            //    where = 7;
            //if (mAzimuth <= 80 && mAzimuth > 10)
            //   where = 8;

            Log.e("whereami", where + "");
//        txt_compass.setText(mAzimuth + "° " + where);
            if (mAzimuth < 350 && mAzimuth > 280) {
            }
/*
        if(where.equals("W"))
        {
            curTime = System.currentTimeMillis();
//////same algo
            long newTime1= System.currentTimeMillis();
            difftime1=newTime1-curTime1;
            if (difftime1<4000) {
                DatabaseReference myRef = database.getReference("zoom");

                myRef.setValue("1");

                Toast.makeText(getApplicationContext(), "s" + difftime, Toast.LENGTH_LONG).show();
//Log.e("diffTime",""+difftime);
            }
            curTime1=0;




        }
        if(where.equals("E"))
        {
            long newTime= System.currentTimeMillis();
            difftime=newTime-curTime;
            if (difftime<4000) {
                DatabaseReference myRef = database.getReference("zoom");

                myRef.setValue("2");

                Toast.makeText(getApplicationContext(), "s" + difftime, Toast.LENGTH_LONG).show();
//Log.e("diffTime",""+difftime);
            }
            curTime=0;
            curTime1 = System.currentTimeMillis();

        }
*/


        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }



    public void stop() {

        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }



















































































































/*
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
      //  compass_img.setRotation(-mAzimuth);

        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";


        txt_compass.setText(mAzimuth + "° " + where);
        if(mAzimuth <350 && mAzimuth > 280 ) {
        }

if(where.equals("W"))
{
    curTime = System.currentTimeMillis();

////////////////////////////////same algo
    long newTime1= System.currentTimeMillis();
    difftime1=newTime1-curTime1;
    if (difftime1<4000) {
        DatabaseReference myRef = database.getReference("zoom");

        myRef.setValue("1");

        Toast.makeText(getApplicationContext(), "s" + difftime, Toast.LENGTH_LONG).show();
//Log.e("diffTime",""+difftime);
    }
    curTime1=0;




}
if(where.equals("E"))
 {
     long newTime= System.currentTimeMillis();
     difftime=newTime-curTime;
     if (difftime<4000) {
         DatabaseReference myRef = database.getReference("zoom");

         myRef.setValue("2");

         Toast.makeText(getApplicationContext(), "s" + difftime, Toast.LENGTH_LONG).show();
//Log.e("diffTime",""+difftime);
     }
     curTime=0;
     curTime1 = System.currentTimeMillis();

 }


        DatabaseReference myRef = database.getReference("dir");

        myRef.setValue(where);




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

*/

































}

