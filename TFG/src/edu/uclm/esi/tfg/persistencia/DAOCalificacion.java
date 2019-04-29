package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.uclm.esi.tfg.dominio.Calificacion;

public class DAOCalificacion {

	private static void registrarNueva(int alumno, int prueba, double nota,String year, SQLBroker broker)throws SQLException{
		broker.getConex();
		PreparedStatement ps =null;

		try {

			String consulta = "INSERT INTO calificacion (alumno,prueba, nota,año) VALUES (?, ?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE "
					+ "alumno = VALUES(alumno), prueba = VALUES(prueba), nota = VALUES(nota), año = VALUES(año)";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, alumno);
			ps.setInt(2, prueba);
			ps.setDouble(3, nota);
			ps.setString(4, year);
			ps.execute();	

		}catch (Exception e) {
			System.err.println("Error" + e);
		}

	}

	//	private static boolean checkExistCalificacion(int alumno, int prueba, double nota, String year, SQLBroker broker)throws SQLException{
	//		boolean exist = false;
	//		broker.getConex();
	//		PreparedStatement ps =null;
	//		ResultSet rs = null;
	//
	//		try {
	//			String consulta = "SELECT * FROM calificacion WHERE (alumno = ? and prueba = ? and nota = ? and año = ?)";
	//			ps = broker.getConex().prepareStatement(consulta);
	//			ps.setInt(1, alumno);
	//			ps.setInt(2, prueba);
	//			ps.setDouble(3, prueba);
	//			ps.setString(4, year);
	//			rs = ps.executeQuery();
	//
	//			if(rs.absolute(1)){
	//				exist = true;
	//			}else {
	//				exist = false;
	//			}
	//		}catch (Exception e) {
	//			System.err.println("Error" + e);
	//		}
	//		return exist;
	//
	//	}

	public static Calificacion registrar(Integer alumno, Integer idPrueba, Double nota, String year) throws Exception {
		SQLBroker broker = new SQLBroker();
		Calificacion cali;
		//		if(checkExistCalificacion(alumno, idPrueba, nota, year, broker)) 
		//			throw new Exception("Esta asignatura ya existe en la BBDD");//Si la asignatura ya existe

		registrarNueva(alumno,idPrueba,nota,year,broker);
		cali =new Calificacion(alumno,idPrueba,nota,year);

		if(broker.getConex() !=null) 
			broker.getConex().close();
		return cali;
	}

	public static ArrayList<Calificacion> cargar() {
		SQLBroker broker = new SQLBroker();
		broker.getConex();
		ArrayList<Calificacion> calificaciones = new ArrayList<Calificacion>();
		Calificacion cali;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {

			String consulta = "SELECT * FROM calificacion";
			ps = broker.getConex().prepareStatement(consulta);
			rs = ps.executeQuery();

			while(rs.next()) {
				cali = new Calificacion();
				cali.setAlumno(rs.getInt("Alumno"));
				cali.setPrueba(rs.getInt("Prueba"));
				cali.setNota(rs.getDouble("Nota"));
				cali.setYear(rs.getString("Año"));

				calificaciones.add(cali);
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return calificaciones;
	}



}
