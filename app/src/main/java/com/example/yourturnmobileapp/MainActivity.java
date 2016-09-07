package com.example.yourturnmobileapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.yourturnmobileapp.R;

public class MainActivity extends Activity implements View.OnClickListener {

	ImageButton ImageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageButton = (ImageButton) findViewById(R.id.button);
		ImageButton.setOnClickListener(this);

		ImageButton = (ImageButton) findViewById(R.id.addmember);
		ImageButton.setOnClickListener(this);

		ImageButton = (ImageButton) findViewById(R.id.viewTasks);
		ImageButton.setOnClickListener(this);

		ImageButton = (ImageButton) findViewById(R.id.supplies);
		ImageButton.setOnClickListener(this);

		ImageButton = (ImageButton) findViewById(R.id.viewMembers);
		ImageButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button:
				startActivity((new Intent(this, AddTaskActivity.class)));

				break;
			case R.id.addmember:
				startActivity(new Intent(this, AddMemberActivity.class));

				break;

			case R.id.viewTasks:
				startActivity(new Intent(this, ViewTaskActivity.class));

				break;

			case R.id.viewMembers:
				startActivity(new Intent(this, ViewMembersActivity.class));

				break;
			case R.id.supplies:
				startActivity(new Intent(this, CreateSupplyList.class));

				break;

		}
	}

}
