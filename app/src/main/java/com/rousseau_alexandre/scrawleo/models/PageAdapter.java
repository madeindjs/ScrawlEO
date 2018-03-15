package com.rousseau_alexandre.scrawleo.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.rousseau_alexandre.scrawleo.R;


/**
 * Adapter for list or pages
 * <p>
 * https://github.com/florent37/TutosAndroidFrance/tree/master/ListViewSample
 * http://tutos-android-france.com/listview-afficher-une-liste-delements/
 */
public class PageAdapter extends ArrayAdapter<ScrapedPage> {

    private Scrawler scrawler;


    public PageAdapter(Context context, Scrawler _scrawler) {
        super(context, 0, _scrawler.getPages(context));
        scrawler = _scrawler;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_page, parent, false);
        }

        RecipeViewHolder viewHolder = (RecipeViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new RecipeViewHolder();
            viewHolder.url = (TextView) convertView.findViewById(R.id.url);
            viewHolder.rate = (CircleProgress) convertView.findViewById(R.id.rate);
            convertView.setTag(viewHolder);
        }

        // `getItem(position)` va récupérer l'item [position] de la List<Tweet> tweets
        ScrapedPage scrapedPage = getItem(position);

        // il ne reste plus qu'à remplir notre vue
        viewHolder.url.setText(scrapedPage.getUrlWithoutDomain());
        viewHolder.rate.setProgress(scrapedPage.getRate());

        return convertView;
    }

    public void reload() {
        clear();
        addAll(scrawler.getPages(getContext()));
        notifyDataSetChanged();
    }

    private class RecipeViewHolder {
        public TextView url;
        public CircleProgress rate;
    }
}
