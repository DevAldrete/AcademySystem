package com.devaldrete.domain;

public class Alumno extends Base {
  private String nombre;
  private int edad;
  private Long cursoId;

  public Alumno(Long id, String nombre, int edad, Long cursoId) {
    super(id);
    this.nombre = nombre;
    this.edad = edad;
    this.cursoId = cursoId;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public Long getCursoId() {
    return cursoId;
  }

  public void setCursoId(Long cursoId) {
    this.cursoId = cursoId;
  }
}
