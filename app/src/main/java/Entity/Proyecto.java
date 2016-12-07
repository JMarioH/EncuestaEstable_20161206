package Entity;

public class Proyecto {

	private String nombreProyecto;
    private String id_proyecto;
	private String cliName;
    
	
	public String getCliName() {
		return cliName;
	}

	public void setCliName(String cliName) {
		this.cliName = cliName;
	}

	public String getId_proyecto() {
		return id_proyecto;
	}

	public void setId_proyecto(String id_proyecto) {
		this.id_proyecto = id_proyecto;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}
}
