package ab.zona_fit.controlador;

import ab.zona_fit.modelo.Cliente;
import ab.zona_fit.servicio.IClienteServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //lo hacemos parte de la fabrica de spring
@ViewScoped //indicamos que el index controlador va a estar disponible por el tiempo que se muestre esta vista
@Data


public class IndexControlador {

    @Autowired
    IClienteServicio clienteServicio;
    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    //creamos un constrctor debido a que la clase la maneja jsp genera automaticamente los constructores
    //si queremos usar un constructor lo hacemos con @PostConstruct

    @PostConstruct //despues de que se mande a llamar el constructor de la clase
    public void inicializar(){//entonces podemos mandar a llamar un metodo para terminar de inicializar cualquier caracteritica que nece...

        cargarDatos();
    }
    public void cargarDatos(){
      //cargamos los clientes
      this.clientes = this.clienteServicio.listarClientes();
      //mostramos
      clientes.forEach((cliente -> logger.info(cliente.toString())));
    }

    //agregamos un cliente--> corresponde al button de + Nuevo Cliente
    public void agregarCliente(){
        this.clienteSeleccionado = new Cliente();
    }

    public void guardarCliente() {
        logger.info("cliente a guardar: " + this.clienteSeleccionado);
        //Agregar (insert)
        try {
            if (this.clienteSeleccionado.getIdcliente() == null) {
                this.clienteServicio.guardarCliente(this.clienteSeleccionado);
                this.clientes.add(this.clienteSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Guardado Exitosamente", null));
            } else {
            //Modificar (update)
                this.clienteServicio.guardarCliente(this.clienteSeleccionado);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Modificado Exitosamente", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar el cliente", null));
            logger.error("Error al guardar el cliente", e);
        }

        // Ocultar la ventana modal
        PrimeFaces.current().executeScript("PF('ventanaModalCliente').hide()");

        // Actualizar la tabla y los mensajes
        PrimeFaces.current().ajax().update("forma-clientes:clientes-tabla", "forma-clientes:mensajes");

        // Reset del objeto cliente seleccionado
        this.clienteSeleccionado = null;
    }

    //elimamos clientes

    public void eliminarCliente(){
        logger.info("Cliente a eliminar" + this.clienteSeleccionado);

        this.clienteServicio.eliminarCliente(this.clienteSeleccionado);
        //eliminar el registro de la lista de cliente
        this.clientes.remove(this.clienteSeleccionado);
        //reset del objeto cliente seleccionado
        this.clienteSeleccionado = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Cliente Eliminado"));

        PrimeFaces.current().ajax().update("forma-clientes:mensajes","forma-clientes:clientes-tabla");

    }

}
