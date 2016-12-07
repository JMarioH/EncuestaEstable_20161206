package com.encuestas.popresearch.popresearchencuestas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import Entity.Proyecto;

/**
 * Created by Admin on 29/09/2015.
 */
public class Proyectos extends AppCompatActivity {
    public String TAG =getClass().getName();
    Bundle bundle;
    Dao db;
    public ListView lstOpciones ;
    public ArrayAdapter<String> adaptador;
    public String cliente;
    public String telefono;
    public List<Proyecto> projectsList;
    public ArrayList<String> arrayProjects;
    public Proyecto proyecto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);
        bundle = new Bundle();
        //instance the DB
        db = new Dao(this);

        Bundle extras = getIntent().getExtras();

        //Getting the client from the bundle
        cliente=extras.getString("cliente");
        bundle.putString("cliente", cliente);
        Log.e(TAG,"cliente" + cliente);
        telefono = extras.getString("telefono");
        Log.e(TAG,"telefono"+ telefono);
        bundle.putString("telefono", telefono);
        projectsList = new ArrayList<Proyecto>();
        try{
            projectsList = db.getAllProyectos(cliente);
            arrayProjects = new ArrayList<String>();
            for(int i = 0; i<projectsList.size();i++){
                proyecto= new Proyecto();
                proyecto = projectsList.get(i);
                arrayProjects.add(proyecto.getNombreProyecto());
            }
            adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayProjects){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View view = super.getView(position,convertView,parent);
                    TextView textView  = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                    return view;
                }
            };
            lstOpciones = (ListView)findViewById(R.id.LstOpciones);
            lstOpciones.setAdapter(adaptador);
            lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object obj = lstOpciones.getAdapter().getItem(position);
                    String project = obj.toString();
                    int idProject = 0;
                    try{
                        idProject = db.getIdProject(project);
                        bundle.putString("id_proyecto", String.valueOf(idProject));
                        Intent i = new Intent(Proyectos.this, TipoEncuesta.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtras(bundle);
                        startActivity(i);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }catch(Exception e){
            e.printStackTrace();

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
