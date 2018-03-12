package com.kuku.kkmerchant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hg.ftas.activity.ftas.ftasview.SimpleFtasMobileActivity;
import com.hg.ftas.util.OpenPageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.ftas.dunit.annotation.DUnit;

@DUnit
public class MainActivity extends SimpleFtasMobileActivity {
	@BindView(R.id.urlEdit)
	EditText urlEdit;
	@BindView(R.id.loginBtn)
	Button loginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.loginBtn)
	public void onClickLoginBtn(View v){
		String url = urlEdit.getText().toString();
		OpenPageUtil.startUrl(url,this);
	}
}
