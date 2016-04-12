package com.example.yin_zeping.myearpiceapplication;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SensorEventListener {// 这里实现传感器监听
    Button btn_start = null;
    Button btn_stop = null;
    Button btn_close = null;
    Button btn_open = null;

    MediaPlayer _mediaPlayer = null; // 音乐播放管理器
    AudioManager audioManager = null; // 声音管理器

    SensorManager _sensorManager = null; // 传感器管理器
    Sensor mProximiny = null; // 传感器实例

    float f_proximiny; // 当前传感器距离
    //屏幕开关
    private PowerManager localPowerManager = null;//电源管理对象
    private PowerManager.WakeLock localWakeLock = null;//电源锁

    private boolean isFirstSetSpeaker = true;
    private String musicPath = Environment.getExternalStorageDirectory() + "/lingchat/files/" + "1.mp3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);

        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        btn_open = (Button) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(this);

        init();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 注册传感器
        _sensorManager.registerListener(this, mProximiny,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // 取消注册传感器
        _sensorManager.unregisterListener(this);
    }

    private void playerMusic(String path, AudioOutputType type) {
        // 重置播放器
        _mediaPlayer.reset();
        try {
            // 设置播放路径
            _mediaPlayer.setDataSource(path);
            //这里是重点注意的地方，必须要设置，并且要在_mediaPlayer.prepare()之前设置
            if(type == AudioOutputType.EARPIECE){
                _mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            }else{
                _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            // 准备播放
            _mediaPlayer.prepare();
            // 开始播放
            _mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopPlayerMusic() {
        // 停止播放
        if (_mediaPlayer.isPlaying()) {
            _mediaPlayer.reset();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_close:
                setSpeakerphoneOn(false);
                playerMusic(musicPath, AudioOutputType.SPEAKER);
                break;
            case R.id.btn_open:
                setSpeakerphoneOn(true);
                playerMusic(musicPath, AudioOutputType.EARPIECE);
                break;
            case R.id.btn_start:// 音乐取自于Sd卡上的音乐
                playerMusic(musicPath, AudioOutputType.SPEAKER);
                break;
            case R.id.btn_stop:
                stopPlayerMusic();
                break;
        }
    }

    /*
     * 实现SensorEventListener需要实现的两个方法。
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] its = event.values;
        //Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
        if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

            System.out.println("its[0]:" + its[0]);
            //经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
            if (its[0] == 0.0) {// 贴近手机
                setSpeakerphoneOn(false);
                playerMusic(musicPath, AudioOutputType.EARPIECE);
                if (localWakeLock.isHeld()) {
                    return;
                } else{

                    localWakeLock.acquire();// 申请设备电源锁
                }
            } else {// 远离手机
                if(isFirstSetSpeaker){
                    isFirstSetSpeaker = false;
                    localWakeLock.acquire();// 申请设备电源锁
                }else{
                    setSpeakerphoneOn(true);
                    playerMusic(musicPath, AudioOutputType.SPEAKER);
                    if (localWakeLock.isHeld()) {
                        return;
                    } else{
                        localWakeLock.setReferenceCounted(false);
                        localWakeLock.release(); // 释放设备电源锁
                    }
                }
            }
        }
    }

    /*
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    /**
     * 初始化
     */
    private void init() {
        _mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximiny = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //获取系统服务POWER_SERVICE，返回一个PowerManager对象
        localPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        localWakeLock = this.localPowerManager.newWakeLock(32, "MyPower");//第一个参数为电源锁级别，第二个是日志tag
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyManager();
    }

    /**
     * 退出注销
     */
    public void onDestroyManager() {
        if(_sensorManager != null){
            if(localWakeLock.isHeld())
                localWakeLock.release();//释放电源锁，如果不释放finish这个acitivity后仍然会有自动锁屏的效果，不信可以试一试
            localWakeLock = null;
        }
    }

    /**
     * 切换麦克风和话筒
     * @param on
     */
    private synchronized void setSpeakerphoneOn(boolean on) {
        if(on) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_NORMAL);
        } else {
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            //			把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    private enum AudioOutputType{
        EARPIECE,
        SPEAKER,
    }
}
