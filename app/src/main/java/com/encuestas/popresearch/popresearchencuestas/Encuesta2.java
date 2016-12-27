package com.encuestas.popresearch.popresearchencuestas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Entity.GeoEstatica;
import Entity.GeoRegister;
import Entity.PreguntaAbierta;
import Entity.PreguntaUniversoEntity;
import Entity.RealizandoEncuestaEntity;
import DB.Dao;
import Utility.GPSTracker;

/**
 * Created by Admin on 29/09/2015.
 */
public class Encuesta2 extends AppCompatActivity implements View.OnClickListener {

    String TAG = getClass().getSimpleName();
    private Spinner cmbOpciones;
    private EditText campoLibre;
    public static ProgressDialog pDialog;
    //parametros que se insertan en la BD
    String id_ArchivoSeleccionado;
    String id_encuestaSeleccionada;
    String id_tiendaSeleccionada;
    //Button for select multiple answers
    public Button selectColoursButton;
    String sig_pregunta;
    String id_respuesta = "0";
    String id_pregunta;
    String id_preguntaAnterior;
    String mobile;
    Boolean dropcontestada = false;
    Boolean mensajeerror = false;

    //List that contains all answers related to the question
    List<String> listRespuestasLinkedPregunta = new ArrayList<String>();

    //Lista que obtiene el id_respuesta y la siguiente pregunta
    List<String> listaIdRespuesta_sigPregunta;

    //Lista preguntas
    boolean isRespuestaLibre;

    Bundle bundle = new Bundle();
    int contadorPreguntas;
    String replySelected;
    String multiple;

    //respuestas abiertas (componentes) ///
    public ArrayList<CharSequence> selectedColours = new ArrayList<CharSequence>();
    protected String arreglo[];

    //Label that shows the answer selected
    TextView lblMensaje;
    TextView txtProduct;

    StringBuilder stringBuilder = new StringBuilder();


    String latitude = "0";
    String longitude = "0";

    //nuevo servicio para geolocalizacion
    double newLatitud = 0.0;
    double newlongitud = 0.0;
    GPSTracker gpsTracker;
    private GeoEstatica geoEstatica;
    Dao db = HiScreen.db;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);

        Bundle extras = getIntent().getExtras();

        contadorPreguntas = Integer.parseInt(extras.getString("contadorPreguntas"));
        id_ArchivoSeleccionado = extras.getString("id_archivoSeleccionado");
        bundle.putString("id_archivoSeleccionado", id_ArchivoSeleccionado);
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);
        id_tiendaSeleccionada = extras.getString("id_tiendaSeleccionada");
        bundle.putString("id_tiendaSeleccionada", id_tiendaSeleccionada);
          /* obtenemos la geolocalizacion */
        gpsTracker = new GPSTracker(this);
        geoEstatica = new GeoEstatica().getInstance();


        if (geoEstatica.ismEstatus()==false) {
            geoEstatica.setmEstatus(true);
            geoEstatica.setsLatitud(gpsTracker.getLatitude());
            geoEstatica.setsLongitud(gpsTracker.getLongitude());
            newLatitud = geoEstatica.getsLatitud();
            newlongitud = geoEstatica.getsLongitud();

            GeoRegister geoRegister = new GeoRegister(Integer.valueOf(id_encuestaSeleccionada) ,Integer.valueOf(id_tiendaSeleccionada),String.valueOf(newLatitud),String.valueOf(newlongitud));
            db.insertGeos(geoRegister);
        }

        mobile = extras.getString("mobile");
        bundle.putString("mobile", mobile);
        sig_pregunta = extras.getString("sig_pregunta");

        cmbOpciones = (Spinner) findViewById(R.id.CmbOpciones);
        lblMensaje = (TextView) findViewById(R.id.LblMensaje);
        txtProduct = (TextView) findViewById(R.id.product_label);
        campoLibre = (EditText) findViewById(R.id.respuestaLibre);
        campoLibre.setVisibility(View.INVISIBLE);
        /////////////////////////////////// LOOPING QUESTIONS AND ANSWERS //////////////////////////////

        if (contadorPreguntas == 0) {
            // TODO VAlidar Cambio de servicio de geolocalizacion
            if(newLatitud!= 0.0){

                latitude = String.valueOf(newLatitud);
                longitude = String.valueOf(newlongitud);
            }
            PreguntaUniversoEntity preguntaObtenida = null;
            try {
                //Se obtiene la primer pregunta
                preguntaObtenida = new PreguntaUniversoEntity();
                preguntaObtenida = db.getPrimerPreguntaLista();
            } catch (Exception e) {
                e.getCause();
            }
            //Se coloca la primer pregunta
            txtProduct.setText(preguntaObtenida.getPregunta());
            //Se obtiene el id_pregunta
            id_pregunta = preguntaObtenida.getId_pregunta();

            try {
                //Se obtienen las respuestas ligadas a ese id_pregunta para colocarse en el adaptador y mostrarlas
                listRespuestasLinkedPregunta = db.getRespuestasLinkedToPreguntaList(id_pregunta);//--db.getRespuestasLinkedToPregunta(id_pregunta);

            } catch (Exception e) {
                e.getCause();
            }

            isRespuestaLibre = listRespuestasLinkedPregunta.get(0).equals("__RESPUESTA__LIBRE__NO__BORRAR__");

            if (isRespuestaLibre) {
                campoLibre.setVisibility(View.VISIBLE);
                cmbOpciones.setVisibility(View.INVISIBLE);
            }

            multiple = preguntaObtenida.getMultiple();

        } else {

            id_pregunta = sig_pregunta;
            // TODO VALIDA GEO
            if(newLatitud != 0.0 ){
                latitude = String.valueOf(newLatitud);
                longitude = String.valueOf(newlongitud);
                bundle.putString("latitude", latitude);
                bundle.putString("longitude", longitude);
            }
            PreguntaUniversoEntity preguntaObtenida = db.getPregunta_getMultipleLista(sig_pregunta);
            txtProduct.setText(preguntaObtenida.getPregunta());
            multiple = preguntaObtenida.getMultiple();
            //Se obtienen las respuestas ligadas a ese id_pregunta para colocarse en el adaptador y mostrarlas
            listRespuestasLinkedPregunta = db.getRespuestasLinkedToPreguntaList(sig_pregunta);
            isRespuestaLibre = listRespuestasLinkedPregunta.get(0).equals("__RESPUESTA__LIBRE__NO__BORRAR__");
            if (isRespuestaLibre) {
                campoLibre.setVisibility(View.VISIBLE);
                cmbOpciones.setVisibility(View.INVISIBLE);
            }
        }
        //Getting the Button for showing multiple answers
        selectColoursButton = (Button) findViewById(R.id.select_colours);
        selectColoursButton.setOnClickListener(this);
        selectColoursButton.setVisibility(View.INVISIBLE);
        ///////////////////    MULTIPLE ANSWERS    ///////////////////////////////////////////
        if (multiple.equals("1")) {
            selectColoursButton.setVisibility(View.VISIBLE);
            cmbOpciones.setVisibility(View.INVISIBLE);
        }
        //Adapter that shows the simple Questions
        ArrayAdapter<String> adaptador2 = new ArrayAdapter<String>(this, R.layout.text_spinnner_item, listRespuestasLinkedPregunta);
        adaptador2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        cmbOpciones.setAdapter(adaptador2);
        cmbOpciones.setPrompt("Selecione una opción");
        //Increment the contadorPreguntas and is inserted into bundle
        contadorPreguntas++;
        //Inserting into bundle contadorPreguntas
        bundle.putString("contadorPreguntas", String.valueOf(contadorPreguntas));

        cmbOpciones.setOnTouchListener(spinnerOnTouch);
        cmbOpciones.setOnItemSelectedListener(spinnerOnSelected);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////Buttons action siguiente
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View arg0) {
///////////////Validaciones/////////////////////////////////////////////////////////////////
                mensajeerror = false;

                if (selectedColours.isEmpty() && multiple.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Debes escoger una opción minimo ", Toast.LENGTH_SHORT).show();
                    mensajeerror = true;
                }
                if (!mensajeerror) {
                    if (isRespuestaLibre) {
                        if (Encuesta2.this.campoLibre.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Respuesta Obligatoria", Toast.LENGTH_SHORT).show();
                            mensajeerror = true;
                        }
                    }
                }
                if (!mensajeerror) {
                    if (!dropcontestada && !multiple.equals("1") && !isRespuestaLibre) {
                        Toast.makeText(getApplicationContext(), "Debes escoger una opcion", Toast.LENGTH_SHORT).show();
                        mensajeerror = true;
                        dropcontestada = false;
                    }
                }
                if (!mensajeerror) {
                    mensajeerror = false;
                    ///////////////Validaciones///////////////////////////////////////////////////////////////////
                    pDialog = new ProgressDialog(Encuesta2.this);
                    pDialog.setMessage("Cargando....");
                    pDialog.setCancelable(true);
                    pDialog.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // do the thing that takes a long time
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.dismiss();
                                }
                            });
                        }
                    }).start();
                    if (multiple.equals("1")) {
                        onChangeSelectedColours();
                    }
                    //Buscando el id_respuesta , id_sig_pregunta con la respuesta dada y el id_pregunta (pregunta anterior).
                    listaIdRespuesta_sigPregunta = new ArrayList<String>();
                    id_preguntaAnterior = id_pregunta;
                    //insertamos la pregunta en el bundle en el caso que se quiera borrar.
                    bundle.putString("id_preguntaAnterior", id_preguntaAnterior);
                    try {
                        listaIdRespuesta_sigPregunta = db.getIdSiguientePregunta_idRespuesta(replySelected, id_pregunta);
                    } catch (Exception e) {
                        e.getCause();
                        Toast.makeText(getApplicationContext(), "No existe id_siguiente_pregunta ", Toast.LENGTH_LONG).show();
                    }

                    sig_pregunta = listaIdRespuesta_sigPregunta.get(1);
                    id_respuesta = listaIdRespuesta_sigPregunta.get(0);
                    //Inserting id_respuesta into the ListRespuestasFinales;
                    // solo si el id_respuesta es diferente a 1875 (respuesta libre) y si no es multiple
                    if (!id_respuesta.trim().equals("1875") && !multiple.equals("1")) {    //&&multiple.equals("0")
                        //Se insertan en la tabla de REALIZANDO ENCUESTA (solo si no es una respuesta abierta)
                        RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
                        realizandoEncuestaEntity.setId_encuestaRealizandoEncuesta(id_encuestaSeleccionada);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date dt = new Date();
                        realizandoEncuestaEntity.setFechaRealizandoEncuesta(dateFormat.format(dt));  //Se debe obtener la fecha.   31/10/2014
                        realizandoEncuestaEntity.setId_tiendaRealizandoEncuesta(id_tiendaSeleccionada);
                        realizandoEncuestaEntity.setId_preguntaRealizandoEncuesta(id_pregunta);
                        realizandoEncuestaEntity.setId_respuestaRealizandoEncuesta(id_respuesta);
                        realizandoEncuestaEntity.setAbiertaRealizandoEncuesta(String.valueOf(isRespuestaLibre));
                        realizandoEncuestaEntity.setIdArchivoRealizandoEncuesta(id_ArchivoSeleccionado);
                        realizandoEncuestaEntity.setLatitudRealizandoEncuesta(latitude);
                        realizandoEncuestaEntity.setLongitudRealizandoEncuesta(longitude);
                        //inserting into the table REALIZANDO ENCUESTA
                        db.addRealizandoEncuesta(realizandoEncuestaEntity);
                    }
                    bundle.putString("sig_pregunta", sig_pregunta);
                    //checando que sea una respuesta libre  para meterla a la tabla local de respuestas libres
                    if (id_respuesta.trim().equals("1875")) {
                        String textoObtenido = Encuesta2.this.campoLibre.getText().toString();
                        if (!textoObtenido.equals("")) {
                            //Creating the obj RealizandoEncuestaEntity
                            // for setting values and store it at the table REALIZANDO ENCUESTA
                            RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
                            realizandoEncuestaEntity.setId_encuestaRealizandoEncuesta(id_encuestaSeleccionada);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date dt = new Date();
                            realizandoEncuestaEntity.setFechaRealizandoEncuesta(dateFormat.format(dt));
                            realizandoEncuestaEntity.setId_tiendaRealizandoEncuesta(id_tiendaSeleccionada);
                            realizandoEncuestaEntity.setId_preguntaRealizandoEncuesta(id_pregunta);
                            realizandoEncuestaEntity.setId_respuestaRealizandoEncuesta(textoObtenido);
                            realizandoEncuestaEntity.setAbiertaRealizandoEncuesta(String.valueOf(isRespuestaLibre));
                            realizandoEncuestaEntity.setIdArchivoRealizandoEncuesta(id_ArchivoSeleccionado);
                            realizandoEncuestaEntity.setLatitudRealizandoEncuesta(latitude);
                            realizandoEncuestaEntity.setLongitudRealizandoEncuesta(longitude);
                            db.addRealizandoEncuesta(realizandoEncuestaEntity);

                        } else {

                            //Creating the obj RealizandoEncuestaEntity for setting
                            // values and store it at the table REALIZANDO ENCUESTA
                            RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
                            realizandoEncuestaEntity.setId_encuestaRealizandoEncuesta(id_encuestaSeleccionada);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date dt = new Date();
                            realizandoEncuestaEntity.setFechaRealizandoEncuesta(dateFormat.format(dt));
                            realizandoEncuestaEntity.setId_tiendaRealizandoEncuesta(id_tiendaSeleccionada);
                            realizandoEncuestaEntity.setId_preguntaRealizandoEncuesta(id_pregunta);
                            realizandoEncuestaEntity.setId_respuestaRealizandoEncuesta("");
                            realizandoEncuestaEntity.setAbiertaRealizandoEncuesta(String.valueOf(isRespuestaLibre));
                            realizandoEncuestaEntity.setIdArchivoRealizandoEncuesta(id_ArchivoSeleccionado);
                            realizandoEncuestaEntity.setLatitudRealizandoEncuesta(latitude);
                            realizandoEncuestaEntity.setLongitudRealizandoEncuesta(longitude);
                            db.addRealizandoEncuesta(realizandoEncuestaEntity);

                        }

                        //Inserting into ListRespuestasFinales
                        PreguntaAbierta preguntaAbierta = new PreguntaAbierta();
                        if (!textoObtenido.equals("")) {
                            preguntaAbierta.setRespuestaPreguntaAbierta(textoObtenido);
                        } else {
                            preguntaAbierta.setRespuestaPreguntaAbierta("");
                        }

                        db.addPreguntaAbierta(preguntaAbierta);
                    }

                    if (sig_pregunta.equals("FOTO")) {
                        Intent i = new Intent(Encuesta2.this, Foto.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtras(bundle);
                        startActivity(i);
                    } else {
                        //Passing again to another question.
                        Intent i = new Intent(Encuesta2.this, Encuesta2.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        i.putExtras(bundle);
                        startActivity(i);
                    }

                }
            }
        });
    }

    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dropcontestada = true;
            }
            return false;
        }
    };

    private AdapterView.OnItemSelectedListener spinnerOnSelected = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent,
                                   android.view.View v, int position, long id) {
            lblMensaje.setEllipsize(TextUtils.TruncateAt.END);
            lblMensaje.setSingleLine(true);
            replySelected = listRespuestasLinkedPregunta.get(position);
        }

        @Override
        public void onNothingSelected(
                AdapterView<?> parent) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_colours:
                opcionMultipleDialog();
                break;
            default:
                break;
        }
    }

    //Function  that shows the checkBoxes list
    // multiple seleccion
    protected void opcionMultipleDialog() {

        boolean[] checkedColours = new boolean[listRespuestasLinkedPregunta.size()];
        int count = listRespuestasLinkedPregunta.size();
        for (int i = 0; i < count; i++)//{
            checkedColours[i] = selectedColours.contains(listRespuestasLinkedPregunta.get(i));

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked)
                    selectedColours.add(listRespuestasLinkedPregunta.get(which));
                else
                    selectedColours.remove(listRespuestasLinkedPregunta.get(which));
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Respuestas");

        arreglo = new String[listRespuestasLinkedPregunta.size()];
        for (int j = 0; j < listRespuestasLinkedPregunta.size(); j++) {
            arreglo[j] = listRespuestasLinkedPregunta.get(j);
        }

        builder.setMultiChoiceItems(arreglo, checkedColours, coloursDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //onChangeSelectedColours (Function that gets the all the multiples answers selected)
    protected void onChangeSelectedColours() {
        String colour1 = "";

        for (int i = 0; i < selectedColours.size(); i++) {

            CharSequence colour = selectedColours.get(i);
            colour1 = String.valueOf(colour);
            String id_respuestaMultiple = "";
            id_respuestaMultiple = db.getId_respuestaMultiple(colour1);
            //insertando en la tabla
            RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
            realizandoEncuestaEntity.setId_encuestaRealizandoEncuesta(id_encuestaSeleccionada);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date dt = new Date();
            realizandoEncuestaEntity.setFechaRealizandoEncuesta(dateFormat.format(dt));  //31/10/2014
            realizandoEncuestaEntity.setId_tiendaRealizandoEncuesta(id_tiendaSeleccionada);
            realizandoEncuestaEntity.setId_preguntaRealizandoEncuesta(id_pregunta);
            realizandoEncuestaEntity.setId_respuestaRealizandoEncuesta(id_respuestaMultiple);
            realizandoEncuestaEntity.setAbiertaRealizandoEncuesta("false");
            realizandoEncuestaEntity.setIdArchivoRealizandoEncuesta(id_ArchivoSeleccionado);
            realizandoEncuestaEntity.setLatitudRealizandoEncuesta(latitude);
            realizandoEncuestaEntity.setLongitudRealizandoEncuesta(longitude);
            db.addRealizandoEncuesta(realizandoEncuestaEntity);
        }
        stringBuilder.append(colour1 + " ,");
        lblMensaje.setEllipsize(TextUtils.TruncateAt.END);
        lblMensaje.setSingleLine(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle extras = getIntent().getExtras();
            id_preguntaAnterior = extras.getString("id_preguntaAnterior");
            db.deletesRecodsRelatedToIdPregunta(id_preguntaAnterior);
        }
        super.onKeyDown(keyCode, event);
        return true;
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
