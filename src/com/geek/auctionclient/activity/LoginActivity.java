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
				// 执行输入校验
				if (validate()) {
					// 验证登录
					if (loginPro()) {
						Intent intent = new Intent();
						startActivity(intent);
						finish();
					} else {
						DialogUtil.showDialog(LoginActivity.this, "帐号或密码有错",
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
			DialogUtil.showDialog(this, "服务器响应异常。", false);
			e.printStackTrace();
		}
		return false;
	}

	// 用户名密码验证非空
	private boolean validate() {
		String username = email.getText().toString().trim();
		if (username.equals("")) {
			DialogUtil.showDialog(this, "用户名不能为空", false);
			return false;
		}
		String userpsd = password.getText().toString();
		if (userpsd.equals("")) {
			DialogUtil.showDialog(this, "密码不能为空", false);
			return false;
		}
		return true;
	}

	private JSONObject query(String username, String password) throws Exception {
		// 使用map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("user", username);
		map.put("pass", password);
		String url = HttpUtil.BASE_URL + "login.jsp";
		// 发送请求
		return new JSONObject(HttpUtil.postRequest(url, map));
	}
}
