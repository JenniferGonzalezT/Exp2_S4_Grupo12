/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.proyecto_biblioteca;

import com.proyecto_biblioteca.procesos.Biblioteca;
import com.proyecto_biblioteca.objetos.*;
import com.proyecto_biblioteca.excepciones.*;
import java.util.Scanner;

/**
 * Proyecto de administración de una biblioteca
 * @author jennifer y guiselle
 */

public class ProyectoBiblioteca {
    static Biblioteca biblioteca = new Biblioteca();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("¡BIENVENID@ A LA BIBLIOTECA!");

        int opcionMenu;
        do {
            opcionMenu = mostrarMenu(scanner);
            switch(opcionMenu) {
                case 0:
                    System.out.println("\n\n==================== SALIR =====================");
                    System.out.println("Saliendo...\n¡Muchas gracias por su visita!");
                    break;
                case 1:
                    prestamo(scanner);
                    break;
                case 2:
                    devolucion(scanner);
                    break;
                case 3:
                    verLibros();
                    break;
                case 4:
                    buscarLibro(scanner);
                    break;
                case 5:
                    agregarLibros(scanner);
                    break;
                case 6:
                    agregarUsuario(scanner);
                    break;
                case 7:
                    buscarUsuario(scanner);
                    break;
            }
        } while (opcionMenu != 0);
        scanner.close();
    }


    static int mostrarMenu(Scanner scanner) {
        System.out.println("\n\n===================== MENÚ =====================");
        System.out.println("(1) Préstamos");
        System.out.println("(2) Devoluciones");
        System.out.println("(3) Ver libros");
        System.out.println("(4) Buscar libro");
        System.out.println("(5) Agregar libro");
        System.out.println("(6) Agregar usuario");
        System.out.println("(7) Buscar usuario");
        System.out.println("(0) Salir");

        final int OPCION_MIN = 0;
        final int OPCION_MAX = 7;
        int opcion = 0;
        boolean opcionValida = false;
        String mensajeOpciones = "\nRecuerde ingresar un número de " + OPCION_MIN + " a " + OPCION_MAX;

        while (!opcionValida) {
            System.out.print("\nIngrese el número correspondiente a su opción: ");
            try {
                opcion = scanner.nextInt();
                if (opcion >= OPCION_MIN && opcion <= OPCION_MAX) {
                    opcionValida = true;
                } else {
                    System.out.println("El número ingresado no es válido." + mensajeOpciones);
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("La entrada no es válida." + mensajeOpciones);
            } finally {
                scanner.nextLine();
            }
        }

        return opcion;
    }


    static void prestamo(Scanner scanner){
        System.out.println("\n\n=================== PRÉSTAMOS ==================");
        if (biblioteca.bibliotecaVacia()) {
            System.out.println("Lo sentimos. Aún no hay libros en la biblioteca.");
            System.out.println("Ahora regresará al menú.");
            return;
        }

        System.out.print("Ingrese el título del libro que necesita: ");
        String titulo = scanner.nextLine().trim().toUpperCase();
        String rut = "";
        try {
            if (biblioteca.buscarLibro(titulo).isDisponible()){
                rut = procesarRut(scanner);
                if (biblioteca.buscarUsuario(rut) == null) {
                    System.out.println("El usuario no se encuentra registrado.");
                    System.out.println("Ahora irá a registrarse para continuar con la operación.");
                    agregarUsuario(scanner, rut);
                }
            }
            biblioteca.prestarLibro(titulo, rut);
        } catch (LibroNoEncontradoException | LibroYaPrestadoException e) {
            System.out.println(e.getMessage());
            System.out.println("Ahora regresará al menú.");
        }
    }


    static void prestamo(Scanner scanner, String titulo) {
        System.out.println("\n\n=================== PRÉSTAMOS ==================");

        String rut = procesarRut(scanner);
        if (biblioteca.buscarUsuario(rut) == null) {
            System.out.println("El usuario no se encuentra registrado.");
            System.out.println("Ahora irá a registrarse para continuar con la operación.");
            agregarUsuario(scanner, rut);
        }

        try{
            biblioteca.prestarLibro(titulo, rut);
        } catch (LibroNoEncontradoException | LibroYaPrestadoException e) {
            System.out.println(e.getMessage());
            System.out.println("Ahora regresará al menú.");
        }
    }


    static void devolucion(Scanner scanner) {
        System.out.println("\n\n================== DEVOLUCIONES ================");
        
        String rut = procesarRut(scanner);
        if (biblioteca.buscarUsuario(rut) == null) {
            System.out.println("El usuario no se encuentra registrado. Por lo tanto no tiene libros que devolver.\nAhora regresará al menú.");
            return;
        }

        if (biblioteca.prestamosPorUsuario(biblioteca.buscarUsuario(rut))) {
            System.out.print("\nIngrese el título del libro que desea devolver: ");
            String titulo = scanner.nextLine().trim().toUpperCase();
            biblioteca.devolverLibro(titulo, rut);
        }

    }


    static void verLibros() {
        System.out.println("\n\n================== VER LIBROS ==================");

        if (biblioteca.bibliotecaVacia()) {
            System.out.println("Lo sentimos. Aún no hay libros en la biblioteca.\nAhora regresará al menú.");
        } else {
            biblioteca.mostrarLibros();
        }
    }


    static void buscarLibro(Scanner scanner) {
        System.out.println("\n\n================= BUSCAR LIBRO =================");

        if (biblioteca.bibliotecaVacia()) {
            System.out.println("Lo sentimos. Aún no hay libros en la biblioteca.\nAhora regresará al menú.");
            return;
        }

        System.out.print("Ingrese el titulo del libro que busca: ");
        String titulo = scanner.nextLine().trim().toUpperCase();
        Libro libro;
        try{
            libro = biblioteca.buscarLibro(titulo);
        } catch (LibroNoEncontradoException e) {
            System.out.println(e.getMessage());
            System.out.println("Ahora regresará al menú.");
            return;
        }

        if (libro != null) {
            System.out.println("Libro encontrado -> " + libro.toString());

            if (libro.isDisponible()) {
                boolean opcionValida = false;
                int opcion = 0;
                do {
                    System.out.println("\n¿Desea pedirlo prestado?");
                    System.out.println("(1) Si");
                    System.out.println("(2) No");
                    System.out.print("Ingrese el número correspondiente a su opción: ");
                    try {
                        String entrada = scanner.nextLine().trim();
                        opcion = Integer.parseInt(entrada);
                        switch (opcion) {
                            case 1:
                                System.out.println("Ahora irá a Préstamos para continuar con la operación.");
                                prestamo(scanner, titulo);
                                return;
                            case 2:
                                opcionValida = true;
                                break;
                            default:
                                System.out.println("El número ingresado no es válido. Intente nuevamente.");
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("El valor ingresado no es válido. Intente nuevamente.");
                    }
                } while (!opcionValida);
            }
        }
        System.out.println("Ahora regresará al menú.");
    }


    static void agregarLibros(Scanner scanner) {
        System.out.println("\n\n================ AGREGAR LIBROS ================");
        boolean opcionAgregar = false;
        do {            
            System.out.println("\nIngrese los siguientes datos del libro que desea agregar:");
            System.out.print("-> Título: ");
            String titulo = scanner.nextLine().trim().toUpperCase();
            System.out.print("-> Autor/a: ");
            String autor = scanner.nextLine().trim().toUpperCase();
            
            if (biblioteca.libroExiste(titulo)) {
                return;
            }

            biblioteca.agregarLibro(new Libro(titulo, autor));
            System.out.println("¡Libro agregado con éxito a la biblioteca!");

            boolean opcionValida = false;
            do {
                System.out.println("\n¿Desea agregar otro libro?");
                System.out.println("(1) Si");
                System.out.println("(2) No");
                System.out.print("Ingrese el número correspondiente a su opción: ");
                String entrada = scanner.nextLine().trim();
                try {
                    int opcion = Integer.parseInt(entrada);
                    switch (opcion) {
                        case 1:
                            opcionValida = true;
                            break;
                        case 2:
                            opcionValida = true;
                            opcionAgregar = true;
                            System.out.println("Muy bien. Ahora regresará al menú");
                            break;
                        default:
                            System.out.println("El número ingresado no es válido. Intente nuevamente.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("La opción ingresada no es válida. Intente nuevamente.");
                }
            } while (!opcionValida);
        } while (!opcionAgregar);
    }


    static void agregarUsuario(Scanner scanner) {
        System.out.println("\n\n================ AGREGAR USUARIO ===============");
        System.out.println("Para registrarse debera ingresar los siguientes datos:");

        System.out.print("-> Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("-> Apellido: ");
        String apellido = scanner.nextLine().trim();

        String rut = procesarRut(scanner);
        if (biblioteca.buscarUsuario(rut) != null) {
            System.out.println("El usuario ya se encuentra registrado.\nAhora regresará al menú.");
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, rut);
        biblioteca.registrarUsuario(usuario);
        System.out.println("\n" + usuario.toString() + " ¡Registrado con éxito!");
    }


    static void agregarUsuario(Scanner scanner, String rut) {
        System.out.println("\n\n================ AGREGAR USUARIO ===============");
        System.out.println("Para registrarse debera ingresar los siguientes datos:");

        System.out.print("-> Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("-> Apellido: ");
        String apellido = scanner.nextLine().trim();

        Usuario usuario = new Usuario(nombre, apellido, rut);
        biblioteca.registrarUsuario(usuario);
        System.out.println(usuario.toString() + " ¡Registrado con éxito!");
    }


    static void buscarUsuario(Scanner scanner) {
        System.out.println("\n\n================ BUSCAR USUARIO ================");

        if (biblioteca.usuariosVacia()) {
            System.out.println("Aún no hay usuarios registrados.\nAhora regresará al menú.");
            return;
        }

        String rut = procesarRut(scanner);
        if (biblioteca.buscarUsuario(rut) == null) {
            System.out.println("El usuario no se encuentra registrado.\nAhora regresará al menú.");
            return;
        }

        Usuario usuario = biblioteca.buscarUsuario(rut);
        System.out.println("Usuario encontrado -> " + usuario.toString());
        biblioteca.prestamosPorUsuario(usuario);
    }


    static String procesarRut(Scanner scanner) {
        boolean rutValido = false;
        while (!rutValido) {
            System.out.print("\nIngrese su Rut (ej: 12.345.678-9): ");
            String rut = scanner.nextLine().trim();
            if (rut.matches("^\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK]$")) {
                return rut;
            } else {
                System.out.println("El rut ingresado no es correcto.");
                System.out.println("Recuerde ingresar su rut con puntos, guion y dígito verificador.");
            }
        }
        return null;
    }

}
