package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.uclm.esi.tfg.dominio.RelacionPrueba;

public class DAORelacionPrueba {

	public static RelacionPrueba registrar(String prueba1, String prueba2) throws Exception {
		SQLBroker broker = new SQLBroker();
		RelacionPrueba rp;

		int pr1 =obtenerIDPrueba(prueba1,broker);
		int pr2 = obtenerIDPrueba(prueba2,broker);

		if(pr1 == 0) {
			throw new Exception("Esta prueba ("+ pr1 + ") no está definida");
		}else if(pr2 == 0) {
			throw new Exception("Esta prueba ("+ pr2 + ") no está definida");
		}
		
		if(checkExistRelacion(pr1, pr2, broker)) 
			throw new Exception("Esta relacion ya existe en la BBDD");//Si la asignatura ya existe

		registrarNueva(pr1,pr2,broker);
		rp =new RelacionPrueba(pr1,pr2);
		System.out.println("Se ha creado con exito la relacion entre las pruebas "+ pr1 + "y" + pr2);
		
		if(broker.getConex() !=null) 
			broker.getConex().close();
		
		return rp;
	}

	private static int obtenerIDPrueba(String prueba, SQLBroker broker) throws SQLException {

		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		int id= 0;
		try {
			String consulta = "SELECT idPrueba FROM prueba WHERE titulo = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, prueba);
			rs = ps.executeQuery();

			while (rs.next()) {
				id = rs.getInt(1);
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return id;
	}


	private static void registrarNueva(int pr1, int pr2, SQLBroker broker)throws Exception{
		broker.getConex();
		PreparedStatement ps =null;

		try {

			String consulta = "INSERT INTO relacionespruebas (prueba1, prueba2) VALUES (?, ?)";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, pr1);
			ps.setInt(2, pr2);
			ps.execute();	

		}catch (Exception e) {
			System.err.println("Error" + e);
		}
		
	}

	private static boolean checkExistRelacion(int prueba1, int prueba2, SQLBroker broker)throws Exception {
		boolean exist = false;
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;

		try {
			String consulta = "SELECT * FROM asignatura WHERE prueba1 = ? and prueba2 = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, prueba1);
			ps.setInt(2, prueba2);
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
}