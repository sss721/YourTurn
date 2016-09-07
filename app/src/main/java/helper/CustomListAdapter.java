package helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourturnmobileapp.R;

import java.util.ArrayList;

import models.Task;

/**
 * Created by Apoorva on 12/11/2015.
 */
public class CustomListAdapter extends ArrayAdapter<Task> {

    private final Activity context;
    private ArrayList<Task> tasks;

    public CustomListAdapter(Activity context, ArrayList<Task> taskList) {
        super(context, R.layout.custom_list_item, taskList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.tasks = taskList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        //TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

        txtTitle.setText(tasks.get(position).getTaskName());
        imageView.setImageResource(R.drawable.profile_icon);
        //extratxt.setText("Description "+itemname[position]);
        return rowView;

    };

}
