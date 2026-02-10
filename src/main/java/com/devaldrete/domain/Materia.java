package com.devaldrete.domain;

public class Materia extends Base {
  private String nombre;
  private int creditos;
  private int horasSemanales;

  public Materia(Long id, String nombre, int creditos, int horasSemanales) {
    super(id);
    this.nombre = nombre;
    this.creditos = creditos;
    this.horasSemanales = horasSemanales;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getCreditos() {
    return creditos;
  }

  public void setCreditos(int creditos) {
    this.creditos = creditos;
  }

  public int getHorasSemanales() {
    return horasSemanales;
  }

  public void setHorasSemanales(int horasSemanales) {
    this.horasSemanales = horasSemanales;
  }
}
