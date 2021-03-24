package steven.li.pocketanimemusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import Model.Anime;

public class AnimeListRecyclerViewAdapter extends RecyclerView.Adapter<AnimeViewHolder> {


    private List<Anime> animes;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public AnimeListRecyclerViewAdapter(Context context, List<Anime> datas ) {
        this.context = context;
        this.animes = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // Inflate view from recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.anime_card, parent, false);

        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRecyclerItemClick( (RecyclerView)parent, v);
            }
        });
        return new AnimeViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        // Cet country in countries via position
        Anime anime = this.animes.get(position);

        RequestQueue queue = MySingleton.getInstance(context).getRequestQueue();
        // Set an request using the vector to get the image and set in the imageView
        //TODO: API IMAGE
        /*
        String link = anime.getImage_url();
        ImageRequest request = new ImageRequest(link,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.getImageView().setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        });
        // Add the image request in the queue
        queue.add(request);*/
        holder.getTitleView().setText(anime.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.animes.size();
    }

    // Find Image ID corresponding to the name of the image (in the directory drawable).
    public int getDrawableResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "drawable", pkgName);
        Log.i("oij", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    // Click on RecyclerView Item.
    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        Anime anime  = this.animes.get(itemPosition);

        //Toast.makeText(this.context, ), Toast.LENGTH_LONG).show();
    }
}

class AnimeViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView titleView;

    // @itemView: recyclerview_item_layout.xml
    public AnimeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.card_image);
        this.titleView = (TextView) itemView.findViewById(R.id.card_title);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitleView(){
        return titleView;
    }
}