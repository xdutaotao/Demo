package com.yankon.smart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yankon.smart.BaseActivity;
import com.yankon.smart.R;

public class RepeatDaysActivity extends BaseActivity {

    boolean repeatDays[];
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_days);
        initActivityUI();
        mList = (ListView) findViewById(android.R.id.list);
        repeatDays = getIntent().getBooleanArrayExtra("days");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice);
        String[] dayNames = getResources().getStringArray(R.array.days);
        adapter.addAll(dayNames);
        mList.setAdapter(adapter);
        mList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        if (repeatDays != null) {
            for (int i = 0; i < 7 && i < repeatDays.length; i++) {
                mList.setItemChecked(i, repeatDays[i]);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repeat_days, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_done: {
                if (repeatDays == null) {
                    repeatDays = new boolean[7];
                }
                for (int i = 0; i < 7; i++) {
                    repeatDays[i] = mList.isItemChecked(i);
                }
                Intent intent = new Intent();
                intent.putExtra("days", repeatDays);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
