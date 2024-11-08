package com.example.CsvToMail.Controller;

import com.example.CsvToMail.DTO.Mail;
import com.example.CsvToMail.Model.MailDetails;
import com.example.CsvToMail.Model.UserEmailDetails;
import com.example.CsvToMail.Services.MailService;
import com.example.CsvToMail.Utils.TextLibraries.MessageType;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/mail")
public class EmailController {

    @Autowired private MailService mailUtils;

    @GetMapping(path = "/mock_body")
    public ResponseEntity<String> sendMockBody(@RequestBody Mail mail) {
        StringBuilder filteredBody = new StringBuilder(mail.getDetails().getBody() + "\n\n\n");
        String[][] informationMatrix = mail.getDetails().getDynamicInformation();

        // Verificación de dimensiones.
        if (informationMatrix.length == 0) return ResponseEntity.status(400).body("No se encuentran los correos.");
        if (informationMatrix[0].length == 0) return ResponseEntity.status(400).body("No hay columnas.");

        int cols = informationMatrix[0].length;
        String[][] userInformation = new String[2][cols];
        userInformation[0] = informationMatrix[0]; // Se asignan los headers

        int index = 0;

        for (String[] row: informationMatrix) {
            if (index != 0) {
                userInformation[1] = row;
                filteredBody.append(mailUtils.bodyFiltered(userInformation, mail.getDetails().getBody())).append("\n\n\n");
            }
            index++;
        }


        return ResponseEntity.status(200).body(filteredBody.toString());
    }
    @GetMapping(path = "/mock_email_template")
    public ResponseEntity<Mail> getMailBody() {
        Mail newMail = new Mail();
        // Detalles Mock
        newMail.setHeaders(new UserEmailDetails());
        newMail.setDetails(new MailDetails());
        newMail.getHeaders().setEmail("example@email.com");
        newMail.getHeaders().setSecurePassword("api_password");
        newMail.getHeaders().setMailType(MessageType.TEXT);

        newMail.getDetails().setSubject("Mock Subject");
        newMail.getDetails().setBody("Body of the mocking mail.");
        String[] sendTo = {"sendTo@gmail.com"};
        newMail.getDetails().setSendTo(sendTo);
        List<byte[]> images = new ArrayList<>();
        byte[] mockImage = {0,1,0,1,1};
        images.add(mockImage);
        newMail.getDetails().setImages(images);


        return ResponseEntity.status(200).body(newMail);
    }
    @PutMapping(path = "/send")
    public ResponseEntity<String> sendEmail(@RequestBody Mail information) {
        try {
            mailUtils.sendEmail(information);
        } catch (MessagingException me) {
            return ResponseEntity
                    .status(400)
                    .body("Error sending the email.");
        }

        return ResponseEntity
                .status(201)
                .body("Email sent.");
    }


}
