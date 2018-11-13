package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.uclm.esi.tfg.dominio.Asignatura;

public class DAOAsignatura {

	public static Asignatura registrar(String asignatura, int curso) throws Exception{
		SQLBroker broker = new SQLBroker();
		Asignatura asig;
		if(checkExistAsignatura(asignatura, curso, broker)) 
			throw new Exception("Esta asignatura ya existe en la BBDD");//Si la asignatura ya existe

		registrarNueva(asignatura,curso,broker);

		int id =obtenerIdAsignatura(asignatura,curso,broker);

		asig =new Asignatura(id,asignatura,curso);

		System.out.println("Asignatura creada con exito: " +asig.getNombre());
		if(broker.getConex() !=null) 
			broker.getConex().close();
		return asig;
	}

	private static int obtenerIdAsignatura(String asignatura, int curso, SQLBroker broker) throws SQLException {
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		int id = 0;
		try {
			String consulta = "SELECT idAsignatura FROM asignatura WHERE nombre = ? and curso = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, asignatura);
			ps.setInt(2, curso);
			rs = ps.executeQuery();

			while (rs.next()) {
				id = rs.getInt(1);
			}

		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return id;
	}

	private static void registrarNueva(String asignatura, int curso, SQLBroker broker)throws Exception{
		broker.getConex();
		PreparedStatement ps =null;

		try {

			String consulta = "INSERT INTO asignatura (nombre, curso) VALUES (?, ?)";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, asignatura);
			ps.setInt(2, curso);
			ps.execute();	

		}catch (Exception e) {
			System.err.println("Error" + e);
		}

	}

	private static boolean checkExistAsignatura(String asignatura, int curso, SQLBroker broker)throws Exception {
		boolean exist = false;
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;

		try {
			String consulta = "SELECT * FROM asignatura WHERE nombre = ? and curso = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, asignatura);
			ps.setInt(2, curso);
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
