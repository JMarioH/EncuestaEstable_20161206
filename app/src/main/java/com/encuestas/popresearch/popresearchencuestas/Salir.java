package com.encuestas.popresearch.popresearchencuestas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import DB.Dao;

/**
 * Created by Admin on 29/09/2015.
 */
public class Salir extends AppCompatActivity {
    Bundle bundle = new Bundle();
    String id_ArchivoSeleccionado;
    String id_encuestaSeleccionada;
    Dao db;
    String mobile;
    String siguienteencuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salir);
        db = new Dao(this);
        Bundle extras = getIntent().getExtras();
        id_ArchivoSeleccionado = extras.getString("id_archivoSeleccionado");
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        TextView lblMensaje ;
        lblMensaje = (TextView)findViewById(R.id.LblMensaje);
        bundle.putString("id_encuestaSeleccionada",id_encuestaSeleccionada);
        bundle.putString("id_ArchivoSeleccionado",id_ArchivoSeleccionado);

        mobile = extras.getString("mobile");
        bundle.putString("mobile", mobile);

        lblMensaje.setEllipsize(TextUtils.TruncateAt.END);
        lblMensaje.setSingleLine(true);
        lblMensaje.setText("Encuesta Terminada exitosamente !!!");

        siguienteencuesta = extras.getString("siguienteencuesta");
        if(siguienteencuesta.equals("1")){
            Intent i = new Intent(Salir.this, HiScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.putExtras(bundle);
            startActivity(i);
        }else {
            Intent i = new Intent(Salir.this, Principal2.class);
            startActivity(i);
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
