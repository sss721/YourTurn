package com.example.yourturnmobileapp;
import com.example.yourturnmobileapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateSupplyList extends Activity
{
	ArrayList<String> arrList= new ArrayList<>();
	ArrayAdapter<String> adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_supply);
		final EditText editText = (EditText) findViewById(R.id.addItem);
		final ListView listView = (ListView)findViewById(R.id.list);
		adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
		ImageButton button1 = (ImageButton) findViewById(R.id.add);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = editText.getText().toString();
				if (str.matches("")) {
					Toast.makeText(getBaseContext(), "Enter an Item", Toast.LENGTH_LONG).show();
				} else {
					arrList.add(str);
					adapter.add(str);
					editText.setText("");
				}
			}
		});

		listView.setAdapter(adapter);
		ImageButton buy = (ImageButton)findViewById(R.id.buy);
		buy.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) {
				if (adapter.getCount() == 0) {
					Toast.makeText(getBaseContext(), "NO ITEM LISTED", Toast.LENGTH_LONG).show();
				} else {
					final ArrayList<Integer> arrList1 = new ArrayList<>();
					String[] a = new String[arrList.size()];

					for (int i = 0; i < arrList.size(); i++) {
						a[i] = new String();
						a[i] = arrList.get(i);
						Log.i("arrList", arrList.get(i));
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(CreateSupplyList.this)
							.setTitle("Items Bought").setMultiChoiceItems(a, null, new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
									if (isChecked) {
										arrList1.add(which);
										adapter.remove(adapter.getItem(which));
									} else if (arrList1.contains(which)) {

										arrList1.remove(Integer.valueOf(which));

									}
								}

							})
							.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int id) {
									for (int i = arrList1.size() - 1; i >= 0; i--) {
										int temp = arrList1.get(i);
										arrList.remove(temp);
									}

									adapter.notifyDataSetChanged();
								}
							});

					builder.show();
				}
			}


		});

	}


}
