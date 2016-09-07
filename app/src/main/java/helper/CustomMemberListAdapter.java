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
import models.User;

/**
 * Created by Apoorva on 12/13/2015.
 */
public class CustomMemberListAdapter extends ArrayAdapter<User> {

    private final Activity context;
    private ArrayList<User> users;

    public CustomMemberListAdapter(Activity context, ArrayList<User> userList) {
        super(context, R.layout.custom_list_item, userList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.users = userList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);


        txtTitle.setText(users.get(position).getUserName()+" , "+users.get(position).getUserGroup());
        imageView.setImageResource(R.drawable.profile_icon);

        return rowView;

    };

}
