<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
					if (ext1.equals("xlsx") || ext1.equals("xls")) {
						Manager.get().readExcelFile(fichero);
%>
<html>
<body onload="redirect()">
	<script>
	function redirect() {
	    alert("Archivo de notas cargado con exito");
	    location.href = "../html/cargaArchivos.html";
	}
    </script>
</body>

<%
	} else {
						Manager.get().leerFichero(fichero);
%>

<body onload="redirect()">
	<script>
	function redirect() {
	    alert("Archivo de configuración cargado con exito");
	    location.href = "../html/cargaArchivos.html";
	}
    </script>
</body>

<%
	}

				}
			}
		} catch (Exception ex) {
%>

<body onload="redirect()">
	<script>
	function redirect() {
	    alert("ERROR AL CARGAR EL ARCHIVO: Revise la consola del servidor para más informacion");
	    location.href = "../html/cargaArchivos.html";
	}
    </script>
</body>
</html>
<%
	System.out.println(ex);
		}
	}
%>