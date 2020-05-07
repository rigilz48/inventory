package com.example.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import com.example.inventory.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TambahBarangActivity extends Activity {
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	EditText inputNamaBarang, inputJumlahBarang, inputDeskripsi;
	Button btnkembali;
	
	private static String url_create_barang ="https://luckytruedev.com/gudang/create_barang.php";

	private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tambah_barang);
		
		inputNamaBarang = (EditText) findViewById(R.id.txtnamabarangt);
		inputJumlahBarang = (EditText) findViewById(R.id.txtjumlahbarangt);
		inputDeskripsi = (EditText) findViewById(R.id.txtdeskripsit);
		
		Button btnsimpan = (Button)findViewById(R.id.btnsimpant);
		btnkembali = (Button)findViewById(R.id.btnkembalit);
		
		btnkembali.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent myintent = new Intent(btnkembali.getContext(),BarangActivity.class);
        		startActivityForResult(myintent, 0);
        		// TODO Auto-generated method stub
        	}
        });
		
		btnsimpan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				new CreateNewBarang().execute();
			}
		});
	}
	
	class CreateNewBarang extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TambahBarangActivity.this);
			pDialog.setMessage("Tambah Barang...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			String nama_barang = inputNamaBarang.getText().toString();
			String jumlah_barang = inputJumlahBarang.getText().toString();
			String deskripsi = inputDeskripsi.getText().toString();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("nama_barang", nama_barang));
			params.add(new BasicNameValuePair("jumlah_barang", jumlah_barang));
			params.add(new BasicNameValuePair("deskripsi", deskripsi));
			
			JSONObject json = jsonParser.makeHttpRequest(url_create_barang,"POST", params);
			
			Log.d("Create Response", json.toString());
			
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = new Intent(getApplicationContext(), BarangActivity.class);
					startActivity(i);
					finish();
				} else {
					//FAILED
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tambah_barang, menu);
		return true;
	}

}
