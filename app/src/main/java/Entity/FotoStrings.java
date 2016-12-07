package Entity;

/**
 * Created by jesus.hernandez on 22/09/16.
 */

public class FotoStrings {
    private String idEstablecimiento;
    private String idEncuesta;
    private String nombre;
    private String stringFoto;


    public FotoStrings() {
    }

    private static FotoStrings instace;

    public FotoStrings(String idEstablecimiento, String idEncuesta, String nombre, String stringFoto) {
        this.idEstablecimiento = idEstablecimiento;
        this.idEncuesta = idEncuesta;
        this.nombre = nombre;
        this.stringFoto = stringFoto;
    }

    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(String idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(String idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStringFoto() {
        return stringFoto;
    }

    public void setStringFoto(String stringFoto) {
        this.stringFoto = stringFoto;
    }

    public static void setInstace(FotoStrings instace) {
        FotoStrings.instace = instace;
    }

    public static synchronized FotoStrings getInstace(){
        if(instace == null){
            instace = new FotoStrings();
        }
        return instace;
    }
}
