package com.demo.cworker.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.demo.cworker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.cworker.Common.Constants.INTENT_KEY;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.save)
    Button save;

    public static void startActivityForResult(Activity context, String s) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra(INTENT_KEY, s);
        context.startActivityForResult(intent, PersonalActivity.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        if(getIntent().getStringExtra(INTENT_KEY) != null){
            address.setText(getIntent().getStringExtra(INTENT_KEY));
        }

        save.setOnClickListener(v -> {

        });
    }
}
