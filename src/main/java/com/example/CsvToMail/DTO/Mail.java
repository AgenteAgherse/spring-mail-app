package com.example.CsvToMail.DTO;

import com.example.CsvToMail.Model.MailDetails;
import com.example.CsvToMail.Model.UserEmailDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {
    private UserEmailDetails headers;
    private MailDetails details;
}
