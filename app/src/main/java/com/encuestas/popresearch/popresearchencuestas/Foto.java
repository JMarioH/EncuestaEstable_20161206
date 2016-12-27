package com.encuestas.popresearch.popresearchencuestas;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import DB.Dao;
import Entity.FotoEncuesta;

/**
 * Created by Admin on 29/09/2015.
 */
public class Foto extends AppCompatActivity {

    Button btonEnviar;
    Button btonFoto;
    Button btnSiguiente;
    Bundle bundle = new Bundle();
    String id_ArchivoSeleccionado;
    String id_encuestaSeleccionada;
    String id_tiendaSeleccionada;
    String mobile;
    String id_preguntaAnterior;
    ArrayList<String> listaPreguntasFinales;
    ArrayList<String> listaRespuestasFinales;
    ArrayList<String> listaPreguntaAbierta;
    Dao db;
    Boolean banderaFotoTomada = false;
    ImageView imgView,imgview2,imgView3,imgView4,imgView5;
    public byte[] byteArray;
    ArrayList<String> arrayFotos , arrayNombrefoto;
    String ba1;
    int numFotos = 0;
    FotoEncuesta fotoEncuesta;
    String TAG = getClass().getSimpleName();
    private int permissionCheck  ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        db = new Dao(this);
        arrayFotos = new ArrayList<>();
        arrayNombrefoto = new ArrayList<>();
        fotoEncuesta = new FotoEncuesta().getInstace();
      //  txtNumfotos = (TextView) findViewById(R.id.txt_numeroFotos);

        Bundle extras = getIntent().getExtras();

        imgView = (ImageView) findViewById(R.id.imageView1);
        imgview2= (ImageView) findViewById(R.id.imageView2);
        imgView3 = (ImageView) findViewById(R.id.imageView3);
        imgView4 = (ImageView) findViewById(R.id.imageView4);
        imgView5 = (ImageView) findViewById(R.id.imageView5);

        id_ArchivoSeleccionado = extras.getString("id_archivoSeleccionado");
        bundle.putString("id_archivoSeleccionado", id_ArchivoSeleccionado);
        id_encuestaSeleccionada = extras.getString("id_encuestaSeleccionada");
        bundle.putString("id_encuestaSeleccionada", id_encuestaSeleccionada);

        mobile = extras.getString("mobile");
        bundle.putString("mobile", mobile);

        id_tiendaSeleccionada = extras.getString("id_tiendaSeleccionada");
        bundle.putString("id_tiendaSeleccionada", id_tiendaSeleccionada);

        listaPreguntasFinales = extras.getStringArrayList("listaPreguntasFinales");
        bundle.putStringArrayList("listaPreguntasFinales", listaPreguntasFinales);

        listaRespuestasFinales = extras.getStringArrayList("listaRespuestasFinales");
        bundle.putStringArrayList("listaRespuestasFinales", listaRespuestasFinales);

        listaPreguntaAbierta = extras.getStringArrayList("listaPreguntaAbierta");
        bundle.putStringArrayList("listaPreguntaAbierta", listaPreguntaAbierta);


                /* Boton Enviar a FinEncuesta.class */
        btonEnviar = (Button) findViewById(R.id.btnEnviar);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);

        btonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Foto.this, FinEncuesta.class);     // Enviar.class
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }

        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Foto.this, FinEncuesta.class);     // Enviar.class
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        /* Tomar foto*/
        if(permissionCheck == -1) {
            Toast.makeText(getBaseContext(),"Debe proporcionar permisos para usar la camara ",Toast.LENGTH_LONG).show();
        }
        btonFoto = (Button) findViewById(R.id.btnFoto);

            btonFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(permissionCheck == 0) {
                        open();
                    }else{
                        Toast.makeText(getBaseContext(),"Debe proporcionar permisos para usar la camara ",Toast.LENGTH_LONG).show();
                    }
                }
            });

    } //Ends onCreate
    @Override
    protected void onResume() {
        super.onResume();
       permissionCheck = ContextCompat.checkSelfPermission(Foto.this,Manifest.permission.CAMERA);
    }
    //Open
    public void open() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);
    }

    //ActivityResult Method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    File file = new File(Environment.getExternalStorageDirectory(), "/ImagenesEncuesta/" + id_encuestaSeleccionada + "/" + id_tiendaSeleccionada); //   990456 / id_tiendaSeleccionada
                    String nombreFoto =  String.valueOf(System.currentTimeMillis()); // nombre del archivo

                    banderaFotoTomada = true;
                    Bitmap newBitmap = redimensionarIMG(bitmap,200,300);
                    if (arrayFotos.size() == 0) {
                        imgView.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 1){
                        imgview2.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 2){
                         imgView3.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() == 3){
                        imgView4.setImageBitmap(newBitmap);
                    }else if(arrayFotos.size() ==4){
                        imgView5.setImageBitmap(newBitmap);
                    }
                    String fname = nombreFoto + ".jpg";
                    File fileFinal = new File(file, fname);
                    if (fileFinal.exists()) fileFinal.delete();

                    byteArray = bytes.toByteArray();
                    ba1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    arrayFotos.add(ba1);
                    arrayNombrefoto.add(nombreFoto);
                    if(arrayFotos.size() == 5){

                        fotoEncuesta.setIdEstablecimiento(id_tiendaSeleccionada);
                        fotoEncuesta.setIdEncuesta(id_encuestaSeleccionada);
                        fotoEncuesta.setNombre(arrayNombrefoto);
                        fotoEncuesta.setArrayFotos(arrayFotos);
                        btonFoto.setVisibility(View.GONE);
                    }else{

                        fotoEncuesta.setIdEstablecimiento(id_tiendaSeleccionada);
                        fotoEncuesta.setIdEncuesta(id_encuestaSeleccionada);
                        fotoEncuesta.setNombre(arrayNombrefoto);
                        fotoEncuesta.setArrayFotos(arrayFotos);
                    }

                    Toast.makeText(Foto.this, "Foto guardada en el dispositivo !!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle extras = getIntent().getExtras();
            id_preguntaAnterior = extras.getString("id_preguntaAnterior");
            //Deletes everything related to this id_pregunta
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
            Intent intent = new Intent(this, Principal2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap redimensionarIMG(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = newWidth / width;
        float scaleHeight =  newHeigth / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

}
