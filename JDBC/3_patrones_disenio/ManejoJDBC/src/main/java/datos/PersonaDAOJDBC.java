package datos;

import domain.PersonaDTO;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Fernando Cuatro
 */

// implementamos la interfaz que hemos hecho para los metodos
public class PersonaDAOJDBC implements PersonaDAO {

 // Para manejar las transacciones con JDBC y que no se hagan los cambios automaticos
 private Connection conexionTransaccional;

 // Domain Access Object > DAO
 // Objeto de acceso de dominio
 // Es como la clase donde se hace el crud
 // Es recomendable definir las sentencias siempre al inicio
 private static final String SQL_SELECT = "SELECT id_persona, nombre, apellido, email, telefono FROM persona";
 private static final String SQL_INSERT = "INSERT INTO persona(nombre, apellido, email, telefono) VALUES (?,?,?,?)";
 private static final String SQL_UPDATE = "UPDATE persona SET nombre=?, apellido=?, email=?, telefono=? WHERE id_persona=?";
 private static final String SQL_DELETE = "DELETE FROM persona WHERE id_persona=?";

 // Constructor para recibir esta conexion si esta afuera de la clase
 public PersonaDAOJDBC() {
 }

 // Es la misma conexion con la diferencia que esta coneccion no se va a cerrar a ejercuar algun metodo
 // Para hace commit o Rollback | Commit para ejercer la instruccion y Rollback para desacer la ejecucion
 public PersonaDAOJDBC(Connection conexionTransaccional) {
  this.conexionTransaccional = conexionTransaccional;
 }

  // Metodo para listar las personas
 public void listarPersonas() {
  List<PersonaDTO> personas = seleccionar();

  //  for (PersonaDTO persona: personas)
  //  {
  //   System.out.println("persona = " + persona);
  //  }
  // Funcion landa con el forEach
  personas.forEach(persona ->
  {
   System.out.println("persona = " + persona);
  });
 }

 // Definimos el metodo que retornara objeto de tipo personas
 public List<PersonaDTO> seleccionar() {
  Connection conn = null;
  PreparedStatement stmt = null;
  ResultSet rs = null;
  PersonaDTO persona = null;
  List<PersonaDTO> personas = new ArrayList<>();

  try
  {
   // Conectamos a la base de datos
   conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConection();
   stmt = conn.prepareStatement(SQL_SELECT);
   rs = stmt.executeQuery();

   while (rs.next())
   {
    // Cada persona se convierte en un objeto de tipo persona
    persona = new PersonaDTO(rs.getInt("id_persona"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"), rs.getString("telefono"));
    personas.add(persona);
   }

  } catch (SQLException e)
  {
   e.printStackTrace(System.out);
  } finally
  {
   try
   {
    Conexion.close(rs);
    Conexion.close(stmt);

    if (this.conexionTransaccional == null)
    {
     Conexion.close(conn);
    }

   } catch (SQLException e)
   {
    e.printStackTrace(System.out);
   }
  }

  return personas;
 }

 // Metodo de insertar
 // Regresara un entero para decirnos cuantos registros fueron afectados
 public int insertar(PersonaDTO persona) {
  Connection conn = null;
  PreparedStatement stmt = null;
  int registro = 0;

  try
  {
   conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConection();
   stmt = conn.prepareStatement(SQL_INSERT);
   stmt.setString(1, persona.getNombre());
   stmt.setString(2, persona.getApellido());
   stmt.setString(3, persona.getEmail());
   stmt.setString(4, persona.getTelefono());

   // Tiene que mandarnos lo que afecto en la base de datos
   // executeUpdate() se usa para todos menos la del select
   registro = stmt.executeUpdate();

  } catch (SQLException e)
  {
   e.printStackTrace(System.out);
  } finally
  {
   try
   {
    Conexion.close(stmt);

    if (this.conexionTransaccional == null)
    {
     Conexion.close(conn);
    }
   } catch (SQLException e)
   {
    e.printStackTrace(System.out);
   }

  }

  return registro;
 }

 // Metodo de actualizar datos de una persona
 public int actualizar(PersonaDTO persona) {
  Connection conn = null;
  PreparedStatement stmt = null;
  int registro = 0;

  try
  {
   conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConection();
   stmt = conn.prepareStatement(SQL_UPDATE);
   stmt.setString(1, persona.getNombre());
   stmt.setString(2, persona.getApellido());
   stmt.setString(3, persona.getEmail());
   stmt.setString(4, persona.getTelefono());
   stmt.setInt(5, persona.getIdPersona());

   // Tiene que mandarnos lo que afecto en la base de datos
   // executeUpdate() se usa para todos menos la del select
   registro = stmt.executeUpdate();

  } catch (SQLException e)
  {
   e.printStackTrace(System.out);
  } finally
  {
   try
   {
    Conexion.close(stmt);

    if (this.conexionTransaccional == null)
    {
     Conexion.close(conn);
    }
   } catch (SQLException e)
   {
    e.printStackTrace(System.out);
   }

  }

  return registro;
 }

 // Metodo para suprimir una persona
 public int eliminar(PersonaDTO persona) {
  Connection conn = null;
  PreparedStatement stmt = null;
  int registro = 0;

  try
  {
   conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConection();
   stmt = conn.prepareStatement(SQL_DELETE);
   stmt.setInt(1, persona.getIdPersona());

   // Tiene que mandarnos lo que afecto en la base de datos
   // executeUpdate() se usa para todos menos la del select
   registro = stmt.executeUpdate();

  } catch (SQLException e)
  {
   e.printStackTrace(System.out);
  } finally
  {
   try
   {
    Conexion.close(stmt);

    if (this.conexionTransaccional == null)
    {
     Conexion.close(conn);
    }
   } catch (SQLException e)
   {
    e.printStackTrace(System.out);
   }

  }

  return registro;
 }
}