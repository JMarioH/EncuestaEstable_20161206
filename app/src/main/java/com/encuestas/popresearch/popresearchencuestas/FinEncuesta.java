package com.encuestas.popresearch.popresearchencuestas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import DB.Dao;

/**
 * Created by Admin on 29/09/2015.
 * solo sirve para conectar entre el fin del cuestionario y el envio de datos al servidor
 */
public class FinEncuesta extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    Bundle bundle =  new Bundle();
    String id_ArchivoSeleccionado;
    String id_encuestaSeleccionada;
    String id_tiendaSeleccionada;
    String mobile;
    Button btonAceptar;
    String id_preguntaAnterior;
    //DB
    Dao db;
    String siguienteencuesta = "0";
    boolean banderaRegresar = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finencuesta);

        db = new Dao(this);
        Bundle extras = getIntent().getExtras();

        id_ArchivoSeleccionado = extras.getString("id_archivoSeleccionado");
        bundle.putString("id_archivoSeleccionado", id_ArchivoSeleccionado);
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);
        id_tiendaSeleccionada = extras.getString("id_tiendaSeleccionada");
        bundle.putString("id_tiendaSeleccionada", id_tiendaSeleccionada);
        mobile = extras.getString("mobile");
        bundle.putString("mobile", mobile);


        btonAceptar = (Button) findViewById(R.id.ButtonFinalizar);
        btonAceptar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){


                siguienteencuesta = "0";
                bundle.putString("siguienteencuesta", siguienteencuesta);
                banderaRegresar = false;
                //Cambia la bandera de las encuestas realizadas
                db.putCheckEncuesta(id_tiendaSeleccionada);
                Intent i = new Intent(FinEncuesta.this, Enviar.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Bundle extras = getIntent().getExtras();
            id_preguntaAnterior = extras.getString("id_preguntaAnterior");
        }
        super.onKeyDown(keyCode, event);

        if(banderaRegresar){
            return true;
        }
        else{
            return false;
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
            //Display Toast
            Intent intent = new Intent(this, Principal2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}
