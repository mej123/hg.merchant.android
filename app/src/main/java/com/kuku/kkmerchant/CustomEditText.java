package com.kuku.kkmerchant;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hg.ftas.util.ToastUtil;

/**
 * Created by tik on 17/7/15.
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText{

	TextView errorView;
	CustomEditTextListener mCustomEditTextListener;

	public CustomEditText(Context context) {
		super(context);

	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void init(CustomEditTextListener customEditTextListener) {
		init(null, customEditTextListener);
	}


	public void init(TextView errorView,CustomEditTextListener customEditTextListener) {
		this.errorView = errorView;
		if (errorView != null){
			errorView.setVisibility(View.GONE);
		}
		this.mCustomEditTextListener = customEditTextListener;

		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mCustomEditTextListener != null) {
					mCustomEditTextListener.afterTextChanged(CustomEditText.this, s.toString());
				}
			}
		});
	}

	public interface CustomEditTextListener {
		void afterTextChanged(CustomEditText customEditText, String val);
	}

	// 设置错误
	public void setError(final String msg) {
		if (errorView != null){
			errorView.setVisibility(View.VISIBLE);
			errorView.setText(msg);
		}else {
			ToastUtil.show(msg);
		}
	}

	// 设置成功
	public void setSuccess() {
		if (errorView != null){
			errorView.setVisibility(View.GONE);
		}
	}
}
