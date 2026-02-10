package com.devaldrete;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.devaldrete.domain.Alumno;
import com.devaldrete.domain.Curso;
import com.devaldrete.domain.Materia;
import com.devaldrete.domain.Profesor;
import com.devaldrete.repositories.AlumnoRepository;
import com.devaldrete.repositories.CursoRepository;
import com.devaldrete.repositories.ProfesorRepository;

public class App {

  private final AlumnoRepository alumnoRepository;
  private final CursoRepository cursoRepository;
  private final ProfesorRepository profesorRepository;
  private Long nextAlumnoId = 100L;
  private Long nextMateriaId = 100L;
  private Long nextProfesorId = 100L;

  public App() {
    // Initialize sample data
    List<Alumno> alumnos = new ArrayList<>();
    alumnos.add(new Alumno(1L, "Juan Garcia", 20, 1L));
    alumnos.add(new Alumno(2L, "Maria Lopez", 22, 1L));
    alumnos.add(new Alumno(3L, "Carlos Rodriguez", 21, 2L));

    this.alumnoRepository = new AlumnoRepository(alumnos);

    List<Materia> materiasCurso1 = new ArrayList<>();
    materiasCurso1.add(new Materia(1L, "Matematicas", 6, 4));
    materiasCurso1.add(new Materia(2L, "Fisica", 5, 3));
    materiasCurso1.add(new Materia(3L, "Programacion", 8, 6));

    List<Materia> materiasCurso2 = new ArrayList<>();
    materiasCurso2.add(new Materia(4L, "Base de Datos", 6, 4));
    materiasCurso2.add(new Materia(5L, "Redes", 5, 3));

    List<Curso> cursos = new ArrayList<>();
    cursos.add(new Curso(1L, "24", "Ingenieria en Software", materiasCurso1));
    cursos.add(new Curso(2L, "18", "Ingenieria en Sistemas", materiasCurso2));

    this.cursoRepository = new CursoRepository(cursos, alumnoRepository);

    // Initialize sample professors
    List<Profesor> profesores = new ArrayList<>();
    profesores.add(new Profesor(1L, "Dr. Roberto Sanchez", 250.0, 1L)); // Matematicas
    profesores.add(new Profesor(2L, "Dra. Ana Martinez", 280.0, 2L)); // Fisica
    profesores.add(new Profesor(3L, "Ing. Pedro Gomez", 300.0, 3L)); // Programacion
    profesores.add(new Profesor(4L, "Mtro. Luis Hernandez", 275.0, 4L)); // Base de Datos
    profesores.add(new Profesor(5L, "Ing. Sofia Ramirez", 260.0, 5L)); // Redes

    this.profesorRepository = new ProfesorRepository(profesores, cursoRepository);
  }

  public static void main(String[] args) {
    App app = new App();
    app.run();
  }

  public void run() {
    printWelcome();
    boolean running = true;

    while (running) {
      printMainMenu();
      int option = Integer.parseInt(IO.readln("Seleccione una opcion: "));

      switch (option) {
        case 1 -> menuAlumnos();
        case 2 -> menuCursos();
        case 3 -> menuMaterias();
        case 4 -> menuProfesores();
        case 0 -> {
          running = false;
          IO.println("\nHasta luego!");
        }
        default -> IO.println("Opcion no valida. Intente de nuevo.");
      }
    }
  }

  private void printWelcome() {
    IO.println("Sistema de Gestion Academica");
    IO.println("Universidad DevAldrete");
  }

  private void printMainMenu() {
    IO.println("Menu principal: ");
    IO.println("1. Gestionar Alumnos");
    IO.println("2. Gestionar Cursos");
    IO.println("3. Gestionar Materias");
    IO.println("4. Gestionar Profesores");
    IO.println("0. Salir");
  }

  // ==================== ALUMNOS ====================

  private void menuAlumnos() {
    boolean inMenu = true;

    while (inMenu) {
      IO.println("GESTION DE ALUMNOS");
      IO.println("1. Listar todos los alumnos");
      IO.println("2. Buscar alumno por ID");
      IO.println("3. Agregar nuevo alumno");
      IO.println("4. Actualizar alumno");
      IO.println("5. Eliminar alumno");
      IO.println("6. Ver materias de un alumno");
      IO.println("0. Volver al menu principal");

      int option = Integer.parseInt(IO.readln("Seleccione una opcion: "));

      switch (option) {
        case 1 -> listarAlumnos();
        case 2 -> buscarAlumno();
        case 3 -> agregarAlumno();
        case 4 -> actualizarAlumno();
        case 5 -> eliminarAlumno();
        case 6 -> verMateriasAlumno();
        case 0 -> inMenu = false;
        default -> IO.println("Opcion no valida.");
      }
    }
  }

  private void listarAlumnos() {
    List<Alumno> alumnos = alumnoRepository.getAll();

    if (alumnos.isEmpty()) {
      IO.println("\nNo hay alumnos registrados.");
      return;
    }

    IO.println("LISTADO DE ALUMNOS");

    for (Alumno alumno : alumnos) {
      String cursoNombre = cursoRepository.getById(alumno.getCursoId())
          .map(Curso::getNombre)
          .orElse("Sin curso");

      IO.println(String.format("ID: %d | Nombre: %s | Edad: %d | Curso: %s",
          alumno.getId(), alumno.getNombre(), alumno.getEdad(), cursoNombre));
    }
  }

  private void buscarAlumno() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del alumno: "));
    Optional<Alumno> alumnoOpt = alumnoRepository.getById(id);

    if (alumnoOpt.isEmpty()) {
      IO.println("Alumno no encontrado.");
      return;
    }

    Alumno alumno = alumnoOpt.get();
    String cursoNombre = cursoRepository.getById(alumno.getCursoId())
        .map(Curso::getNombre)
        .orElse("Sin curso");

    IO.println("\nDETALLE DEL ALUMNO");
    IO.println(String.format("ID:     %-30d", alumno.getId()));
    IO.println(String.format("Nombre: %-30s", alumno.getNombre()));
    IO.println(String.format("Edad:   %-30d", alumno.getEdad()));
    IO.println(String.format("Curso:  %-30s", cursoNombre));
  }

  private void agregarAlumno() {
    IO.println("\nAgregar Nuevo Alumno");
    String nombre = IO.readln("Nombre: ");
    int edad = Integer.parseInt(IO.readln("Edad: "));

    listarCursosSimple();
    Long cursoId = Long.parseLong(IO.readln("ID del curso: "));

    if (cursoRepository.getById(cursoId).isEmpty()) {
      IO.println("Curso no encontrado. Operacion cancelada.");
      return;
    }

    Alumno nuevoAlumno = new Alumno(nextAlumnoId++, nombre, edad, cursoId);
    alumnoRepository.save(nuevoAlumno);
    IO.println("Alumno agregado exitosamente con ID: " + nuevoAlumno.getId());
  }

  private void actualizarAlumno() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del alumno a actualizar: "));
    Optional<Alumno> alumnoOpt = alumnoRepository.getById(id);

    if (alumnoOpt.isEmpty()) {
      IO.println("Alumno no encontrado.");
      return;
    }

    Alumno alumno = alumnoOpt.get();
    IO.println("Datos actuales - Nombre: " + alumno.getNombre() + ", Edad: " + alumno.getEdad());

    String nombre = IO.readln("Nuevo nombre (Enter para mantener): ");
    if (!nombre.isEmpty()) {
      alumno.setNombre(nombre);
    }

    String edadStr = IO.readln("Nueva edad (Enter para mantener): ");
    if (!edadStr.isEmpty()) {
      alumno.setEdad(Integer.parseInt(edadStr));
    }

    alumnoRepository.update(alumno);
    IO.println("Alumno actualizado exitosamente.");
  }

  private void eliminarAlumno() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del alumno a eliminar: "));

    if (alumnoRepository.getById(id).isEmpty()) {
      IO.println("Alumno no encontrado.");
      return;
    }

    String confirm = IO.readln("Esta seguro? (s/n): ");
    if (confirm.equalsIgnoreCase("s")) {
      alumnoRepository.deleteById(id);
      IO.println("Alumno eliminado exitosamente.");
    } else {
      IO.println("Operacion cancelada.");
    }
  }

  private void verMateriasAlumno() {
    Long alumnoId = Long.parseLong(IO.readln("Ingrese el ID del alumno: "));
    List<Materia> materias = cursoRepository.getMateriasByAlumnoId(alumnoId);

    if (materias.isEmpty()) {
      IO.println("No se encontraron materias para este alumno.");
      return;
    }

    IO.println("\nMaterias del Alumno");
    for (Materia materia : materias) {
      IO.println(String.format("  - %s (Creditos: %d, Horas/semana: %d)%n",
          materia.getNombre(), materia.getCreditos(), materia.getHorasSemanales()));
    }
  }

  // ==================== CURSOS ====================

  private void menuCursos() {
    boolean inMenu = true;

    while (inMenu) {
      IO.println("\nGESTION DE CURSOS");
      IO.println("1. Listar todos los cursos");
      IO.println("2. Ver detalles de un curso");
      IO.println("3. Ver alumnos de un curso");
      IO.println("0. Volver al menu principal");

      int option = Integer.parseInt(IO.readln("Seleccione una opcion: "));

      switch (option) {
        case 1 -> listarCursos();
        case 2 -> verDetalleCurso();
        case 3 -> verAlumnosCurso();
        case 0 -> inMenu = false;
        default -> IO.println("Opcion no valida.");
      }
    }
  }

  private void listarCursos() {
    List<Curso> cursos = cursoRepository.getAll();

    if (cursos.isEmpty()) {
      IO.println("\nNo hay cursos registrados.");
      return;
    }

    IO.println("\nLISTADO DE CURSOS");

    for (Curso curso : cursos) {
      IO.println("ID: " + curso.getId() +
          " Nombre: " + curso.getNombre() +
          " Creditos: " + curso.getTotalCreditos() +
          " Materias: " + curso.getMaterias().size());
    }
  }

  private void listarCursosSimple() {
    IO.println("\nCursos disponibles:");
    for (Curso curso : cursoRepository.getAll()) {
      IO.println("[" + curso.getId() + "] " + curso.getNombre());
    }
  }

  private void verDetalleCurso() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del curso: "));
    Optional<Curso> cursoOpt = cursoRepository.getById(id);

    if (cursoOpt.isEmpty()) {
      IO.println("Curso no encontrado.");
      return;
    }

    Curso curso = cursoOpt.get();
    long numAlumnos = alumnoRepository.getAll().stream()
        .filter(a -> a.getCursoId().equals(id))
        .count();

    IO.println("DETALLE DEL CURSO");
    IO.println("ID: " + curso.getId());
    IO.println("Nombre: " + curso.getNombre());
    IO.println("Total Creditos: " + curso.getTotalCreditos());
    IO.println("Numero de Materias: " + curso.getMaterias().size());
    IO.println("Numero de Alumnos: " + numAlumnos);

    IO.println("\nMaterias");

    for (Materia materia : curso.getMaterias()) {
      IO.println(materia.getNombre() + " - Creditos: " + materia.getCreditos() +
          ", Horas semanales: " + materia.getHorasSemanales());
    }
  }

  private void verAlumnosCurso() {
    Long cursoId = Long.parseLong(IO.readln("Ingrese el ID del curso: "));

    if (cursoRepository.getById(cursoId).isEmpty()) {
      IO.println("Curso no encontrado.");
      return;
    }

    List<Alumno> alumnos = alumnoRepository.getAll().stream()
        .filter(a -> a.getCursoId().equals(cursoId))
        .toList();

    if (alumnos.isEmpty()) {
      IO.println("No hay alumnos en este curso.");
      return;
    }

    IO.println("\n--- Alumnos del Curso ---");
    for (Alumno alumno : alumnos) {
      IO.println("[" + alumno.getId() + "] " + alumno.getNombre() + " (Edad: " + alumno.getEdad() + ")");
    }
  }

  // ==================== MATERIAS ====================

  private void menuMaterias() {
    boolean inMenu = true;

    while (inMenu) {
      IO.println("\nGESTION DE MATERIAS");
      IO.println("1. Listar todas las materias");
      IO.println("2. Agregar materia a un curso");
      IO.println("3. Eliminar materia de un curso");
      IO.println("0. Volver al menu principal");

      int option = Integer.parseInt(IO.readln("Seleccione una opcion: "));

      switch (option) {
        case 1 -> listarMaterias();
        case 2 -> agregarMateria();
        case 3 -> eliminarMateria();
        case 0 -> inMenu = false;
        default -> IO.println("Opcion no valida.");
      }
    }
  }

  private void listarMaterias() {
    IO.println("LISTADO DE MATERIAS");

    for (Curso curso : cursoRepository.getAll()) {
      for (Materia materia : curso.getMaterias()) {
        IO.println("ID: " + materia.getId() +
            " Nombre: " + materia.getNombre() +
            " Creditos: " + materia.getCreditos() +
            " Horas semanales: " + materia.getHorasSemanales() +
            " Curso: " + curso.getNombre());
      }
    }
  }

  private void agregarMateria() {
    listarCursosSimple();
    Long cursoId = Long.parseLong(IO.readln("ID del curso al que desea agregar la materia: "));

    Optional<Curso> cursoOpt = cursoRepository.getById(cursoId);
    if (cursoOpt.isEmpty()) {
      IO.println("Curso no encontrado.");
      return;
    }

    String nombre = IO.readln("Nombre de la materia: ");
    int creditos = Integer.parseInt(IO.readln("Creditos: "));
    int horas = Integer.parseInt(IO.readln("Horas semanales: "));

    Materia nuevaMateria = new Materia(nextMateriaId++, nombre, creditos, horas);
    cursoOpt.get().getMaterias().add(nuevaMateria);
    cursoRepository.update(cursoOpt.get());

    IO.println("Materia agregada exitosamente con ID: " + nuevaMateria.getId());
  }

  private void eliminarMateria() {
    listarMaterias();
    Long materiaId = Long.parseLong(IO.readln("ID de la materia a eliminar"));

    Optional<Curso> cursoOpt = cursoRepository.getCursoByMateriaId(materiaId);
    if (cursoOpt.isEmpty()) {
      IO.println("Materia no encontrada.");
      return;
    }

    String confirm = IO.readln("Esta seguro? (s/n): ");
    if (confirm.equalsIgnoreCase("s")) {
      cursoRepository.removeMateriaById(cursoOpt.get().getId(), materiaId);
      IO.println("Materia eliminada exitosamente.");
    } else {
      IO.println("Operacion cancelada.");
    }
  }

  // ==================== PROFESORES ====================

  private void menuProfesores() {
    boolean inMenu = true;

    while (inMenu) {
      IO.println("GESTION DE PROFESORES");
      IO.println("1. Listar todos los profesores");
      IO.println("2. Buscar profesor por ID");
      IO.println("3. Agregar nuevo profesor");
      IO.println("4. Actualizar profesor");
      IO.println("5. Eliminar profesor");
      IO.println("6. Ver sueldo semanal de un profesor");
      IO.println("7. Ver nomina completa (todos los sueldos)");
      IO.println("8. Ver profesores de un alumno");
      IO.println("0. Volver al menu principal");

      int option = Integer.parseInt(IO.readln("Seleccione una opcion"));

      switch (option) {
        case 1 -> listarProfesores();
        case 2 -> buscarProfesor();
        case 3 -> agregarProfesor();
        case 4 -> actualizarProfesor();
        case 5 -> eliminarProfesor();
        case 6 -> verSueldoProfesor();
        case 7 -> verNominaCompleta();
        case 8 -> verProfesoresAlumno();
        case 0 -> inMenu = false;
        default -> IO.println("Opcion no valida.");
      }
    }
  }

  private void listarProfesores() {
    List<Profesor> profesores = profesorRepository.getAll();

    if (profesores.isEmpty()) {
      IO.println("\nNo hay profesores registrados.");
      return;
    }

    IO.println("\nLISTADO DE PROFESORES");

    for (Profesor profesor : profesores) {
      String materiaNombre = cursoRepository.getMateriaById(profesor.getMateriaId())
          .map(Materia::getNombre)
          .orElse("Sin asignar");

      IO.println("ID: " + profesor.getId() + " | Nombre: " + profesor.getNombre() +
          " | Sueldo/hora: $" + profesor.getSueldoPorHora() +
          " | Materia: " + materiaNombre);
    }
  }

  private void buscarProfesor() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del profesor: "));
    Optional<Profesor> profesorOpt = profesorRepository.getById(id);

    if (profesorOpt.isEmpty()) {
      IO.println("Profesor no encontrado.");
      return;
    }

    Profesor profesor = profesorOpt.get();
    String materiaNombre = cursoRepository.getMateriaById(profesor.getMateriaId())
        .map(Materia::getNombre)
        .orElse("Sin asignar");
    double sueldoSemanal = profesorRepository.getSueldoSemanal(profesor.getId(), profesor.getMateriaId());

    IO.println("\nDATOS DEL PROFESOR");
    IO.println("ID: " + profesor.getId());
    IO.println("Nombre: " + profesor.getNombre());
    IO.println("Sueldo por hora: $" + profesor.getSueldoPorHora());
    IO.println("Materia: " + materiaNombre);
    IO.println("Sueldo semanal: $" + sueldoSemanal);
    IO.println("Sueldo mensual: $" + (sueldoSemanal * 4));
  }

  private void agregarProfesor() {
    IO.println("\n--- Agregar Nuevo Profesor ---");
    String nombre = IO.readln("Nombre: ");
    double sueldoPorHora = Double.parseDouble(IO.readln("Sueldo por hora: $"));

    listarMateriasSimple();
    Long materiaId = Long.parseLong(IO.readln("ID de la materia que imparte: "));

    if (cursoRepository.getMateriaById(materiaId).isEmpty()) {
      IO.println("Materia no encontrada. Operacion cancelada.");
      return;
    }

    Profesor nuevoProfesor = new Profesor(nextProfesorId++, nombre, sueldoPorHora, materiaId);
    profesorRepository.save(nuevoProfesor);
    IO.println("Profesor agregado exitosamente con ID: " + nuevoProfesor.getId());
  }

  private void actualizarProfesor() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del profesor a actualizar: "));
    Optional<Profesor> profesorOpt = profesorRepository.getById(id);

    if (profesorOpt.isEmpty()) {
      IO.println("Profesor no encontrado.");
      return;
    }

    Profesor profesor = profesorOpt.get();
    IO.println("Datos actuales - Nombre: " + profesor.getNombre() +
        ", Sueldo/hora: $" + profesor.getSueldoPorHora());

    String nombre = IO.readln("Nuevo nombre (Enter para mantener): ");
    if (!nombre.isEmpty()) {
      profesor.setNombre(nombre);
    }

    String sueldoStr = IO.readln("Nuevo sueldo por hora (Enter para mantener): $");
    if (!sueldoStr.isEmpty()) {
      profesor.setSueldoPorHora(Double.parseDouble(sueldoStr));
    }

    profesorRepository.update(profesor);
    IO.println("Profesor actualizado exitosamente.");
  }

  private void eliminarProfesor() {
    Long id = Long.parseLong(IO.readln("Ingrese el ID del profesor a eliminar: "));

    if (profesorRepository.getById(id).isEmpty()) {
      IO.println("Profesor no encontrado.");
      return;
    }

    String confirm = IO.readln("Esta seguro? (s/n): ");
    if (confirm.equalsIgnoreCase("s")) {
      profesorRepository.deleteById(id);
      IO.println("Profesor eliminado exitosamente.");
    } else {
      IO.println("Operacion cancelada.");
    }
  }

  private void verSueldoProfesor() {
    Long profesorId = Long.parseLong(IO.readln("Ingrese el ID del profesor: "));
    Optional<Profesor> profesorOpt = profesorRepository.getById(profesorId);

    if (profesorOpt.isEmpty()) {
      IO.println("Profesor no encontrado.");
      return;
    }

    Profesor profesor = profesorOpt.get();
    Optional<Materia> materiaOpt = cursoRepository.getMateriaById(profesor.getMateriaId());

    if (materiaOpt.isEmpty()) {
      IO.println("El profesor no tiene materia asignada.");
      return;
    }

    Materia materia = materiaOpt.get();
    double sueldoSemanal = profesorRepository.getSueldoSemanal(profesorId, profesor.getMateriaId());

    IO.println("\nCALCULO DEL SUELDO");
    IO.println("Profesor: " + profesor.getNombre());
    IO.println("Materia: " + materia.getNombre());
    IO.println("Horas semanales: " + materia.getHorasSemanales());
    IO.println("Sueldo por hora: $" + profesor.getSueldoPorHora());
    IO.println("Sueldo semanal: $" + sueldoSemanal);
    IO.println("Sueldo mensual: $" + (sueldoSemanal * 4));
    IO.println("Sueldo anual: $" + (sueldoSemanal * 52));
  }

  private void verNominaCompleta() {
    List<Profesor> profesores = profesorRepository.getAll();

    if (profesores.isEmpty()) {
      IO.println("\nNo hay profesores registrados.");
      return;
    }

    double totalSemanal = 0;

    IO.println("\nNOMINA COMPLETA");

    for (Profesor profesor : profesores) {
      Optional<Materia> materiaOpt = cursoRepository.getMateriaById(profesor.getMateriaId());
      String materiaNombre = materiaOpt.map(Materia::getNombre).orElse("Sin asignar");
      int horas = materiaOpt.map(Materia::getHorasSemanales).orElse(0);
      double sueldoSemanal = profesorRepository.getSueldoSemanal(profesor.getId(), profesor.getMateriaId());
      totalSemanal += sueldoSemanal;

      IO.println("Profesor: " + profesor.getNombre() + ", Materia: " + materiaNombre +
          ", Horas/Semana: " + horas + ", $/Hora: $" + profesor.getSueldoPorHora() +
          ", Sueldo/Semana: $" + sueldoSemanal);
    }

    IO.println("TOTAL SEMANAL: $" + totalSemanal);
    IO.println("TOTAL MENSUAL: $" + (totalSemanal * 4));
    IO.println("TOTAL ANUAL: $" + (totalSemanal * 52));
  }

  private void verProfesoresAlumno() {
    Long alumnoId = Long.parseLong(IO.readln("Ingrese el ID del alumno"));
    Optional<Alumno> alumnoOpt = alumnoRepository.getById(alumnoId);

    if (alumnoOpt.isEmpty()) {
      IO.println("Alumno no encontrado.");
      return;
    }

    List<Profesor> profesores = profesorRepository.getByAlumnoId(alumnoId);

    if (profesores.isEmpty()) {
      IO.println("No se encontraron profesores para este alumno.");
      return;
    }

    IO.println("\n--- Profesores del Alumno: " + alumnoOpt.get().getNombre() + " ---");
    for (Profesor profesor : profesores) {
      String materiaNombre = cursoRepository.getMateriaById(profesor.getMateriaId())
          .map(Materia::getNombre)
          .orElse("Sin asignar");
      IO.println("Profesor: " + profesor.getNombre() + ", Materia: " + materiaNombre +
          ", Sueldo por hora: $" + profesor.getSueldoPorHora());
    }
  }

  private void listarMateriasSimple() {
    IO.println("\nMaterias disponibles:");
    for (Curso curso : cursoRepository.getAll()) {
      for (Materia materia : curso.getMaterias()) {
        IO.println("[" + materia.getId() + "] " + materia.getNombre() + " (" + curso.getNombre() + ")");
      }
    }
  }
}
