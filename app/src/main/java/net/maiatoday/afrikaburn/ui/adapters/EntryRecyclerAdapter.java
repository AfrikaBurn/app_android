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

package net.maiatoday.afrikaburn.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.maiatoday.afrikaburn.BR;
import net.maiatoday.afrikaburn.R;
import net.maiatoday.afrikaburn.databinding.RowItemEntryBinding;
import net.maiatoday.afrikaburn.model.Entry;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Recycler adapter to show Entries
 * Created by maia on 2017/03/04.
 */

public class EntryRecyclerAdapter extends RealmRecyclerViewAdapter<Entry, EntryRecyclerAdapter.BindingViewHolder> {

    private final OnEntryClickListener listener;

    public EntryRecyclerAdapter(Context context, OnEntryClickListener activity, OrderedRealmCollection<Entry> data) {
        super(data, true);
        this.listener = activity;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowItemEntryBinding itembinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.row_item_entry, parent, false);
        return new BindingViewHolder(itembinding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        Entry obj = getData().get(position);
        holder.bind(obj, listener);
    }

    class BindingViewHolder extends RecyclerView.ViewHolder {
        
        private ViewDataBinding binding;

        public BindingViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Entry data, OnEntryClickListener listener) {
            binding.setVariable(BR.data, data);
            binding.setVariable(BR.handler, listener);
            binding.executePendingBindings();
        }
    }

}
