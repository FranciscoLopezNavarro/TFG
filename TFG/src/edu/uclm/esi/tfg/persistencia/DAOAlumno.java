package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.uclm.esi.tfg.dominio.Alumno;

public class DAOAlumno {
	private static boolean checkExistAlumno(int id, SQLBroker broker) throws SQLException{
		boolean exist = false;
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {

			String consulta = "SELECT * FROM alumno WHERE idAlumno = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, id);
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

	public static Alumno registrar(int id) throws Exception {
		SQLBroker broker = new SQLBroker();
		PreparedStatement ps =null;

		if(checkExistAlumno(id,broker))//Si el usuario no existe
			throw new Exception("Este usuario ya existe.");

		try {
			String consulta = "INSERT INTO Alumno (idAlumno) VALUES (?);";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, id);

			ps.executeUpdate();

		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		Alumno alumno = new Alumno(id);
		if(broker.getConex() !=null) 
			broker.getConex().close();

		return alumno;	
	}

	public static ArrayList<Alumno> cargar() {
		SQLBroker broker = new SQLBroker();
		broker.getConex();
		ArrayList<Alumno> alumnos = new ArrayList<Alumno>();
		Alumno alumno;
		PreparedStatement ps =null;
		ResultSet rs = null;
		try {

			String consulta = "SELECT * FROM alumno";
			ps = broker.getConex().prepareStatement(consulta);
			rs = ps.executeQuery();

			while(rs.next()) {
				alumno = new Alumno();
				alumno.setId(rs.getInt("idAlumno"));
				alumnos.add(alumno);
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}

		return alumnos;
	}

}
