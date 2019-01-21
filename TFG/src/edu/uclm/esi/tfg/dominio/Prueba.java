package edu.uclm.esi.tfg.dominio;

public class Prueba {
		private int id;
		private String titulo;
		private int orden;
		private double n_min;
		private double n_max;
		private double n_corte;
		private int asig;
		
		public Prueba(Integer id,String titulo, int orden, double n_min, double n_corte, double n_max, int asig) {
			this.id = id;
			this.titulo = titulo;
			this.orden = orden;
			this.n_min = n_min;
			this.n_max = n_max;
			this.n_corte = n_corte;
			this.asig = asig;
			
		}
		public Prueba() {
			// TODO Auto-generated constructor stub
		}
		
		public String getTitulo() {
			return titulo;
		}
		public void setTitulo(String titulo) {
			this.titulo = titulo;
		}
		public double getN_min() {
			return n_min;
		}
		public void setOrden(int orden) {
			this.orden = orden;
		}
		public int getOrden() {
			return orden;
		}
		public void setN_min(double nota_min) {
			n_min = nota_min;
		}
		public double getN_max() {
			return n_max;
		}
		public void setN_max(double nota_max) {
			n_max = nota_max;
		}
		public double getN_corte() {
			return n_corte;
		}
		public void setN_corte(double nota_corte) {
			n_corte = nota_corte;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getAsig() {
			return asig;
		}
		public void setAsig(int asig) {
			this.asig = asig;
		}
}
