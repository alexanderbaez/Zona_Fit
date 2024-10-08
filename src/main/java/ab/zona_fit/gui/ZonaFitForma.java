package ab.zona_fit.gui;

import ab.zona_fit.modelo.Cliente;
import ab.zona_fit.servicio.ClienteServicio;
import ab.zona_fit.servicio.IClienteServicio;
import ch.qos.logback.core.util.DelayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


//@Component //hacemos que ZonaFitForma forme parte de la fabriga de Spring
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    //definimos el atributao que es del tipo de la interfaz
    IClienteServicio clienteServicio;
    //declaramos la varibal de la tabla de cliente, maneja los datos de la tabla
    private DefaultTableModel tablaModeloCliente;

    //cremos el atributo para el Id cliente y que lo muestre en los campos despues de hacerle click
    private Integer idCliente;


    //constructor de la clase
    @Autowired//inyectamos la relacion de dependencia desde el constructor
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;//inicializamos la variable
        iniciarForma();

        //Guardar Cliente
        guardarButton.addActionListener(e -> guardarCliente());

        //modificar cliente haciendo click en la tabla y que aparezcan los datos en los campos(nombre-apellido-menbresia)
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //hacemos click al cliente en la tabla y se llenan los campos de datos
                cargarClienteSeleccionado();
            }
        });
        //Eliminar un cliente haciendo click en la tabla
        eliminarButton.addActionListener(e -> eliminarCliente());

        //limpiamos el campo del formulario
        limpiarButton.addActionListener(e -> limpiarFormulario());
    }
    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//al cerrar pestaña finaliza programa
        setSize(800,600);//tamaño de la ventana
        setLocationRelativeTo(null);// centramos la ventana
    }

    //creamos y modificamos los elementos de la tabla de cliente
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //creamos objetos y le decimos la cantidad de registros y de columnos
        //(los registro se autoincrementa)
        this.tablaModeloCliente = new DefaultTableModel(0,4){// 4 columna
            //queremos que de modifiquen los datos desde la tabla?
            @Override
            public boolean isCellEditable(int row, int column){//row -> renglon . column -> columna
                return false; //false-> no . true -> si
            }
        };

        //definimos los cabeceros de nuestra tabla
        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresia"};

        //mandamos los nombres de las columnas de nuestra tabla. le enviamos el cabecero
        this.tablaModeloCliente.setColumnIdentifiers(cabeceros);

        //creamos la tabla, utilizando la variable que declatamos al princio de cleintesTabla
        this.clientesTabla = new JTable(tablaModeloCliente);//al objeto tabla le mandamos la tabla creada

        //restringimo la seleccion de la tabla a un solo cliente
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargamos el listado de tipoClientes
        listarClientes();
    }

    //Listamos los cliente de la base de datos para mostrar en la tabla
    private void listarClientes(){
        //realizamos el conteo de registro que inicializa en 0
        this.tablaModeloCliente.setRowCount(0);
        //definimos la variable de cliente
        var clientes = this.clienteServicio.listarClientes();//recibimos el listado de cliente
        clientes.forEach(cliente -> {//mostramos los clientes
            //creamos un objeto para mostrar los cliente con un renglon, para ubicacione en la tabla
            Object[] renglonClientes = {
                    cliente.getIdcliente(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia(),
            };
            //por cada objeto que tengamos lo mandamos a la tablaModeloCliente(depende de Jtable)
            this.tablaModeloCliente.addRow(renglonClientes); //mandamos los clientes en renglon
        });
    }

    //guardamos y tambien modificamos un cliente
    private void guardarCliente(){

        //preguntamos si el texto esta vacio
        //si precionamos Guardar y y el campo de nombre esta vacio: nos muestra el mensaje
        if (nombreTexto.getText().equals("")){
            mostrarMensaje("Ingresa un nombre. ");
            //si aparece el mensaje, nos dirije al campo nombre
            nombreTexto.requestFocusInWindow();
            return;
        }
        //si precionamos Guardar y y el campo de membresia esta vacio: nos muestra el mensaje
        if (membresiaTexto.getText().equals("")){
            mostrarMensaje("Ingrese la Membresia. ");
            //si aparece el mensaje, nos dirije al campo membresia
            membresiaTexto.requestFocusInWindow();
            return;
        }

        //Recuperamos los valores y los asignamos a la base de dato y a la tabla
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var membresia = Double.parseDouble(membresiaTexto.getText());

        //creamos un nuevo objeto de tipo cliente (un nuevo cliente)
        //el idCliente lo mandamos en caso de que se modifique un cliente
        var cliente = new Cliente(this.idCliente, nombre, apellido,membresia);//

        // otra manera de asignar los datos del cliente
//        cliente.setIdcliente(this.idCliente);//agregamos el idCliente que obtenemos al hacer click para modificarlo
//        cliente.setNombre(nombre);
//        cliente.setApellido(apellido);
//        cliente.setMembresia(membresia);

        //llamamos al metodo clienteServicio para guardar el cliente
        this.clienteServicio.guardarCliente(cliente);//insertamos o modicamos el cliente a la base de dato

        //verificamos
        if (this.idCliente ==null)
            mostrarMensaje("Se agrego un nuevo Cliente");
        else
            mostrarMensaje("Se actualizo el Cliente");

        //una vez se agrego el cliente, limpiamos el formulario.
        limpiarFormulario();
        //recargamos y mostramos la tabla de cliente actualizada
        listarClientes();
    }

    //modificar cliente tambien
    private void cargarClienteSeleccionado(){
        //seleccionamos el registro donde dimos click
        var renglon = clientesTabla.getSelectedRow();

        //si no seleccionamos ningun renglon devolvemos -1
        if (renglon != -1){
            //recuperamos cada uno de los registro(cliente) que dimos el click


            //definimos la variables- recuperamos el valor de idcliente
            var id = clientesTabla.getModel().getValueAt(renglon,0).toString();//del registro traeme el reglon(fila) y el indice 0 (Columna - idcliente)
            //agremos el atributo de IdCliente. en el constructor
            this.idCliente = Integer.parseInt(id);

            var nombre = clientesTabla.getModel().getValueAt(renglon,1).toString();//indice 2-nombre
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon,2).toString();//indice 3-apellido
            this.apellidoTexto.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon,3).toString();// indice 3-membresia
            this.membresiaTexto.setText(membresia);
        }
    }


    //eliminar Cliente
    private void eliminarCliente(){
        //obtenemos el clinete que se selecciono con el click
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1){
            var idClienteStri = clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente = Integer.parseInt(idClienteStri);

            //creamos un nuevo objeto de tipo cliente
            var cliente = new Cliente();
            cliente.setIdcliente(this.idCliente);
            //eliminamos el cliente
            clienteServicio.eliminarCliente(cliente);
            //mostramos un ventana emergente
            mostrarMensaje("Cliente con Id "+ this.idCliente + " eliminado");
            //limpiamos los campos de formulario
            limpiarFormulario();
            //y mostramos los cliente
            listarClientes();

        }
        else
            mostrarMensaje("Debe seleccionar un Cliente a Eliminar");
    }

    //limpiamos el formulario despues de agregar el cliente
    private void limpiarFormulario(){
        //limpiamos los datos al agregar un nueco cliente
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");

        //limpiamos el del cliente seleccionado. (al modificar)
        this.idCliente = null;
        //desmarcamos el registro una vez modificado de la tabla
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this,mensaje);
    }
}
