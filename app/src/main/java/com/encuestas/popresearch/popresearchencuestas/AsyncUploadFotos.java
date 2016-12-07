package com.encuestas.popresearch.popresearchencuestas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import AsynckData.ServiceHandler;
import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by jesus.hernandez on 28/09/16.
 * Clase para sincronizar las fotos con el servidor
 */
class AsyncUploadFotos extends AsyncTask<Void, Void, Boolean> {

    public ProgressDialog pDialog ;
    private Context context ;
    private ArrayList<NameValuePair> data;
    private String TAG = getClass().getSimpleName();
    private String Url;
    private int count;
    private int mContador;

    public AsyncUploadFotos(Context context , ArrayList<NameValuePair> data, String URL , int contador ) {
        this.context = context;
        this.data = data;
        this.Url=URL;
        this.count = contador;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Enviando Fotos ");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);

        if(count == mContador) {
            Toast.makeText(context, "Fotos enviadas correctamente", Toast.LENGTH_SHORT).show();
        }
        pDialog.hide();
        pDialog.dismiss();
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        Boolean respuesta = false;
        ServiceHandler jsonParser = new ServiceHandler();
        String jsonRes = jsonParser.makeServiceCall(Url, ServiceHandler.POST, data);
        mContador = 0 ;
        try {

            JSONObject jsonObject = new JSONObject(jsonRes);
            JSONObject result = jsonObject.getJSONObject("result");
            String success = result.getString("success");

            respuesta = true;
            mContador++;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}
