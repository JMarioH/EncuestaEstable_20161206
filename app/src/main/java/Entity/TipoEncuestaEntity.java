package Entity;

public class TipoEncuestaEntity {

	private String nombreEncuesta;
	private String idArchivoEncuesta;
	private String idEncuesta;
	private String numeroTelefono;
	private String nombreCliente;
	private String id_proyecto;
	
	
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getId_proyecto() {
		return id_proyecto;
	}
	public void setId_proyecto(String id_proyecto) {
		this.id_proyecto = id_proyecto;
	}
	
	public String getNombreEncuesta() {
		return nombreEncuesta;
	}
	public void setNombreEncuesta(String nombreEncuesta) {
		this.nombreEncuesta = nombreEncuesta;
	}
	public String getIdEncuesta() {
		return idEncuesta;
	}
	public void setIdEncuesta(String idEncuesta) {
		this.idEncuesta = idEncuesta;
	}
	public String getIdArchivoEncuesta() {
		return idArchivoEncuesta;
	}
	public void setIdArchivoEncuesta(String idArchivoEncuesta) {
		this.idArchivoEncuesta = idArchivoEncuesta;
	}
	
	
}
