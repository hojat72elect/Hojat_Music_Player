package ca.on.sudbury.hojat.hojatmusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;


/**
 * The kind of adapter we'll have for managing our RecyclerViews.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    static ArrayList<MusicFile> mFiles;
    private Context mContext;

    MusicAdapter(Context mContext, ArrayList<MusicFile> mFiles) {
        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    public static byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.fileName.setText(mFiles.get(position).getTitle());
        byte[] image = getAlbumArt(mFiles.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .into(holder.albumArt);
        } else {
            Glide.with(mContext)
                    .load(R.mipmap.ic_launcher)
                    .into(holder.albumArt);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position", position);


                mContext.startActivity(intent);

            }
        });

        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete) {
                            Toast.makeText(mContext, "Delete Clicked!!", Toast.LENGTH_LONG).show();
                            deleteFile(position, v);
                        }
                        return true;
                    }
                });
            }
        });

    }

    private void deleteFile(int position, View v) {

        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mFiles.get(position).getId())); //   content://
        File file = new File(mFiles.get(position).getPath());
        boolean deleted = file.delete(); //delete your file.
        if (deleted) {
            mContext.getContentResolver().delete(contentUri, null, null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "File Deleted : ", Snackbar.LENGTH_LONG).show();
        } else {
            //maybe the file is in SD card.
            Snackbar.make(v, "Can't be deleted : ", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    void updateList(ArrayList<MusicFile> musicFileArrayList) {
        mFiles = new ArrayList<>();
        mFiles.addAll(musicFileArrayList);
        notifyDataSetChanged();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fileName;
        ImageView albumArt, menuMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.music_file_name);
            albumArt = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menuMore);

        }
    }

}
