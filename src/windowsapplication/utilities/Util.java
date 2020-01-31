package windowsapplication.utilities;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains the different methods that we are going to use in our
 * project
 *
 * @author Adrian
 */
public class Util {

    /**
     * This method inputs something from the keyboard, assigns it on a String
     *
     * @return A String with the input from the keyboard
     */
    public static String introducirCadena() {
        String cadena = "";
        InputStreamReader entrada = new InputStreamReader(System.in);
        BufferedReader teclado = new BufferedReader(entrada);
        try {
            do {
                cadena = teclado.readLine();
                if (cadena.length() == 0) {
                    System.out.println("Error, cadena vacia.");
                }
            } while (cadena.length() == 0);
        } catch (IOException e) {
            System.out.println("Error al introducir datos");
        }
        return cadena;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a String
     * showing a message before the input and the return has a max length
     *
     * @param mensaje A String that contains a message before the input
     * @param max An int that contains the max length
     * @return A String with the input from the keyboard
     */
    public static String introducirCadena(String mensaje, int max) {
        String cadena = "";
        InputStreamReader entrada = new InputStreamReader(System.in);
        BufferedReader teclado = new BufferedReader(entrada);
        System.out.println(mensaje);
        System.out.println("Tama�o m�ximo " + max + ".");
        try {
            do {
                cadena = teclado.readLine();
                if (cadena.length() > max) {
                    System.out.println("Error, cadena vacia:");
                }
            } while (cadena.length() <= max);
        } catch (IOException e) {
            System.out.println("Error al introducir datos");
        }
        return cadena;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a String
     * showing a message before the input
     *
     * @param mensaje A String that contains a message before the input
     * @return A String with the input from the keyboard
     */
    public static String introducirCadena(String mensaje) {
        String cadena = "";
        InputStreamReader entrada = new InputStreamReader(System.in);
        BufferedReader teclado = new BufferedReader(entrada);
        System.out.println(mensaje);
        try {
            do {
                cadena = teclado.readLine();
                if (cadena.length() == 0) {
                    System.out.println("La cadena debe contener algo:");
                }
            } while (cadena.length() == 0);
        } catch (IOException e) {
            System.out.println("Error al introducir datos");
        }
        return cadena;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a int
     * showing a message before the input
     *
     * @param mensaje A String that contains a message before the input
     * @return A int with the input from keyboard
     */
    public static int leerInt(String mensaje) {
        int numero = 0;
        boolean error;
        System.out.println(mensaje);
        do {
            error = false;
            try {
                numero = Integer.parseInt(introducirCadena());
            } catch (NumberFormatException e) {
                System.out.println("Error, el dato no es num�rico. Introduce de nuevo: ");
                error = true;
            }
        } while (error);
        return numero;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a int
     * showing a message before the input. With a min and a max number that he
     * can introduce
     *
     * @param mensaje A String that contains a message before the input
     * @param min A int with the smallest number possible
     * @param max A int with the biggest number possible
     * @return A int with the input from the keyboard with the correct
     * parameters
     */
    public static int leerInt(String mensaje, int min, int max) {
        int numero = 0;
        boolean error;
        System.out.println(mensaje);
        do {
            error = false;
            try {
                numero = Integer.parseInt(introducirCadena());
            } catch (NumberFormatException e) {
                System.out.println("Error, el dato no es num�rico. Introduce de nuevo: ");
                error = true;
            }
            if (numero < min || numero > max) {
                System.out.println("Error, dato fuera de rango. Introduce de nuevo: ");
                error = true;
            }
        } while (error);
        return numero;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a float
     * showing a message before the input.
     *
     * @param mensaje A String that contains a message before the input
     * @return A float with the input from the keyboard
     */
    public static float leerFloat(String mensaje) {
        float numero = 0;
        boolean error;
        System.out.println(mensaje);
        do {
            error = false;
            try {
                numero = Float.parseFloat(introducirCadena());
            } catch (NumberFormatException e) {
                System.out.println("Error, el dato no es num�rico. Introduce de nuevo: ");
                error = true;
            }
        } while (error);
        return numero;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a float
     * showing a message before the input.
     *
     * @param mensaje A String that contains a message before the input
     * @param min A float with the smallest number possible
     * @param max A float with the biggest number possible
     * @return A float with the input from the keyboard with the correct
     * parameters
     */
    public static float leerFloat(String mensaje, float min, float max) {
        float numero = 0;
        boolean error;
        System.out.println(mensaje);
        do {
            error = false;
            try {
                numero = Float.parseFloat(introducirCadena());
            } catch (NumberFormatException e) {
                System.out.println("Error, el dato no es num�rico. Introduce de nuevo: ");
                error = true;
            }
            if (numero < min || numero > max) {
                System.out.println("Error, dato fuera de rango. Introduce de nuevo: ");
                error = true;
            }
        } while (error);
        return numero;
    }

    /**
     * This method inputs something from the keyboard, assigns it on a float
     * showing a message before the input.
     *
     * @param mensaje A String that contains a message before the input
     * @return A LocalDate with the input from the keyboard
     */
    public static LocalDate leerFecha(String mensaje) {
        String fechaAux;
        LocalDate fechaNac = LocalDate.now();
        boolean error;
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        do {
            error = false;
            System.out.println(mensaje);
            fechaAux = Util.introducirCadena();
            try {
                fechaNac = LocalDate.parse(fechaAux, formateador);
            } catch (DateTimeParseException e) {
                error = true;
                System.out.println("Error,Introduce fecha con formato dd/mm/aaaa: ");
            }
        } while (error);
        return fechaNac;
    }

    //Devuelve la cantidad de objetos de un fichero
    /**
     * This method returns from a file how many objects have.
     *
     * @param fich A File that contains some objects
     * @return A int with the number of objects the File contains
     */
    public static int calculoFichero(File fich) {
        int cont = 0;
        if (fich.exists()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;

            try {
                fis = new FileInputStream(fich);
                ois = new ObjectInputStream(fis);

                Object aux = ois.readObject();

                while (aux != null) {
                    cont++;
                    aux = ois.readObject();
                }

            } catch (EOFException e1) {
                System.out.println("Has acabado de leer, tienes " + cont + " objetos.");

            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                ois.close();
                fis.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar los flujos.");
            }
        }
        return cont;
    }

    /**
     * This method validate if one email got the correct format
     *
     * @param email A String with the email
     * @return A boolean that confirms if the email is correct
     */
    public static boolean validarEmail(String email) {

        //String regex = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+){1,40}\\@[a-zA-Z0-9\\-]{0,30}\\.[a-zA-Z]{2,4}$";
        String regex = "^[a-zA-Z0-9\\.\\-\\_]{1,20}\\@[a-zA-Z0-9\\-]{1,20}\\.[a-zA-Z]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * This method checks if a DNI is correct using the security character
     *
     * @param dni A String that contains all the DNI
     * @return A boolean that confirms if the DNI is correct
     */
    public static boolean validarDni(String dni) {
        char l23[] = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H',
            'L', 'C', 'K', 'E'};
        boolean ok = true;
        String sDni, sAux;
        char aDni[] = new char[8];

        int iNumDni = 0;
        sDni = introducirCadena(dni);
        sDni = sDni.toUpperCase();
        if (sDni.length() < 9 | sDni.length() > 9) {
            ok = false;
        }
        if (sDni.charAt(0) != 'X' & sDni.charAt(0) != 'Y' & sDni.charAt(0) != 'Z' & !(sDni.charAt(8) > 'A' & sDni.charAt(8) < 'Z')) {
            ok = false;
        }

        for (int i = 0; i < (aDni.length); i++) {
            aDni[i] = sDni.charAt(i);
        }
        if (aDni[0] == 'X') {
            aDni[0] = '0';
        } else if (aDni[0] == 'Y') {
            aDni[0] = '1';
        } else if (aDni[0] == 'Z') {
            aDni[0] = '2';
        }
        sAux = String.copyValueOf(aDni);
        try {

            iNumDni = Integer.parseInt(sAux);
        } catch (NumberFormatException e) {

            ok = false;
        }

        if (sDni.charAt(8) != l23[(iNumDni % 23)]) {
            ok = false;
        }
        return ok;
    }

}
