package com.encuestas.popresearch.popresearchencuestas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import AsynckData.Conexiones;
import AsynckData.ServiceHandler;
import DB.Dao;
import Entity.FotoEncuesta;
import Entity.FotoStrings;
import Entity.GeoEstatica;
import Entity.GeoRegister;
import Entity.RealizandoEncuestaEntity;
import Utility.Connectivity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Admin on 29/09/2015.
 */
public class Enviar extends Activity {
    private String TAG = getClass().getSimpleName();
    private String NAMESPACE = "http://tempuri.org/"; //the namespace that you'll find in the header of your asmx webservice
    private String METHOD_NAME = "setResultadosEncuesta";  //the webservice method that you want to call
    private final String URL = "http://" + Conexiones.IP_Server + "/wsDroidLogin3/wsDroidLogin3.asmx";
    private String URLFOTO = "http://popresearch8.cloudapp.net/b/fotosws.php";
    private String URLEncuesta = "http://popresearch8.cloudapp.net/b/setEncuesta.php"; // todo URL test set encuestas

    private JSONArray jsonArray, jsonFotos;
    private SoapObject request;
    private SoapPrimitive response;
    private ProgressDialog pDialog;
    // httpHandler handler;////////////////////////////////////////////
    private int totalRegistrosEncuesta;
    private String id_ArchivoSeleccionado;
    private String id_encuestaSeleccionada;
    private String id_tiendaSeleccionada;
    private String mobile;
    private String txtPendientes = "";
    private int flagenvio;
    private String siguienteencuesta;
    //DB
    Dao db;
    //Bundle
    Bundle bundle = new Bundle();
    //ListaRealizandoEncuestaEntity
    private ArrayList<RealizandoEncuestaEntity> listRealizandoEncuesta = new ArrayList<RealizandoEncuestaEntity>();
    private FotoEncuesta fotoEncuesta;
    private String idEstablecimiento;
    private String idEncuesta;
    private String nomArchivo,base64;
    private ArrayList<String> arrayFotos , arrayNomFoto;
    private ArrayList<NameValuePair> datosPost ,data;
    Connectivity connectivity;
    GeoEstatica geoEstatica;
    GeoRegister geoRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        connectivity = new Connectivity();
        geoEstatica = new GeoEstatica();
        geoRegister = new GeoRegister();

        Boolean validaRed = connectivity.isConnected(this);
        db = new Dao(this); // instanciamos la Base de Datos
        Bundle extras = getIntent().getExtras(); // recuperamos  los valores de la encuesta
        id_ArchivoSeleccionado = extras.getString("id_archivoSeleccionado");
        bundle.putString("id_archivoSeleccionado", id_ArchivoSeleccionado);
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);
        id_tiendaSeleccionada = extras.getString("id_tiendaSeleccionada");
        mobile = extras.getString("mobile");
        siguienteencuesta = extras.getString("siguienteencuesta");
        bundle.putString("siguienteencuesta", siguienteencuesta);
        //////////////////// TABLA LOCAL REALIZANDO ENCUESTA ///////////
        // shows the information that contains the table REALIZANDO ENCUESTA (SON LOS ID_PREGUNTA,ID_RESPUESTA, ABIERTA (BANDERA), latitud, longitud)
        listRealizandoEncuesta = db.getRealizandoEncuestaEntity();
        totalRegistrosEncuesta = listRealizandoEncuesta.size();
        arrayFotos = new ArrayList<>();
        fotoEncuesta = new FotoEncuesta().getInstace();
        // si hay fotos para enviar
        boolean connecTionAvailable = isNetworkAvailable();
        if (connecTionAvailable) {
            // si tenemos internet enviamos
            if (validaRed == true) { // Enviamos la encuesta
                jsonArray = new JSONArray();
                try {
                    //Getting the current date time
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date date = new Date();
                    data = new ArrayList<>();

                    for (int i = 0; i < totalRegistrosEncuesta; i++) {
                        RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
                        realizandoEncuestaEntity = listRealizandoEncuesta.get(i);
                        geoRegister = new GeoRegister();

                        geoRegister = db.getGeoRegister(Integer.parseInt(realizandoEncuestaEntity.getId_encuestaRealizandoEncuesta()),Integer.parseInt(realizandoEncuestaEntity.getId_tiendaRealizandoEncuesta()));
                        JSONObject json = new JSONObject();

                        // TODO NUEVO ENVIO TEST PHP 7
                        json.put("idEncuesta", Integer.parseInt(realizandoEncuestaEntity.getId_encuestaRealizandoEncuesta()));
                        json.put("idEstablecimiento", Integer.parseInt(realizandoEncuestaEntity.getId_tiendaRealizandoEncuesta()));
                        json.put("idTienda",Integer.parseInt(realizandoEncuestaEntity.getIdArchivoRealizandoEncuesta()));
                        json.put("usuario", mobile);
                        json.put("idPregunta", Integer.parseInt(realizandoEncuestaEntity.getId_preguntaRealizandoEncuesta()));
                        json.put("idRespuesta", realizandoEncuestaEntity.getId_respuestaRealizandoEncuesta());
                        json.put("abierta", Boolean.parseBoolean(realizandoEncuestaEntity.getAbiertaRealizandoEncuesta()));  //boolean
                        json.put("latitud", geoRegister.getLatitud().toString());
                        json.put("longitud", geoRegister.getLongitud().toString());
                        json.put("fecha", dateFormat.format(date));

                        jsonArray.put(json);
                    }
                    data.add(new BasicNameValuePair("setEncuestas",jsonArray.toString()));

                } catch (Exception e) {
                    e.getCause();
                }
                prepareFotos();
                new async().execute();
            }else{ // si no hay red guardamos localmente

                geoEstatica.reset();
                //encuestas
                db.passTableRealinzandoEncuestaToTableEncuestasResultadosPre();
               /* db.deletesTablaRealizandoEncuesta();*/
                Toast.makeText(getApplicationContext(), "Encuesta guardada localmente", Toast.LENGTH_LONG).show();
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(200);

                Intent i = new Intent(Enviar.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
                // fotos
                prepareFotos();
            }
        }else{

            geoEstatica.reset();
            //encuestas
            db.passTableRealinzandoEncuestaToTableEncuestasResultadosPre();
        /*    db.deletesTablaRealizandoEncuesta();*/
            // fotos
            prepareFotos();
            Toast.makeText(getApplicationContext(), "Encuesta guardada localmente", Toast.LENGTH_LONG).show();
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
            Intent i = new Intent(Enviar.this, Principal2.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.putExtras(bundle);
            startActivity(i);
        }
    }
    //Ends on created
    class async extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Enviar.this);
            pDialog.setMessage("Enviando.... " + totalRegistrosEncuesta + " registros");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            txtPendientes = "0";
            try {
                //TODO cambio de webservices php 7
                ServiceHandler serviceHandler = new ServiceHandler();
                String response = serviceHandler.makeServiceCall(URLEncuesta, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");

                txtPendientes = result.getString("success").toString();
                if (response.toString().equals("1")) {  // si los datos de la encuesta fueron subidos correctamente
                    txtPendientes = response.toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return txtPendientes;
        }
        protected void onPostExecute(String result) {

            if (result.equals("1")) {

                geoEstatica.reset();
                Toast.makeText(getApplicationContext(), "Encuesta enviada al server exitosamente", Toast.LENGTH_LONG).show();
                // terminando de enviar las encuestas envia las fotos
                enviaFotos();
                db.deleteGeosTable();
                db.passTableRealinzandoEncuestaToTableEncuestasResultadosPreEnvio();
                db.deletesTablaRealizandoEncuesta();
                db.deletesRecordsTable_encuestasResultadosPre();
                pDialog.dismiss();
                pDialog.hide();
                Intent i = new Intent(Enviar.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }else{

                geoEstatica.reset();
                db.passTableRealinzandoEncuestaToTableEncuestasResultadosPre();
               // db.deletesTablaRealizandoEncuesta();
                pDialog.dismiss();
                pDialog.hide();
                Toast.makeText(getApplicationContext(), "No se pudieron enviar los datos", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Encuesta guardada localmente ", Toast.LENGTH_LONG).show();
                //TODO aqui viajaba a Salir.java
                Intent i = new Intent(Enviar.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
    }

    //Checks if there is connection // revisa si existe una conexion a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void enviaFotos(){
        // iniciamos el envio de las fotos
        int x;
        if (fotoEncuesta.getNombre() != null) {
            idEstablecimiento = fotoEncuesta.getIdEstablecimiento();
            idEncuesta = fotoEncuesta.getIdEncuesta();
            arrayNomFoto = fotoEncuesta.getNombre();   //  array de  nombres de las fotos
            arrayFotos = fotoEncuesta.getArrayFotos(); // array de base64 de las fotos

            for (x = 0 ; x < arrayFotos.size(); x++) {
                nomArchivo = idEncuesta + "_" + idEstablecimiento + "_" + fotoEncuesta.getNombre().get(x) + "_" + x + ".jpg";
                JSONObject jsonObject = new JSONObject();
                jsonFotos = new JSONArray();
                datosPost = new ArrayList<>();
                base64 = fotoEncuesta.getArrayFotos().get(x);
                // agregamos las fotos ala base ;
                db.addFotoEncuesta(new FotoStrings(idEstablecimiento, idEncuesta, (fotoEncuesta.getNombre().get(x)).toString(), base64));
                try {
                    jsonObject.put("idEstablecimiento", idEstablecimiento);
                    jsonObject.put("idEncuesta", idEncuesta);
                    jsonObject.put("nombreFoto", nomArchivo);
                    jsonObject.put("base64", base64);
                    jsonFotos.put(jsonObject);
                    datosPost.add(new BasicNameValuePair("subeFotos", jsonFotos.toString()));
                    new AsyncUploadFotos(Enviar.this, datosPost, URLFOTO,x).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(x==arrayFotos.size()){
                db.deleteFotosTable();
            }
        }
    }
    public void prepareFotos(){
        int x = 0;
        if (fotoEncuesta.getNombre() != null) {
            idEstablecimiento = fotoEncuesta.getIdEstablecimiento();
            idEncuesta = fotoEncuesta.getIdEncuesta();
            arrayFotos = fotoEncuesta.getArrayFotos(); // array de base64 de las fotos
            for (x = 0; x < arrayFotos.size(); x++) {
                nomArchivo = idEncuesta + "_" + idEstablecimiento + "_" + fotoEncuesta.getNombre().get(x) + "_" + x + ".jpg";
                base64 = fotoEncuesta.getArrayFotos().get(x);
                db.addFotoEncuesta(new FotoStrings(idEstablecimiento, idEncuesta, (fotoEncuesta.getNombre().get(x)).toString(), base64));
            }
        }
    }
}
