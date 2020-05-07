package com.example.inventory;

import android.os.Bundle;

import com.example.inventory.R;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView nama_login;
	
	Button btnbarang, btnkeluar;
	
	SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnbarang = (Button)findViewById(R.id.btnbarang);
        btnkeluar = (Button)findViewById(R.id.btnkeluar);
        
        sharedPrefManager = new SharedPrefManager(this);
        
        nama_login = (TextView)findViewById(R.id.txtviewnama);
        nama_login.setText(sharedPrefManager.getSPUsername());
        
        btnbarang.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		Intent myintent = new Intent(btnbarang.getContext(),BarangActivity.class);
        		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivityForResult(myintent, 0);
        		// TODO Auto-generated method stub
        	}
        });
        
        btnkeluar.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        		sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(MainActivity.this, MasukActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
        	}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
