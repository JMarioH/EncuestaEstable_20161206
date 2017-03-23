package com.encuestas.popresearch.popresearchencuestas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import AsynckData.Conexiones;
import AsynckData.ServiceHandler;
import DB.Dao;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by jesus.hernandez on 07/12/16.
 * Clase para logear el usuario
 */

public class LoginUser extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    ProgressDialog pDialog;
    EditText editUser , editPassword;
    public String  password;
    public static String usuario;
    Bundle bundle;
    String URL;
    ArrayList<NameValuePair> data;
    Conexiones conexiones;
    Dao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bundle = new Bundle();
        dao = new Dao(this);
        conexiones = new Conexiones();
        URL = conexiones.getIP_ServerLogin();

        editUser = (EditText) findViewById(R.id.edusuario);
        editPassword = (EditText) findViewById(R.id.edpassword);
    }
    public void login(View v){
        usuario = editUser.getText().toString();
        password = editPassword.getText().toString();
        new LoginAsynck().execute();
    }
    public class LoginAsynck extends AsyncTask<String,String,String> {
        String success= "0";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginUser.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Registrado usuario");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            pDialog.dismiss();
            pDialog.hide();
            if(res.equals("1")){

                Toast.makeText(getApplicationContext(), "Login Exitoso",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginUser.this, Principal2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtras(bundle);
                startActivity(intent);

            }else{
                Toast.makeText(LoginUser.this,"Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            data = new ArrayList<>();
            data.add(new BasicNameValuePair("f", "login"));
            data.add(new BasicNameValuePair("usuario", usuario));
            data.add(new BasicNameValuePair("password", password));

            try{
                ServiceHandler jsonParser = new ServiceHandler();
                String jsonRes = jsonParser.makeServiceCall(URL, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(jsonRes);
                JSONObject result = jsonObject.getJSONObject("result");
                success = result.getString("logstatus");

                return success;
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }
    }
    @Override
    public
    boolean onCreateOptionsMenu(Menu menu) {
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
