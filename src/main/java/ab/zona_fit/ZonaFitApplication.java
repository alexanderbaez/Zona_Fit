package ab.zona_fit;

import ab.zona_fit.modelo.Cliente;
import ab.zona_fit.repositorio.ClienteRepositorio;
import ab.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	//agregamos una notacion de dependecia de IClienteServicio para que tengo conexion con las demas clases
	@Autowired
	private IClienteServicio clienteServicio;

	//empezamos a utilizar logger.info() para mostrar por pantalla la informacion
	private  static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);

	//generamos un salgo de linea \n para generarlo manualmente
	String nl = System.lineSeparator();

	public static void main(String[] args) {

		logger.info("Iniciando la aplicacion\n");
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("Aplicacion finalizada\n");
	}

	//implements la interface LineRunner para ejecutar la aplicacion como una app de consola
	@Override
	public void run(String... args) throws Exception {

		zonaFitApp();
	}
	private void zonaFitApp() {
		var salir = false;
		Scanner entrada = new Scanner(System.in);

		//Utilizamos el objeto de cliente servicio que agregamos arriba

		while (!salir) {
				var opcion = mostrarMenu(entrada);
				salir = ejecutarOpciones(opcion, entrada);
				logger.info(nl);
		}
	}

	private int mostrarMenu(Scanner entrada){
		logger.info("""
			   \n----- Aplicacion Zona Fit (GYM) -----
				   1. Listar Cliente
				   2. Buscar Cliente
				   3. Agregar Cliente
				   4. Modificar Cliente
				   5. Eliminar Cliente
				   6. Salir
			   Elije una Opcion:\s""");
	return Integer.parseInt(entrada.nextLine());
	}

	private Boolean ejecutarOpciones(int opcion, Scanner entrada){
		var salir = false;

		switch (opcion){

			case 1 -> {// 1.Listar Clientes
				logger.info(nl + "----- Listado de Clientes -----"+nl);
				//recuperamos el listado de clientes
				List<Cliente> clientes = clienteServicio.listarClientes();
				//mostramos los clientes
				clientes.forEach(cliente -> logger.info(cliente.toString() + nl));
			}
			case 2 -> {// 2.Buscar Cliente
				logger.info(nl+" ------ Buscar Cliente por Id ------ "+nl);
				logger.info(nl +"Introduce el Id del cliente a buscar: ");
				var idCliente = Integer.parseInt(entrada.nextLine());
				Cliente buscarCliente = clienteServicio.buscarClientePorId(idCliente);
				if (buscarCliente!=null)
					logger.info("Cliente Encontrado: "+buscarCliente + nl);
				else
					logger.info("Cliente no encontrado: "+buscarCliente+nl);
			}
			case 3 -> {// 3. Agregar Cliente

				logger.info("----- Agregar Cliente -----"+nl);
				logger.info("Nombre: ");
				var nombre = entrada.nextLine();
				logger.info("Apellido: ");
				var apellido = entrada.nextLine();
				logger.info("Membresia: ");
				var membresia = Double.parseDouble(entrada.nextLine());

				//creamos el objetos de tipo cliente
				Cliente cliente = new Cliente();
				//utilizamos los metodos setters para agregar los datos al objeto cliente
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setMembresia(membresia);

				//Guardamos cliente en la base de datos
				clienteServicio.guardarCliente(cliente);
				logger.info("Cliente agragado: "+cliente +nl);
			}
			case 4 ->{// 4. Modificar Cliente
				logger.info(nl+ "----- Modificar Clinete ----- "+nl);

				logger.info("Ingrese el Id del cliente a modificar: ");
				var idCliente = Integer.parseInt(entrada.nextLine());
				//verificamos si el cliente esta en la base de dato
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);

				if (cliente != null){
					logger.info("Nombre: ");
					var nombre = entrada.nextLine();
					logger.info("Apellido: ");
					var apellido = entrada.nextLine();
					logger.info("Membresia: ");
					var membresia = Double.parseDouble(entrada.nextLine());

					//Asignamos los datos
					cliente.setIdcliente(idCliente);
					cliente.setNombre(nombre);
					cliente.setApellido(apellido);
					cliente.setMembresia(membresia);

					//modificamos el cliente de la base de datos
					clienteServicio.guardarCliente(cliente);
					logger.info("Cliente modificado: "+cliente+nl);
				}
				else
					logger.info("Cliente No Encontrado: "+cliente +nl);

			}
			case 5 ->{// 5. Eliminar Cliente
				logger.info(nl +"----- Eliminar Cliente -----"+nl);
				logger.info("Ingresa del Id del cliente a Eliminar: ");
				var idCliente = Integer.parseInt(entrada.nextLine());

				//buscamos el cliente que deseamos eliminar
				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);

				if (cliente!=null){
					//mandamos el cliente con el id a eliminar
					clienteServicio.eliminarCliente(cliente);
					logger.info("Cliente Eliminado: "+cliente +nl);
				}
				else
					logger.info("Cliente No Encontrado: "+cliente + nl);

			}
			case 6 ->{// 6. Salir
				logger.info(nl +"----- Muchas Gracias -----"  +nl + nl);
				salir = true;
			}
			default -> logger.info("Error. Opcion Invalida: "+opcion + nl);
		}
	return salir;
	}
}
