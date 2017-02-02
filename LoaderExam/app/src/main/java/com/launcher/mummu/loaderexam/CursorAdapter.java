package com.launcher.mummu.loaderexam;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by muhammed on 2/2/2017.
 */

public class CursorAdapter extends android.widget.CursorAdapter {
    private Context context;
    private LayoutInflater cursorInflater;

    public CursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.contact_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);


        String name = cursor.getString(cursor.getColumnIndex(MainActivity.DISPLAY_NAME));
        String photo = cursor.getString(cursor.getColumnIndex(MainActivity.DISPLAY_PHOTO));
        Glide.with(context).load(photo)
                .thumbnail(0.5f)
                .crossFade().fallback(R.drawable.person)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        nameTextView.setText(name);

    }
}
