package com.jskgmail.cricview;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyService extends Service implements SensorEventListener {

    int mAzimuth;
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
    FirebaseDatabase database ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MyService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.e("myservice", "Ongoing");
        final String TAG = "service";
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        database = FirebaseDatabase.getInstance();
        start();
        scanWifiList();



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }



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

Log.e("whereami",where+"");
//        txt_compass.setText(mAzimuth + "° " + where);
        if(mAzimuth <350 && mAzimuth > 280 ) {
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

        DatabaseReference myRef = database.getReference("dir");
        myRef.setValue(where);




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




    private void scanWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
        if (wifiManager.isWifiEnabled())
        {
            final WifiInfo wifiInfo=wifiManager.getConnectionInfo();

            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                  //    Toast.makeText(getApplicationContext(),wifiInfo.getRssi()+"a",Toast.LENGTH_SHORT).show();
                    int rssi = -1 * wifiInfo.getRssi();
                    Log.e("wiwiwi",rssi+"");

                    // close this activity
                    DatabaseReference myRef = database.getReference("zoom");
                    if(rssi>54)
                    {
                        myRef.setValue(0);
                        scanWifiList();

                    }
                    else if(rssi<54&&rssi>40)
                    {
                        myRef.setValue(1);
                        scanWifiList();

                    }
                    else if(rssi<40)
                    {
                        myRef.setValue(2);
                        scanWifiList();
                    }
                }
            }, 2000);
        }
    }
}


