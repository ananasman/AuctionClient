package com.geek.auctionclient.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.geek.auctionclient.R;
import com.geek.auctionclient.util.DialogUtil;
import com.geek.auctionclient.util.HttpUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
	EditText email, password;
	Button email_sign_in_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		email_sign_in_button = (Button) findViewById(R.id.email_sign_in_button);
		email_sign_in_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ִ������У��
				if (validate()) {
					// ��֤��¼
					if (loginPro()) {
						Intent intent = new Intent();
						startActivity(intent);
						finish();
					} else {
						DialogUtil.showDialog(LoginActivity.this, "�ʺŻ������д�",
								false);
					}
				}
			}
		});
	}

	private boolean loginPro() {
		String username = email.getText().toString();
		String userpsd = password.getText().toString();
		JSONObject jsonObj;
		try {
			jsonObj = query(username, userpsd);
			if (jsonObj.getInt("userid") > 0) {
				return true;
			}
		} catch (Exception e) {
			DialogUtil.showDialog(this, "��������Ӧ�쳣��", false);
			e.printStackTrace();
		}
		return false;
	}

	// �û���������֤�ǿ�
	private boolean validate() {
		String username = email.getText().toString().trim();
		if (username.equals("")) {
			DialogUtil.showDialog(this, "�û�������Ϊ��", false);
			return false;
		}
		String userpsd = password.getText().toString();
		if (userpsd.equals("")) {
			DialogUtil.showDialog(this, "���벻��Ϊ��", false);
			return false;
		}
		return true;
	}

	private JSONObject query(String username, String password) throws Exception {
		// ʹ��map��װ�������
		Map<String, String> map = new HashMap<String, String>();
		map.put("user", username);
		map.put("pass", password);
		String url = HttpUtil.BASE_URL + "login.jsp";
		// ��������
		return new JSONObject(HttpUtil.postRequest(url, map));
	}
}
