package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Entity.Cliente;
import Entity.EncuestaResultadosPreEntity;
import Entity.FotoStrings;
import Entity.GeoRegister;
import Entity.HiScreenEntity;
import Entity.PreguntaAbierta;
import Entity.PreguntaUniversoEntity;
import Entity.Proyecto;
import Entity.RealizandoEncuestaEntity;
import Entity.RespuestaUniversoEntity;
import Entity.TelefonoEntity;
import Entity.TipoEncuestaEntity;
//import db.BdEncuestas;
/**
 * Created by Admin on 28/09/2015.
 */
public class Dao {
    // Database fields
    private SQLiteDatabase database;
    private BdEncuestas bdEncuestas;
    //Listas
    public ArrayList<PreguntaUniversoEntity> listaPreguntasEncuesta  = new ArrayList<PreguntaUniversoEntity>();
    public ArrayList<RespuestaUniversoEntity> listaRespuestasEncuesta = new ArrayList<RespuestaUniversoEntity>();

    //Constructor
    public Dao(Context context) {
        bdEncuestas = new BdEncuestas(context);
        this.database = this.bdEncuestas.getWritableDatabase();
    }

    //open connection
    public void open() throws SQLException {
        database = bdEncuestas.getWritableDatabase();
    }

    //close connection
    public void close() {
        database.close();
    }

    //deletes tables
    public void deletesTables(){
        open();
        database.delete(BdEncuestas.TABLE_GEO,null,null);
        database.delete(BdEncuestas.TABLE_FOTO_ENCUESTA,null,null);
        database.delete(BdEncuestas.TABLE_CLIENTES,null,null);
        database.delete(BdEncuestas.TABLE_PROYECTOS,null,null);
        database.delete(BdEncuestas.TABLE_TIPO_ENCUESTA,null,null);
        database.delete(BdEncuestas.TABLE_HISCREEN,null,null);
        database.delete(BdEncuestas.TABLE_PREGUNTAS_UNIVERSO,null,null);
        database.delete(BdEncuestas.TABLE_PREGUNTAS_ENCUESTA,null,null);
        database.delete(BdEncuestas.TABLE_RESPUESTAS_UNIVERSO,null,null);
        database.delete(BdEncuestas.TABLE_RESPUESTAS_ENCUESTA,null,null);
        database.delete(BdEncuestas.TABLE_PREGUNTA_ABIERTA,null,null);
        // database.delete(BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE,null,null);   ///// -----
        database.delete(BdEncuestas.TABLE_REALIZANDO_ENCUESTA,null,null);
        //database.delete(BdEncuestas.TABLE_TELEFONO_LOGGED,null,null);
        //database.delete(BdEncuestas.TABLE_PREGUNTAS_ENCUESTA_SELECTED_INTO_TABLE,null,null);
        close();

    }
    /*
     * add table geo
     */
    public void insertGeos(GeoRegister geoRegister){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.stringEncuesta ,geoRegister.getIdEncuesta());
        values.put(BdEncuestas.stringEstablecimiento ,geoRegister.getIdEstablecimiento());
        values.put(BdEncuestas.stringLatitud ,geoRegister.getLatitud());
        values.put(BdEncuestas.stringLongitud ,geoRegister.getLongitud());
        database.insert(BdEncuestas.TABLE_GEO,null,values);
        database.close();
    }

    public ArrayList<GeoRegister> getAllGeos(){
        open();
        ArrayList<GeoRegister> geoRegisters = new ArrayList<>();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_GEO ;
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                GeoRegister geoRegister = new GeoRegister();
                geoRegister.setIdEncuesta(cursor.getInt(1));
                geoRegister.setIdEstablecimiento(cursor.getInt(2));
                geoRegister.setLatitud(cursor.getString(3));
                geoRegister.setLongitud(cursor.getString(4));
                geoRegisters.add(geoRegister);
            }while (cursor.moveToNext());
        }
        close();
        return geoRegisters;
    }

    public int getCountGeos(){
        int result;
        String countQuery = "SELECT id FROM " + BdEncuestas.TABLE_GEO;
        Cursor cursor = database.rawQuery(countQuery, null);
        result = cursor.getCount();
        cursor.close();
        return result;
    }
    public GeoRegister getGeoRegister(int idEncuesta, int idEstablecimiento){
        open();
        GeoRegister geoRegister = new GeoRegister();
        String query = "SELECT * FROM "+BdEncuestas.TABLE_GEO + " WHERE  idEncuesta = '"+ idEncuesta +"' and idEstablecimiento= '"+idEstablecimiento+"' ;";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                geoRegister.setIdEncuesta(cursor.getInt(1));
                geoRegister.setIdEstablecimiento(cursor.getInt(2));
                geoRegister.setLatitud(cursor.getString(3));
                geoRegister.setLongitud(cursor.getString(4));

            }while (cursor.moveToNext());
        }
        close();
        return geoRegister;
    }
    // add fotoEncuesta
    public void addFotoEncuesta(FotoStrings fotoEncuesta){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idEstablecimiento, fotoEncuesta.getIdEstablecimiento());
        values.put(BdEncuestas.idEncuestaFoto, fotoEncuesta.getIdEncuesta());
        values.put(BdEncuestas.nombre,fotoEncuesta.getNombre());
        values.put(BdEncuestas.arrayString, String.valueOf(fotoEncuesta.getStringFoto()));
        database.insert(BdEncuestas.TABLE_FOTO_ENCUESTA,null,values);
        database.close();
    }

    // get fotoEncuesta

    public ArrayList<FotoStrings> getAllFotos(){
        open();
        ArrayList<FotoStrings> fotoEncuestas = new ArrayList<>();
        String query = "SELECT * FROM "+ BdEncuestas.TABLE_FOTO_ENCUESTA;

        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                FotoStrings fotoEnc = new FotoStrings();
                fotoEnc.setIdEstablecimiento(cursor.getString(1));
                fotoEnc.setIdEncuesta(cursor.getString(2));
                fotoEnc.setNombre(cursor.getString(3));
                fotoEnc.setStringFoto(cursor.getString(4));
                fotoEncuestas.add(fotoEnc);
            }while (cursor.moveToNext());
        }
        close();
        return fotoEncuestas;
    }
    ////////////////////////////////////////////////////////// FOTOS /////////////////////////////////////////////////////////
    public int getfotosCount(){

        int result;
        String countQuery = "SELECT id FROM " + BdEncuestas.TABLE_FOTO_ENCUESTA;
        Cursor cursor = database.rawQuery(countQuery, null);
        result = cursor.getCount();
        cursor.close();
        // return count
        return result;
    }

    ////////////////////////////////////////////////////////// CLIENTES //////////////////////////////////////////////////////

    //Adds a client
    public void addCliente(Cliente cliente){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.nombreCliente,cliente.getNombreCliente());
        database.insert(BdEncuestas.TABLE_CLIENTES, null, values);
        close();
    }
    //Gets all clients
    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new LinkedList<Cliente>();
        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_CLIENTES;
        Cursor cursor = database.rawQuery(query, null);

        Cliente cliente = null;
        if (cursor.moveToFirst()) {
            do {
                cliente = new Cliente();
                cliente.setNombreCliente(cursor.getString(0));
                clientes.add(cliente);
            } while (cursor.moveToNext());
        }
        close();
        // return clientes
        return clientes;
    }
    //////////////////////////////////////////////////// PROJECTS //////////////////////////////////////////////////////

    //Adds projects
    public void addProyecto(Proyecto proyecto){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.nombreProyecto,proyecto.getNombreProyecto());
        values.put(BdEncuestas.id_proyecto,proyecto.getId_proyecto());
        values.put(BdEncuestas.cliName,proyecto.getCliName());
        database.insert(BdEncuestas.TABLE_PROYECTOS, null, values);
        close();
    }

    //Gets all projects
    public List<Proyecto> getAllProyectos(String nombreCliente) {
        List<Proyecto> proyectos = new ArrayList<Proyecto>();
        open();

        String query = "SELECT * FROM " + BdEncuestas.TABLE_PROYECTOS + " WHERE "+BdEncuestas.cliName+" = ?";

        Cursor cursor = database.rawQuery(query, new String[] {nombreCliente});
        Proyecto proyecto = null;
        if (cursor.moveToFirst()) {

            do {

                proyecto = new Proyecto();
                proyecto.setNombreProyecto(cursor.getString(0));
                proyecto.setId_proyecto(cursor.getString(1));
                proyecto.setCliName(cursor.getString(2));
                proyectos.add(proyecto);
            } while (cursor.moveToNext());
        }

        close();

        return proyectos;
    }



    //Gets the id_project
    public int getIdProject(String nameProject){
        int idProject = 0;
        open();

        String query = "SELECT id_proyecto FROM " + BdEncuestas.TABLE_PROYECTOS + " WHERE "+BdEncuestas.nombreProyecto+" = ?";
        Cursor cursor = database.rawQuery(query, new String[] {nameProject});
        if (cursor.moveToFirst()) {
            do {
                idProject = Integer.parseInt(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return idProject;
    }

    ////////////////////////////////////////////////// TIPO ENCUESTA /////////////////////////////////////////////////////////

    //Adds TipoEncuesta
    public void addTipoEncuesta(TipoEncuestaEntity tipoEncuestaEntity){

        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.nombreEncuesta,tipoEncuestaEntity.getNombreEncuesta());
        values.put(BdEncuestas.idArchivoEncuesta,tipoEncuestaEntity.getIdArchivoEncuesta());
        values.put(BdEncuestas.idEncuesta,tipoEncuestaEntity.getIdEncuesta());
        values.put(BdEncuestas.numeroTelEncuesta,tipoEncuestaEntity.getNumeroTelefono());
        values.put(BdEncuestas.nombreClienteEncuesta,tipoEncuestaEntity.getNombreCliente());
        values.put(BdEncuestas.idProyectoEncuesta,tipoEncuestaEntity.getId_proyecto());
        database.insert(BdEncuestas.TABLE_TIPO_ENCUESTA, null, values);
        close();
    }

    //Gets Tipo encuestas related to telefono, cliente and id_archivo
    public List<TipoEncuestaEntity> getTipoEncuesta(String mobile,String cliente, String id_proyecto) {
        List<TipoEncuestaEntity> tipoEncuestaList = new ArrayList<TipoEncuestaEntity>();
        open();

        String query = "SELECT distinct * FROM " + BdEncuestas.TABLE_TIPO_ENCUESTA + " WHERE "+BdEncuestas.numeroTelEncuesta+" = ? AND "+BdEncuestas.nombreClienteEncuesta+" = ? AND "+BdEncuestas.idProyectoEncuesta+" = ?";

        Cursor cursor = database.rawQuery(query, new String[] {mobile,cliente,id_proyecto});
        TipoEncuestaEntity tipoEncuestaEntity = null;

        if (cursor.moveToFirst()) {

            do {
                tipoEncuestaEntity = new TipoEncuestaEntity();
                tipoEncuestaEntity.setNombreEncuesta(cursor.getString(0));
                tipoEncuestaEntity.setIdArchivoEncuesta(cursor.getString(1));
                tipoEncuestaEntity.setIdEncuesta(cursor.getString(2));
                tipoEncuestaEntity.setNumeroTelefono(cursor.getString(3));
                tipoEncuestaEntity.setNombreCliente(cursor.getString(4));
                tipoEncuestaEntity.setId_proyecto(cursor.getString(5));
                tipoEncuestaList.add(tipoEncuestaEntity );
            } while (cursor.moveToNext());
        }

        close();
        return tipoEncuestaList;
    }

    //Gets id_archivo with the tipoEncuestaSelected and idArchivoIdTienda, idArchivoIdTiendaEncuesta
    public List<String> getIdArchivoIdEncuesta(String tipoEncuestaSelected){
        List<String> idArchivoIdEncuestaList = new ArrayList<String>();
        String id_archivo = "";
        String id_encuesta = "";
        String idArchivoIdTienda = "";

        open();
        String query = "SELECT id_archivoEncuesta, id_encuesta  FROM " + BdEncuestas.TABLE_TIPO_ENCUESTA + " WHERE "+BdEncuestas.nombreEncuesta+" = ? ";

        Cursor cursor = database.rawQuery(query, new String[] {tipoEncuestaSelected});

        if (cursor.moveToFirst()) {
            do {
                id_archivo = cursor.getString(0);
                idArchivoIdEncuestaList.add(id_archivo);
                id_encuesta = cursor.getString(1);
                idArchivoIdEncuestaList.add(id_encuesta);
            } while (cursor.moveToNext());
        }
        close();
        return idArchivoIdEncuestaList;//id_archivo;
    }

    //////////////////////////////////////////////////// ENCUESTAS (HiScreen) /////////////////////////////////////

    //Adds Encuesta(hiScreen)
    public void addEncuestaHiScreen(HiScreenEntity hiScreen){

        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idTiendaHiscreen,hiScreen.getIdTienda());
        values.put(BdEncuestas.nombreEncuestaHiscreen,hiScreen.getNombreEncuesta());
        values.put(BdEncuestas.id_ArchivoHiscreen,hiScreen.getId_archivo());
        database.insert(BdEncuestas.TABLE_HISCREEN, null, values);
        close();
    }

    //bandera para cambiar el status
    public void putCheckEncuesta(String idTienda) {
        open();
        String strSQL = "UPDATE "+ BdEncuestas.TABLE_HISCREEN  +" SET check_realizada = 1 WHERE idTiendaHiScreen = '"+ idTienda+"'";
        database.execSQL(strSQL);
        close();
    }

    //Gets Encuestas related to the telephone number
    public List<HiScreenEntity> getEncuestas(String id_ArchivoSeleccionado) { //,String cliente, String id_proyecto
        List<HiScreenEntity> encuestasList = new ArrayList<HiScreenEntity>();
        open();
        String query = "SELECT  * FROM " + BdEncuestas.TABLE_HISCREEN+ " WHERE "+BdEncuestas.id_ArchivoHiscreen+" = ? AND check_realizada = '0'" ; //+ " WHERE "+BdEncuestas.numeroTelEncuesta+" = ? AND "+BdEncuestas.nombreClienteEncuesta+" = ? AND "+BdEncuestas.idProyectoEncuesta+" = ?";
        Cursor cursor = database.rawQuery(query,new String[] {id_ArchivoSeleccionado});  // 4551 //  new String[] {mobile} ---         ,cliente,id_proyecto
        HiScreenEntity hiScreenEntity = null;
        if (cursor.moveToFirst()) {
            do {
                hiScreenEntity = new HiScreenEntity();
                hiScreenEntity.setIdTienda(cursor.getString(0));
                hiScreenEntity.setNombreEncuesta(cursor.getString(1));
                hiScreenEntity.setId_archivo(cursor.getString(2));
                hiScreenEntity.setCheckedEncuesta(String.valueOf(cursor.getString(3)));
                encuestasList.add(hiScreenEntity);
            } while (cursor.moveToNext());
        }
        close();
        return encuestasList;
    }


    /////////////////////////////////////////////// PREGUNTAS  ///////////////////////////////////////////////
    //Adds Preguntas (UNIVERSO DE PREGUNTAS DE ESE TELEFONO)
    public void addPreguntas(PreguntaUniversoEntity pregunta){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idPregunta,pregunta.getId_pregunta());
        values.put(BdEncuestas.pregunta,pregunta.getPregunta());
        values.put(BdEncuestas.multiple,pregunta.getMultiple());
        values.put(BdEncuestas.orden,pregunta.getOrden());
        values.put(BdEncuestas.idEncuestaPreguntaUniverso,pregunta.getId_encuesta());
        open();
        database.insert(BdEncuestas.TABLE_PREGUNTAS_UNIVERSO, null, values);
        close();
    }

    //Getting the preguntas linked to the mobile selected and id_encuesta selected. (for showing them at the label of questions) INTO TABLE_PREGUNTAS_ENCUESTA
    public void getPreguntasEncuestaSelectedIntoTable(String id_encuesta){

        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_PREGUNTAS_UNIVERSO+ " WHERE "+BdEncuestas.idEncuestaPreguntaUniverso+" = " + id_encuesta ;
        //Cursor cursor = database.rawQuery(query,new String[] {id_encuesta}); //id_encuesta
        Cursor cursor  = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put(BdEncuestas.idPreguntaEncuesta, cursor.getString(0));
                values.put(BdEncuestas.preguntaEncuesta, cursor.getString(1));
                values.put(BdEncuestas.multipleEncuesta, cursor.getString(2));
                values.put(BdEncuestas.ordenEncuesta, cursor.getString(3));
                values.put(BdEncuestas.idEncuestaEncuesta, cursor.getString(4));

                PreguntaUniversoEntity pregunta = new PreguntaUniversoEntity();
                pregunta.setId_pregunta(cursor.getString(0));
                pregunta.setPregunta(cursor.getString(1));
                pregunta.setMultiple(cursor.getString(2));
                pregunta.setOrden(cursor.getString(3));
                pregunta.setId_encuesta(cursor.getString(4));

                database.insert(BdEncuestas.TABLE_PREGUNTAS_ENCUESTA, null, values);
                //Insertandolo en la Lista dinamica listaPreguntasEncuesta
                if(!listaPreguntasEncuesta.contains(pregunta)){   //!HiScreen.listaPreguntasEncuesta.contains(pregunta)
                    listaPreguntasEncuesta.add(pregunta);  // HiScreen.listaPreguntasEncuesta
                }

            } while (cursor.moveToNext());
        }
        close();
    }

    //Getting the count of rows of the table   TABLE_PREGUNTAS_ENCUESTA
    public int getCountTABLE_PREGUNTAS_ENCUESTA(){
        int total;
        open();
        Cursor dataCount = database.rawQuery("SELECT id_preguntaEncuesta FROM " + BdEncuestas.TABLE_PREGUNTAS_ENCUESTA, null);
        total = dataCount.getCount();
        close();
        return total;
    }

    //Getting the count of rows of table TABLE_RESPUESTAS_ENCUESTA
    public int getCountTABLE_RESPUESTAS_ENCUESTA(){
        int total;
        open();
        Cursor dataCount = database.rawQuery("SELECT id_preguntaEncuesta FROM " + BdEncuestas.TABLE_RESPUESTAS_ENCUESTA, null);
        total = dataCount.getCount();
        close();
        return total;
    }


    //Obtiene la primer pregunta de la lista
    public PreguntaUniversoEntity getPrimerPreguntaLista(){
        PreguntaUniversoEntity pregunta = null;
        for(int i = 0; i<listaPreguntasEncuesta.size();i++){  //HiScreen.listaPreguntasEncuesta
            pregunta = new PreguntaUniversoEntity();
            pregunta = listaPreguntasEncuesta.get(i);  // HiScreen.listaPreguntasEncuesta
            if(pregunta.getOrden().equals("1")){
                return pregunta;
            }
        }
        return pregunta;    //return null;
    }


    //Obtiene la primera pregunta de la tabla (TABLE_PREGUNTAS_ENCUESTA) de preguntas seleccionadas
    public List<String> getPrimerPregunta(){
        String orden = "1";
        List<String> PrimerPregunta_idPregunta_multiple = new ArrayList<String>();
        open();
        String query = "SELECT  id_preguntaEncuesta , preguntaEncuesta , multipleEncuesta FROM " + BdEncuestas.TABLE_PREGUNTAS_ENCUESTA+ " WHERE "+BdEncuestas.ordenEncuesta+" = ? ";
        Cursor cursor = database.rawQuery(query, new String[] {orden});
        if (cursor.moveToFirst()) {
            do {
                PrimerPregunta_idPregunta_multiple.add(cursor.getString(0));
                PrimerPregunta_idPregunta_multiple.add(cursor.getString(1));
                PrimerPregunta_idPregunta_multiple.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }
        close();
        return PrimerPregunta_idPregunta_multiple;
    }

    //Obtiene el id_siguientePregunta y id_respuesta con la respuesta seleccionada
    public List<String> getIdSiguientePregunta_idRespuesta(String respuestaSeleccionada, String id_preguntaAnterior){
        List<String> listaIdSiguientePregunta_idRespuesta = new ArrayList<String>();
        open();
        String query = "SELECT id_respuestaEncuesta , sigPreguntaEncuesta  FROM " + BdEncuestas.TABLE_RESPUESTAS_ENCUESTA+ " WHERE "+BdEncuestas.RespuestaRespuestasEncuesta +" = ? AND "+BdEncuestas.idPreguntaRespuestaEncuesta +" = ?";   //, id_respuestaEncuesta
        Cursor cursor = database.rawQuery(query, new String[] {respuestaSeleccionada,id_preguntaAnterior});
        if (cursor.moveToFirst()) {
            do {
                listaIdSiguientePregunta_idRespuesta.add(cursor.getString(0));
                listaIdSiguientePregunta_idRespuesta.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        close();
        return listaIdSiguientePregunta_idRespuesta;
    }


    //Obtiene la  pregunta de la tabla (TABLE_PREGUNTAS_ENCUESTA) de preguntas seleccionadas
    public List<String> getPregunta_getMultiple(String idPregunta){
        List<String> listaGetPregunta_GetMultiple = new ArrayList<String>();
        open();
        String query = "SELECT preguntaEncuesta , multipleEncuesta FROM " + BdEncuestas.TABLE_PREGUNTAS_ENCUESTA+ " WHERE "+BdEncuestas.idPreguntaEncuesta+" = ? ";
        Cursor cursor = database.rawQuery(query, new String[] {idPregunta});

        if (cursor.moveToFirst()) {
            do {
                listaGetPregunta_GetMultiple.add(cursor.getString(0));
                //Inserting multiple value
                listaGetPregunta_GetMultiple.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }

        close();
        return listaGetPregunta_GetMultiple;
    }

    //Obtiene la pregunta de la lista de la lista de preguntas
    public PreguntaUniversoEntity getPregunta_getMultipleLista(String idPregunta){
        PreguntaUniversoEntity pregunta = new PreguntaUniversoEntity();
        for(int i = 0; i< listaPreguntasEncuesta.size();i++){   // HiScreen.listaPreguntasEncuesta
            pregunta = listaPreguntasEncuesta.get(i);  // HiScreen.listaPreguntasEncuesta
            if(pregunta.getId_pregunta().equals(idPregunta)){
                return pregunta;
            }
        }
        return null;
    }


    /////////////////////////////////////////////////  RESPUESTAS ////////////////////////////////////////
    //Adds Respuestas (UNIVERSO DE RESPUESTAS DE ESE TELEFONO)
    public void addRespuestas(RespuestaUniversoEntity respuesta){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idPreguntaRespuestasUniverso,respuesta.getId_pregunta());
        values.put(BdEncuestas.idRespuestaRespuestasUniverso,respuesta.getId_respuesta());
        values.put(BdEncuestas.RespuestaRespuestasUniverso,respuesta.getRespuesta());
        values.put(BdEncuestas.siguientePreguntaRespuestasUniverso,respuesta.getSig_pregunta());
        values.put(BdEncuestas.respuestaLibreRespuestasUniverso,respuesta.getRespuestaLibre());
        values.put(BdEncuestas.idEncuestaRespuestasUniverso,respuesta.getId_encuesta());
        database.insert(BdEncuestas.TABLE_RESPUESTAS_UNIVERSO, null, values);
        close();
    }


    //Getting the RESPUESTAS linked to the mobile selected and id_encuesta selected. (for showing them at the Spinner ) INTO TABLE_RESPUESTAS_ENCUESTA
    public void getRespuestasEncuestaSelectedIntoTable(String id_encuesta){
        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_RESPUESTAS_UNIVERSO+ " WHERE "+BdEncuestas.idEncuestaRespuestasUniverso+" = " + id_encuesta ; //+ " WHERE "+BdEncuestas.numeroTelEncuesta+" = ? AND "+BdEncuestas.nombreClienteEncuesta+" = ? AND "+BdEncuestas.idProyectoEncuesta+" = ?";
        Cursor cursor = database.rawQuery(query,null); //id_encuesta
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put(BdEncuestas.idPreguntaRespuestaEncuesta,cursor.getString(0));
                values.put(BdEncuestas.idRespuestaRespuestasEncuesta,cursor.getString(1));
                values.put(BdEncuestas.RespuestaRespuestasEncuesta,cursor.getString(2));
                values.put(BdEncuestas.siguientePreguntaRespuestasEncuesta,cursor.getString(3));
                values.put(BdEncuestas.respuestaLibreRespuestasEncuesta,cursor.getString(4));
                values.put(BdEncuestas.idEncuestaRespuestasEncuesta,cursor.getString(5));

                //Insertandolo en la Lista dinamica listaPreguntasEncuesta
                RespuestaUniversoEntity respuesta = new RespuestaUniversoEntity();
                respuesta.setId_pregunta(cursor.getString(0));
                respuesta.setId_respuesta(cursor.getString(1));
                respuesta.setRespuesta(cursor.getString(2));
                respuesta.setSig_pregunta(cursor.getString(3));
                respuesta.setRespuestaLibre(cursor.getString(4));
                respuesta.setId_encuesta(cursor.getString(5));

                database.insert(BdEncuestas.TABLE_RESPUESTAS_ENCUESTA, null, values);


                if(!listaRespuestasEncuesta.contains(respuesta)){ //HiScreen.listaRespuestasEncuesta
                    listaRespuestasEncuesta.add(respuesta); // HiScreen.listaRespuestasEncuesta
                }
            } while (cursor.moveToNext());
        }
        close();
    }


    //Getting the answers linked to the id_question from the table TABLE_PREGUNTAS_ENCUESTA
    public List<String> getRespuestasLinkedToPregunta(String id_pregunta){
        List<String> respuestasLinkedToPregutasList = new ArrayList<String>();
        open();
        String query = "SELECT  DISTINCT(respuestaEncuesta) FROM " + BdEncuestas.TABLE_RESPUESTAS_ENCUESTA+ " WHERE "+BdEncuestas.idPreguntaRespuestaEncuesta+" = ? ";  //TABLE_RESPUESTAS_UNIVERSO
        Cursor cursor = database.rawQuery(query, new String[] {id_pregunta});
        if (cursor.moveToFirst()) {
            do {
                respuestasLinkedToPregutasList.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        close();
        return respuestasLinkedToPregutasList;
    }


    //Getting the answers linked to the id_question from the list of answers
    public List<String> getRespuestasLinkedToPreguntaList(String id_pregunta){
        List<String> respuestasLinkedToPreguntasList = new ArrayList<String>();
        for(int i = 0; i<listaRespuestasEncuesta.size(); i++){     //HiScreen.listaRespuestasEncuesta
            RespuestaUniversoEntity respuesta = new RespuestaUniversoEntity();
            respuesta = listaRespuestasEncuesta.get(i);  //HiScreen.listaRespuestasEncuesta
            if(respuesta.getId_pregunta().equals(id_pregunta)){
                if(!respuestasLinkedToPreguntasList.contains(respuesta.getRespuesta())){
                    respuestasLinkedToPreguntasList.add(respuesta.getRespuesta());
                }
            }
        }
        return respuestasLinkedToPreguntasList;
    }


    //Obteniendo el id_respuesta de la respuesta seleccionada para el la pregunta multiple
    public String getId_respuestaMultiple(String respuestaSeleccionada){
        String id_respuesta = "";
        open();
        String query = "SELECT  id_respuestaEncuesta FROM " + BdEncuestas.TABLE_RESPUESTAS_ENCUESTA+ " WHERE "+BdEncuestas.RespuestaRespuestasEncuesta+" = ? ";  //TABLE_RESPUESTAS_UNIVERSO
        Cursor cursor = database.rawQuery(query, new String[] {respuestaSeleccionada});

        if (cursor.moveToFirst()) {
            do {
                id_respuesta = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        close();
        return id_respuesta;
    }

    //Getting the next question comparing the answer given.

    ////////////////////////////////////////////// PREGUNTA ABIERTA ////////////////////////////////////////
    //Adds pregunta Abierta into the table (PREGUNTA ABIERTA)
    public void addPreguntaAbierta(PreguntaAbierta preguntaAbierta){
        open();
        ContentValues values = new ContentValues();
        //Log.d("addPreguntaAbierta ","!!");
        values.put(BdEncuestas.respuestaPreguntaAbierta,preguntaAbierta.getRespuestaPreguntaAbierta());   //preguntaAbierta.getRespuestaPreguntaAbierta()
        database.insert(BdEncuestas.TABLE_PREGUNTA_ABIERTA, null, values);
        close();
        //   Log.d("preguntaAbiertInsertada", "");
    }


    //Getting all preguntas abiertas from the Table
    public ArrayList<PreguntaAbierta> getPreguntasAbiertas(){
        ArrayList<PreguntaAbierta> preguntaAbiertaList = new ArrayList<PreguntaAbierta>();
        open();
        //PreguntaAbierta preguntaAbierta = new PreguntaAbierta();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_PREGUNTA_ABIERTA;  //+ " WHERE "+BdEncuestas.numeroTelEncuesta+" = ? AND "+BdEncuestas.nombreClienteEncuesta+" = ? AND "+BdEncuestas.idProyectoEncuesta+" = ?";
        Cursor cursor = database.rawQuery(query,null); //id_encuesta
        if (cursor.moveToFirst()) {
            do {
                PreguntaAbierta preguntaAbierta = new PreguntaAbierta();
                preguntaAbierta.setRespuestaPreguntaAbierta(cursor.getString(0));
                preguntaAbiertaList.add(preguntaAbierta);
            } while (cursor.moveToNext());
        }
        close();
        return preguntaAbiertaList;
    }


    //Inserting the final data to the TABLE_ENCUESTAS_RESULTADOS_PRE  (final data)
    public void addEncuestaResultadosPre(EncuestaResultadosPreEntity encuestaResultadoPreEntity){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idEncuestaResultadosPre,encuestaResultadoPreEntity.getIdEncuestaResultadosPre());
        values.put(BdEncuestas.fechaResultadoPre,encuestaResultadoPreEntity.getFechaResultadosPre());
        values.put(BdEncuestas.idTiendaResultadosPre,encuestaResultadoPreEntity.getIdTiendaResultadosPre());
        values.put(BdEncuestas.idPreguntaResultadosPre,encuestaResultadoPreEntity.getIdPreguntaResultadosPre());
        values.put(BdEncuestas.idRespuestaResultadosPre,encuestaResultadoPreEntity.getIdRespuestaResultadosPre());
        values.put(BdEncuestas.abiertaResultadosPre, encuestaResultadoPreEntity.getAbiertaResultadosPre());
        values.put(BdEncuestas.idArchivoResultadosPre,encuestaResultadoPreEntity.getIdArchivoResultadosPre());
        values.put(BdEncuestas.latitudResultadosPre,encuestaResultadoPreEntity.getLatitudResultadosPre());
        values.put(BdEncuestas.longitudResultadosPre,encuestaResultadoPreEntity.getLongitudResultadosPre());
        values.put(BdEncuestas.FlagEnviada,encuestaResultadoPreEntity.getFlagEnviada());

        database.insert(BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE, null, values);
        close();
    }

    //Getting the rows of Encuestas_resultados_pre
    public ArrayList<EncuestaResultadosPreEntity> getAllEncuestaResultadosPre(){
        ArrayList<EncuestaResultadosPreEntity> encuestaResultadosPreEntityList = new ArrayList<EncuestaResultadosPreEntity>();
        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE + " WHERE FlagEnviada = 0 order by idTiendaResultadosPre";
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                EncuestaResultadosPreEntity encuestaResultadosPreEntity = new EncuestaResultadosPreEntity();
                encuestaResultadosPreEntity.setIdEncuestaResultadosPre(cursor.getString(0));
                encuestaResultadosPreEntity.setFechaResultadosPre(cursor.getString(1));
                encuestaResultadosPreEntity.setIdTiendaResultadosPre(cursor.getString(2));
                encuestaResultadosPreEntity.setIdPreguntaResultadosPre(cursor.getString(3));
                encuestaResultadosPreEntity.setIdRespuestaResultadosPre(cursor.getString(4));
                encuestaResultadosPreEntity.setAbiertaResultadosPre(cursor.getString(5));
                encuestaResultadosPreEntity.setIdArchivoResultadosPre(cursor.getString(6));
                encuestaResultadosPreEntity.setLatitudResultadosPre(cursor.getString(7));
                encuestaResultadosPreEntity.setLongitudResultadosPre(cursor.getString(8));
                encuestaResultadosPreEntity.setFlagEnviada(cursor.getString(9));
                encuestaResultadosPreEntityList.add(encuestaResultadosPreEntity);
            } while (cursor.moveToNext());
        }
        close();
        return encuestaResultadosPreEntityList;
    }

    //Regresa la cantidad de id_tiendas encontrado en la tabla de pendientes
    public void addRealizandoEncuesta(RealizandoEncuestaEntity realizandoEncuestaEntity){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.idEncuestaRealizandoEncuesta,realizandoEncuestaEntity.getId_encuestaRealizandoEncuesta());
        values.put(BdEncuestas.fechaRealizandoEncuesta,realizandoEncuestaEntity.getFechaRealizandoEncuesta());
        values.put(BdEncuestas.idTiendaRealizandoEncuesta,realizandoEncuestaEntity.getId_tiendaRealizandoEncuesta());
        values.put(BdEncuestas.idPreguntaRealizandoEncuesta,realizandoEncuestaEntity.getId_preguntaRealizandoEncuesta());
        values.put(BdEncuestas.idRespuestaRealizandoEncuesta,realizandoEncuestaEntity.getId_respuestaRealizandoEncuesta());
        values.put(BdEncuestas.abiertaRealizandoEncuesta,realizandoEncuestaEntity.getAbiertaRealizandoEncuesta());
        values.put(BdEncuestas.idArchivoRealizandoEncuesta,realizandoEncuestaEntity.getIdArchivoRealizandoEncuesta());
        values.put(BdEncuestas.latitudRealizandoEncuesta,realizandoEncuestaEntity.getLatitudRealizandoEncuesta());
        values.put(BdEncuestas.longitudRealizandoEncuesta,realizandoEncuestaEntity.getLongitudRealizandoEncuesta());
        database.insert(BdEncuestas.TABLE_REALIZANDO_ENCUESTA, null, values);
        close();

    }

    //Getting the rows from the table RealizandoEncuesta
    public ArrayList<RealizandoEncuestaEntity> getRealizandoEncuestaEntity(){
        ArrayList<RealizandoEncuestaEntity> RealizandoEncuestaEntityList = new ArrayList<RealizandoEncuestaEntity>();
        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_REALIZANDO_ENCUESTA;
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                RealizandoEncuestaEntity realizandoEncuestaEntity = new RealizandoEncuestaEntity();
                realizandoEncuestaEntity.setId_encuestaRealizandoEncuesta(cursor.getString(0));
                realizandoEncuestaEntity.setFechaRealizandoEncuesta(cursor.getString(1));
                realizandoEncuestaEntity.setId_tiendaRealizandoEncuesta(cursor.getString(2));
                realizandoEncuestaEntity.setId_preguntaRealizandoEncuesta(cursor.getString(3));
                realizandoEncuestaEntity.setId_respuestaRealizandoEncuesta(cursor.getString(4));
                realizandoEncuestaEntity.setAbiertaRealizandoEncuesta(cursor.getString(5));
                realizandoEncuestaEntity.setIdArchivoRealizandoEncuesta(cursor.getString(6));
                realizandoEncuestaEntity.setLatitudRealizandoEncuesta(cursor.getString(7));
                realizandoEncuestaEntity.setLongitudRealizandoEncuesta(cursor.getString(8));
                RealizandoEncuestaEntityList.add(realizandoEncuestaEntity);
            }while (cursor.moveToNext());
        }
        return RealizandoEncuestaEntityList;
    }


    //pasa informacion de la tabla TABLE_REALIZANDO_ENCUESTA a la tabla TABLE_ENCUESTAS_RESULTADOS_PRE
    public void passTableRealinzandoEncuestaToTableEncuestasResultadosPre(){
        ArrayList<RealizandoEncuestaEntity> RealizandoEncuestaEntityList  = getRealizandoEncuestaEntity();
        open();
        for(int i = 0; i<RealizandoEncuestaEntityList.size();i++){
            //parametros a inserta en la tabla final (TABLE_ENCUESTAS_RESULTADOS_PRE)
            ContentValues values = new ContentValues();
            RealizandoEncuestaEntity realizandoEncuestasEntity = new RealizandoEncuestaEntity();
            realizandoEncuestasEntity = RealizandoEncuestaEntityList.get(i);
            values.put(BdEncuestas.idEncuestaResultadosPre,realizandoEncuestasEntity.getId_encuestaRealizandoEncuesta());
            values.put(BdEncuestas.fechaResultadoPre,realizandoEncuestasEntity.getFechaRealizandoEncuesta());
            values.put(BdEncuestas.idTiendaResultadosPre,realizandoEncuestasEntity.getId_tiendaRealizandoEncuesta());
            values.put(BdEncuestas.idPreguntaResultadosPre,realizandoEncuestasEntity.getId_preguntaRealizandoEncuesta());
            values.put(BdEncuestas.idRespuestaResultadosPre ,realizandoEncuestasEntity.getId_respuestaRealizandoEncuesta());
            values.put(BdEncuestas.abiertaResultadosPre ,realizandoEncuestasEntity.getAbiertaRealizandoEncuesta());
            values.put(BdEncuestas.idArchivoResultadosPre ,realizandoEncuestasEntity.getIdArchivoRealizandoEncuesta());
            values.put(BdEncuestas.latitudResultadosPre ,realizandoEncuestasEntity.getLatitudRealizandoEncuesta());
            values.put(BdEncuestas.longitudResultadosPre ,realizandoEncuestasEntity.getLongitudRealizandoEncuesta());
            values.put(BdEncuestas.FlagEnviada, 0);
            values.put(BdEncuestas.FlagFotoEnviada, 0);
            database.insert(BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE, null, values);
        }
        close();
    }


    public void passTableRealinzandoEncuestaToTableEncuestasResultadosPreEnvio(){
        ArrayList<RealizandoEncuestaEntity> RealizandoEncuestaEntityList  = getRealizandoEncuestaEntity();
        open();
        for(int i = 0; i<RealizandoEncuestaEntityList.size();i++){
            //parametros a inserta en la tabla final (TABLE_ENCUESTAS_RESULTADOS_PRE)
            ContentValues values = new ContentValues();
            RealizandoEncuestaEntity realizandoEncuestasEntity = new RealizandoEncuestaEntity();
            realizandoEncuestasEntity = RealizandoEncuestaEntityList.get(i);
            values.put(BdEncuestas.idEncuestaResultadosPre,realizandoEncuestasEntity.getId_encuestaRealizandoEncuesta());
            values.put(BdEncuestas.fechaResultadoPre,realizandoEncuestasEntity.getFechaRealizandoEncuesta());
            values.put(BdEncuestas.idTiendaResultadosPre,realizandoEncuestasEntity.getId_tiendaRealizandoEncuesta());
            values.put(BdEncuestas.idPreguntaResultadosPre,realizandoEncuestasEntity.getId_preguntaRealizandoEncuesta());
            values.put(BdEncuestas.idRespuestaResultadosPre ,realizandoEncuestasEntity.getId_respuestaRealizandoEncuesta());
            values.put(BdEncuestas.abiertaResultadosPre ,realizandoEncuestasEntity.getAbiertaRealizandoEncuesta());
            values.put(BdEncuestas.idArchivoResultadosPre ,realizandoEncuestasEntity.getIdArchivoRealizandoEncuesta());
            values.put(BdEncuestas.latitudResultadosPre ,realizandoEncuestasEntity.getLatitudRealizandoEncuesta());
            values.put(BdEncuestas.longitudResultadosPre ,realizandoEncuestasEntity.getLongitudRealizandoEncuesta());
            values.put(BdEncuestas.FlagEnviada, 1);
            values.put(BdEncuestas.FlagFotoEnviada, 0);
            database.insert(BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE, null, values);
        }
        close();
    }
    public void deleteFotosTable(){
        open();
        database.delete(BdEncuestas.TABLE_FOTO_ENCUESTA,null,null);
        close();
    }
    public void deleteGeosTable(){
        open();
        database.delete(BdEncuestas.TABLE_GEO,null,null);
        close();
    }

    //Deletes records from the table TABLE_PREGUNTAS_ENCUESTA
    public void deletesRecordsTable_TABLE_PREGUNTAS_ENCUESTA(){
        open();
        database.delete(BdEncuestas.TABLE_PREGUNTAS_ENCUESTA,null,null);
        close();
    }


    //Deletes records from table
    public void deletesRecordsTable_TABLE_RESPUESTAS_ENCUESTA(){
        open();
        database.delete(BdEncuestas.TABLE_RESPUESTAS_ENCUESTA,null,null);
        close();
    }

    //Deletes all records from table
    public void deletesRecordsTable_encuestasResultadosPre(){
        open();
        database.delete(BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE,null,null);
        close();
    }

    //Deletes all the records related to the id_pregunta
    public void deletesRecodsRelatedToIdPregunta(String id_pregunta){
        open();
        database.delete(BdEncuestas.TABLE_REALIZANDO_ENCUESTA, BdEncuestas.idPreguntaRealizandoEncuesta+" = ? ", new String[] {id_pregunta});
        close();
    }
    //Limpia la tabla despues de realizar una encuesta
    public void deletesTablaRealizandoEncuesta(){
        open();
        database.delete(BdEncuestas.TABLE_REALIZANDO_ENCUESTA,null,null);
        close();
    }
    //////////////////////////// TABLA TELEFONO_LOGGED //////////////////////s///////
    //inserting the telefono logged
    public void addTelefonoLogged(TelefonoEntity telefonoEntity){
        open();
        ContentValues values = new ContentValues();
        values.put(BdEncuestas.telefono,telefonoEntity.getTelefono());
        values.put(BdEncuestas.flagDescargado, telefonoEntity.getFlagDescargado());
        database.insert(BdEncuestas.TABLE_TELEFONO_LOGGED, null, values);
        close();
    }

    //Deletes table telefono_logged
    public void deleteTableTelefonoLogged(){
        open();

        database.delete(BdEncuestas.TABLE_TELEFONO_LOGGED, null, null);
        close();
    }

    //Getting the telefono logged
    public ArrayList<TelefonoEntity> getTelefonoLogged(){
        ArrayList<TelefonoEntity> telefonos = new ArrayList<TelefonoEntity>();
        open();
        String query = "SELECT * FROM " + BdEncuestas.TABLE_TELEFONO_LOGGED;
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {

                TelefonoEntity telefono = new TelefonoEntity();
                telefono.setTelefono(cursor.getString(0));
                telefono.setFlagDescargado(cursor.getString(1));
                telefonos.add(telefono);

            }while (cursor.moveToNext());
        }
        close();
        return telefonos;
    }


    //looking for the mobile into the table
    public boolean findTelefono(String telefono){
        boolean flagExistsMobile = false;
        String telefonoObtenido = "";
        open();
        String query = "SELECT telefono FROM " + BdEncuestas.TABLE_TELEFONO_LOGGED+ " WHERE "+BdEncuestas.telefono+" = ? ";  //TABLE_RESPUESTAS_UNIVERSO
        Cursor cursor = database.rawQuery(query, new String[] {telefono});

        if (cursor.moveToFirst()) {
            do {
                telefonoObtenido = cursor.getString(0);
                if(telefonoObtenido!=null){
                    flagExistsMobile = true;

                }

            }while (cursor.moveToNext());
        }
        close();
        return flagExistsMobile;
    }


    //bandera para cambiar el status
    public void updateflagenviada() {

        open();
        String strSQL = "UPDATE "+ BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE +" SET FlagEnviada = 1 WHERE FlagEnviada = 0";
        database.execSQL(strSQL);
        close();

    }

    //bandera para cambiar el status
    public void updateflagFotoenviada() {
        open();
        String strSQL = "UPDATE "+ BdEncuestas.TABLE_ENCUESTAS_RESULTADOS_PRE +" SET FlagFotoEnviada = 1 WHERE FlagfotoEnviada = 0";
        database.execSQL(strSQL);
        close();

    }

}
