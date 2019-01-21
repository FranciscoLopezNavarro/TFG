package edu.uclm.esi.tfg.dominio;

public class RelacionPrueba {
	private int prueba1;
	private int prueba2;
	
	public RelacionPrueba(int pr1, int pr2) {
		this.setPrueba1(pr1);
		this.setPrueba2(pr2);
	}

	public RelacionPrueba() {
		// TODO Auto-generated constructor stub
	}

	public int getPrueba1() {
		return prueba1;
	}

	public void setPrueba1(int prueba1) {
		this.prueba1 = prueba1;
	}

	public int getPrueba2() {
		return prueba2;
	}

	public void setPrueba2(int prueba2) {
		this.prueba2 = prueba2;
	}

}
