package com.shenni.lives.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shenni.lives.R;
import com.shenni.lives.utils.ImageUtils;
import com.shenni.lives.utils.SPUserUtils;
import com.shenni.lives.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.shenni.lives.utils.ToastUtil.toast;

public class PhotoGraphActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.btn_selectphoto)
    Button btnSelectphoto;
    @InjectView(R.id.btn_creame)
    Button btnCreame;
    @InjectView(R.id.btn_cancle)
    Button btnCancle;
    @InjectView(R.id.rl_photo)
    RelativeLayout rlPhoto;

    private boolean isCrop = true;

    private String PHOTO_FILE_NAME;
    private static final int PHOTO_REQUEST_GALLERY = 0;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_CUT = 2;
    protected SPUserUtils config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_graph2);
        ButterKnife.inject(this);
        config = SPUserUtils.sharedInstance();
        if (null != getIntent()) {
            String iscrop = getIntent().getStringExtra("iscrop");
            if (!StringUtil.isNullOrEmpty(iscrop)&&iscrop.equalsIgnoreCase("no"))
                isCrop = false;
        }
        init();
    }

    public void init() {

        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(//添加必须需要的权限
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .request();


        rlPhoto.getBackground().setAlpha(200);

        btnSelectphoto.setOnClickListener(this);
        btnCreame.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {


    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        toast("您已禁止应用拍照");
    }


    /**
     * 获取当前系统时间作为文件名
     *
     * @return
     */
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }


    // 从相册获取
    public void gallery() {
        PHOTO_FILE_NAME = getNowTime() + ".jpeg";
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void camera() {
        PHOTO_FILE_NAME = getNowTime() + ".jpeg";
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {

            // 从文件中创建uri
            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }


    private void takePhotos() {

        Context context = this;
        PackageManager packageManager = context.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            toast("您的设备不支持拍照");
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = ImageUtils.createImageFile();
            } catch (IOException ex) {
                toast("保存图片失败");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                PHOTO_FILE_NAME = fileUri.getPath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CAREMA);
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
                    crop(uri);
                }
                break;
            case PHOTO_REQUEST_CAREMA:
                if (hasSdcard()) {
                    crop(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
                } else {
                    Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case PHOTO_REQUEST_CUT:
                Intent intent = new Intent();
                intent.putExtra("PHOTO_FILE_NAME", PHOTO_FILE_NAME);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(Uri uri) {
        if(!isCrop){
            Intent intent = new Intent();
            intent.putExtra("PHOTO_FILE_NAME", PHOTO_FILE_NAME);
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));


//        intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(Environment.getExternalStorageDirectory(), UN_CROP).getAbsolutePath());
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//        Log.e("all_pic", FileUtils.getPath(this,uri));
    }

    // 判断SD卡是否存在
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selectphoto:
                gallery();
                break;
            case R.id.btn_creame:
                camera();
//                takePhotos();
                break;

            case R.id.btn_cancle:
                finish();
                break;
        }
    }


}
