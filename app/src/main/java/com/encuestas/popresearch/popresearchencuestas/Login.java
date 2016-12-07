package com.encuestas.popresearch.popresearchencuestas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import static com.encuestas.popresearch.popresearchencuestas.HiScreen.db;

/**
 * Created by Admin on 28/09/2015.
 */
public class Login extends AppCompatActivity {
    String TAG = getClass().getName();
    ProgressDialog prgDialog;
    RequestParams params = new RequestParams();
    EditText user;
    EditText pass;
    public static String usuario2;
    //String usuario;
    String password;
    int logstatus = -1;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);

    }

    public void login(View v) {
        prgDialog.setMessage("Conectandose al servidor");
        prgDialog.show();
        user = (EditText) findViewById(R.id.edusuario);
        pass = (EditText) findViewById(R.id.edpassword);
        usuario2 = user.getText().toString();
        password = pass.getText().toString();
        LoginServer();
    }

    public void LoginServer() {
        params.put("usuario", usuario2);
        params.put("password", password);
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://popresearch8.cloudapp.net/a/acces2.php";
        client.post(url, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    JSONArray jdata = new JSONArray(response);

                    JSONObject json_data;
                    json_data = jdata.getJSONObject(0);

                    logstatus = json_data.getInt("logstatus");
                    if (logstatus == 0) {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Login NO valido",
                                Toast.LENGTH_LONG).show();
                    } else {
                        prgDialog.hide();
                        Toast.makeText(getApplicationContext(), "Login Exitoso",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, Principal2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Ocurrio Error [La respuesta JSON del server es invalida]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "No se encuentra el recurso ", Toast.LENGTH_LONG).show();
                }else if (statusCode == 500) {// When Http response code is '500'
                    Toast.makeText(getApplicationContext(), "Hay un problema con el servidor", Toast.LENGTH_LONG).show();
                }else { // When Http response code other than 404, 500
                    Toast.makeText(getApplicationContext(), "ERROR en la conexión [Lo mas probable es que no tengas conexión a Internet]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, Principal2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
