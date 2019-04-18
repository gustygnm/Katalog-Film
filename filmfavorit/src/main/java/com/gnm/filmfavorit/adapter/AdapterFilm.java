package com.gnm.filmfavorit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnm.filmfavorit.BuildConfig;
import com.gnm.filmfavorit.activity.DetailActivity;
import com.gnm.filmfavorit.R;
import com.gnm.filmfavorit.model.DetailFilm;
import com.gnm.filmfavorit.util.DateFormat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterFilm extends RecyclerView.Adapter<AdapterFilm.ItemDetailHolder> {

    private List<DetailFilm> detailList = new ArrayList<>();
    private Context context;

    public AdapterFilm(Context context) {
        this.context = context;
    }

    @Override
    public ItemDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemDetailHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false)
        );
    }

    public void setDetail(List<DetailFilm> detail) {
        this.detailList = detail;
    }

    public List<DetailFilm> getList() {
        return detailList;
    }

    @Override
    public void onBindViewHolder(ItemDetailHolder holder, int position) {
        holder.bindView(detailList.get(position));
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    class ItemDetailHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgCover)
        ImageView imgCover;

        @BindView(R.id.imgBg)
        ImageView imgBg;

        @BindView(R.id.txtJudul)
        TextView txtJudul;

        @BindView(R.id.txtDate)
        TextView txtDate;

        @BindView(R.id.txtRating)
        TextView txtRating;

        ItemDetailHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final DetailFilm detail) {
            txtJudul.setText(detail.getmOriginalTitle());
            Picasso.get()
                    .load(BuildConfig.URL_IMAGE + detail.getmPosterPath())
                    .placeholder(R.drawable.no_image2)
                    .error(R.drawable.no_image2)
                    .into(imgCover);
            Picasso.get()
                    .load(BuildConfig.URL_IMAGE  + detail.getmBackdropPath())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(imgBg);
            txtDate.setText(DateFormat.getDate(detail.getmReleaseDate()));
            txtRating.setText(String.valueOf(detail.getmVoteAverage())+"/10");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra("id", detail.getmId().toString());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
