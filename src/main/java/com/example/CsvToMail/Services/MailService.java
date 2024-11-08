package com.example.CsvToMail.Services;

import com.example.CsvToMail.DTO.Mail;
import com.example.CsvToMail.Model.UserEmailDetails;
import com.example.CsvToMail.Utils.TextLibraries.CsvUtils;
import com.example.CsvToMail.Utils.TextLibraries.MessageType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Properties;

@Service
public class MailService {

    @Value("${app.params.attribute_identifier}")
    private String attrSep;

    private String getHost(String infoHost) {
        String filtered = infoHost.toLowerCase();

        String host = "";
        switch (filtered) {
            case MessageType.GMAIL -> host = "smtp.gmail.com";
            case MessageType.OUTLOOK_HOTMAIL -> host = "smtp.office365.com";
            case MessageType.YAHOO -> host = "smtp.mail.yahoo.com";
            default -> host = "Not valid";
        }
        return host;
    }

    private JavaMailSender createUser(UserEmailDetails userDetails) {
        JavaMailSenderImpl token = new JavaMailSenderImpl();
        token.setUsername(userDetails.getEmail());
        token.setPassword(userDetails.getSecurePassword());
        Properties properties = token.getJavaMailProperties();
        properties.put("mail.smtp.host", getHost(userDetails.getMailService()));
        properties.put("mail.smtp.port", "587"); // Asegúrate de usar el puerto adecuado
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.localhost", "localhost"); // Usa un nombre sin caracteres especiales, como "localhost" o "myhostname"

        return token;
    }

    public void sendEmail(Mail mensaje) throws MessagingException {
        boolean isHTML =
                mensaje.getHeaders().getMailType().equalsIgnoreCase(MessageType.HTML);

        // Si el correo es en texto
        if (!isHTML) {
            System.out.println("Pasa por mensajes de texto");
            textMail(mensaje);
        }
    }

    private void textMail(Mail mail) throws MessagingException {
        // Cuerpo del correo
        String bodyTemplate = mail.getDetails().getBody();
        String[][] informationMatrix = mail.getDetails().getDynamicInformation();
        if (!CsvUtils.validarMatriz(informationMatrix)) return;

        int emailCol = mail.getHeaders().getEmailColumnIndex();
        int cols = informationMatrix[0].length, rows = informationMatrix.length;

        // Dataset ahora limitado a 1 persona
        String[][] userInformation = new String[2][cols];
        userInformation[0] = informationMatrix[0]; // Se asignan los headers
        // Correos individuales
        if (!mail.getDetails().isSendToGroup()) {
            for (int row = 1; row < rows; row++) {
                JavaMailSender headers = createUser(mail.getHeaders());
                MimeMessage message = headers.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                userInformation[1] = informationMatrix[row];
                String newBody = bodyFiltered(userInformation, bodyTemplate); // Se obtiene el cuerpo filtrado


                helper.setFrom(mail.getHeaders().getEmail());
                helper.setTo(userInformation[1][emailCol]);
                helper.setSubject(mail.getDetails().getSubject());
                helper.setText(newBody);

                headers.send(message);
            }
        }
        // Casos grupales (Realizar el sábado el requerimiento).
    }

    /**
     * Toma en cuenta el cuerpo del correo junto con los parámetros sin reemplazarlos. De ahí,
     * se toma en cuenta la matriz (2xn) y se reemplaza la información. Información de las dimensiones:
     *  * 2 viene siendo la cantidad de filas. La primera fila es el header y la segunda la información de la persona.
     *  * n viene siendo la cantidad dinámica de columnas del csv.
      * @param information
     * @param body
     * @return body
     */
    public String bodyFiltered(String[][] information, String body) {
        int col = 0;
        for (String header: information[0]) {
            String param = attrSep + header + attrSep;
            body = body.replaceAll(param, information[1][col]);
            col++;
        }
        return body;
    }
}
