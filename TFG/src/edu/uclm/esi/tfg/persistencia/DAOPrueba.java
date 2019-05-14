package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.uclm.esi.tfg.dominio.Prueba;

public class DAOPrueba {

	public static Prueba registrar(String prueba, int orden, double n_min, double n_corte, double n_max, int idAsig) throws Exception {
		SQLBroker broker = new SQLBroker();
		Prueba pr;
		if(checkExistPrueba(prueba, idAsig, broker)) 
			throw new Exception("Esta prueba ya existe en la BBDD");//Si la asignatura ya existe

		registrarNueva(prueba,orden,n_min,n_corte,n_max,idAsig,broker);
		int id =obtenerIdPrueba(prueba,idAsig,broker);
		
		pr =new Prueba(id,prueba,orden,n_min,n_corte,n_max,idAsig);

		if(broker.getConex() !=null) 
			broker.getConex().close();

		return pr;
	}


	private static void registrarNueva(String prueba, int orden, double n_min, double n_corte, double n_max, int asig,
			SQLBroker broker)throws Exception{
		broker.getConex();
		PreparedStatement ps =null;

		try {

			String consulta = "INSERT INTO prueba (titulo, orden, n_min, n_corte, n_max, asignatura) VALUES(?, ?, ?, ?, ?, ?)";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, prueba);
			ps.setInt(2, orden);
			ps.setDouble(3, n_min);
			ps.setDouble(4, n_corte);
			ps.setDouble(5, n_max);
			ps.setInt(6, asig);
			ps.execute();	

		}catch (Exception e) {
			System.err.println("Error" + e);
		}
	}

	private static boolean checkExistPrueba(String prueba, int asig, SQLBroker broker)throws Exception {
		boolean exist = false;
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;

		try {
			String consulta = "SELECT * FROM prueba WHERE titulo = ? and asignatura = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, prueba);
			ps.setInt(2, asig);
			rs = ps.executeQuery();

			if(rs.absolute(1)){
				exist = true;
			}else {
				exist = false;
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}
		return exist;
	}

	private static int obtenerIdPrueba(String prueba, int asignatura, SQLBroker broker) throws SQLException {
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		int id = 0;
		try {
			String consulta = "SELECT idPrueba FROM prueba WHERE titulo = ? and asignatura = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, prueba);
			ps.setInt(2, asignatura);
			rs = ps.executeQuery();

			while (rs.next()) {
				id = rs.getInt(1);
			}

		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return id;
	}
	public static ArrayList<Prueba> cargar() {
		SQLBroker broker = new SQLBroker();
		broker.getConex();
		ArrayList<Prueba> pruebas = new ArrayList<Prueba>();
		Prueba prueba;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {

			String consulta = "SELECT * FROM prueba";
			ps = broker.getConex().prepareStatement(consulta);
			rs = ps.executeQuery();

			while(rs.next()) {
				prueba = new Prueba();
				prueba.setId(rs.getInt("idPrueba"));
				prueba.setTitulo(rs.getString("Titulo"));
				prueba.setOrden(rs.getInt("Orden"));
				prueba.setN_min(rs.getDouble("n_min"));
				prueba.setN_corte(rs.getDouble("n_corte"));
				prueba.setN_max(rs.getDouble("n_max"));
				prueba.setAsig(rs.getInt("Asignatura"));

				pruebas.add(prueba);
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return pruebas;
	}
}

