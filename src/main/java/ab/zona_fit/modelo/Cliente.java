package ab.zona_fit.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity  //se lo conoce como JPA
@Data    //generamos los metodos getters y setters
@NoArgsConstructor // agregamos el constructor vacio
@AllArgsConstructor // agregamos el constructos con todos los argumentos
@ToString // agregamos el metodo toString
@EqualsAndHashCode //agregamos el metodo Equals - Hashcode

public class Cliente {

    //@Id es la primari de la base de datos y es Autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idcliente;//Integer para que el valor sea null y no cero
    private String nombre;
    private String apellido;
    private Double membresia;
}
