package com.example.on;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    List<CategoryItem> list;

    public CategoryAdapter(Context context, List<CategoryItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null)
            view = View.inflate(context, R.layout.categoryitem,null);
        TextView categoryname = view.findViewById(R.id.categoryname);
        TextView message= view.findViewById(R.id.message);

        categoryname.setText(list.get(i).getCategory());
        message.setText(list.get(i).getMessage());

        return view;
    }
}
