package com.wordpress.zeel.notepad;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class Note extends AppCompatActivity{

    private NotesDb mDbHelper;
    private String body;

    private static final int CAMERA_REQUEST=1;
    private static final int PICK_FROM_GALLERY=2;
    private boolean setImage;
    String image;
    String cam_image;
    CircularImageView imageView;
    FloatingActionButton button;
    TextView space;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setStatusBarColor(Color.parseColor("#ff00ddff"));
        setContentView(R.layout.activity_note);

        final Bundle extras = getIntent().getExtras();

        if (extras.getBoolean("image")) {
            imageView = (CircularImageView) findViewById(R.id.image2);
            button =(FloatingActionButton) findViewById(R.id.but_image);
            space = (TextView) findViewById(R.id.space1);
            assert imageView != null;
            imageView.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            final CharSequence[] options = new String[]{"Take from Camera", "Take from Gallery"};

            final AlertDialog.Builder builder = new AlertDialog.Builder(Note.this);
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        callCamera();

                    } else if (which == 1) {
                        callGallery();
                    }
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog dialog = builder.create();
                    WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                    wmlp.gravity = Gravity.BOTTOM | Gravity.LEFT;
                    wmlp.x = 100;
                    wmlp.y = 100;
                    dialog.show();
                }
            });

            space.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (count%2==0) {
                        imageView.setVisibility(View.GONE);
                        button.setVisibility(View.GONE);
                        count++;
                    }
                    else {
                        imageView.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        count++;
                    }
                }
            });


        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText ed1=(EditText) findViewById(R.id.ed1);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Organo.ttf");
        ed1.setTypeface(typeface);


        mDbHelper=new NotesDb(this);
        try {
            mDbHelper.open();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                View myVIew = findViewById(R.id.fab);
                int cx= myVIew.getWidth()/2;
                int cy = myVIew.getHeight()/2;
                float radius = (float) Math.hypot(cx,cy);
                Animator animator = ViewAnimationUtils.createCircularReveal(myVIew, cx, cy, 0, radius);
                animator.start();

                body=ed1.getText().toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(Note.this);
                alert.setIcon(R.drawable.ic_save_black_36dp);
                alert.setTitle("Save");
                alert.setMessage("enter name of file");
                final EditText ed2 = new EditText(Note.this);
                alert.setView(ed2);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            mDbHelper.close();
                    }
                });
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
                        Integer year = calendar.get(Calendar.YEAR);
                        Integer month = calendar.get(Calendar.MONTH);
                        Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                        Integer min = Calendar.MINUTE;
                        String date = day.toString()+"/"+month.toString()+"/"+year.toString()+"\n"+hour.toString()+":"+min.toString();
                        if (setImage) {

                            mDbHelper.createEntry(body, ed2.getText().toString(), date, image);

                            mDbHelper.close();
                        }
                        else {
                            mDbHelper.createEntry(body,ed2.getText().toString(),date);

                            mDbHelper.close();
                        }
                        Snackbar.make(v, "Your note is Saved", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Intent intent=new Intent(Note.this,Main2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                        finish();
                    }
                });
                alert.show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CAMERA_REQUEST: {
                Bitmap bitmap = BitmapFactory.decodeFile(cam_image);
                Drawable d = new BitmapDrawable(bitmap);

                imageView.setImageDrawable(d);
                image = cam_image;
                setImage = true;
                break;
             }


            case PICK_FROM_GALLERY: {

                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filePath = cursor.getString(columnIndex);
              //  Toast.makeText(Note.this,filePath,Toast.LENGTH_LONG).show();
                cursor.close();
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                Drawable d = new  BitmapDrawable(yourSelectedImage);

                imageView.setImageDrawable(d);
                image = filePath;
                setImage=true;

                break;
            }
        }
    }

    /**
     * open camera method
     */
        public void callCamera() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getFile();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
            cameraIntent.putExtra("crop", true);
            cameraIntent.putExtra("aspectX", 0);
            cameraIntent.putExtra("aspectY", 0);
            cameraIntent.putExtra("outputX", 200);
            cameraIntent.putExtra("outputY", 150);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }

        /**
         * open gallery method
         */

        public void callGallery() {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 150);
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_GALLERY);

        }

        private File getFile(){
            File folder = new File("sdcard/notepad");
            if (!folder.exists()){
                folder.mkdir();
            }

            Random random = new Random();
            int count = random.nextInt(10000)+9999;
            File imagefile = new File(folder,"notepad"+count+".jpg");
            cam_image = imagefile.getPath();
            return imagefile;
        }



    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Note.this,Main2Activity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        finish();
    }
}
