package com.example.CsvToMail.Controller;

import com.example.CsvToMail.Services.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/csv")
public class CsvController {

    @Autowired private CsvService csvService;
    @PostMapping(path = "/convertCsv/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String[][]> csvToMatrix(@RequestParam(value = "file") MultipartFile file, @RequestParam(value = "separator") String separator) {
        if (file == null || separator == null)
            return ResponseEntity.status(400).body(null);

        String[][] information = csvService.csvToMatrix(file, separator);
        return (information == null)?
            ResponseEntity.status(400).body(null) :
            ResponseEntity.status(200).body(information);
    }
}
