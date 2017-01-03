package com.encuestas.popresearch.popresearchencuestas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import Entity.GeoEstatica;
import Entity.HiScreenEntity;

/**
 * Created by Admin on 29/09/2015.
 */
public class HiScreen extends AppCompatActivity {
    String TAG = getClass().getName();
    //variables
    public String id_tiendaSeleccionado;
    public String mobile;
    public String id_archivoSeleccionado;
    public String id_encuestaSeleccionada;
    //listas
    public ListView lstOpciones ;
    public ArrayAdapter<String> adaptador;
    public List<HiScreenEntity> EncuestaList;
    public ArrayList<String> arrayEncuestas;
    // obejetos
    public static ProgressDialog pDialog;
    public HiScreenEntity hiScreenEntity;
    public Bundle bundle = new Bundle();
    public static Dao db;
    GeoEstatica geoEstatica;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_screen);
        db = new Dao(this);
        Bundle extras = getIntent().getExtras();
        geoEstatica = new GeoEstatica();
        geoEstatica.reset();

        id_archivoSeleccionado =extras.getString("id_ArchivoSeleccionado");
        bundle.putString("id_archivoSeleccionado", id_archivoSeleccionado);
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);
        mobile = extras.getString("mobile");
        bundle.putString("mobile", mobile);
        //passing contadorPreguntas porque el archivo encuesta se llama a si mismo y en primera instancia necesita este parametro.
        bundle.putString("contadorPreguntas", "0");
        //passing sig_pregunta to the bundle se llama a si mismo y en primera instancia necesita este parametro.
        bundle.putString("sig_pregunta", "0");

        EncuestaList = new ArrayList<HiScreenEntity>();

        try{
            EncuestaList = db.getEncuestas(id_archivoSeleccionado);
            arrayEncuestas = new ArrayList<String>();
            for(int i = 0; i<EncuestaList.size();i++){
                hiScreenEntity = new HiScreenEntity();
                hiScreenEntity = EncuestaList.get(i);
                id_tiendaSeleccionado = hiScreenEntity.getIdTienda();
                arrayEncuestas.add(hiScreenEntity.getNombreEncuesta());
            }
            adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayEncuestas){
                @NonNull
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view  = super.getView(position,convertView,parent);
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                    return view;
                }
            };
            lstOpciones = (ListView)findViewById(R.id.LstOpciones);
            lstOpciones.setAdapter(adaptador);

            lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    HiScreenEntity encuesta = new HiScreenEntity();
                    encuesta =  EncuestaList.get(position);
                    bundle.putString("id_tiendaSeleccionada", encuesta.getIdTienda());
                    new asynclogin().execute();

                }
            });
        }catch(Exception e){e.getCause();
        }


    }
    class asynclogin extends AsyncTask< String, String, String > {
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(HiScreen.this);
            pDialog.setMessage("Cargando....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            //limpimos la base de antes de cargar nuevos datos
            db.deletesTablaRealizandoEncuesta();
            //checking if PreguntasEncuestasSeleccionada and RespuestasEncuestaSeleccionada
   /*         int countRowsPreguntasEncuestaMobileIdTiendaSelected = db.getCountTABLE_PREGUNTAS_ENCUESTA();
            Log.e(TAG,"count Preguntas : " + countRowsPreguntasEncuestaMobileIdTiendaSelected);
            //Checking if RespuestasEncuestasSeleccionada
            int countRowsRespuestasEncuestaMobileIdTiendaSelected = db.getCountTABLE_RESPUESTAS_ENCUESTA();
            Log.e(TAG,"count REspuesta : " + countRowsRespuestasEncuestaMobileIdTiendaSelected);*/

            db.deletesRecordsTable_TABLE_PREGUNTAS_ENCUESTA();
            db.getPreguntasEncuestaSelectedIntoTable(id_encuestaSeleccionada);

            db.deletesRecordsTable_TABLE_RESPUESTAS_ENCUESTA();
            db.getRespuestasEncuestaSelectedIntoTable(id_encuestaSeleccionada);

            return null;
        }
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            pDialog.hide();
            Intent i = new Intent(HiScreen.this, Encuesta2.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.putExtras(bundle);
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
            //Display Toast
            Intent intent = new Intent(this, Principal2.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
