package com.wordpress.zeel.notepad;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * Created by zeel on 12-07-2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder>  {

    private final Activity context;
    private ArrayList<CardItem> cardItems;
    private LayoutInflater inflater;
    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack {
        void onItemClick(int position);
        void onSecondaryItemClick(int position);
    }

    public void setItemClickCallBack( final ItemClickCallBack itemClickCallBack){
        this.itemClickCallBack=itemClickCallBack;
    }

    public CustomAdapter(Activity context,ArrayList<CardItem> cardItems) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.cardItems=cardItems;
    }

    @Override
    public CustomAdapter.CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item,parent,false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.CustomHolder holder, int position) {

                CardItem cardItem = cardItems.get(position);
                if (cardItem.getImagebool()==1){
                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(cardItem.getImage());
                    Drawable d = new BitmapDrawable(yourSelectedImage);
                    holder.icon.setBackground(d);

                }
                holder.title.setText(cardItem.getTitle1());
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Ming in Bling.ttf");
        holder.title.setTypeface(typeface);
                if (cardItem.getBool()==1){
                    holder.lock.setImageResource(R.drawable.ic_lock_white_24dp);
                }
                else holder.lock.setImageResource(R.drawable.ic_lock_open_white_24dp);
                holder.subtitle.setText(cardItem.getSubtitle());
        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(),"fonts/Organo.ttf");
        holder.subtitle.setTypeface(typeface1);
                holder.date.setText(cardItem.getTime1());
                 setFadeAnimation(holder.itemView, position);

    }

    private  int lastPosition=-1;
    private void setFadeAnimation(View view,int position){
            if (position > lastPosition){
               YoYo.with(Techniques.Tada)
                       .duration(1000)
                       .playOn(view);

                lastPosition = position;
            }

    }
    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView icon;
        TextView title;
        TextView subtitle;
        TextView date;
        View container;
        ImageView lock;

        public CustomHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.image1);
            title = (TextView) itemView.findViewById(R.id.text1);
            subtitle=(TextView) itemView.findViewById(R.id.subtxt);
            date = (TextView) itemView.findViewById(R.id.time1);
            container = itemView.findViewById(R.id.cont_root);
            container.setOnClickListener(this);
            lock=(ImageView) itemView.findViewById(R.id.lock);
            lock.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.cont_root) {
                itemClickCallBack.onItemClick(getAdapterPosition());
            }
            else {
                itemClickCallBack.onSecondaryItemClick(getAdapterPosition());
            }
        }
    }
}
