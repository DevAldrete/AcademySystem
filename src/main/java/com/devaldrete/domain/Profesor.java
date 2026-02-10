package com.devaldrete.domain;

public class Profesor extends Base {
  private String nombre;
  private double sueldoPorHora;
  private Long materiaId;

  public Profesor(Long id, String nombre, double sueldoPorHora, Long materiaId) {
    super(id);
    this.nombre = nombre;
    this.sueldoPorHora = sueldoPorHora;
    this.materiaId = materiaId;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public double getSueldoPorHora() {
    return sueldoPorHora;
  }

  public void setSueldoPorHora(double sueldoPorHora) {
    this.sueldoPorHora = sueldoPorHora;
  }

  public Long getMateriaId() {
    return materiaId;
  }

  public void setMateriaId(Long materiaId) {
    this.materiaId = materiaId;
  }
}
