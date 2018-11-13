<%@page import="org.json.JSONObject, edu.uclm.esi.tfg.dominio.*"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>

<%
   File file ;
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
	   upload.setSizeMax( maxFileSize );
	   // Parse the request
	  try{
	   	List<FileItem> items = upload.parseRequest(request);
	  	 Iterator<FileItem> iter = items.iterator();
         while ( iter.hasNext () ) 
         {
            FileItem fi = (FileItem)iter.next();
            if ( !fi.isFormField () )  {
            	String fileName = fi.getName();
                File fichero = new File(fileName);
                fi.write( fichero ) ;
                Manager.get().leerFichero(fichero);
               
                out.println("Uploaded Filename: " + fileName + "<br>");
                
            }
         }
      }catch(Exception ex) {
         System.out.println(ex);
      }
   }
%>