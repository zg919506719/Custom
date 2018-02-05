package com.xingjian.custom;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.io.FileDescriptor;

/**
 * Created by thinkpad on 2018/2/5.
 */

public class MusicService extends Service {
    private MediaPlayer player;
    private IMusic.Stub mBinder = new IMusic.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void start(String path) throws RemoteException {
            if (player != null) {
                //重置
//                player.reset();
            } else {
                player = MediaPlayer.create(MusicService.this, R.raw.test);
            }
            player.start();
        }

        @Override
        public void stop(String path) throws RemoteException {
            if (player != null) {
                player.pause();
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
