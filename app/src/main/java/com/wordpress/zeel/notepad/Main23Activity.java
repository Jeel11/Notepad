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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Main23Activity extends AppCompatActivity {

    String title;
    String body;
    private NotesDb mDbHelper;
    private ArrayList<CardItem> cardItems;
    public static final String IMAGE_SET="image note";
    private static final int CAMERA_REQUEST=1;
    private static final int PICK_FROM_GALLERY=2;

    boolean edit,editImage;
    String cam_image;
    int count=0;

    int position;
    CircularImageView imageView;
    TextView space;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#ff4444"));
        setContentView(R.layout.activity_main23);

        space = (TextView) findViewById(R.id.space2);


        Bundle extras = getIntent().getExtras();
        position=extras.getInt("position");
        mDbHelper = new NotesDb(Main23Activity.this);
        try {
            mDbHelper.open();
            cardItems= mDbHelper.getAllData();
            mDbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        if (IMAGE_SET.equals(intent.getAction())){
             imageView = (CircularImageView) findViewById(R.id.image3);
            imageView.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(cardItems.get(position).getImage());
            Drawable d = new BitmapDrawable(yourSelectedImage);
            imageView.setImageDrawable(d);

            final CharSequence[] options = new String[]{"Take from Camera", "Take from Gallery"};

            final AlertDialog.Builder builder = new AlertDialog.Builder(Main23Activity.this);
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

            imageView.setOnClickListener(new View.OnClickListener() {
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


        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/From Cartoon Blocks.ttf");



        if (Widget.LIST_ROW_NUMBER.equals(intent.getAction())){

            int position = intent.getIntExtra("position",0);
            try {
                mDbHelper.open();
                ArrayList<String> result = mDbHelper.getData();
                title=result.get(position);
                this.setTitle("");
                textView.setText(title);
                textView.setTypeface(typeface1);
                body = mDbHelper.getBody(title);
                mDbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            body=intent.getStringExtra("body");
            title=intent.getStringExtra("title");
            this.setTitle("");
            textView.setText(title);
            textView.setTypeface(typeface1);
        }

        final EditText ed1=(EditText)findViewById(R.id.ed1);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Organo.ttf");

        ed1.setText(body);
        ed1.setVisibility(View.INVISIBLE);
        ed1.setTypeface(typeface);

        final TextView tx1=(TextView)findViewById(R.id.tx1);
        mDbHelper = new NotesDb(this);

        tx1.setText(body);
        tx1.setTypeface(typeface);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab!= null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myVIew = findViewById(R.id.fab);
                int cx= myVIew.getWidth()/2;
                int cy = myVIew.getHeight()/2;
                float radius = (float) Math.hypot(cx,cy);
                Animator animator = ViewAnimationUtils.createCircularReveal(myVIew, cx, cy, 0, radius);
                animator.start();
                edit=true;

                tx1.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                ed1.setVisibility(View.VISIBLE);

            }
        });

        space.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (count%2==0) {
                    imageView.setVisibility(View.GONE);
                    count++;
                }
                else {
                    imageView.setVisibility(View.VISIBLE);
                    count++;
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.delete){
            try {
                mDbHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            mDbHelper.deleteEntry(title);
            mDbHelper.close();
            Toast.makeText(Main23Activity.this,"Note is deleted",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(Main23Activity.this,Main2Activity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                editImage=true;
                cardItems.get(position).setImage(cam_image);
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
                editImage=true;
                cardItems.get(position).setImage(filePath);
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
          /*  cameraIntent.putExtra("crop", "true");
            cameraIntent.putExtra("aspectX", 0);
            cameraIntent.putExtra("aspectY", 0);
            cameraIntent.putExtra("outputX", 200);
            cameraIntent.putExtra("outputY", 150);  */
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

        if (edit || editImage) {
            EditText editText = (EditText) findViewById(R.id.ed1);


            try {
                mDbHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            Integer day = calendar.get(Calendar.DAY_OF_MONTH);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH);
            Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
            Integer min = calendar.get(Calendar.MINUTE);
            String date = day.toString() + "/" + month.toString() + "/" + year.toString() + "\n" + hour.toString() + ":" + min.toString();
            if (cardItems.get(position).getBool() == 1 && cardItems.get(position).getImagebool() == 1) {
                mDbHelper.upgradeEntry(title, editText.getText().toString(), date, cardItems.get(position).getImage(), true, cardItems.get(position).getPassword(), true);
                mDbHelper.close();
            } else if (cardItems.get(position).getBool() == 0 && cardItems.get(position).getImagebool() == 0) {
                mDbHelper.upgradeEntry(title, editText.getText().toString(), date, cardItems.get(position).getImage(), false, cardItems.get(position).getPassword(), false);
                mDbHelper.close();
            } else if (cardItems.get(position).getBool() == 0 && cardItems.get(position).getImagebool() == 1) {
                mDbHelper.upgradeEntry(title, editText.getText().toString(), date, cardItems.get(position).getImage(), false, cardItems.get(position).getPassword(), true);
                mDbHelper.close();
            } else if (cardItems.get(position).getBool() == 1 && cardItems.get(position).getImagebool() == 0) {
                mDbHelper.upgradeEntry(title, editText.getText().toString(), date, cardItems.get(position).getImage(), true, cardItems.get(position).getPassword(), false);
                mDbHelper.close();
            }

            Toast.makeText(Main23Activity.this, "your note is updated", Toast.LENGTH_LONG).show();
        }
        Intent intent=new Intent(Main23Activity.this,Main2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        finish();
    }
}
