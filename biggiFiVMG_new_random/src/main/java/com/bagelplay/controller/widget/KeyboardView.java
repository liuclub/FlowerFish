package com.bagelplay.controller.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;


import com.bagelplay.controller.com.R;
import com.bagelplay.controller.utils.BagelPlayUtil;
import com.bagelplay.controller.utils.BagelPlayVmaStub;
import com.bagelplay.controller.utils.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class KeyboardView extends LinearLayout {

    private EditText mEditText;

    private Button mSendBtn;

    private Button mDoneBtn;

    private TextView mTextView;

    private String content;

    private int length;


    private Context context;

    private BagelPlayVmaStub bfVmaStub;


    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        View view = View.inflate(context, R.layout.keyboard, this);

        mEditText = (EditText) findViewById(R.id.input_method);
        mSendBtn = (Button) findViewById(R.id.Send);
        mDoneBtn = (Button) findViewById(R.id.Done);
        mTextView = (TextView) findViewById(R.id.show_maxbyte);

        mEditText.addTextChangedListener(mEditTextListener);
        mEditText.setOnKeyListener(mEditTextKeyListener);

        mSendBtn.setOnClickListener(mSendBtnListener);
        mDoneBtn.setOnClickListener(mDoneBtnListener);

        bfVmaStub = BagelPlayVmaStub.getInstance();

    }


    /**
     * add the mEditText change listener.
     */
    private TextWatcher mEditTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            content = mEditText.getText().toString();
            length = content.length();
            mTextView.setText((140 - length) + " " + "words...");
        }
    };

    private OnKeyListener mEditTextKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                Log.v(BagelPlayUtil.TAG, "Del up KeyEvent");
                if (mEditText.getText().toString().equals("")) {

                }

            }

            return false;
        }
    };

    private OnClickListener mSendBtnListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String editData = mEditText.getText().toString();
            if (editData != null && !editData.equals("")) {
                sendString(editData);
                mEditText.setText(null);
            }
        }
    };

    private OnClickListener mDoneBtnListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mEditText.setText(null);
            KeyboardView.this.setVisibility(View.GONE);
        }
    };

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visibility == View.VISIBLE) {
            mEditText.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }


    }

    ;

    private void sendString(String editData) {
        Log.d(BagelPlayUtil.TAG, "mEditText.getText().toString()" + editData);
        bfVmaStub.sendInputTextData(editData);
        mEditText.setText(null);
        length = 0;
        mTextView.setText((140 - length) + " " + "words...");
    }


}
