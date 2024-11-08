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
    private String mailType;
}
