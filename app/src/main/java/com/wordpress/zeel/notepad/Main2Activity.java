package com.wordpress.zeel.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.sql.SQLException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class Main2Activity extends AppCompatActivity implements CustomAdapter.ItemClickCallBack {

    private NotesDb mDbHelper;
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    ArrayList<CardItem> cardItems;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#ff4444"));
        setContentView(R.layout.activity_main2);


        this.setTitle("");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Transformers Movie.ttf");
        textView.setText("Notepad");
        textView.setTypeface(typeface);
        setSupportActionBar(toolbar);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDbHelper = new NotesDb(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        final ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_add_circle_black_36dp);


        com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSoundEffectsEnabled(true);
                v.playSoundEffect(SoundEffectConstants.CLICK);
            }
        });

        try {
            mDbHelper.open();
          //  mDbHelper.deleteAll();
            cardItems = mDbHelper.getAllData();
           // Toast.makeText(Main2Activity.this,mDbHelper.getData().get(0),Toast.LENGTH_SHORT).show();
            mDbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        customAdapter = new CustomAdapter(Main2Activity.this,cardItems);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(customAdapter));
        YoYo.with(Techniques.ZoomInUp)
                .duration(2000)
                .playOn(findViewById(R.id.rv));
        customAdapter.setItemClickCallBack(this);

        ImageView note = new ImageView(this);
        note.setImageResource(R.drawable.ic_note_add_black_36dp);


        ImageView imageNote = new ImageView(this);
        imageNote.setImageResource(R.drawable.ic_camera_black_36dp);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        SubActionButton fab = itemBuilder.setContentView(note).build();
        SubActionButton fab1 = itemBuilder.setContentView(imageNote).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(fab)
                .addSubActionView(fab1)
                .attachTo(actionButton)
                .build();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   View myVIew = findViewById(R.id.fab);
                int cx = myVIew.getWidth() / 2;
                int cy = myVIew.getHeight() / 2;
                float radius = (float) Math.hypot(cx, cy);
                Animator animator = ViewAnimationUtils.createCircularReveal(myVIew, cx, cy, 0, radius);
                animator.start();  */
                Intent intent = new Intent(Main2Activity.this, Note.class);
                intent.putExtra("image", false);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
        });


        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   View myVIew = findViewById(R.id.fab1);
                int cx= myVIew.getWidth()/2;
                int cy = myVIew.getHeight()/2;
                float radius = (float) Math.hypot(cx,cy);
                Animator animator = ViewAnimationUtils.createCircularReveal(myVIew, cx, cy, 0, radius);
                animator.start();  */
                Intent intent = new Intent(Main2Activity.this, Note.class);
                intent.putExtra("image",true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                finish();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createCallBack());
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }




    public ItemTouchHelper.Callback createCallBack() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                cardItems.add(target.getAdapterPosition(), cardItems.get(viewHolder.getAdapterPosition()));
                cardItems.remove(viewHolder.getAdapterPosition());
                customAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
                dialog.setTitle("DELETE " + cardItems.get(viewHolder.getAdapterPosition()).getTitle1());
                dialog.setIcon(R.drawable.ic_delete_black_36dp);
                dialog.setMessage("Are you sure...?");

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cardItems.get(viewHolder.getAdapterPosition()).getBool() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                            builder.setIcon(R.drawable.ic_lock_open_white_24dp);
                            builder.setTitle("Enter Password");
                            final EditText editText = new EditText(Main2Activity.this);
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            builder.setView(editText);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if (editText.getText().toString().length() < 5 && !editText.getText().toString().equals("")) {

                                            CardItem cardItem = cardItems.get(viewHolder.getAdapterPosition());
                                            if (cardItem.getPassword() == Integer.parseInt(editText.getText().toString())) {
                                                mDbHelper.open();
                                                mDbHelper.deleteEntry(cardItems.get(viewHolder.getAdapterPosition()).getTitle1());
                                                cardItems.remove(viewHolder.getAdapterPosition());
                                                mDbHelper.close();
                                                customAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                            } else {
                                                Toast.makeText(Main2Activity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                customAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                            }

                                        } else
                                            Toast.makeText(Main2Activity.this, "Enter 4 or less digits pin only", Toast.LENGTH_LONG).show();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    customAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                            });
                            builder.show();
                        } else if (cardItems.get(viewHolder.getAdapterPosition()).getBool() == 0) {
                            try {
                                mDbHelper.open();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            mDbHelper.deleteEntry(cardItems.get(viewHolder.getAdapterPosition()).getTitle1());
                            cardItems.remove(viewHolder.getAdapterPosition());
                            mDbHelper.close();
                            customAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });
                dialog.show();

            }

        };
        return simpleCallback;
    }



    @Override
    public void onItemClick(final int position) {
        final Intent intent=new Intent(Main2Activity.this,Main23Activity.class);
                intent.putExtra("title",  cardItems.get(position).getTitle1());
                intent.putExtra("body",  cardItems.get(position).getSubtitle());
                intent.putExtra("position",position);
                if (cardItems.get(position).getImagebool()==1){
                    intent.setAction("image note");
                }
                if (cardItems.get(position).getBool()==1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                    builder.setTitle("Enter password");
                    final EditText editText = new EditText(Main2Activity.this);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(editText);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (cardItems.get(position).getPassword() == Integer.parseInt(editText.getText().toString())) {
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                finish();
                            } else
                                Toast.makeText(Main2Activity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();


                }
                else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }
    }

    @Override
    public void onSecondaryItemClick(final int position) {
        if (cardItems.get(position).getBool() == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setIcon(R.drawable.ic_lock_open_white_24dp);
            builder.setTitle("Remove Password");
            builder.setMessage("Enter old password");
            final EditText editText = new EditText(Main2Activity.this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setLines(1);
            builder.setView(editText);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (editText.getText().toString().length()<5 && !editText.getText().toString().equals("")) {
                            mDbHelper.open();
                            CardItem cardItem = cardItems.get(position);
                            if (cardItem.getPassword() == Integer.parseInt(editText.getText().toString()) && cardItem.getImagebool() == 1) {
                                mDbHelper.upgradeEntry(cardItem.getTitle1(), cardItem.getSubtitle(), cardItem.getTime1(), cardItem.getImage(), false, cardItem.getPassword(), true);
                                cardItems.get(position).setBool(0);
                            } else if (cardItem.getPassword() == Integer.parseInt(editText.getText().toString()) && cardItem.getImagebool() == 0) {
                                mDbHelper.upgradeEntry(cardItem.getTitle1(), cardItem.getSubtitle(), cardItem.getTime1(), cardItem.getImage(), false, cardItem.getPassword(), false);
                                cardItems.get(position).setBool(0);
                            }

                            mDbHelper.close();
                        }
                        else Toast.makeText(Main2Activity.this,"Enter 4 or less digits pin only",Toast.LENGTH_LONG).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                        customAdapter.notifyItemChanged(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // customAdapter.notifyItemChanged(position);
                }
            });
            builder.show();
        }
        else if (cardItems.get(position).getBool() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setIcon(R.drawable.ic_lock_white_24dp);
            builder.setTitle("Set Password");
            builder.setMessage("Enter 4 digit pin");
            final EditText editText = new EditText(Main2Activity.this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(editText);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (editText.getText().toString().length() < 5 && !editText.getText().toString().equals("")) {
                            mDbHelper.open();
                            CardItem cardItem = cardItems.get(position);
                            if (cardItem.getImagebool() == 1) {
                                mDbHelper.upgradeEntry(cardItem.getTitle1(), cardItem.getSubtitle(), cardItem.getTime1(), cardItem.getImage(), true, Integer.parseInt(editText.getText().toString()), true);
                                cardItems.get(position).setBool(1);
                                cardItems.get(position).setPassword(Integer.parseInt(editText.getText().toString()));
                            } else if (cardItem.getImagebool() == 0) {
                                mDbHelper.upgradeEntry(cardItem.getTitle1(), cardItem.getSubtitle(), cardItem.getTime1(), cardItem.getImage(), true, Integer.parseInt(editText.getText().toString()), false);
                                cardItems.get(position).setBool(1);
                                cardItems.get(position).setPassword(Integer.parseInt(editText.getText().toString()));
                            }
                            mDbHelper.close();
                        }
                        else Toast.makeText(Main2Activity .this,"Enter 4 or less digit pin only",Toast.LENGTH_LONG).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    customAdapter.notifyItemChanged(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                //    customAdapter.notifyItemChanged(position);
                }
            });
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.delete_all){

            AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
            dialog.setTitle("DELETE ALL");
            dialog.setIcon(R.drawable.ic_delete_black_36dp);
            dialog.setMessage("Are you sure...?");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        mDbHelper.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    mDbHelper.deleteAll();
                    mDbHelper.close();
                    Toast.makeText(Main2Activity.this, "All notes deleted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
