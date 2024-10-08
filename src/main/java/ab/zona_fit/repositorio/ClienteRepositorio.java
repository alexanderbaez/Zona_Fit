package ab.zona_fit.repositorio;

import ab.zona_fit.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

//Utilizamos la interfaz de spring JpaRepository obtenemos automaticamente el CRUD
public interface ClienteRepositorio extends JpaRepository<Cliente,Integer> {
}
