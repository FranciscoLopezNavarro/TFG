package edu.uclm.esi.tfg.persistencia;

import java.sql.*;

import edu.uclm.esi.tfg.dominio.Profesor;

public class DAOProfesor {
	private static boolean checkExistUser(int id, String password,SQLBroker broker) throws Exception {
		boolean exist = false;
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		System.out.println("Ha llegado hasta aquí");
		try {

			String consulta = "SELECT * FROM profesor WHERE idProfesor = ? and Password = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, id);
			ps.setString(2, password);
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
	public static Profesor login(int id,String pwd) throws Exception{
		SQLBroker broker = new SQLBroker();
		if(!checkExistUser(id, pwd, broker))//Si el usuario no existe
			throw new Exception("Usuario o contraseña incorrectos");

		Profesor user = new Profesor(id,pwd);
		user.setNombre(getNombre(id, broker));
		return user;
	}


	private static String getNombre(int id,SQLBroker broker) throws Exception {
		broker.getConex();
		PreparedStatement ps =null;
		ResultSet rs = null;
		String nombre = null;
		System.out.println("Ha llegado hasta aquí");
		try {

			String consulta = "SELECT nombre FROM profesor WHERE idProfesor = ?";
			ps = broker.getConex().prepareStatement(consulta);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			while (rs.next()) {
				nombre = rs.getString(1);
			}
		}catch (Exception e) {
			System.err.println("Error" + e);
		}
		if(broker.getConex() !=null) 
			broker.getConex().close();
		return nombre;
	}
	//	public static Profesor insert(int id, String pwd, String nombre, String apellido) throws Exception {
	//		SQLBroker broker = new SQLBroker();
	//		if(checkExistUser(id, pwd, broker))//Si el usuario no existe
	//			throw new Exception("Este usuario ya existe.");
	//
	//		return null;
	//	}

	//TODO test this method
	//	public static User insert(String email,String pwd,String nick) throws Exception{
	//		MongoClient db_client =  MongoBroker.get().getBD();
	//		//Check if the user exists before insert
	//		if(checkExistUser(email,null,db_client)) {
	//			MongoBroker.get().close(db_client);
	//			throw new Exception("Usuario ya registrado");
	//		}
	//		UserRegistered user = new UserRegistered(email,pwd);
	//		if(checkExistNick(nick, db_client)) {
	//			MongoBroker.get().close(db_client);
	//			throw new Exception("Este nick ya existe, elige otro");
	//		}
	//		user.setNick(nick);
	//		user.setScore(0);
	//
	//		BsonDocument criteria = new BsonDocument();
	//		criteria.append("email", new BsonString(email));
	//		criteria.append("pwd", new BsonString(pwd));
	//		criteria.append("nick", new BsonString(nick));
	//		criteria.append("score", new BsonInt32(0));
	//
	//		MongoDatabase db = db_client.getDatabase("LaOca");
	//		MongoCollection<BsonDocument> users = db.getCollection("users",BsonDocument.class);
	//		users.insertOne(criteria);
	//		MongoBroker.get().close(db_client);
	//		return user;
	//	}
	//	//TODO test this method
	//	public static boolean changePassword(User user,String newPassword) throws Exception{
	//		MongoClient db_client =  MongoBroker.get().getBD();
	//		//Check if the user exists before insert
	//		if(!checkExistUser(user.getEmail(),user.getPwd(),db_client)) {
	//			MongoBroker.get().close(db_client);
	//			throw new Exception("Email o contraseï¿½a incorrecto");
	//		}
	//		BsonDocument criteria = new BsonDocument();
	//		criteria.append("email", new BsonString(user.getEmail()));
	//
	//		BsonDocument updateCirteria = new BsonDocument();
	//
	//		updateCirteria.append("$set",new BsonDocument("pwd", new BsonString(newPassword)));
	//
	//		MongoDatabase db = db_client.getDatabase("LaOca");
	//		MongoCollection<BsonDocument> users = db.getCollection("users",BsonDocument.class);
	//		users.updateOne(criteria, updateCirteria);
	//		MongoBroker.get().close(db_client);
	//		return true;
	//	}
	//	//TODO test this method
	//
	//	//TODO Falta hacer un sort cogiendo el mayor score y poniendo un limit en la consulta
	//
	//
	//	public static void changePasswordEmail(String email, String pwd) throws Exception {
	//		SQLclient db_client =  MongoBroker.get().getBD();
	//		//Check if the user exists before insert
	//		BsonDocument criteria = new BsonDocument();
	//		criteria.append("email", new BsonString(email));
	//
	//		BsonDocument updateCirteria = new BsonDocument();
	//
	//		updateCirteria.append("$set",new BsonDocument("pwd", new BsonString(pwd)));
	//
	//		MongoDatabase db = db_client.getDatabase("LaOca");
	//		MongoCollection<BsonDocument> users = db.getCollection("users",BsonDocument.class);
	//		users.updateOne(criteria, updateCirteria);
	//		MongoBroker.get().close(db_client);
	//	}
	//}
}
