package com.rousseau_alexandre.scrawleo.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.rousseau_alexandre.scrawleo.R;

import java.util.Date;
import java.util.List;

/**
 * Adapter for list or recipes
 *
 * https://github.com/florent37/TutosAndroidFrance/tree/master/ListViewSample
 * http://tutos-android-france.com/listview-afficher-une-liste-delements/
 */
public class ScrawlerAdapter extends ArrayAdapter<Scrawler> {


    public ScrawlerAdapter(Context context, List<Scrawler> scrawlers) {
        super(context, 0, scrawlers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_scrawler, parent, false);
        }

        RecipeViewHolder viewHolder = (RecipeViewHolder) convertView.getTag();

        if(viewHolder == null){
            viewHolder = new RecipeViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            // viewHolder.progressBarPages = (CircleProgress) convertView.findViewById(R.id.rate);

            View bar = convertView.findViewById(R.id.scrawlerBar);
            viewHolder.nbLink = (TextView) bar.findViewById(R.id.nbLinkValue);
            viewHolder.scrawledAt = (TextView) bar.findViewById(R.id.scrawledAtValue);
            convertView.setTag(viewHolder);
        }

        // `getItem(position)` va récupérer l'item [position] de la List<Tweet> tweets
        Scrawler scrawler = getItem(position);

        // il ne reste plus qu'à remplir notre vue
        viewHolder.name.setText(scrawler.url);
        int count = scrawler.countPages(context);
        viewHolder.nbLink.setText(Integer.toString(count));
        Date lastScrawl = scrawler.getLastScrawlDate(context);
        viewHolder.scrawledAt.setText(lastScrawl != null ? lastScrawl.toString() : "never");
        // viewHolder.progressBarPages.setMax(count);

        return convertView;
    }

    public void reload() {
        clear();
        addAll(Scrawler.all(getContext()));
        notifyDataSetChanged();
    }

    private class RecipeViewHolder {
        public TextView name;
        public TextView nbLink;
        public TextView scrawledAt;
        // public CircleProgress progressBarPages;
    }
}
