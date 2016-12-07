package Entity;

import java.io.Serializable;

public class PreguntaUniversoEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id_pregunta;
	private String pregunta;
	private String multiple;
	private String orden;
	private String id_encuesta;

	public String getId_encuesta() {
		return id_encuesta;
	}
	public void setId_encuesta(String id_encuesta) {
		this.id_encuesta = id_encuesta;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getId_pregunta() {
		return id_pregunta;
	}
	public void setId_pregunta(String id_pregunta) {
		this.id_pregunta = id_pregunta;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

}
