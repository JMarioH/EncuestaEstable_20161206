package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 28/09/2015.
 * creamos la bae de datos dentro de la aplicacion
 */
public class BdEncuestas extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Encuestas";
    ////////////////////  Clientes Table //////////////////////
    public static final String TABLE_CLIENTES = "Clientes";
    // Columns names
    public static final String nombreCliente = "cliente";
    //////////////////// Projectos Table //////////////////////
    public static final String TABLE_PROYECTOS = "Proyectos";
    // Columns names
    public static final String nombreProyecto = "proyecto";
    public static final String id_proyecto = "id_proyecto";
    public static final String cliName = "cliName";
    ////////////////// Tipo Encuesta Table ////////////////////
    public static final String TABLE_TIPO_ENCUESTA = "TipoEncuesta";
    //Column names
    public static final String nombreEncuesta = "nombreEncuesta";
    public static final String idArchivoEncuesta = "id_archivoEncuesta";
    public static final String idEncuesta = "id_encuesta";
    public static final String numeroTelEncuesta = "numero_tel";
    public static final String nombreClienteEncuesta = "nombreClienteEncuesta";
    public static final String idProyectoEncuesta = "id_proyecto";
    ///////////////// HiScreen Table ////////////////////////////

    public static final String TABLE_HISCREEN = "Encuesta";
    //column names
    public static final String idTiendaHiscreen = "idTiendaHiScreen";
    public static final String nombreEncuestaHiscreen = "nombreEncuestaHiscreen";
    public static final String id_ArchivoHiscreen = "idArchivoHiscreen";
    public static final String check_realizada = "check_realizada";
    //////////////// Preguntas Table (Universo) /////////////////////////

    public static final String TABLE_PREGUNTAS_UNIVERSO = "Preguntas";
    //column names
    public static final String idPregunta = "id_pregunta";
    public static final String pregunta = "pregunta";
    public static final String multiple = "multiple";
    public static final String orden = "orden";
    public static final String idEncuestaPreguntaUniverso = "idEncuestaPregunta";

    ////////////// PreguntasEncuesta Table /////////////////////
    public static final String TABLE_PREGUNTAS_ENCUESTA = "PreguntasEncuesta";
    //column names
    public static final String idPreguntaEncuesta = "id_preguntaEncuesta";
    public static final String preguntaEncuesta = "preguntaEncuesta";
    public static final String multipleEncuesta = "multipleEncuesta";
    public static final String ordenEncuesta = "ordenEncuesta";
    public static final String idEncuestaEncuesta = "ordenEncuestaEncuesta";

    /////////////// Respuestas Table (Universo) //////////////////////////
    public static final String TABLE_RESPUESTAS_UNIVERSO = "Respuestas";
    //column names
    public static final String idPreguntaRespuestasUniverso = "id_pregunta";
    public static final String idRespuestaRespuestasUniverso = "id_respuesta";
    public static final String RespuestaRespuestasUniverso = "respuesta";
    public static final String siguientePreguntaRespuestasUniverso = "sigPregunta";
    public static final String respuestaLibreRespuestasUniverso = "respuestaLibre";
    public static final String idEncuestaRespuestasUniverso = "idEncuesta";

    ///////////// RespuestasEncuesta table ////////////////////////
    public static final String TABLE_RESPUESTAS_ENCUESTA = "RespuestasEncuesta";
    //column names
    public static final String idPreguntaRespuestaEncuesta = "id_preguntaEncuesta";
    public static final String idRespuestaRespuestasEncuesta = "id_respuestaEncuesta";
    public static final String RespuestaRespuestasEncuesta = "respuestaEncuesta";
    public static final String siguientePreguntaRespuestasEncuesta = "sigPreguntaEncuesta";
    public static final String respuestaLibreRespuestasEncuesta = "respuestaLibreEncuesta";
    public static final String idEncuestaRespuestasEncuesta = "idEncuestaEncuesta";
    ///////////// PreguntaAbierta table ///////////////////////////
    public static final String TABLE_PREGUNTA_ABIERTA = "PreguntaAbierta";
    //column names
    //  public static final String idPreguntaAbierta = "id_preguntaAbierta";
    public static final String respuestaPreguntaAbierta = "respuestaPreguntaAbierta";
    //////////////////// ENCUESTAS_RESULTADOS_PRE //////////////////////

    public static final String TABLE_ENCUESTAS_RESULTADOS_PRE = "EncuestaResultadosPre";
    //column names
    public static final String idEncuestaResultadosPre = "idEncuestaResultadosPre";
    public static final String fechaResultadoPre = "fechaResultadoPre";
    public static final String idTiendaResultadosPre = "idTiendaResultadosPre";
    public static final String idPreguntaResultadosPre = "idPreguntaResultadosPre";
    public static final String idRespuestaResultadosPre = "idRespuestaResultadosPre";
    public static final String abiertaResultadosPre = "abiertaResultadosPre";
    public static final String idArchivoResultadosPre = "idArchivoResultadosPre";
    public static final String latitudResultadosPre = "latitudResultadosPre";
    public static final String longitudResultadosPre = "longitudResultadosPre";
    public static final String FlagEnviada = "FlagEnviada";
    public static final String FlagFotoEnviada = "FlagFotoEnviada";
    ////////////////// REALIZANDO_ENCUESTA_TABLA ////////////////////
    public static final String TABLE_REALIZANDO_ENCUESTA = "RealizandoEncuesta";
    //column names
    public static final String idEncuestaRealizandoEncuesta = "idEncuestaRealizandoEncuesta";
    public static final String fechaRealizandoEncuesta = "fechaRealizandoEncuesta";
    public static final String idTiendaRealizandoEncuesta = "idTiendaRealizandoEncuesta";
    public static final String idPreguntaRealizandoEncuesta = "idPreguntaRealizandoEncuesta";
    public static final String idRespuestaRealizandoEncuesta = "idRespuestaRealizandoEncuesta";
    public static final String abiertaRealizandoEncuesta = "abiertaRealizandoEncuesta";
    public static final String idArchivoRealizandoEncuesta = "idArchivoRealizandoEncuesta";
    public static final String latitudRealizandoEncuesta = "latitudRealizandoEncuesta";
    public static final String longitudRealizandoEncuesta = "longitudRealizandoEncuesta";

    /////////////////// TELEFONO LOGEADO TABLA ////////////////////
    public static final String TABLE_TELEFONO_LOGGED = "telefonoLogeado";
    //column name
    public static final String telefono = "telefono";
    public static final String flagDescargado = "flagDescargado";

    /////////////////// FOTO GUARDADA EN LA BASE ////////////////////
    public static final String TABLE_FOTO_ENCUESTA = "fotoencuesta";
    public static final String id = "id";
    public static final String idEstablecimiento = "idEstablecimiento";
    public static final String idEncuestaFoto = "idEncuestaFoto";
    public static final String nombre = "nombre";
    public static final String arrayString = "arrayString";
    /*
        Tabla de geo
     */
    public static final String TABLE_GEO = "geolocalizacion";
    public static final String idGeo = "id";
    public static final String stringEncuesta = "idEncuesta";
    public static final String stringEstablecimiento = "idEstablecimiento";
    public static final String stringLatitud = "latitud";
    public static final String stringLongitud = "longitud";

    //Constructor
    public BdEncuestas(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_GEO = "CREATE TABLE "+ TABLE_GEO +
                "(" + idGeo + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + stringEncuesta + " INTEGER, "
                + stringEstablecimiento + " INTEGER, "
                + stringLatitud + " TEXT , "
                + stringLongitud + " TEXT )" ;

        db.execSQL(CREATE_TABLE_GEO);

        String CREATE_FOTO_ENCUESTA = "CREATE TABLE " + TABLE_FOTO_ENCUESTA +
                "(" + id + " INTEGER, "
                + idEstablecimiento + " INTEGER, "
                + idEncuestaFoto + " INTEGER, "
                + nombre + " TEXT,"
                + arrayString + " TEXT )";

        db.execSQL(CREATE_FOTO_ENCUESTA);
        ////////////////// SQL statement CREATES CLIENTES table
        String CREATE_CLIENTES_TABLE = "CREATE TABLE " + TABLE_CLIENTES + "(" + nombreCliente + " TEXT )";
        // create cliente table
        db.execSQL(CREATE_CLIENTES_TABLE);
        ///////////////// SQL statement CREATES PROYECTOS table
        String CREATE_PROYECTOS_TABLE = "CREATE TABLE " + TABLE_PROYECTOS + "(" + nombreProyecto + " TEXT, "
                + id_proyecto + " INTEGER , "     //TEXT    //PRIMARY KEY
                + cliName + " TEXT )";
        // create projects table
        db.execSQL(CREATE_PROYECTOS_TABLE);
        //////////////// SQL statement CREATES TIPO ENCUESTA table
        String CREATE_TIPO_ENCUESTA_TABLE = "CREATE TABLE " + TABLE_TIPO_ENCUESTA + "(" + nombreEncuesta + " TEXT,"
                + idArchivoEncuesta + " INTEGER, " //TEXT
                + idEncuesta + " INTEGER , " //TEXT    PRIMARY KEY
                + numeroTelEncuesta + " TEXT, "
                + nombreClienteEncuesta + " TEXT, "
                + idProyectoEncuesta + " INTEGER ) ";
        // create tipoEncuesta table
        db.execSQL(CREATE_TIPO_ENCUESTA_TABLE);
        String CREATE_HISCREEN_TABLE = "CREATE TABLE " + TABLE_HISCREEN + "(" + idTiendaHiscreen + " INTEGER , "  //TEXT    //PRIMARY KEY
                + nombreEncuestaHiscreen + " TEXT, "
                + id_ArchivoHiscreen + " INTEGER, " //TEXT
                + check_realizada + " INTEGER DEFAULT 0)";
        // create HiScreen table
        db.execSQL(CREATE_HISCREEN_TABLE);
        ///////////////SQL statament CREATES PREGUNTAS UNIVERSO TABLE
        String CREATE_PREGUNTAS_TABLE = "CREATE TABLE " + TABLE_PREGUNTAS_UNIVERSO + "(" + idPregunta + " INTEGER, " //TEXT
                + pregunta + " TEXT, "
                + multiple + " TEXT, "
                + orden + " TEXT, "
                + idEncuestaPreguntaUniverso + " INTEGER )"; //,
        //+"PRIMARY KEY ("+idPregunta+","+idEncuestaPreguntaUniverso+"))"; //TEXT

        // create Preguntas table
        db.execSQL(CREATE_PREGUNTAS_TABLE);
        //////////////SQL statement CREATES PREGUNTAS ENCUESTA TABLE
        String CREATE_PREGUNTAS_ENCUESTA_TABLE = "CREATE TABLE " + TABLE_PREGUNTAS_ENCUESTA + "(" + idPreguntaEncuesta + " INTEGER, " //TEXT
                + preguntaEncuesta + " TEXT, "
                + multipleEncuesta + " TEXT, "
                + ordenEncuesta + " TEXT, "
                + idEncuestaEncuesta + " INTEGER)";  //TEXT
        // create Preguntas encuesta table
        db.execSQL(CREATE_PREGUNTAS_ENCUESTA_TABLE);
        /////////////SQL statement CREATES RESPUESTAS UNIVERSO TABLE
        String CREATE_RESPUESTAS_TABLE = "CREATE TABLE " + TABLE_RESPUESTAS_UNIVERSO + "("
                + idPreguntaRespuestasUniverso + " INTEGER, "  //TEXT
                + idRespuestaRespuestasUniverso + " INTEGER , "   //TEXT
                + RespuestaRespuestasUniverso + " TEXT, "
                + siguientePreguntaRespuestasUniverso + " TEXT,"
                + respuestaLibreRespuestasUniverso + " TEXT,"
                + idEncuestaRespuestasUniverso + " INTEGER); ";
        // +"PRIMARY KEY ("+idPreguntaRespuestasUniverso+","+idRespuestaRespuestasUniverso+","+idEncuestaRespuestasUniverso+"))";  //TEXT
        //creates Respuestas table
        db.execSQL(CREATE_RESPUESTAS_TABLE);
        /////////////SQL statement CREATES RESPUESTAS ENCUESTAS TABLE
        String CREATE_RESPUESTAS_ENCUESTA = "CREATE TABLE " + TABLE_RESPUESTAS_ENCUESTA + "(" + idPreguntaRespuestaEncuesta + " INTEGER, "  //TEXT
                + idRespuestaRespuestasEncuesta + " INTEGER, "  //TEXT
                + RespuestaRespuestasEncuesta + " TEXT, "
                + siguientePreguntaRespuestasEncuesta + " TEXT,"
                + respuestaLibreRespuestasEncuesta + " TEXT,"
                + idEncuestaRespuestasEncuesta + " INTEGER)";  //TEXT
        //creates Respuestas encuesta table
        db.execSQL(CREATE_RESPUESTAS_ENCUESTA);
        //////////// SQL statement CREATES PREGUNTA ABIERTA TABLE
        String CREATE_PREGUNTA_ABIERTA = "CREATE TABLE " + TABLE_PREGUNTA_ABIERTA + "(" + respuestaPreguntaAbierta + " TEXT)";
        //+idPreguntaAbierta +" INTEGER PRIMARY KEY, "
        //creates Pregunta Abierta table
        db.execSQL(CREATE_PREGUNTA_ABIERTA);
        /////////////// SQL statement CREATES TABLE_ENCUESTAS_RESULTADOS_PRE
        String CREATE_ENCUESTA_RESULTADOS_PRE = "CREATE TABLE " + TABLE_ENCUESTAS_RESULTADOS_PRE + "(" + idEncuestaResultadosPre + " INTEGER, "   //TEXT
                + fechaResultadoPre + " TEXT, "
                + idTiendaResultadosPre + " INTEGER, "         //TEXT
                + idPreguntaResultadosPre + " INTEGER, "          //TEXT
                + idRespuestaResultadosPre + " INTEGER, "
                + abiertaResultadosPre + " TEXT, "
                + idArchivoResultadosPre + " INTEGER, "        //TEXT
                + latitudResultadosPre + " TEXT, "
                + longitudResultadosPre + " TEXT, "
                + FlagEnviada + " INTEGER, "
                + FlagFotoEnviada + " INTEGER)";
        //creates encuestaResultadosPre
        db.execSQL(CREATE_ENCUESTA_RESULTADOS_PRE);
        ////////////// SQL statement CREATES TABLE_REALIZANDO_ENCUESTA /////////////////
        String CREATE_REALIZANDO_ENCUESTA = "CREATE TABLE " + TABLE_REALIZANDO_ENCUESTA + "(" + idEncuestaRealizandoEncuesta + " INTEGER, "  //TEXT
                + fechaRealizandoEncuesta + " TEXT, "
                + idTiendaRealizandoEncuesta + " INTEGER, "     //TEXT
                + idPreguntaRealizandoEncuesta + " INTEGER, "      //TEXT
                + idRespuestaRealizandoEncuesta + " INTEGER, "     //TEXT
                + abiertaRealizandoEncuesta + " TEXT, "
                + idArchivoRealizandoEncuesta + " INTEGER, "       //TEXT
                + latitudRealizandoEncuesta + " TEXT, "
                + longitudRealizandoEncuesta + " TEXT)";
        //creates realizando_encuesta
        db.execSQL(CREATE_REALIZANDO_ENCUESTA);
        ////////////// SQL statement CREATES TABLE_TELEFONO_LOGGED ///////////////////
        String CREATE_TELEFONO_LOGGED = "CREATE TABLE " + TABLE_TELEFONO_LOGGED + "(" + telefono + " TEXT, "
                + flagDescargado + " TEXT )";
        //creates telefono_logged
        db.execSQL(CREATE_TELEFONO_LOGGED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PROYECTOS);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TIPO_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_HISCREEN);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PREGUNTAS_UNIVERSO);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PREGUNTAS_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RESPUESTAS_UNIVERSO);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_RESPUESTAS_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PREGUNTA_ABIERTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_FOTO_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_REALIZANDO_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TELEFONO_LOGGED);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GEO);

        this.onCreate(db);

    }


}
