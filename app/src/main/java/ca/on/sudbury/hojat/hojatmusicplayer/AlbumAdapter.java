package ca.on.sudbury.hojat.hojatmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static ca.on.sudbury.hojat.hojatmusicplayer.MusicAdapter.getAlbumArt;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    View view;
    private Context mContext;
    private ArrayList<MusicFile> albumFiles;

    public AlbumAdapter(Context mContext, ArrayList<MusicFile> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        holder.albumName.setText(albumFiles.get(position).getAlbum());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.albumImage);
        } else {
            Glide.with(mContext)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.albumImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetails.class);
                intent.putExtra("albumName", albumFiles.get(position).getAlbum());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView albumImage;
        TextView albumName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.album_image);
            albumName = itemView.findViewById(R.id.album_name);

        }
    }


}
