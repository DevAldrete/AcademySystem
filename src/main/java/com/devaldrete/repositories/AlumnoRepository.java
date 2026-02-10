package com.devaldrete.repositories;

import java.util.List;

import com.devaldrete.domain.Alumno;

public class AlumnoRepository extends Repository<Alumno> {

  public AlumnoRepository(List<Alumno> entities) {
    super(entities);
  }

  public List<Alumno> getByCursoId(Long cursoId) {
    return this.getAll().stream()
        .filter(alumno -> alumno.getCursoId().equals(cursoId))
        .toList();
  }
}
