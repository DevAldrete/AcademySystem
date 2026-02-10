package com.devaldrete.domain;

import java.util.List;
import java.util.ArrayList;

public class Curso extends Base {
  private String totalCreditos;
  private String nombre;
  private List<Materia> materias;

  // Default constructor
  public Curso() {
    super(null);
    this.totalCreditos = "";
    this.nombre = "";
    this.materias = new ArrayList<Materia>();
  }

  // Constructor with parameters
  public Curso(Long id, String totalCreditos, String nombre, List<Materia> materias) {
    super(id);
    this.totalCreditos = totalCreditos;
    this.nombre = nombre;
    this.materias = materias;
  }

  // Constructor for copying another Curso object
  public Curso(Curso other) {
    super(other.getId());
    this.totalCreditos = other.getTotalCreditos();
    this.nombre = other.getNombre();
    this.materias = new ArrayList<Materia>(other.getMaterias());
  }

  public String getTotalCreditos() {
    return totalCreditos;
  }

  public void setTotalCreditos(String totalCreditos) {
    this.totalCreditos = totalCreditos;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Materia> getMaterias() {
    return materias;
  }

  public void setMaterias(List<Materia> materias) {
    this.materias = materias;
  }
}
