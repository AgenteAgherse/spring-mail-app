package com.example.CsvToMail.Utils.TextLibraries;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// JUST STATIC METHODS.
public class CsvUtils {

    /**
     * Se toma en cuenta el archivo que se manda por la API y se separa la
     * información en el separador que se tenga en cuenta en el momento
     * @param file archivo
     * @param separator separador de la línea del csv.
     * @return las lineas de csv separadas
     * @throws IOException
     */
    public static String[][] csvInformation(MultipartFile file, String separator) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        BufferedReader aux = new BufferedReader(new InputStreamReader(file.getInputStream()));

        // Dimensiones de la matriz
        int csvRows = (int) aux.lines().count();
        if (csvRows <= 0) return null;

        String actualLine = reader.readLine();
        System.out.println(separator);
        String[] header = actualLine.split(separator);

        // Se genera una matriz para optimización de tiempo
        String[][] information = new String[csvRows][header.length];
        information[0] = header;


        int row = 1;
        // Lectura de las demás filas.
        while ((actualLine = reader.readLine()) != null) {
            String[] line = actualLine.split(separator);
            information[row] = line;
            row++;
        }
        return information;
    }
}
