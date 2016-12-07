package com.encuestas.popresearch.popresearchencuestas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import DB.Dao;
import Entity.TipoEncuestaEntity;

/**
 * Created by Admin on 29/09/2015.
 */
public class TipoEncuesta extends AppCompatActivity {

    ListView lstOpciones;
    ArrayAdapter<String> adaptador;
    String cliente;
    String mobile;
    String id_proyecto;
    Bundle bundle;

    List<String> id_ArchivoSeleccionadoIdEncuestaList;
    String tipoEncuestaSeleccionada;
    // static String id_encuesta;   //funciona el enviar con este campo
    String id_ArchivoSeleccionado;
    String id_encuestaSeleccionada;
    Dao db;
    List<TipoEncuestaEntity> tipoEncuestaList;
    ArrayList<String> arrayTipoEncuestas;
    TipoEncuestaEntity tipoEncuestaEntity ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipoencuesta);

        Bundle extras = getIntent().getExtras();
        bundle = new Bundle();
        db = new Dao(this);
        id_ArchivoSeleccionadoIdEncuestaList = new ArrayList<String>();

        mobile = extras.getString("telefono");
        mobile = mobile.trim();
        cliente = extras.getString("cliente");
        id_proyecto = extras.getString("id_proyecto");
        tipoEncuestaList = new ArrayList<>();

        try {
            tipoEncuestaList = db.getTipoEncuesta(mobile, cliente, id_proyecto);
            arrayTipoEncuestas = new ArrayList<String>();

            for (int i = 0; i < tipoEncuestaList.size(); i++) {
                tipoEncuestaEntity = new TipoEncuestaEntity();
                tipoEncuestaEntity = tipoEncuestaList.get(i);
                arrayTipoEncuestas.add(tipoEncuestaEntity.getNombreEncuesta());
            }
            adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayTipoEncuestas){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view  = super.getView(position,convertView,parent);
                    TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                    return  view;
                }
            };
            lstOpciones = (ListView) findViewById(R.id.LstOpciones);
            lstOpciones.setAdapter(adaptador);
            //Action when a tipoEncuesta is selected
            lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object obj = lstOpciones.getAdapter().getItem(position);
                    tipoEncuestaSeleccionada = obj.toString();

                    try {
                        id_ArchivoSeleccionadoIdEncuestaList = db.getIdArchivoIdEncuesta(tipoEncuestaSeleccionada);
                        id_ArchivoSeleccionado = id_ArchivoSeleccionadoIdEncuestaList.get(0);
                        id_encuestaSeleccionada = id_ArchivoSeleccionadoIdEncuestaList.get(1);
                        bundle.putString("id_ArchivoSeleccionado", id_ArchivoSeleccionado);
                        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);
                        bundle.putString("mobile", mobile);
                        Intent i = new Intent(TipoEncuesta.this, HiScreen.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtras(bundle);
                        startActivity(i);

                    } catch (Exception e) {
                        e.getCause();
                    }


                }
            });
        } catch (Exception e) {
            e.getCause();

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
