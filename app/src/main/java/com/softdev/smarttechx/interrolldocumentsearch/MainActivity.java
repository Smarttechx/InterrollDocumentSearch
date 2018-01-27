package com.softdev.smarttechx.interrolldocumentsearch;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softdev.smarttechx.interrolldocumentsearch.adapter.DataAdapter;
import com.softdev.smarttechx.interrolldocumentsearch.model.InterrollDoc;
import com.softdev.smarttechx.interrolldocumentsearch.utils.ExcelToJsonConverter;
import com.softdev.smarttechx.interrolldocumentsearch.utils.ExcelToJsonConverterConfig;
import com.softdev.smarttechx.interrolldocumentsearch.utils.ExcelWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mLogo;
    private SearchView searchPart;
    private DataAdapter adapter;
    private RecyclerView recList;
    LinearLayoutManager llm;
    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask doAsynchronousTask;
    FirebaseStorage storage;
    StorageReference gsReference;
    int i;
    public ArrayList<InterrollDoc> interroll_Docu;
    boolean boolean_permission;
    public static int REQUEST_PERMISSIONS = 1;
    private CoordinatorLayout mRoot;
    String path = "/sdcard/" + "Interroll";
    File folder;
    String excelPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLogo=(LinearLayout)findViewById(R.id.logoLayout);
        searchPart =(SearchView) findViewById(R.id.searchPart);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        searchPart.setQueryHint("Search part number");
        mRoot=(CoordinatorLayout)findViewById(R.id.parent);
        storage = FirebaseStorage.getInstance();
         folder = new File(path);
        if(!folder.exists())
        {
            folder.mkdirs();
        }


        fn_permission();
        getDocument();
        adapter = new DataAdapter(this, interroll_Docu);
        recList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        searchPart.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mLogo.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE

                adapter.getFilter().filter(query);
                return false;
            }
        });
       searchPart.setOnCloseListener(new SearchView.OnCloseListener() {
           @Override
           public boolean onClose() {
               mLogo.setVisibility(View.VISIBLE);
               return false;
           }
       });
        searchPart.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogo.setVisibility(View.GONE);
            }
        });

    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(),android. Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

        } else {
            boolean_permission = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
            } else {
                Snackbar snackbar = Snackbar.make(mRoot,  "Please allow the permission", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDocument(){
       gsReference = storage.getReferenceFromUrl("gs://interoll-document-search.appspot.com/PartDescLink.xlsx");

        try{
            final File localFile = File.createTempFile("PartDescLink", ".xlsx");
            gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    excelPath=  localFile.getAbsolutePath();
                    String jsonOut= excel2json(excelPath);
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, jsonOut, Toast.LENGTH_LONG).show();

                    Snackbar snackbar = Snackbar.make(mRoot,  "Loading part links", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Snackbar snackbar = Snackbar.make(mRoot,  "Error occur try again!!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // Handle any errors
                }
            });


        }catch (Exception e){

        }
    }
    public String excel2json(String args){
        String json=null;
        try{
            ExcelToJsonConverterConfig config = ExcelToJsonConverterConfig.create(args);
            ///String valid = config.valid();
            ExcelWorkbook book = ExcelToJsonConverter.convert(config);
            json = book.toJson(config.isPretty());
        }catch (Exception e){
            json=e.getMessage();
        }
        return json;
    }
}
