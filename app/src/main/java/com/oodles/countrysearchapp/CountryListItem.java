package com.oodles.countrysearchapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.mindorks.butterknifelite.ButterKnifeLite;
import com.mindorks.butterknifelite.annotations.BindView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CountryListItem extends RecyclerView.Adapter<CountryListItem.MyViewHolder> {

    private Context context;
    List<Countries> countrylistItem;


    public CountryListItem(Context context, List<Countries> countrylistItem) {
        this.countrylistItem = countrylistItem;
        this.context = context;
        //1

    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_list_item2, parent, false);
        return new MyViewHolder(view);  //2
        //5
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //4

        final Countries model = countrylistItem.get(position);
        holder.countryName_TV.setText(model.getName());
        holder.capital_TV.setText(model.getCapital());


        StringBuilder builder = new StringBuilder();

        for (Currency currency : model.getCurrencies()) {
            builder.append(currency.getName() + " : " + currency.getSymbol() + " , ");
        }
        builder.setLength(builder.length() - 2);
        holder.currencies_TV.setText(builder.toString());
        if (model.getFlag() != null && model.getFlag().contains("http")) {
            //Picasso.with(context).load(model.getFlag()).placeholder(R.drawable.flag).into(holder.countyFlag_IV);
            // Glide.with(context).load(model.getFlag()).into(holder.countyFlag_IV);


            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .listener(new SvgSoftwareLayerSetter<Uri>());

            requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(model.getFlag()))
                    .into(holder.countyFlag_IV);


        }


    }


    @Override
    public int getItemCount() {
        return countrylistItem == null ? 0 : countrylistItem.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.countyFlag_IV)
        CircleImageView countyFlag_IV;

        @BindView(R.id.countryName_TV)
        TextView countryName_TV;

        @BindView(R.id.capital_TV)
        TextView capital_TV;

        @BindView(R.id.currencies_TV)
        TextView currencies_TV;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnifeLite.bind(this, itemView);
//3

        }
    }

}
