package com.example.Agendamento_de_consulta.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Agendamento_de_consulta.entity.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    // BUSCA O USUÁRIO PELO EMAIL, RETORNANDO UM OPCIONAL RETONANDO O USUÁRIO (CASO EXISTA) OU TA VAZIO
    Optional<Usuario> findByEmail(String email);

    // BUSCA O USUÁRIO PELO CPF, retornando true ou false;
    boolean existsByCpf(String cpf);
 
    // BUSA O USUÁRIO PELO EMAIL, retornando true ou false;
    boolean existsByEmailIgnoreCase(String email);
}
