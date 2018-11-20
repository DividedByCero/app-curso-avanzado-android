package com.example.example.appfinalavanzadoandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.models.Post;
import com.example.example.appfinalavanzadoandroid.ui.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostAdapter extends BaseAdapter<Post> {
    private Context mContext;

    public View.OnClickListener GetListener(final Post post){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, post.getFileName());
                intent.putExtra(Intent.EXTRA_TEXT, post.getDownloadUrl().toString());
                mContext.startActivity(Intent.createChooser(intent, "Share Image"));
            }
        };

    }
    public View.OnClickListener GetLocationListener(final Post post){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:" + post.getLocation();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);
            }
        };
    }


    public PostAdapter(Context Context, int mViewId, ArrayList<Post> items) {
        super(Context, mViewId, items);
        mContext = Context;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        View view = customViewHolder.mView;
        Post post = GetItem(i);
        Bitmap bmp = post.getImage();

        TextView textView = view.findViewById(R.id.image_title);
        TextView DateView = view.findViewById(R.id.date_text);
        TextView authorTextView = view.findViewById(R.id.author_text);
        ImageView imgView = view.findViewById(R.id.image_view);
        ImageButton imgBtn = view.findViewById(R.id.share_btn);
        ImageButton locationBtn = view.findViewById(R.id.location_btn);

        if (bmp != null){
            imgView.setImageBitmap(bmp);
        }

        imgBtn.setOnClickListener(GetListener(post));

        if(post.getLocation() == null){
            locationBtn.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }
        else {
            locationBtn.setImageResource(R.drawable.ic_location_on_black_24dp);
            locationBtn.setOnClickListener(GetLocationListener(post));
        }

        authorTextView.setText(post.author);

        if(post.getDate() != null)
            DateView.setText(new SimpleDateFormat("MM/dd/yyyy").format(post.getDate()));
        if(post.title != null)
            textView.setText( post.title);
        else
            textView.setText( post.fileName);
    }
}
