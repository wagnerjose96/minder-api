package br.hela.usuario;

import org.springframework.data.jpa.repository.JpaRepository;;

public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioId> {

	
}
