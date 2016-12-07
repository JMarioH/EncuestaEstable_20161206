package Entity;

import java.io.Serializable;

public class RespuestaUniversoEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String id_pregunta;
	private String id_respuesta;
	private String respuesta;
	private String respuestaLibre;
	private String sig_pregunta;
	private String id_encuesta;
	
	
	public String getRespuestaLibre() {
		return respuestaLibre;
	}
	public void setRespuestaLibre(String respuestaLibre) {
		this.respuestaLibre = respuestaLibre;
	}
	
	public String getId_pregunta() {
		return id_pregunta;
	}
	public void setId_pregunta(String id_pregunta) {
		this.id_pregunta = id_pregunta;
	}
	public String getId_respuesta() {
		return id_respuesta;
	}
	public void setId_respuesta(String id_respuesta) {
		this.id_respuesta = id_respuesta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public String getSig_pregunta() {
		return sig_pregunta;
	}
	public void setSig_pregunta(String sig_pregunta) {
		this.sig_pregunta = sig_pregunta;
	}
	public String getId_encuesta() {
		return id_encuesta;
	}
	public void setId_encuesta(String id_encuesta) {
		this.id_encuesta = id_encuesta;
	}
	
}
