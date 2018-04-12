package br.hela.exceptions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorDetailRepository extends JpaRepository<ErrorDetail, ErrorDetailId> {

}
