package com.example.android.testingfun.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.electriccarstationsapp.R;
import com.example.electriccarstationsapp.ui.MainActivity;

public class MyFirstTestActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mFirstTestActivity;
    private TextView mFirstTestText;

    public MyFirstTestActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
        mFirstTestText =
                (TextView) mFirstTestActivity
                .findViewById(R.id.textView1);
    }
	
}
