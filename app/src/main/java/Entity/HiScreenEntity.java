package Entity;

public class HiScreenEntity {

	private String idTienda;
	private String nombreEncuesta;
	private String id_archivo;
	private String isCheckedEncuesta;

	public String isCheckedEncuesta() {
		return isCheckedEncuesta;
	}
	public void setCheckedEncuesta(String isCheckedEncuesta) {
		this.isCheckedEncuesta = isCheckedEncuesta;
	}
	public String getId_archivo() {
		return id_archivo;
	}
	public void setId_archivo(String id_archivo) {
		this.id_archivo = id_archivo;
	}
	public String getIdTienda() {
		return idTienda;
	}
	public void setIdTienda(String idTienda) {
		this.idTienda = idTienda;
	}
	public String getNombreEncuesta() {
		return nombreEncuesta;
	}
	public void setNombreEncuesta(String nombreEncuesta) {
		this.nombreEncuesta = nombreEncuesta;
	}

}
