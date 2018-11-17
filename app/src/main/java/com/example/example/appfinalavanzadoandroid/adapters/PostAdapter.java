package com.example.example.appfinalavanzadoandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
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

    public PostAdapter(Context Context, int mViewId, ArrayList<Post> items) {
        super(Context, mViewId, items);
        mContext = Context;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        View view = customViewHolder.mView;
        TextView textView = view.findViewById(R.id.image_title);
        TextView DateView = view.findViewById(R.id.date_text);
        TextView authorTextView = view.findViewById(R.id.author_text);
        ImageView imgView = view.findViewById(R.id.image_view);
        ImageButton imgBtn = view.findViewById(R.id.share_btn);
        Post post = GetItem(i);

        imgBtn.setOnClickListener(GetListener(post));

        if(post.getDate() != null)
            DateView.setText(new SimpleDateFormat("MM/dd/yyyy").format(post.getDate()));

        authorTextView.setText(post.author);
        textView.setText(post.fileName);

        Bitmap bmp = post.getImage();
        if (bmp != null){
            imgView.setImageBitmap(bmp);
        }
    }
}
