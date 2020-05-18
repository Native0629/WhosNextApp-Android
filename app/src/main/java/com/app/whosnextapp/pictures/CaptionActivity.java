package com.app.whosnextapp.pictures;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;

import com.app.whosnextapp.R;
import com.app.whosnextapp.utility.BaseAppCompatActivity;
import com.app.whosnextapp.utility.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptionActivity extends BaseAppCompatActivity {
    @BindView(R.id.et_caption)
    AppCompatEditText et_caption;

    String caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            caption = getIntent().getStringExtra(Constants.WN_SHARED_VIDEO_CAPTION);
            et_caption.setText(caption);
            et_caption.setSelection(et_caption.getText().length());
        }
    }

    @OnClick(R.id.tv_done)
    public void onDoneClick() {
        caption = et_caption.getText().toString();
        if (!caption.isEmpty()) {
            Intent i = new Intent();
            i.putExtra(Constants.WN_SHARED_VIDEO_CAPTION, caption);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
