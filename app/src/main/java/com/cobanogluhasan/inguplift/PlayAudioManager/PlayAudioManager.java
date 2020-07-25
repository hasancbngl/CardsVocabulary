package com.cobanogluhasan.inguplift.PlayAudioManager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.cobanogluhasan.inguplift.Interfaces.MediaPlayerListener;

public class PlayAudioManager {

    private static final String TAG = "PlayAudioManager";
    private static MediaPlayer mediaPlayer;

    public static void playAudio(Context context, final String url, final MediaPlayerListener listener) throws Exception {


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                listener.onErrorListener(true);
                Log.e("MediaError","hata var");

                return false;
            }
        });



        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

               // Log.i(TAG, "onPrepared: " + "file is ready.");
                mediaPlayer.start();
            }
        });





    }


}



     /*  if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
      }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {



            }
        });

        mediaPlayer = MediaPlayer.create(context, Uri.parse(url));*/
       // mediaPlayer.start();
    //}*/
