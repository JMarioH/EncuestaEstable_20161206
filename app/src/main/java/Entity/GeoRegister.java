package Entity;

/**
 * Created by jesus.hernandez on 06/12/16.
 */

public class GeoRegister {


    private Integer idEncuesta ;
    private Integer idEstablecimiento ;
    private String latitud ;
    private String longitud ;

    public GeoRegister() {
    }

    public GeoRegister(Integer idEncuesta, Integer idEstablecimiento, String latitud, String longitud) {

        this.idEncuesta = idEncuesta;
        this.idEstablecimiento = idEstablecimiento;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Integer idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public Integer getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(Integer idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
