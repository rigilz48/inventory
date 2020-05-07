package com.example.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MasukActivity extends Activity {
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	EditText xusername, xpassword;
	TextView daftar;
	Button btnmasuk, btntutup;
	
	private static final String TAG_SUCCESS = "success";
	private static String url_cek_login ="https://luckytruedev.com/gudang/cek_login.php";
	
	SharedPrefManager sharedPrefManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_masuk);
		
		xusername = (EditText)findViewById(R.id.txtusername);
		xpassword = (EditText)findViewById(R.id.txtpassword);
		daftar = (TextView)findViewById(R.id.txtviewdaftar);
		btnmasuk = (Button)findViewById(R.id.btnmasuk);
		btntutup = (Button)findViewById(R.id.btntutup);
		
		sharedPrefManager = new SharedPrefManager(this);
		
		if (sharedPrefManager.getSPSudahLogin()){
			startActivity(new Intent(MasukActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
			finish();
		}
		
		daftar.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent myintent = new Intent(daftar.getContext(),DaftarActivity.class);
        		startActivityForResult(myintent, 0);
        		// TODO Auto-generated method stub
        	}
        });
		
		btnmasuk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// login inventory in background thread
				new MasukAkunInventory().execute();
			}
		});
		
		btntutup.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		AlertDialog.Builder builder = new AlertDialog.Builder(MasukActivity.this);
        		builder.setMessage("Apakah Anda ingin keluar ?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface arg0, int arg1) {
        				MasukActivity.this.finish();
        				// TODO Auto-generated method stub
        			}
        		}).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
        			@Override
        			public void onClick(DialogInterface arg0, int arg1) {
        				arg0.cancel();
        				// TODO Auto-generated method stub
        			}
        		}).show();
        	}
        });
	}
	
	class MasukAkunInventory extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MasukActivity.this);
			pDialog.setMessage("Menunggu Masuk...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args0) {
			String username = xusername.getText().toString();
			String password = xpassword.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			// getting JSON Object
			// Note that login inventory url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_cek_login, "POST", params);
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success==1) {
					// successfully login inventory
					sharedPrefManager.saveSPString(SharedPrefManager.SP_USERNAME, username);
					sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					// closing this screen
					finish();
				} else {
					// failed to login inventory
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getBaseContext(), "Username / Password Salah",Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.masuk, menu);
		return true;
	}

}
