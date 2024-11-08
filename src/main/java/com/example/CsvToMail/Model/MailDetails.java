package com.example.CsvToMail.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MailDetails {
    private String[] sendTo; // Listado de personas
    private String body; // Tomamos en cuenta el cuerpo del mensaje
    private String subject; // Asunto del mensaje
    private List<byte[]> images; // Imagenes del programa
    private String[][] dynamicInformation; // Información o dataset de las personas.

}
