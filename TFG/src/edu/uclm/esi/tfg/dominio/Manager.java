package edu.uclm.esi.tfg.dominio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.uclm.esi.tfg.persistencia.DAOAlumno;
import edu.uclm.esi.tfg.persistencia.DAOAsignatura;
import edu.uclm.esi.tfg.persistencia.DAOCalificacion;
import edu.uclm.esi.tfg.persistencia.DAOProfesor;
import edu.uclm.esi.tfg.persistencia.DAOPrueba;
import edu.uclm.esi.tfg.persistencia.DAORelacionPrueba;

public class Manager {

	/////////////////////////////////////////////////////METODOS DE CONFIGURACION////////////////////////////////////////////

	private ConcurrentHashMap<String, Profesor> usuarios;
	private ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>();
	private ArrayList<Alumno> alumnos = new ArrayList<Alumno>();
	private ArrayList<Prueba> pruebas = new ArrayList<Prueba>();
	private ArrayList<RelacionPrueba> relacionesPruebas = new ArrayList<RelacionPrueba>();
	private ArrayList<Calificacion> calificaciones = new ArrayList<Calificacion>();

	private Manager() {
		this.usuarios = new ConcurrentHashMap<>();
		this.asignaturas = new ArrayList<>();
		this.alumnos = new ArrayList<>();
		this.pruebas = new ArrayList<>();
		this.relacionesPruebas = new ArrayList<>();
		this.calificaciones = new ArrayList<>();

	}

	public static class ManagerHolder {
		static Manager singleton = new Manager();
	}

	public static Manager get() {
		return ManagerHolder.singleton;
	}

	/**
	 * Analiza el excel que se carga con las notas y lo almacena en base de datos
	 * 
	 * @param excelFile
	 */
	public void readExcelFile(File excelFile) {

		ArrayList<String> nombrePruebas = new ArrayList<String>();
		try {
			FileInputStream inputStream = new FileInputStream(excelFile);

			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);

			Iterator<Row> iterator = firstSheet.iterator();
			DataFormatter formatter = new DataFormatter();
			// Bucle para las filas
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				int rowIndex = nextRow.getRowNum();

				if (isRowEmpty(nextRow) == false) {
					Iterator<Cell> cellIterator = nextRow.cellIterator();

					// Vamos a leer la primera linea que nos dira el numero de pruebas que tiene la
					// asignatura as� como su nombre
					if (rowIndex == 0) {
						// Se descartan las 2 primeras columnas que corresponden al N� de alumno y al
						// a�o, vendr� as� en todos los excel de notas
						cellIterator.next();
						cellIterator.next();
						// Comprobamos el numero de pruebas que corresponden a esa asignatura
						while (cellIterator.hasNext()) {
							String contenidoCelda = formatter.formatCellValue(cellIterator.next());
							nombrePruebas.add(contenidoCelda);
						}

					} else {
						// AQUI VAMOS LEYENDO EL PRIMER ELEMENTO DE CADA FILA (ID ALUMNO) Y CREANDO LOS
						// ALUMNOS EN LA BD

						int id = Integer.parseInt(formatter.formatCellValue(cellIterator.next()));
						try {
							registrarAlumno(id);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// AHORA LEEMOS LAS SIGUIENTES CELDAS DE LA FILA, EL SEGUNDO VALOR SERA EL A�O Y
						// DESPUES SE LEEN TANTAS CELDAS COMO PRUEBAS HAYAMOS LEIDO EN LA PRIMERA LINEA
						// Bucle para las celdas
						Double nota;
						String year = formatter.formatCellValue(cellIterator.next());

						for (int x = 0; x < nombrePruebas.size(); x++) {
							Cell celda = cellIterator.next();
							if (celda.getCellTypeEnum() == CellType.STRING) {
								nota = -1.0;
							} else {
								nota = Double.parseDouble(formatter.formatCellValue(celda).replaceAll(",", "."));
							}
							try {
								registrarCalificacion(id, getIdPruebas(nombrePruebas.get(x)), year, nota);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}
			}
		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("The file not exists (No se ha encontrado el fichero): " + fileNotFoundException);
		} catch (IOException ex) {
			System.out.println("Error in file procesing (Error al procesar el fichero): " + ex);
		} catch (NullPointerException e) {
			System.out.println("Se ha leido el archivo completo" + e);
		}
		System.out.println("ARCHIVO DE NOTAS CARGADO CON EXITO");
	}

	/**
	 * Nos dice si una fila del Excel esta vacia o no
	 * 
	 * @param nextRow
	 * @return
	 */
	private boolean isRowEmpty(Row nextRow) {
		if (nextRow == null) {
			return true;
		}
		if (nextRow.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = nextRow.getFirstCellNum(); cellNum < nextRow.getLastCellNum(); cellNum++) {
			Cell cell = nextRow.getCell(cellNum);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Lee el archivo de configuracion para cada asignatura
	 * 
	 * @param fichero
	 * @throws Exception
	 */
	public void leerFichero(File fichero) throws Exception {
		String linea = "";

		// Datos necesarios para crear una asignatura
		String asignatura;
		int curso;
		// Datos necesarios para crear una prueba
		String prueba;
		int orden = 0;
		double N_min;
		double N_corte;
		double N_max;

		// Datos necesarios para crear una relacion entre pruebas
		String prueba1;
		String prueba2;

		// Aqui sacamos los datos de la asignatura (nombre y curso) del nombre del
		// fichero

		StringTokenizer st = new StringTokenizer(fichero.getName(), "_");
		asignatura = st.nextToken();
		curso = Integer.parseInt(st.nextToken());
		Asignatura asig = DAOAsignatura.registrar(asignatura, curso);
		if (asig != null) {
			updateAsignaturas(asig);
		}
		// Ya hemos terminado de trabajar con el nombre del fichero, pasamos a hacerlo
		// con su contenido
		StringTokenizer stSaltoLinea = new StringTokenizer(getContenidoFichero(fichero), "/");

		// Con esto recorremos todas las l�nas del archivo
		while (stSaltoLinea.hasMoreTokens()) {

			linea = stSaltoLinea.nextToken();
			linea = linea.replaceAll(" ;", ";");

			StringTokenizer stPuntoComa = new StringTokenizer(linea, ";");

			// Si el primer elemento es % todo lo que viene a continuaci�n hasta el
			// siguiente salto de linea
			// es informaci�n referente a una asignatura. En cambio si viene # ser�
			// informaci�n referente a
			// relaciones entre pruebas

			if (linea.startsWith("%")) {

				// Despreciamos el primer token ya que solo nos sirve para identificar la
				// informacion que viene a continuacion
				stPuntoComa.nextToken();

				prueba = stPuntoComa.nextToken();
				orden = Integer.parseInt(stPuntoComa.nextToken());
				N_min = Double.parseDouble(stPuntoComa.nextToken());
				N_corte = Double.parseDouble(stPuntoComa.nextToken());
				N_max = Double.parseDouble(stPuntoComa.nextToken());

				registrarPrueba(prueba, orden, N_min, N_corte, N_max, asig.getId());
			} else if (linea.startsWith("#")) {
				// Despreciamos el primer token ya que solo nos sirve para identificar la
				// informacion que viene a continuacion
				stPuntoComa.nextToken();

				prueba1 = stPuntoComa.nextToken();
				prueba2 = stPuntoComa.nextToken();

				try {
					registrarRelacionPrueba(prueba1, prueba2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("ARCHIVO DE CONFIGURACION CARGADO CON EXITO");
	}

	/**
	 * Nos devuelve el contenido de un fichero
	 * @param fichero
	 * @return
	 */
	public static String getContenidoFichero(File fichero) {

		StringBuffer buffer = new StringBuffer();
		String line;
		FileReader fReader;
		BufferedReader bReader;

		try {
			fReader = new FileReader(fichero);
			bReader = new BufferedReader(fReader);
			while ((line = bReader.readLine()) != null) {
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

	/**
	 * Carga los datos de BD en las estructuras de datos (ArrayList)
	 */
	public void cargarDatos() {
		cargarAsignaturas();
		cargarPruebas();
		cargarRelaciones();
		cargarAlumnos();
		cargarCalificaciones();
	}

	private void cargarAsignaturas() {
		asignaturas = DAOAsignatura.cargar();
	}

	private void cargarAlumnos() {
		alumnos = DAOAlumno.cargar();
	}

	private void cargarPruebas() {
		pruebas = DAOPrueba.cargar();
	}

	private void cargarRelaciones() {
		relacionesPruebas = DAORelacionPrueba.cargar();
	}

	private void cargarCalificaciones() {
		calificaciones = DAOCalificacion.cargar();

	}

	/////////////////////////////////////////////////////METODOS DE REGISTRO EN LA BASE DE DATOS////////////////////////////////////////////


	/**
	 * Este metodo se utiliza para registrar un nuevo profesor en la BD * 
	 * @param id
	 * @param pwd
	 * @param nombre
	 * @return
	 * @throws Exception
	 */
	public Profesor registrar(String id, String pwd, String nombre) throws Exception {
		Profesor user = DAOProfesor.insert(Integer.parseInt(id), pwd, nombre);
		usuarios.put(nombre, user);
		return user;
	}

	public void registrarCalificacion(Integer alumno, Integer idPrueba, String year, Double nota) throws Exception {
		Calificacion cali = DAOCalificacion.registrar(alumno, idPrueba, nota, year);
		updateCalificaciones(cali);
	}

	public void registrarAlumno(int id) throws Exception {

		Alumno alu = DAOAlumno.registrar(id);
		if(alu != null) {
			updateAlumnos(alu);
		}else {
			System.out.println("Este alumno ya existe");
		}
	}

	private void registrarRelacionPrueba(String prueba1, String prueba2) throws Exception {
		RelacionPrueba rp = DAORelacionPrueba.registrar(prueba1, prueba2);
		updateRelaciones(rp);
	}

	private void registrarPrueba(String prueba, int orden, double n_min, double n_corte, double n_max, int asig)
			throws Exception {
		Prueba pr = DAOPrueba.registrar(prueba, orden, n_min, n_corte, n_max, asig);
		updatePruebas(pr);
	}

	private void updateAsignaturas(Asignatura asig) {
		if (asig != null) {
			asignaturas.add(asig);
		}
	}

	private void updateCalificaciones(Calificacion cali) {
		if (cali != null) {
			calificaciones.add(cali);
		}
	}

	private void updateAlumnos(Alumno alu) {
		if (alu != null) {
			alumnos.add(alu);
		}
	}

	private void updatePruebas(Prueba pr) {
		if (pr != null) {
			pruebas.add(pr);
		}
	}

	private void updateRelaciones(RelacionPrueba rp) {
		if (rp != null) {
			relacionesPruebas.add(rp);
		}
	}

	/**
	 * Se utiliza para almacenar calificaciones
	 * 
	 * @param nombrePrueba
	 * @return
	 */
	private Integer getIdPruebas(String nombrePrueba) {
		int id = 0;
		for (int i = 0; i < pruebas.size(); i++) {
			if (pruebas.get(i).getTitulo().equals(nombrePrueba)) {
				id = pruebas.get(i).getId();
			}
		}
		return id;
	}


	/////////////////////////////////////////////////////METODOS FUNCIONALES////////////////////////////////////////////

	/**
	 * Este metodo se utiliza para logguearse
	 * 
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public Profesor login(String id, String pwd) throws Exception {
		Profesor user;
		user = DAOProfesor.login(Integer.parseInt(id), pwd);
		if (user != null) {
			String nombre = user.getNombre();
			if (nombre != null) {
				usuarios.put(nombre, user);
			}
		}
		return user;
	}

	/**
	 * Este metodo se utiliza para hacer logout
	 * 
	 * @param nombre
	 * @return
	 */
	public Profesor logout(String nombre) {
		return this.usuarios.remove(nombre);
	}

	/**
	 * Devuelve las distintas asignaturas que haya almacenadas
	 * 
	 * @return
	 */
	public ArrayList<Asignatura> getAsignaturas() {
		return asignaturas;
	}

	/**
	 * Devuelve los alumnos que hay en el sistema
	 * 
	 * @return
	 */
	public ArrayList<Alumno> getAlumnos() {
		return alumnos;
	}


	/**
	 * Devuelve un array con las pruebas que tiene una determinada asignatura
	 * @param asignatura
	 * @return
	 */
	public ArrayList<Prueba> getPruebasAsignatura(Integer asignatura) {
		ArrayList<Prueba> pruebas_aux = new ArrayList<Prueba>();
		for (int i = 0; i < pruebas.size(); i++) {
			if (pruebas.get(i).getAsig() == asignatura) {
				pruebas_aux.add(pruebas.get(i));
			}
		}
		return pruebas_aux;
	}

	/**
	 * Devuelve todas las calificaciones de un alumno independientemente de la asignatura
	 * @param idalumno
	 * @return
	 */
	public ArrayList<Calificacion> getCalificacionesAlumno(Integer idalumno) {
		ArrayList<Calificacion> calificaciones_aux = new ArrayList<Calificacion>();
		for (int i = 0; i < calificaciones.size(); i++) {
			if (calificaciones.get(i).getAlumno() == idalumno) {
				calificaciones_aux.add(calificaciones.get(i));
			}
		}

		return calificaciones_aux;
	}

	/**
	 * Devuelve todas las calificaciones de un determinado grupo de pruebas
	 * @param pruebas
	 * @return
	 */
	public ArrayList<Calificacion> getCalificacionesPruebas(ArrayList<Prueba> pruebas) {


		ArrayList<Calificacion> calificaciones_aux = new ArrayList<Calificacion>();
		for (int i = 0; i < calificaciones.size(); i++) {
			for (int j = 0; j < pruebas.size(); j++) {
				if (calificaciones.get(i).getPrueba() == pruebas.get(j).getId()) {
					calificaciones_aux.add(calificaciones.get(i));
				}
			}
		}
		return calificaciones_aux;
	}

	/**
	 * Devuelve todas las calificaciones del curso actual
	 * @param pruebas
	 * @param curso_actual
	 * @return
	 */
	public ArrayList<Calificacion> getCalificacionesPruebasCursoActual(ArrayList<Prueba> pruebas, String curso_actual) {

		ArrayList<Calificacion> calificaciones_aux = new ArrayList<Calificacion>();
		for (int i = 0; i < calificaciones.size(); i++) {
			for (int j = 0; j < pruebas.size(); j++) {
				if (calificaciones.get(i).getPrueba() == pruebas.get(j).getId()
						&& calificaciones.get(i).getYear().equals(curso_actual)) {
					calificaciones_aux.add(calificaciones.get(i));
				}
			}
		}
		return calificaciones_aux;
	}
	/**
	 * Devuelve los alumnos a los que pertenecen una serie de calificaciones
	 * @param calificaciones
	 * @return
	 */
	public ArrayList<Alumno> getCalificacionesAlumnos(ArrayList<Calificacion> calificaciones) {

		ArrayList<Alumno> alumnos_aux = new ArrayList<Alumno>();
		for (int i = 0; i < alumnos.size(); i++) {
			for (int j = 0; j < calificaciones.size(); j++) {
				if (alumnos.get(i).getId() == calificaciones.get(j).getAlumno()) {
					alumnos_aux.add(alumnos.get(i));
				}
			}
		}
		return alumnos_aux;
	}

	/**
	 * Elimina un alumno y sus calificaciones de la Base de Datos
	 * @param id
	 */
	public void eliminarRegistro(int id, String year) {
		try {
			if (DAOCalificacion.eliminar(id,year)) {
				System.out.println("Calificaciones eliminadas");
				if(!DAOCalificacion.existeRegistroAlumno(id)) {
					DAOAlumno.eliminar(id);
					System.out.println("Alumno eliminado");
				}


				cargarAlumnos();
				cargarCalificaciones();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve las pruebas que estan relacionadas con la que se llama al metodo en una asignatura
	 * @param prueba
	 * @param asignatura
	 * @return
	 */
	public ArrayList<Prueba> getRelacionesPrueba(String prueba, int asignatura) {
		ArrayList<Prueba> pruebas_asig = getPruebasAsignatura(asignatura);
		ArrayList<Prueba> pruebas_aux = new ArrayList<Prueba>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		;

		ids = pruebasRelacionadas(getIDPrueba(prueba, pruebas_asig));
		for (int j = 0; j < pruebas_asig.size(); j++) {
			for (int i = 0; i < ids.size(); i++) {
				if (ids.get(i) == pruebas_asig.get(j).getId()) {
					pruebas_aux.add(pruebas_asig.get(j));
				}
			}
		}
		return pruebas_aux;
	}

	/**
	 * Devuelve la nota min/corte/max de una determinada prueba
	 * @param prueba
	 * @param asignatura
	 * @return
	 */
	public double[] getInfoPrueba(String prueba, int asignatura) {
		ArrayList<Prueba> pruebas_asig = getPruebasAsignatura(asignatura);
		double[] notas= new double[3];
		for (int j = 0; j < pruebas_asig.size(); j++) {
			if (prueba.equals(pruebas_asig.get(j).getTitulo())) {
				notas[0] = pruebas_asig.get(j).getN_min();
				notas[1] = pruebas_asig.get(j).getN_corte();
				notas[2] =pruebas_asig.get(j).getN_max();
			}
		}
		return notas;
	}

	/**
	 * Es llamado por el metodo anterior y es parte del proceso del mismo
	 * @param id
	 * @return
	 */
	public ArrayList<Integer> pruebasRelacionadas(int id) {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		for (int i = 0; i < relacionesPruebas.size(); i++) {
			if (relacionesPruebas.get(i).getPrueba1() == id) {
				ids.add(relacionesPruebas.get(i).getPrueba2());
			}
		}
		return ids;
	}

	/**
	 * Devuelve el id de una Prueba y Asignatura
	 * @param prueba
	 * @param pruebas_aux
	 * @return
	 */
	public int getIDPrueba(String prueba, ArrayList<Prueba> pruebas_aux) {
		int id = 0;
		for (int i = 0; i < pruebas_aux.size(); i++) {
			if (pruebas_aux.get(i).getTitulo().equals(prueba)) {
				id = pruebas_aux.get(i).getId();
			}
		}
		return id;
	}


	public double getMediaPrueba(int id) {
		double media = 0.0;
		double suma = 0.0;
		int size = 0;

		for (int i = 0; i < calificaciones.size(); i++) {
			if (calificaciones.get(i).getPrueba() == id && calificaciones.get(i).getNota() != (-1.0)) {
				suma += calificaciones.get(i).getNota();
				size++;
			}
		}
		media = suma / size;
		return media;
	}

	public int getAprobadosPrueba(int prueba) {
		int aprobados = 0;
		for (int j = 0; j < pruebas.size(); j++) {
			for (int i = 0; i < calificaciones.size(); i++) {
				if (calificaciones.get(i).getPrueba() == prueba && pruebas.get(j).getId() == prueba) {
					if (calificaciones.get(i).getNota() >= pruebas.get(j).getN_corte()) {
						aprobados++;
					}
				}
			}
		}
		return aprobados;
	}
	/**
	 * Este metodo devuelve el grado de alerta de un alumno en una asignatura
	 * @param alumno
	 * @param asignatura
	 * @param curso
	 * @return
	 */
	public double calculoAlertaAsig(int alumno, int asignatura, String curso) {
		double alerta = 0.0;
		Modelo modelo = new Modelo();
		ArrayList<Prueba> pruebas_asig = getPruebasAsignatura(asignatura);
		ArrayList<Calificacion> calificaciones_alumno = getCalificacionesAlumno(alumno);
		ArrayList<Calificacion> calificaciones_alumno_curso = new ArrayList<Calificacion>();

		for (int i = 0; i < calificaciones_alumno.size(); i++) {
			if (calificaciones_alumno.get(i).getYear().equals(curso)) {
				calificaciones_alumno_curso.add(calificaciones_alumno.get(i));
			}
		}
		alerta = modelo.calcularAlerta(calificaciones_alumno_curso, pruebas_asig);

		return alerta;
	}

	/**
	 * Este metodo devuelve el numero de alumnos que aprueban una determinada
	 * asignatura
	 * 
	 * @param asignatura
	 * @return counter
	 */
	public int alumnosApruebanAsig(ArrayList<Prueba> pruebas_asig) {
		int counter = 0;
		double nota_alumno;
		for (int i = 0; i < alumnos.size(); i++) {
			nota_alumno = 0.0;

			ArrayList<Calificacion> calificaciones_alumno = getCalificacionesAlumno(alumnos.get(i).getId());
			for (int j = 0; j < calificaciones_alumno.size(); j++) {
				for (int k = 0; k < pruebas_asig.size(); k++) {
					if (calificaciones_alumno.get(j).getPrueba() == pruebas_asig.get(k).getId()) {
						nota_alumno += calificaciones_alumno.get(j).getNota();
					}
				}
			}
			if (nota_alumno >= (nota_max(pruebas_asig) / 2)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Este metodo devuelve la nota maxima que se puede sacar en una determinada
	 * asignatura
	 * 
	 * @param pruebas_asig
	 * @return nota_max
	 */
	private double nota_max(ArrayList<Prueba> pruebas_asig) {
		double nota_max = 0.0;
		for (int i = 0; i < pruebas_asig.size(); i++)
			nota_max += pruebas_asig.get(i).getN_max();
		return nota_max;
	}

	public double[] devolverNotas(Integer alumno, String year, Integer n_pruebas,ArrayList<Calificacion> calificaciones) {
		double[] notas = new double[n_pruebas];
		Integer aux = 0;
		for (int i = 0; i < calificaciones.size(); i++) {
			if (calificaciones.get(i).getAlumno() == alumno && calificaciones.get(i).getYear().equals(year)) {
				notas[aux] = calificaciones.get(i).getNota();
				aux++;
			}
		}

		return notas;
	}

	public Map<Integer, ArrayList<String>> obtenerHashMap(ArrayList<Calificacion> calificaciones) {
		Map<Integer, ArrayList<String>> mapa = new HashMap<Integer,ArrayList<String>>();
		for (Calificacion  i : calificaciones) {
			int alumno = i.getAlumno();
			if(!mapa.containsKey(alumno)) {
				ArrayList<String> years = new ArrayList<String>();
				mapa.put(alumno,years);
			}
		}
		for (Calificacion  i : calificaciones) {
			int alumno = i.getAlumno();
			String year = i.getYear();

			ArrayList<String> aux = mapa.get(alumno);
			if(!aux.contains(year)) {
				aux.add(year);
			}

		}
		//		for (Map.Entry<Integer, ArrayList<String>> entry : mapa.entrySet()) {
		//			System.out.println(entry.getKey() + " = " + entry.getValue().size());
		//		}
		return mapa;
	}


}

