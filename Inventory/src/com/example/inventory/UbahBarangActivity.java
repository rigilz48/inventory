package com.example.inventory;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

public class UbahBarangActivity extends Activity {
	
	EditText txtnamabarang, txtjumlahbarang,txtdeskripsi;
	Button btnhapus, btnsimpan, btnkembali;
	
	String id_barang;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String url_barang_detials ="https://luckytruedev.com/gudang/detail_barang.php";
	private static final String url_update_barang ="https://luckytruedev.com/gudang/update_barang.php";
	private static final String url_delete_barang ="https://luckytruedev.com/gudang/delete_barang.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BARANG = "barang";
	private static final String TAG_IDBARANG = "id_barang";
	private static final String TAG_NAMABARANG = "nama_barang";
	private static final String TAG_JUMLAHBARANG = "jumlah_barang";
	private static final String TAG_DESKRIPSI = "deskripsi";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubah_barang);
		
		btnhapus = (Button)findViewById(R.id.btnhapusub);
		btnsimpan = (Button)findViewById(R.id.btnsimpanub);
		btnkembali = (Button)findViewById(R.id.btnkembaliub);
		
		Intent i = getIntent();
		
		id_barang = i.getStringExtra(TAG_IDBARANG);
		
		btnhapus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new DeleteBarang().execute();
			}
		});
		
		btnsimpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new SaveBarangDetails().execute();
			}
		});
		
		btnkembali.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent myintent = new Intent(btnkembali.getContext(),BarangActivity.class);
        		startActivityForResult(myintent, 0);
        		// TODO Auto-generated method stub
        	}
        });
	}
	
	class SaveBarangDetails extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UbahBarangActivity.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			String nama_barang = txtnamabarang.getText().toString();
			String jumlah_barang = txtjumlahbarang.getText().toString();
			String deskripsi = txtdeskripsi.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_IDBARANG, id_barang));
			params.add(new BasicNameValuePair(TAG_NAMABARANG, nama_barang));
			params.add(new BasicNameValuePair(TAG_JUMLAHBARANG, jumlah_barang));
			params.add(new BasicNameValuePair(TAG_DESKRIPSI, deskripsi));
			JSONObject json = jsonParser.makeHttpRequest(url_update_barang,"POST", params);
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
				} else {
					
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
	
	class DeleteBarang extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UbahBarangActivity.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id_barang", id_barang));
				JSONObject json = jsonParser.makeHttpRequest(url_delete_barang, "POST", params);
				Log.d("Delete Product", json.toString());
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
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
		getMenuInflater().inflate(R.menu.ubah_barang, menu);
		return true;
	}

}
