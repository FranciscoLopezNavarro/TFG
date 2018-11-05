package edu.uclm.esi.tfg.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.uclm.esi.tfg.dominio.Asignatura;

public class DAOAsignatura {

	public static Asignatura registrar(String asignatura, int curso) throws Exception{
		SQLBroker broker = new SQLBroker();
		Asignatura asig;
		if(checkExistAsignatura(asignatura, curso, broker)) 
			throw new Exception("Esta asignatura ya existe en la BBDD");//Si la asignatura ya existe
		
		registrarNueva(asignatura,curso,broker);
		//obteneridASIGNSTASDAS
		asig =new Asignatura(asignatura,curso);
		System.out.println("Asignatura creada con exito: " +asig.getNombre());
		return asig;
	}

	private static void registrarNueva(String asignatura, int curso, SQLBroker broker)throws Exception{
		broker.getConex();
		PreparedStatement ps =null;

		try {
			
			String consulta = "INSERT * INTO asignatura(nombre,curso) VALUES (?, ?)";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setString(1, asignatura);
			ps.setInt(2, curso);
			ps.executeQuery();	
			
		}catch (Exception e) {
			System.err.println("Error" + e);
		}
		if(broker.getConex() !=null) 
			broker.getConex().close();
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
		if(broker.getConex() !=null) 
			broker.getConex().close();

		return exist;

	}

}
