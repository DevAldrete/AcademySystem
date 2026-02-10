package com.devaldrete.repositories;

import java.util.List;
import java.util.Optional;

import com.devaldrete.domain.Alumno;
import com.devaldrete.domain.Curso;
import com.devaldrete.domain.Materia;

public class CursoRepository extends Repository<Curso> {

  private final AlumnoRepository alumnoRepository;

  public CursoRepository(List<Curso> entities, AlumnoRepository alumnoRepository) {
    super(entities);
    this.alumnoRepository = alumnoRepository;
  }

  public List<Materia> getMateriasByCursoId(Long cursoId) {
    Optional<Curso> cursoOpt = this.getById(cursoId);

    if (cursoOpt.isPresent()) {
      return cursoOpt.get().getMaterias();
    } else {
      return List.of();
    }
  }

  public void updateMateriaById(Long cursoId, Materia updatedMateria) {
    Optional<Curso> cursoOpt = this.getById(cursoId);
    if (cursoOpt.isPresent()) {
      Curso curso = cursoOpt.get();
      List<Materia> materias = curso.getMaterias();
      for (int i = 0; i < materias.size(); i++) {
        if (materias.get(i).getId().equals(updatedMateria.getId())) {
          materias.set(i, updatedMateria);
          this.update(curso);
          break;
        }
      }
    }
  }

  public Optional<Materia> getMateriaById(Long materiaId) {
    for (Curso curso : this.getAll()) {
      for (Materia materia : curso.getMaterias()) {
        if (materia.getId().equals(materiaId)) {
          return Optional.of(materia);
        }
      }
    }
    return Optional.empty();
  }

  public void removeMateriaById(Long cursoId, Long materiaId) {
    Optional<Curso> cursoOpt = this.getById(cursoId);
    if (cursoOpt.isPresent()) {
      Curso curso = cursoOpt.get();
      List<Materia> materias = curso.getMaterias();
      materias.removeIf(materia -> materia.getId().equals(materiaId));
      this.update(curso);
    }
  }

  public List<Materia> getMateriasByAlumnoId(Long alumnoId) {
    Optional<Curso> cursoOpt = getCursoByAlumnoId(alumnoId);

    if (cursoOpt.isEmpty()) {
      return List.of();
    }

    return cursoOpt.get().getMaterias();

  }

  public Optional<Curso> getCursoByAlumnoId(Long alumnoId) {
    Optional<Alumno> alumnoOpt = alumnoRepository.getById(alumnoId);

    if (alumnoOpt.isEmpty()) {
      return Optional.empty();
    }

    Optional<Curso> cursoOpt = this.getAll().stream()
        .filter(c -> c.getId().equals(alumnoOpt.get().getCursoId()))
        .findFirst();

    return cursoOpt;
  }

  public Optional<Curso> getCursoByMateriaId(Long materiaId) {
    for (Curso curso : this.getAll()) {
      for (Materia materia : curso.getMaterias()) {
        if (materia.getId().equals(materiaId)) {
          return Optional.of(curso);
        }
      }
    }
    return Optional.empty();
  }
}
