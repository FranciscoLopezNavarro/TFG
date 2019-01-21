<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.*"%>


<%
	File file;
	int maxFileSize = 5000 * 1024;
	int maxMemSize = 5000 * 1024;

	String contentType = request.getContentType();
	if ((contentType.indexOf("multipart/form-data") >= 0)) {

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
	 	ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxFileSize);
		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem fi = (FileItem) iter.next();
				if (!fi.isFormField()) {
					String fileName = fi.getName();
					File fichero = new File(fileName);
					fi.write(fichero);
					String ext1 = FilenameUtils.getExtension(fichero.getName());
					if (ext1.equals( "xlsx") || ext1.equals("xls")) {
						Manager.get().readExcelFile(fichero);
					} else {
						Manager.get().leerFichero(fichero);
					}
					out.println("Uploaded Filename: " + fileName + "<br>");
////HAY QUE TRATAR LO QUE PASA UNA VEZ CARGADOS LOS ARCHIVOS DE MAQUINA Y FIERA, CON EL EXCEL HAY NULL POINTER DESPUES DE CARGARLO
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
%>