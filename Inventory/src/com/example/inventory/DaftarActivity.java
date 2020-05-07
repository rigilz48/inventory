package com.example.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DaftarActivity extends Activity {
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	EditText xnamalengkap, xusername, xpassword;
	Button btndaftar, btnmasuk;
	
	private static final String TAG_SUCCESS = "success";
	private static String url_cek_daftar ="https://luckytruedev.com/gudang/reg_account.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daftar);
		
		xnamalengkap = (EditText)findViewById(R.id.txtnamalengkap);
		xusername = (EditText)findViewById(R.id.txtusername);
		xpassword = (EditText)findViewById(R.id.txtpassword);
		btndaftar = (Button)findViewById(R.id.btndaftar);
		btnmasuk = (Button)findViewById(R.id.btnmasuk);
		
		btndaftar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// login inventory in background thread
				new DaftarAkunInventory().execute();
			}
		});
		
		btnmasuk.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent myintent = new Intent(btnmasuk.getContext(),MasukActivity.class);
        		startActivityForResult(myintent, 0);
        		// TODO Auto-generated method stub
        	}
        });
	}
	
	class DaftarAkunInventory extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DaftarActivity.this);
			pDialog.setMessage("Mendaftar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args0) {
			String namalengkap = xnamalengkap.getText().toString();
			String username = xusername.getText().toString();
			String password = xpassword.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("namalengkap", namalengkap));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			JSONObject json = jsonParser.makeHttpRequest(url_cek_daftar, "POST", params);
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success==1) {
					Intent i = new Intent(getApplicationContext(), MasukActivity.class);
					startActivity(i);
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getBaseContext(), "Berhasil Daftar Akun",Toast.LENGTH_SHORT).show();
						}
					});
					finish();
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getBaseContext(), "Gagal Daftar Akun",Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				//TODO: handle exception
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.daftar, menu);
		return true;
	}

}
