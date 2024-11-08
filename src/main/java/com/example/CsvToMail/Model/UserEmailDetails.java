package com.example.CsvToMail.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEmailDetails {
    private String email;
    private String securePassword;
    private String mailService;
    private String mailType;
    private int emailColumnIndex; // Toma en cuenta un índice donde va a estar ubicada la columna de los emails.
}
