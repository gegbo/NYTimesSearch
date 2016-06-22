package com.example.gegbo.nytimessearch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gegbo.nytimessearch.Models.Article;
import com.example.gegbo.nytimessearch.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gegbo on 6/20/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private List<Article> articles;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvTitle) TextView tvHeadline;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        TextView title = viewHolder.tvHeadline;
        title.setText(article.getHeadline());

        ImageView picture = viewHolder.ivImage;
        picture.setImageResource(0);

        if (!article.getThumbnail().isEmpty()) {
            Glide.with(mContext).load(article.getThumbnail()).into(picture);
        }
        else {
            picture.setImageResource(R.drawable.noflyerthumbnail);
        }

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return articles.size();
    }

    //Don't need anymore because working with Recycler View Instead
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //get the data item for position
//        Article article = getItem(position);
//
//        //check to see if existing view is being recycled
//        //not using recycled view -> inflate the layout
//
//        if(convertView == null ) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_article_result,parent,false);
//        }
//
//        //find the image view
//        ImageView imageView = (ImageView)convertView.findViewById(R.id.ivImage);
//
//        //clear out the recycled image from from convertView from last time
//        imageView.setImageResource(0);
//
//        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
//        tvTitle.setText(article.getHeadline());
//
//        //populate the thumbnail image
//        //remote download the image in the background
//        String thumbnail = article.getThumbnail();
//
//        if (!thumbnail.isEmpty()) {
//            Picasso.with(getContext()).load(thumbnail).into(imageView);
//        }
//        return convertView;
//    }
}
