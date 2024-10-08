package ab.zona_fit.servicio;

import ab.zona_fit.modelo.Cliente;
import ab.zona_fit.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//agregamos @Service para que ya forme parte de la fabrica de spring
@Service

public class ClienteServicio implements IClienteServicio{

    //autoinyectamos una referencia de la capa de datos de la clase de IClienteRepositorio
    //para poder utilizarlo dentro de la clase de ClienteServicio
    @Autowired
    private ClienteRepositorio clienteRepositorio;//nos conectamos a repositoria y a cliente

    @Override
    public List<Cliente> listarClientes() {
        //listamos los clientes. regresamos los clientes que tenemos en nuestra tabla cliente
        List<Cliente> clientes = clienteRepositorio.findAll();
        return clientes;
    }

    @Override
    public Cliente buscarClientePorId(Integer idCliente) {
        //buscamos un cliente
        Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);//si hay -> id cliente, sino -> null
        return cliente;
    }

    @Override
    public void guardarCliente(Cliente cliente) {//y tambien se puede agragar o modificar
        //guardamos un cliente.
        clienteRepositorio.save(cliente);
        /* si el idCliente es igual a null entonces de genera un INSERT
           De lo contrario se hace un UPDATE/ modificacion */
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        //eliminamos un cliente
        clienteRepositorio.delete(cliente);//toma el valor de idCliente y lo elimina
    }
}
