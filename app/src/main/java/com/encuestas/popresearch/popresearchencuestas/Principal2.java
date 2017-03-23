package com.encuestas.popresearch.popresearchencuestas;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import AsynckData.Conexiones;
import AsynckData.ServiceHandler;
import DB.Dao;
import Entity.Cliente;
import Entity.EncuestaResultadosPreEntity;
import Entity.FotoStrings;
import Entity.GeoRegister;
import Entity.HiScreenEntity;
import Entity.LoginEntity;
import Entity.PreguntaUniversoEntity;
import Entity.Proyecto;
import Entity.RespuestaUniversoEntity;
import Entity.TelefonoEntity;
import Entity.TipoEncuestaEntity;
import Utility.Connectivity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import static android.R.attr.x;

/**
 * Created by Admin on 29/09/2015.
 * Es la clase que inicia la descarga de todos los datos , cliente, proyecto , tipoEncuesta , preguntas, respuestas
 */
public class Principal2 extends AppCompatActivity {


    private String TAG = getClass().getSimpleName();
    final String URL = "http://" + Conexiones.getIP_Server() + "/wsDroidLogin3/wsDroidLogin3.asmx"; // ruta web para cargar toda la informacion de las encuestas

    JSONArray jsonArray;
    SoapObject request;
    SoapPrimitive response;

    /* GET CLIENTES IN ASMX */
    final String NAMESPACE_GET_CLIENTES = "http://tempuri.org/";
    String METHOD_NAME_GET_CLIENTES = "getClientes";
    JSONArray jsonArrayClientes;

    /* GET PROYECTOS IN ASMX */
    final String NAMESPACE_GET_PROYECTOS = "http://tempuri.org/";
    String METHOD_NAME_GET_PROYECTOS = "getProyectos";
    JSONArray jsonArrayProyectos;

    /* GET TIPO ENCUESTA IN ASMX */
    final String NAMESPACE_GET_TIPO_ENCUESTA = "http://tempuri.org/";
    String METHOD_NAME_GET_TIPO_ENCUESTA = "getTiposEncuestas";
    JSONArray jsonArrayTipoEncuesta;

    /* GET ENCUESTAS IN ASMX */
    final String NAMESPACE_GET_CAT_MASTER = "http://tempuri.org/";
    String METHOD_NAME_GET_CAT_MASTER = "getCatMaster";
    JSONArray jsonArrayCatMaster;

    /* GET PREGUNTAS UNIVERSO */
    final String NAMESPACE_GET_PREGUNTAS_UNIVERSO = "http://tempuri.org/";
    String METHOD_NAME_GET_PREGUNTAS_UNIVERSO = "getPreguntas";
    JSONArray jsonArrayPreguntasUniverso;

    /*GET RESPUESTAS UNIVERSO*/
    final String NAMESPACE_GET_RESPUESTAS_UNIVERSO = "http://tempuri.org/";
    String METHOD_NAME_GET_RESPUESTAS_UNIVERSO = "getRespuestas";
    JSONArray jsonArrayRespuestasUniverso;

    private String URLFOTO = "http://popresearch8.cloudapp.net/b/fotosws.php";
    private String URLEncuesta = "http://popresearch8.cloudapp.net/b/setEncuesta.php";
    boolean banderaClientes = false;
    boolean banderaProyectos = false;
    boolean banderaTipoEncuesta = false;
    boolean banderaEncuesta = false;
    boolean banderaPreguntasUniverso = false;
    boolean banderaRespuestasUniverso = false;

    Button blogin, bver, bsalir, bEncuestasPendientes, btnuploadPhoto;
    Bundle bundle;
    ProgressDialog pDialog;
    String telefono = "";
    String txtPendientes = "";
    String usuario = LoginUser.usuario;
    String txtsync = "";
    TextView lblMensaje;
    LoginEntity loginEntity;
    //Lista encuestaResultadosPre
    ArrayList<EncuestaResultadosPreEntity> listaEncuestaResultadosPre = new ArrayList<EncuestaResultadosPreEntity>();
    String totalRegistrosPendientes = "0";
    Dao db;
    List<Cliente> clientsList;
    public static String arrayClientes[];
    int numeroFotos;
    private ArrayList<NameValuePair> datosPost;
    private JSONArray jsonFotos;
    ArrayList<FotoStrings> fotos;

    Connectivity connectivity;
    boolean connecTionAvailable;
    ArrayList<GeoRegister> listGeos;
    GeoRegister geoRegister;
    EncuestaResultadosPreEntity encuestaResultadosPre;
    private ArrayList<NameValuePair> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        db = new Dao(this);
        bundle = new Bundle();
        loginEntity = new LoginEntity();
        blogin = (Button) findViewById(R.id.Button01);
        bver = (Button) findViewById(R.id.Button03);
        bsalir = (Button) findViewById(R.id.Button04);
        btnuploadPhoto = (Button) findViewById(R.id.btnuploadPhoto);
        bEncuestasPendientes = (Button) findViewById(R.id.ButtonEncuestasPendientes);
        bEncuestasPendientes.setVisibility(View.INVISIBLE);

        lblMensaje = (TextView) findViewById(R.id.LblMensaje);
        try {
            if (usuario != null) {
                loginEntity.setUsuario(usuario);
                telefono = loginEntity.getUsuario();

            }
        } catch (Exception e) {
            e.getCause();
        }

        ArrayList<TelefonoEntity> telefonos = new ArrayList<TelefonoEntity>();
        try {
            telefonos = db.getTelefonoLogged();

            if (telefonos.size() > 0) {
                for (int i = 0; i < telefonos.size(); i++) {
                    TelefonoEntity telefonoObj = new TelefonoEntity();
                    telefonoObj = telefonos.get(i);
                    telefono = telefonoObj.getTelefono();
                }
            }
        } catch (Exception e) {
            e.getCause();
        }

        if (telefono != null) {
            lblMensaje.setEllipsize(TextUtils.TruncateAt.END);
            lblMensaje.setSingleLine(true);
            lblMensaje.setText("Teléfono : " + telefono);
            //Se inserta en telefono del usuario en el Bundle
            bundle.putString("telefonoUsuario", telefono);
        }
        //Logins action
        blogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(telefono != "" && loginEntity.getUsuario()!="" ){
                    showAlert();
                }else{
                    connectivity = new Connectivity();
                    connecTionAvailable = connectivity.isConnected(getBaseContext());
                    if (connecTionAvailable) {
                        try {
                            db.deleteTableTelefonoLogged();
                        } catch (Exception e) {
                            e.getCause();
                        }
                            db.deleteTableTelefonoLogged();
                        lblMensaje.setText("");
                        Intent intent = new Intent(Principal2.this, LoginUser.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Principal2.this,"Debe conectarse para loguearse" ,Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        //boton login
        bver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new asynclogin().execute();
            }
        });
        //salir action
        bsalir.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        //Enviar Encuestas Pendientes
        bEncuestasPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectivity = new Connectivity();
                connecTionAvailable = connectivity.isConnected(getBaseContext());

                if (connecTionAvailable) {
                    new asyncEncuestasPendientes().execute();
                }else{
                    Toast.makeText(Principal2.this,"Debe estar conectado a una red  para sincronizar " ,Toast.LENGTH_LONG).show();
                }
            }
        });
        //Revisando si existen encuestas pendientes (Se muestra el boton de en encuestasPendientes)
        try {
            listaEncuestaResultadosPre = db.getAllEncuestaResultadosPre();

        } catch (Exception e) {
            e.getCause();
        }
        if (listaEncuestaResultadosPre.size() > 0) {
            bEncuestasPendientes.setVisibility(View.VISIBLE);

;        } else {
            bEncuestasPendientes.setVisibility(View.INVISIBLE);
        }

        try {
            db.open();
            numeroFotos = db.getfotosCount();

            if (numeroFotos > 0) {
                btnuploadPhoto.setVisibility(View.VISIBLE);

            } else {

                btnuploadPhoto.setVisibility(View.GONE);
            }
            db.close();
        } catch (Exception e) {
            // error
            e.printStackTrace();
        }
        // boton fotos pendientes
        btnuploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connectivity = new Connectivity();
                connecTionAvailable = connectivity.isConnected(getBaseContext());

                if (connecTionAvailable) {
                    db.open();
                    fotos = db.getAllFotos();
                    db.close();

                    String nomArchivo;

                    int j = 0;
                    jsonFotos = new JSONArray();
                    for (int x = 0; x < fotos.size(); x++) {
                        datosPost = new ArrayList<>();
                        JSONObject jsonFoto = new JSONObject();
                        try {
                            nomArchivo = fotos.get(x).getIdEncuesta() + "_" + fotos.get(x).getIdEstablecimiento() + "_" + fotos.get(x).getNombre() + "_" + x + ".jpg";
                            jsonFoto.put("idEstablecimiento", fotos.get(x).getIdEstablecimiento());
                            jsonFoto.put("idEncuesta", fotos.get(x).getIdEncuesta());
                            jsonFoto.put("nombreFoto", nomArchivo.toString());
                            jsonFoto.put("base64", fotos.get(x).getStringFoto());
                            jsonFotos.put(jsonFoto);
                            j++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    datosPost.add(new BasicNameValuePair("subeFotos", jsonFotos.toString()));
                    new AsyncUploadFotos(Principal2.this, datosPost, URLFOTO, x).execute();

                    if (j == fotos.size()) {
                        db.open();
                        db.deleteFotosTable();
                        db.close();
                        btnuploadPhoto.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(Principal2.this,"Debe estar conectado a una red WIFI para sincronizar las fotos " ,Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    //descarga la informacion total para la aplicacion
    class asynclogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Principal2.this);
            pDialog.setMessage("Descargando información....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            boolean existeTelefono = false;
            try {
                existeTelefono = db.findTelefono(telefono);

            } catch (Exception e) {
                e.getCause();
            }
            if (!existeTelefono) {
                String clientes = getClientes();
                String proyectos = getProjects();
                String tipoEncuesta = getTipoEncuesta();
                String encuestas = getEncuestas();
                String preguntasUniverso = getPreguntasUniverso();
                String respuestasUniverso = getRespuestasUniverso();
                try {
                    db.deletesRecordsTable_TABLE_PREGUNTAS_ENCUESTA();
                    db.deletesRecordsTable_TABLE_RESPUESTAS_ENCUESTA();
                    db.deletesRecordsTable_encuestasResultadosPre();
                } catch (Exception e) {
                    e.getCause();
                }
                if (clientes.equals("ok") && proyectos.equals("ok") && tipoEncuesta.equals("ok") && encuestas.equals("ok") && preguntasUniverso.equals("ok") && respuestasUniverso.equals("ok")) {
                    return "ok";
                } else {
                    return "false";
                }
            } else {
                return "ok";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();
            if (result.equals("ok")) {
                boolean existeTelefono = false;
                //Se verifica si existe , sino existe se inserta en la BD
                try {
                    existeTelefono = db.findTelefono(telefono);
                } catch (Exception e) {
                    e.getCause();
                }
                if (!existeTelefono) {
                    TelefonoEntity telefonoEntity = null;
                    ArrayList<TelefonoEntity> telefonos = null;
                    try {
                        db.deleteTableTelefonoLogged();
                        telefonoEntity = new TelefonoEntity();
                        telefonoEntity.setTelefono(telefono);
                        telefonoEntity.setFlagDescargado("true");
                        db.addTelefonoLogged(telefonoEntity);
                        telefonos = new ArrayList<TelefonoEntity>();
                        telefonos = db.getTelefonoLogged();

                    } catch (Exception e) {
                        e.getCause();
                    }

                }
                // variables para pasar ala clase proyectos
                clientsList = db.getAllClientes();
                bundle.putString("cliente", clientsList.get(0).getNombreCliente());
                bundle.putString("telefono", telefono);

                pDialog.dismiss();
                Intent intent = new Intent(Principal2.this, Proyectos.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    //////////// CLASS ASYNCRONA PARA ENVIAR ENCUESTAS PENDIENTES /////////////
    class asyncEncuestasPendientes extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(Principal2.this);
            pDialog.setMessage("Enviando encuestas pendientes ..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            //Se realizara la conexion a la BD para traer
            txtPendientes = "0";
            try {
                listaEncuestaResultadosPre = db.getAllEncuestaResultadosPre();
                totalRegistrosPendientes = String.valueOf(listaEncuestaResultadosPre.size());
                listGeos = new ArrayList<>();
                listGeos = db.getAllGeos();
                jsonArray = new JSONArray();
                //Getting the current date time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date date = new Date();
                data = new ArrayList<>();
                String mlat,mlong;

                for (int i = 0; i < listaEncuestaResultadosPre.size(); i++) {

                    geoRegister = new GeoRegister();
                    encuestaResultadosPre = new EncuestaResultadosPreEntity();
                    encuestaResultadosPre = listaEncuestaResultadosPre.get(i);
                    geoRegister = db.getGeoRegister(Integer.parseInt(encuestaResultadosPre.getIdEncuestaResultadosPre()),Integer.parseInt(encuestaResultadosPre.getIdTiendaResultadosPre()));
                    if(geoRegister==null){
                        mlat = "0.0";
                        mlong = "0.0";
                    }else{
                        mlat = geoRegister.getLatitud();
                        mlong = geoRegister.getLongitud();
                    }
                    JSONObject json = new JSONObject();
                    //Adding jsons into Array of jsons
                    json.put("idEncuesta", Integer.parseInt(encuestaResultadosPre.getIdEncuestaResultadosPre()));
                    json.put("idEstablecimiento", Integer.parseInt(encuestaResultadosPre.getIdTiendaResultadosPre()));
                    json.put("idTienda",Integer.parseInt(encuestaResultadosPre.getIdArchivoResultadosPre()));
                    json.put("usuario", telefono.toString());
                    json.put("idPregunta", Integer.parseInt(encuestaResultadosPre.getIdPreguntaResultadosPre()));
                    json.put("idRespuesta", encuestaResultadosPre.getIdRespuestaResultadosPre().toString());
                    json.put("abierta", Boolean.parseBoolean(encuestaResultadosPre.getAbiertaResultadosPre()));  //boolean
                    json.put("latitud",mlat);  // geo de la base de datos
                    json.put("longitud",mlong); // geo de la base de datos
                    json.put("fecha", dateFormat.format(date).toString());
                    jsonArray.put(json);
                }

                data.add(new BasicNameValuePair("setEncuestas",jsonArray.toString()));
                Log.e(TAG,"array : " + data.toString());
                ServiceHandler serviceHandler = new ServiceHandler();
                String response = serviceHandler.makeServiceCall(URLEncuesta, ServiceHandler.POST, data);
                JSONObject jsonObject = new JSONObject(response);
                JSONObject result = jsonObject.getJSONObject("result");
                txtPendientes = result.getString("success").toString();

            } catch (Exception e) {
               e.printStackTrace();
            }
            return txtPendientes;
        }


        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            pDialog.hide();
            if (result.equals("1")) {
                Toast.makeText(Principal2.this, totalRegistrosPendientes + " Registros en encuestas Pendientes enviadas exitosamente !!", Toast.LENGTH_LONG).show();
                db.updateflagenviada();
                db.deleteGeosTable();
            } else {
                Toast.makeText(Principal2.this, "Encuesta Guardada localmente.", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(Principal2.this, Principal2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
    ///////////////////////////////////////////////// GETTING CLIENTES /////////////////////////////////////////////////////////
    public String getClientes() {
        try {
            //JSONArray
            jsonArrayClientes = new JSONArray();

            //JSON object
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_CLIENTES, METHOD_NAME_GET_CLIENTES);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_CLIENTES + METHOD_NAME_GET_CLIENTES, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            jsonArrayClientes = new JSONArray(response.toString());
            //Validating the jsonArrayClients
            if (jsonArrayClientes == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar clientes (null) !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    banderaClientes = true;

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen Clientes para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        } finally {
            if (banderaClientes) {
                Intent i = new Intent(Principal2.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        }

        int numeroRegistros = jsonArrayClientes.length();
        int contador = 0;

        //instance the DB
        db = new Dao(Principal2.this);

        //deletes the table (clientes)
        try {
            db.deletesTables();
        } catch (Exception e) {
            e.getCause();
        }

        while (numeroRegistros > 0) {
            JSONObject json_data = null;
            try {
                //Getting the json data
                json_data = jsonArrayClientes.getJSONObject(contador);
                //Creating a client object
                Cliente cliente1 = new Cliente();
                cliente1.setNombreCliente(json_data.getString("NOMBRE"));

                db.addCliente(cliente1);
                numeroRegistros--;
                contador++;

            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar clientes !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }//ends while

        //"Getting all the clients() from the DB
        clientsList = new ArrayList<Cliente>();
        clientsList = db.getAllClientes();
        //Creating the array of clients
        arrayClientes = new String[clientsList.size()];
        //Getting clients
        for (int i = 0; i < clientsList.size(); i++) {
            Cliente cliente1 = new Cliente();
            cliente1 = clientsList.get(i);
            arrayClientes[i] = cliente1.getNombreCliente();
        }

        if (numeroRegistros == 0) {
            return "ok";
        } else {
            return "false";
        }
    }

    ///////////////////////////////////////// GETTING PROJECTS //////////////////////////////////////////////////////////////
    public String getProjects() {
        jsonArrayProyectos = new JSONArray();
        try {
            //JSON object
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_PROYECTOS, METHOD_NAME_GET_PROYECTOS);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_PROYECTOS + METHOD_NAME_GET_PROYECTOS, envelope);
            response = (SoapPrimitive) envelope.getResponse();

            jsonArrayProyectos = new JSONArray(response.toString());

            if (jsonArrayProyectos == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar proyectos (null) !!!!", Toast.LENGTH_SHORT);
                toast1.show();

            }

        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banderaProyectos = true;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen Proyectos para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        } finally {
            if (banderaProyectos == true) {
                Intent i = new Intent(Principal2.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        }

        int numeroRegistrosPro = jsonArrayProyectos.length();
        int contadorPro = 0;

        while (numeroRegistrosPro > 0) {
            JSONObject json_data;
            try {
                //Getting the json data
                json_data = jsonArrayProyectos.getJSONObject(contadorPro);
                //creating the obj project
                Proyecto proyecto = new Proyecto();
                proyecto.setNombreProyecto(json_data.getString("NOMBRE"));
                proyecto.setId_proyecto(json_data.getString("ID_PROYECTO"));  //id_proyecto
                proyecto.setCliName(json_data.getString("cliName"));
                //Inserting into the table proyectos
                try {
                    db.addProyecto(proyecto);
                } catch (Exception e) {
                    e.getCause();
                }

                numeroRegistrosPro--;
                contadorPro++;

            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar proyectos  !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }

        } //ends while projects


        if (numeroRegistrosPro == 0) {
            return "ok";
        } else {
            return "false";
        }
    }

    //////////////////////////////// GETTING TIPO ENCUESTA //////////////////////////////////////////////////
    public String getTipoEncuesta() {
        //JSONArray
        jsonArrayTipoEncuesta = new JSONArray();
        try {
            //JSON object
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_TIPO_ENCUESTA, METHOD_NAME_GET_TIPO_ENCUESTA);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_TIPO_ENCUESTA + METHOD_NAME_GET_TIPO_ENCUESTA, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            jsonArrayTipoEncuesta = new JSONArray(response.toString());

            if (jsonArrayTipoEncuesta == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar tipo encuesta (null) !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banderaTipoEncuesta = true;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen Tipo encuestas para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        } finally {
            if (banderaTipoEncuesta) {
                Intent i = new Intent(Principal2.this, Principal2.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
        int numeroRegistrosTipoEncuesta = jsonArrayTipoEncuesta.length();
        int contadorTipoEncuesta = 0;
        while (numeroRegistrosTipoEncuesta > 0) {
            JSONObject json_data ;
            try {
                json_data = jsonArrayTipoEncuesta.getJSONObject(contadorTipoEncuesta);
                TipoEncuestaEntity tipoEncuestaEntity = new TipoEncuestaEntity();
                tipoEncuestaEntity.setNombreEncuesta(json_data.getString("ENCUESTA"));
                tipoEncuestaEntity.setIdArchivoEncuesta(json_data.getString("id_archivo"));
                tipoEncuestaEntity.setIdEncuesta(json_data.getString("id_encuesta"));
                tipoEncuestaEntity.setNumeroTelefono(json_data.getString("numero_tel"));
                tipoEncuestaEntity.setNombreCliente(json_data.getString("NOMBRE"));
                tipoEncuestaEntity.setId_proyecto(json_data.getString("ID_PROYECTO"));
                db.addTipoEncuesta(tipoEncuestaEntity);
                numeroRegistrosTipoEncuesta--;
                contadorTipoEncuesta++;
            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar tipo encuesta  !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }
        if (numeroRegistrosTipoEncuesta == 0) {
            return "ok";
        } else {
            return "false";
        }
    }

    //////////////////////////////////////////////////GETTING ENCUESTAS ///////////////////////////////////////
    public String getEncuestas() {
        jsonArrayCatMaster = new JSONArray();
        try {
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_CAT_MASTER, METHOD_NAME_GET_CAT_MASTER);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_CAT_MASTER + METHOD_NAME_GET_CAT_MASTER, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            jsonArrayCatMaster = new JSONArray(response.toString());

            if (jsonArrayCatMaster == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar encuestas (null) !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banderaEncuesta = true;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen Encuestas para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        } finally {
            if (banderaEncuesta) {
                Intent i = new Intent(Principal2.this, Principal2.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        }
        int numeroRegistrosEncuestas = jsonArrayCatMaster.length();
        int contadorEncuestas = 0;
        while (numeroRegistrosEncuestas > 0) {
            JSONObject json_data;
            try {
                //Getting the json data
                json_data = jsonArrayCatMaster.getJSONObject(contadorEncuestas);
                //creating the obj Encuesta
                HiScreenEntity hiScreenEntity = new HiScreenEntity();
                hiScreenEntity.setIdTienda(json_data.getString("ID_TIENDA"));
                hiScreenEntity.setNombreEncuesta(json_data.getString("NOMBRE"));
                hiScreenEntity.setId_archivo(json_data.getString("id_archivo"));
                //Inserting into the table hiScreen
                db.addEncuestaHiScreen(hiScreenEntity);
                numeroRegistrosEncuestas--;
                contadorEncuestas++;

            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar encuestas !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }//ends while encuestas
        if (numeroRegistrosEncuestas == 0) {
            return "ok";
        } else {
            return "false";
        }
    }//ends getEncuestas()

    //////////////////////////////////////////////////GETTING PREGUNTAS UNIVERSO (Correspondientes al telefono (todas las preguntas)) ///////////////////////////////////////
    public String getPreguntasUniverso() {
        //JSONArray
        jsonArrayPreguntasUniverso = new JSONArray();
        try {
            //JSON object
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_PREGUNTAS_UNIVERSO, METHOD_NAME_GET_PREGUNTAS_UNIVERSO);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_PREGUNTAS_UNIVERSO + METHOD_NAME_GET_PREGUNTAS_UNIVERSO, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            jsonArrayPreguntasUniverso = new JSONArray(response.toString());
            if (jsonArrayPreguntasUniverso == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar preguntas universo (null) !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banderaPreguntasUniverso = true;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen preguntas universo para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        }
        int numeroRegistrosPreguntas = jsonArrayPreguntasUniverso.length();

        int contadorPreguntas = 0;
        while (numeroRegistrosPreguntas > 0) {
            JSONObject json_data;
            try {
                //Getting the json data
                json_data = jsonArrayPreguntasUniverso.getJSONObject(contadorPreguntas);
                //creating the obj Encuesta
                PreguntaUniversoEntity pregunta = new PreguntaUniversoEntity();
                pregunta.setId_pregunta(json_data.getString("ID_PREGUNTA")); //idPregunta
                pregunta.setPregunta(json_data.getString("PREGUNTA")); //pregunta
                pregunta.setMultiple(json_data.getString("MULTIPLE")); //multiple
                pregunta.setOrden(json_data.getString("ORDEN"));  //orden
                pregunta.setId_encuesta(json_data.getString("id_encuesta")); //idEncuesta
                //Inserting into the table hiScreen
                db.addPreguntas(pregunta);
                //Insertandolos en la lista de preguntas universo
                numeroRegistrosPreguntas--;
                contadorPreguntas++;

            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar Preguntas Universo  !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }// ends while preguntas universo

        if (numeroRegistrosPreguntas == 0) {
            return "ok";
        } else {
            return "false";
        }
    }//ends getPreguntasUniverso

    //////////////////////////////////////////////////GETTING RESPUESTAS UNIVERSO (Correspondientes al telefono (todas las respuestas)) ///////////////////////////////////////
    public String getRespuestasUniverso() {
        jsonArrayRespuestasUniverso = new JSONArray();
        try {
            JSONObject json = new JSONObject();
            json.put("telefono", telefono);
            request = new SoapObject(NAMESPACE_GET_RESPUESTAS_UNIVERSO, METHOD_NAME_GET_RESPUESTAS_UNIVERSO);
            request.addProperty("telefono", json.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(NAMESPACE_GET_RESPUESTAS_UNIVERSO + METHOD_NAME_GET_RESPUESTAS_UNIVERSO, envelope);
            response = (SoapPrimitive) envelope.getResponse();
            jsonArrayRespuestasUniverso = new JSONArray(response.toString());
            if (jsonArrayRespuestasUniverso == null) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar Respuestas Universo (null)  !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banderaRespuestasUniverso = true;
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "No existen Respuestas Universo para este telefono !!!", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent i = new Intent(Principal2.this, Principal2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
        } finally {
            if (banderaRespuestasUniverso) {
                Intent i = new Intent(Principal2.this, Principal2.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }

        }
        int numeroRegistrosRespuestas = jsonArrayRespuestasUniverso.length();
        int contadorRespuestas = 0;
        while (numeroRegistrosRespuestas > 0) {
            JSONObject json_data = null;
            try {
                //Getting the json data
                json_data = jsonArrayRespuestasUniverso.getJSONObject(contadorRespuestas);
                // Respuesta
                RespuestaUniversoEntity respuesta = new RespuestaUniversoEntity();
                respuesta.setId_pregunta(json_data.getString("ID_PREGUNTA")); //idPregunta
                respuesta.setId_respuesta(json_data.getString("ID_RESPUESTA")); //idRespuesta
                respuesta.setRespuesta(json_data.getString("RESPUESTA")); //respuesta
                respuesta.setSig_pregunta(json_data.getString("SIG_PREGUNTA")); //sigPregunta
                respuesta.setRespuestaLibre(json_data.getString("RESPUESTA_LIBRE"));  //respuestaLibre
                respuesta.setId_encuesta(json_data.getString("id_encuesta")); //idEncuesta
                //Inserting into the table respuestas
                db.addRespuestas(respuesta);
                numeroRegistrosRespuestas--;
                contadorRespuestas++;

            } catch (JSONException e) {
                e.printStackTrace();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error: al descargar Respuestas Universo   !!!!", Toast.LENGTH_SHORT);
                toast1.show();
            }
        }//ends while
        if (numeroRegistrosRespuestas == 0) {
            return "ok";
        } else {
            return "false";
        }
    }//ends getRespuestasUniverso()

    public void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(Principal2.this).create();
        alertDialog.setTitle("Mensaje");
        alertDialog.setMessage("Cambiar de usuario borrara los datos existentes");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
            new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    connectivity = new Connectivity();
                    connecTionAvailable = connectivity.isConnected(getBaseContext());
                    if (connecTionAvailable) {
                        try {
                            db.deletesTables();
                        } catch (Exception e) {
                            e.getCause();
                        }
                           db.deletesTables();
                        lblMensaje.setText("");
                        Intent intent = new Intent(Principal2.this, LoginUser.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Principal2.this,"Debe conectarse para loguearse" ,Toast.LENGTH_SHORT).show();
                    }
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
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
