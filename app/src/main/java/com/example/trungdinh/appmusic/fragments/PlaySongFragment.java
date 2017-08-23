package com.example.trungdinh.appmusic.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trungdinh.appmusic.R;
import com.example.trungdinh.appmusic.ShadowImageView;
import com.example.trungdinh.appmusic.utils.PlayerConstants;


public class PlaySongFragment extends Fragment {

    private static ShadowImageView imgRun;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playsong, container, false);

        imgRun = (ShadowImageView) view.findViewById(R.id.imgRun);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_music1);
        imgRun.setImageBitmap(getCroppedBitmap(bitmap));
        animate();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void animate() {
        try {
            if (PlayerConstants.SONG_PAUSED) {
                imgRun.pauseRotateAnimation();
            } else {
                imgRun.resumeRotateAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            imgRun.cancelRotateAnimation();
            imgRun.startRotateAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
