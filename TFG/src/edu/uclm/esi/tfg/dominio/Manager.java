package edu.uclm.esi.tfg.dominio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uclm.esi.tfg.persistencia.DAOAsignatura;
import edu.uclm.esi.tfg.persistencia.DAOProfesor;
import edu.uclm.esi.tfg.persistencia.DAOPrueba;
import edu.uclm.esi.tfg.persistencia.DAORelacionPrueba;

public class Manager {
	private ConcurrentHashMap<String, Profesor> usuarios;
	private ConcurrentHashMap<Integer, Asignatura> asignaturas;
	private ConcurrentHashMap<Integer, Prueba> pruebas;

	private Manager() {
		this.usuarios = new ConcurrentHashMap<>();
		this.asignaturas = new ConcurrentHashMap<>();
		this.pruebas = new ConcurrentHashMap<>();

	}
	public static class ManagerHolder{
		static Manager singleton = new Manager();
	}

	public static Manager get() {
		return ManagerHolder.singleton;
	}
	public Profesor login(String id,String pwd) throws Exception{
		Profesor user;
		user = DAOProfesor.login(Integer.parseInt(id),pwd);
		if(user != null) {
			String nombre = user.getNombre();
			if(nombre != null) {
				usuarios.put(nombre, user);
			}
		}
		return user;
	}

	public Profesor logout(String nombre) {
		return this.usuarios.remove(nombre);
	}

	public Profesor registrar(String id,String pwd,String nombre) throws Exception{
		Profesor user = DAOProfesor.insert(Integer.parseInt(id), pwd, nombre);
		usuarios.put(nombre, user);
		return  user;
	}
	//	public void addRecoverCodePwd(long codigo,String email) {
	//		this.codePwd.put(codigo, email);
	//	}
	//	public void changeRecoverCodePwd(String code,String pwd) {
	//		Long aux = Long.parseLong(code);
	//		String email = this.codePwd.remove(aux);
	//		try {
	//			DAOUser.changePasswordEmail(email, pwd);
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	public static ArrayList<String[]> readExcelFileToArray(File excelFile){    
		ArrayList<String[]> arrayDatos = new ArrayList<>();
		InputStream excelStream = null;
		try {
			String rutaArchivoExcel = "Asignatura_Informatica.xlsx";
			FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));

			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);

			Iterator<Row> iterator = firstSheet.iterator();
			DataFormatter formatter = new DataFormatter();
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					String contenidoCelda = formatter.formatCellValue(cell);
					System.out.println("celda: " + contenidoCelda);
				}
				//DAOCALIFICACION
			}    
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se encontró el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} finally {
			try {
				excelStream.close();
			} catch (IOException ex) {
				System.out.println("Error in file processing after close it (Error al procesar el fichero después de cerrarlo): " + ex);
			}
		}
		return arrayDatos;
	}

	public void leerFichero(File fichero) throws Exception {
		String linea="";	

		//Datos necesarios para crear una asignatura
		String asignatura ;
		int curso ;
		//Datos necesarios para crear una prueba
		String prueba;
		int orden = 0;
		double N_min;
		double N_corte;
		double N_max;
		//Datos necesarios para crear una relacion entre pruebas
		String prueba1;
		String prueba2;

		//Aqui sacamos los datos de la asignatura (nombre y curso) del nombre del fichero
		StringTokenizer st = new StringTokenizer(fichero.getName(), "_");
		asignatura= st.nextToken();
		curso = Integer.parseInt(st.nextToken());

		System.out.println("Asignatura: "+ asignatura +" Curso:" + curso + "º");


		Asignatura asig = DAOAsignatura.registrar(asignatura,curso);
		if(asig != null) {
			int id_asig = asig.getId();
			if(id_asig != 0) {
				asignaturas.put(id_asig, asig);
			}
		}

		//Recorremos todo el fichero haciendo un token de cada linea

		// Ya hemos terminado de trabajar con el nombre del fichero, pasamos a hacerlo con su contenido
		StringTokenizer stSaltoLinea = new StringTokenizer(getContenidoFichero(fichero),"/");

		//Con esto recorremos todas las línas del archivo
		while(stSaltoLinea.hasMoreTokens()){

			linea = stSaltoLinea.nextToken();
			linea = linea.replaceAll(" ;",";");

			StringTokenizer stPuntoComa = new StringTokenizer(linea,";");
			//Si el primer elemento es % todo lo que viene a continuación hasta el siguiente salto de linea
			//es información referente a una asignatura. En cambio si viene # será información referente a 
			//relaciones entre pruebas
			if(linea.startsWith("%")) {

				//Despreciamos el primer token ya que solo nos sirve para identificar la informacion que viene a continuacion
				stPuntoComa.nextToken();

				prueba = stPuntoComa.nextToken();
				orden = Integer.parseInt(stPuntoComa.nextToken());
				N_min = Double.parseDouble(stPuntoComa.nextToken());
				N_corte = Double.parseDouble(stPuntoComa.nextToken());
				N_max = Double.parseDouble(stPuntoComa.nextToken());

				Prueba pr = DAOPrueba.registrar(prueba,orden,N_min,N_corte, N_max, asig.getId());
				if(pr != null) {
					String nombre_pr = pr.getTitulo();
					if(nombre_pr != null) {
						pruebas.put(pr.getId(), pr);
					}
				}
			}
			else if(linea.startsWith("#")) {
				//Despreciamos el primer token ya que solo nos sirve para identificar la informacion que viene a continuacion
				stPuntoComa.nextToken();

				prueba1 =stPuntoComa.nextToken();
				prueba2 =stPuntoComa.nextToken();
				System.out.println("Es una relacion entre las pruebas " + prueba1 + "y" + prueba2);

				RelacionPrueba rp = DAORelacionPrueba.registrar(prueba1,prueba2);
			}
		}
		System.out.println("Done");
	}

	public static String getContenidoFichero(File fichero) {

		StringBuffer buffer = new StringBuffer();
		String line;
		FileReader fReader;
		BufferedReader bReader;

		try {
			fReader = new FileReader(fichero);
			bReader = new BufferedReader(fReader);
			while ((line = bReader.readLine()) != null){
				buffer.append(line);
			}
			bReader.close();
			fReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
}



