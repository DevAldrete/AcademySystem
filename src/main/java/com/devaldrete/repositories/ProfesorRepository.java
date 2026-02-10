package com.devaldrete.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.devaldrete.domain.Materia;
import com.devaldrete.domain.Profesor;

public class ProfesorRepository extends Repository<Profesor> {

  private final CursoRepository cursoRepository;

  public ProfesorRepository(List<Profesor> entities, CursoRepository cursoRepository) {
    super(entities);
    this.cursoRepository = cursoRepository;
  }

  public List<Profesor> getByMateriaId(Long materiaId) {
    return this.getAll().stream()
        .filter(profesor -> profesor.getMateriaId().equals(materiaId))
        .toList();
  }

  public double getSueldoSemanal(Long profesorId, Long materiaId) {
    Optional<Materia> materiaOpt = cursoRepository.getMateriaById(materiaId);
    Optional<Profesor> profesorOpt = this.getById(profesorId);

    if (materiaOpt.isPresent() && profesorOpt.isPresent()) {
      int horas = materiaOpt.get().getHorasSemanales();
      double sueldo = profesorOpt.get().getSueldoPorHora();

      return horas * sueldo;
    } else {
      return 0.0;
    }
  }

  public List<Profesor> getByAlumnoId(Long alumnoId) {
    List<Materia> materias = cursoRepository.getMateriasByAlumnoId(alumnoId);

    if (materias.isEmpty()) {
      return List.of();
    }

    List<Profesor> profesores = new ArrayList<>();

    for (Materia materia : materias) {
      profesores.addAll(this.getByMateriaId(materia.getId()));
    }

    return profesores;
  }

}
