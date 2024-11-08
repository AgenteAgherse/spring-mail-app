package com.example.CsvToMail.Services;

import com.example.CsvToMail.Utils.TextLibraries.CsvUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CsvService {

    public String[][] csvToMatrix(MultipartFile file, String sep) {
        try {
            return CsvUtils.csvInformation(file, sep);
        } catch (IOException io) {
            return null;
        }
    }

}
