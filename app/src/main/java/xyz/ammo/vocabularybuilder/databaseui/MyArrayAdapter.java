package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xyz.ammo.vocabularybuilder.R;

class MyArrayAdapter extends ArrayAdapter<String> {

    static class ViewHolder {
        TextView wordView;
        TextView typeView;
    }

    MyArrayAdapter(Context context, int resource, List<String> object) {
        super(context, resource, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = ((LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.wordtype_spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.wordView = convertView.findViewById(R.id.spinner_word);
            viewHolder.typeView = convertView.findViewById(R.id.spinner_type);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String item = getItem(position);
        if(position == 0) {
            viewHolder.wordView.setText(item);
            viewHolder.typeView.setText("");
        }
        else {
            int m = item.lastIndexOf("(");
            String word = item.substring(0, m);
            String type = item.substring(m, item.length());
            viewHolder.wordView.setText(word);
            viewHolder.typeView.setText(type);
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}
