package com.example.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.inventory.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BarangActivity extends ListActivity {
	
	private ProgressDialog pDialog;
	
	JSONParser jParser = new JSONParser();
	
	ArrayList<HashMap<String, String>> BarangList;
	
	private static String url_all_barang ="https://luckytruedev.com/gudang/barang.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BARANG = "barang";
	private static final String TAG_IDBARANG = "id_barang";
	private static final String TAG_NAMABARANG = "nama_barang";
	
	JSONArray barang = null;
	Button btnkembali;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barang);
		
		BarangList = new ArrayList<HashMap<String, String>>();
		
		new LoadAllBarang().execute();
		
		btnkembali = (Button) findViewById(R.id.btnkembalib);
		
		btnkembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});
		
		ListView lv = getListView();;
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String id_barang = ((TextView) view.findViewById(R.id.id_barang)).getText().toString();
				Intent in = new Intent(getApplicationContext(),UbahBarangActivity.class);
				in.putExtra(TAG_IDBARANG, id_barang);
				startActivityForResult(in, 100);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}
	
	class LoadAllBarang extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(BarangActivity.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(url_all_barang, "GET", params);
			Log.d("All Barang: ", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					barang = json.getJSONArray(TAG_BARANG);
					for (int i = 0; i < barang.length(); i++) {
						JSONObject c = barang.getJSONObject(i);
						String id_barang = c.getString(TAG_IDBARANG);
						String nama_barang = c.getString(TAG_NAMABARANG);
						HashMap<String, String> map = new HashMap<String,String>();
						map.put(TAG_IDBARANG, id_barang);
						map.put(TAG_NAMABARANG, nama_barang);
						BarangList.add(map);
					}
				} else {
					Intent i = new Intent(getApplicationContext(),TambahBarangActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			}  catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					ListAdapter adapter = new SimpleAdapter( BarangActivity.this, BarangList, R.layout.activity_list_item, new String[] { TAG_IDBARANG,TAG_NAMABARANG},new int[] { R.id.id_barang, R.id.nama_barang });
					setListAdapter(adapter);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barang, menu);
		return true;
	}

}
