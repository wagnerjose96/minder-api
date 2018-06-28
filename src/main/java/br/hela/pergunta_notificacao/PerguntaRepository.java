package br.hela.pergunta_notificacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, PerguntaId> {

}
