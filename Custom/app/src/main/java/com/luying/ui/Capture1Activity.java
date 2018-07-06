package com.luying.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.luying.QrScanProxy;
import com.luying.util.ActivityUtils;
import com.luying.zxing.camera.CameraManager;
import com.luying.zxing.decoding.CaptureActivityHandler;
import com.luying.zxing.decoding.InactivityTimer;
import com.luying.zxing.util.ImageUtil;
import com.luying.zxing.view.ScanView;
import com.xingjian.custom.R;

import java.io.IOException;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 * @modify by zhengrui
 */
public class Capture1Activity extends BasePermissionActivity implements Callback, View.OnClickListener {
    private static final int REQUEST_CODE_SELECT_PIC_FROM_GALLERY = 101;
    private SurfaceView mSurfaceView;
    private ImageView mBackImageView;
    private TextView openlight;
    private TextView changeNumber;
    private RelativeLayout mLayoutTitle;
    private TextView mTitleTextView;
    private Button mSelectQRfromGallery;
    private CaptureActivityHandler handler;
    private ScanView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;


    private boolean localGalleryQRdecodeFinished = true;//用于指示解码相册中的二维码工作是否已经完成。如果没完成，则不能恢复zxing的摄像头扫描并解码；如果完成了，这是再去重新开启zxing的摄像头扫描解码功能


    /**
     * 手机的屏幕密度
     */
    private static float density;

    /**
     * Called when the activity is first created.
     */

    public static void launch(Context context) {
        Intent intent = new Intent(context, Capture1Activity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_capture1);

        initView();
    }

    private void initView() {

        density = this.getResources().getDisplayMetrics().density;

        viewfinderView = (ScanView) findViewById(R.id.viewfinder_view);

        mLayoutTitle = (RelativeLayout) findViewById(R.id.layout_title);
        mLayoutTitle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (QrScanProxy.getInstance().getTitleHeight() * density)));

        mTitleTextView = (TextView) findViewById(R.id.capture_title_txv);

        mTitleTextView.setText(QrScanProxy.getInstance().getTitleText(this.getResources()));
        mTitleTextView.setTextSize(QrScanProxy.getInstance().getTitleTextSize());
        mTitleTextView.setTextColor(QrScanProxy.getInstance().getTitleTextColor(this.getResources()));


        mSelectQRfromGallery = (Button) findViewById(R.id.capture_select_from_gallery_tv);

        requestCameraPermission();
        mSelectQRfromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicFromSystemGallery();
            }
        });

        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);


        mBackImageView = (ImageView) findViewById(R.id.capture_back_btn);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CameraManager.init(getApplication());

        hasSurface = false;


        openlight = (TextView) findViewById(R.id.open_light);
        changeNumber = (TextView) findViewById(R.id.change_number);
        openlight.setText("打开");
        openlight.setOnClickListener(this);
        changeNumber.setOnClickListener(this);


    }

    public void setTitleName(String title) {
        mTitleTextView.setText(title);
    }

    private void setCapture() {
        mSurfaceView.setBackgroundColor(0xff000000);
        mSurfaceView.setVisibility(View.VISIBLE);
        mSurfaceView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    /**
     * 初始化照相机和surfaceView，使zxing不断尝试从摄像头获取照片并解码
     * 本方法在Activity的onResume中，以及在扫描功能被关闭后需要再次开启时调用
     */
    private void initZXingCamera() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }

        vibrate = true;
    }

    /**
     * 停止zxing对摄像头的调用以及解码工作
     */
    private void closeZXingCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (localGalleryQRdecodeFinished) {
            initZXingCamera();
        }

        setCapture();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeZXingCamera();
    }

    @Override
    protected void onDestroy() {
//		inactivityTimer.shutdown();
        super.onDestroy();
    }


    /**
     * 处理扫描成功后的方法
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        playBeepSoundAndVibrate();
        initZXingCamera();
        if (result != null) {
            QrScanProxy.getInstance().getCallBack().OnReceiveDecodeResult(Capture1Activity.this, result.getText());
        } else {
            QrScanProxy.getInstance().getCallBack().OnReceiveDecodeResult(Capture1Activity.this, "");
        }

    }


    /**
     * 初始化摄像头
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }


    /**
     * 从系统相册选择二维码
     */
    private void selectPicFromSystemGallery() {
        localGalleryQRdecodeFinished = false;
        closeZXingCamera();

        // 激活系统图库，选择一张图片
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC_FROM_GALLERY);
    }

    /**
     * 通过从相册选取照片得到的URI，转换成图片的绝对路径
     *
     * @param uri
     * @return
     */
    private String getBitmapAbsolutePathFromMediaURI(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);  //获取照片路径
        cursor.close();
        return picturePath;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_PIC_FROM_GALLERY:
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        String picturePath = getBitmapAbsolutePathFromMediaURI(uri);
                        bitmap = ImageUtil.decodeSampledBitmapFromFile(picturePath, 500, 500);

                        /*bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);*/

                        if (bitmap != null) {
                            MyGalleryQRdecoderTask localDecodeTask = new MyGalleryQRdecoderTask(bitmap, new DecodeLocalGalleryQRcodeCallback() {
                                @Override
                                public void onDecodeResult(Result result) {
                                    handleDecode(result, null);
                                    localGalleryQRdecodeFinished = true;
                                }
                            });
                            localDecodeTask.execute();
                        } else {
                            localGalleryQRdecodeFinished = true;
                            initZXingCamera();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        localGalleryQRdecodeFinished = true;
                        initZXingCamera();
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }

                } else {
                    localGalleryQRdecodeFinished = true;
                    initZXingCamera();
                }

                break;
        }
    }


    /**
     * 闪光灯
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.open_light) {
            if (openlight.getText().toString().equals("打开")) {
                CameraManager.get().openLight();
                openlight.setText("关闭");
            } else if (openlight.getText().toString().equals("关闭")) {
                CameraManager.get().offLight();
                openlight.setText("打开");
            }
        } else if (id == R.id.change_number) {
            Intent intent = ActivityUtils.buildIntent(this, ActivityUtils.CHARGE_NUMBER);
            startActivity(intent);
        }
    }


    /**
     * 从相册获取二维码回调
     */
    interface DecodeLocalGalleryQRcodeCallback {
        void onDecodeResult(Result result);
    }

    /**
     * 重新启动扫描
     */
    public void restartCamera() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    /**
     * 返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        restartCamera();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ScanView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }


    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    class MyGalleryQRdecoderTask extends AsyncTask<Void, Void, Result> {
        private DecodeLocalGalleryQRcodeCallback callback;
        private Bitmap bitmap;

        public MyGalleryQRdecoderTask(Bitmap bitmap, DecodeLocalGalleryQRcodeCallback callback) {
            this.bitmap = bitmap;
            this.callback = callback;
        }

        @Override
        protected Result doInBackground(Void... params) {

            Result result = null;
            try {
                Bitmap compressedBitmap = ImageUtil.decodeSampledBitmapFromBitmap(bitmap, 80, 500, 500);
                int width = compressedBitmap.getWidth();
                int height = compressedBitmap.getHeight();

                bitmap.recycle();
                bitmap = null;

                int[] bitmapDataArray = new int[compressedBitmap.getWidth() * compressedBitmap.getHeight()];
                compressedBitmap.getPixels(bitmapDataArray, 0, compressedBitmap.getWidth(), 0, 0, compressedBitmap.getWidth(), compressedBitmap.getHeight());
                compressedBitmap.recycle();
                compressedBitmap = null;


                RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, bitmapDataArray);
                final BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
                final QRCodeReader qrCodeReader = new QRCodeReader();


                result = qrCodeReader.decode(binaryBitmap);
                Log.d("TAG", result.toString());
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            } finally {
                localGalleryQRdecodeFinished = true;
                return result;
            }

        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (callback != null) {
                callback.onDecodeResult(result);
            }
        }
    }

    private void requestCameraPermission() {
        performCodeWithPermission("悦电请求访问权限用于拍摄头像和从相册中选择相片", new PermissionCallback() {
            @Override
            public void hasPermission() {
            }

            @Override
            public void noPermission() {

            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

}