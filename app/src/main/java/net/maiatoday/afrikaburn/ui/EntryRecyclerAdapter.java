/*
 * MIT License
 *                            
 * Copyright (c) 2017 Maia Grotepass
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.maiatoday.afrikaburn.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.maiatoday.afrikaburn.R;
import net.maiatoday.afrikaburn.model.Entry;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Recycler adapter to show Entries
 * Created by maia on 2017/03/04.
 */

class EntryRecyclerAdapter extends RealmRecyclerViewAdapter<Entry, EntryRecyclerAdapter.MyViewHolder> {

    private final OnEntryClickListener listener;

    public EntryRecyclerAdapter(Context context, OnEntryClickListener activity, OrderedRealmCollection<Entry> data) {
        super(context, data, true);
        this.listener = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_entry, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Entry obj = getData().get(position);
        holder.data = obj;
        holder.title.setText(obj.title);
        holder.blurb.setText(obj.blurb);
        holder.what.setImageResource(Entry.whatToDrawableId(obj.what));
        holder.favourite.setImageResource(obj.favourite?R.drawable.ic_favorite_hot:R.drawable.ic_favorite_border_hot);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView blurb;
        public ImageView favourite;
        public ImageView what;
        public Entry data;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textTitle);
            blurb = (TextView) view.findViewById(R.id.textBlurb);
            favourite = (ImageView) view.findViewById(R.id.imageFav);
            what = (ImageView) view.findViewById(R.id.imageWhat);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.openItem(data);
        }
    }

}
