package kr.ac.kopo.sang.myapplicationtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomMainAdapter extends BaseAdapter {
    Context context;
    ArrayList<TodoItem> items;

    public CustomMainAdapter(Context context, ArrayList<TodoItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override public int getCount() { return items.size(); }
    @Override public Object getItem(int i) { return items.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View v, ViewGroup vg) {
        if (v == null) v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, vg, false);
        TextView t1 = v.findViewById(android.R.id.text1);
        TextView t2 = v.findViewById(android.R.id.text2);
        t1.setText(items.get(i).getTitle());
        t2.setText(items.get(i).getTime());
        return v;
    }
}