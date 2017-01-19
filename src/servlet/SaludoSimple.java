package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Ejemplo de uso de servlet
 * Muestra un saludo simple empleando el parámetro nombre
 * Servlet implementation class SaludoSimple
 */
@WebServlet(name="SaludoSimple",urlPatterns="/saludar")
public class SaludoSimple extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * Nombre de la aplicación
	 */
	private String nombreAp;
	
	/**
	 * Datasource que se empleará
	 */
	private DataSource dsBdTest;
	
	/**
	 * Constante para establecer el tipo de contenido devuelto y codificación
	 */
	private static final String CONTENT_TYPE="text/html;charset=UTF-8";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaludoSimple() {
        super();
    }

    /**
     * Método de inicialización del servlet
     */
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	
    	System.out.println("En método init");
    	ServletContext application = config.getServletContext();
    	nombreAp = (String) application.getInitParameter("nombreAp");
    	System.out.println("Nombre Ap: "+nombreAp);
    	try {
	    	InitialContext initCtx = new InitialContext();
	        dsBdTest = (DataSource) initCtx.lookup("java:jboss/datasources/MySQLDS2");
	        //DataSource ds = (DataSource) initCtx.lookup("java:app/env/MySqlDS");
	        System.out.println("Probando el acceso al DataSource al arrancar el servlet...");
	        Connection conDB = dsBdTest.getConnection();
	        Statement s = conDB.createStatement();
	        System.out.println("Todo está ¡¡OK!!");
    	}
    	catch(NamingException ne) {
    		System.out.println("Error en servicio JNDI al intentar crear el contexto inicial del servlet o al buscar el datasource.");
    	}
    	catch(SQLException sqle) {
    		System.out.println("Error al acceder a la base de datos.");
    	}

    }
	/**
	 * Procesamiento de peticiones GET
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * Procesamiento de peticiones POST
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.write("<html>");
		out.write("<head><title>Saludo Simple</title></head>");
		out.write("<body><p>");
		out.write("Aplicación: "+nombreAp);
		out.write("<br>");
		out.write("\n¡Saludos! "+ nombre);
		out.write("</body>");
		out.write("</html>");
		out.close();
	}

}
