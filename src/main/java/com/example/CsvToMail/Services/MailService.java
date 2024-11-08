package com.example.CsvToMail.Services;

import com.example.CsvToMail.DTO.Mail;
import com.example.CsvToMail.Model.UserEmailDetails;
import com.example.CsvToMail.Utils.TextLibraries.MessageType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    @Value("${app.params.attribute_identifier}")
    private String attrSep;

    private String getHost(String infoHost) {
        String filtered = infoHost.toLowerCase();

        String host = "";
        switch (filtered) {
            case "gmail" -> host = "smtp.gmail.com";
            case "outlook" -> host = "smtp.office365.com";
            case "yahoo" -> host = "smtp.mail.yahoo.com";
            default -> host = "Not valid";
        }
        return host;
    }

    private JavaMailSender createUser(UserEmailDetails userDetails) {
        JavaMailSenderImpl token = new JavaMailSenderImpl();
        token.setUsername(userDetails.getEmail());
        token.setPassword(userDetails.getSecurePassword());
        token.setHost(getHost(userDetails.getMailType()));

        Properties prop = token.getJavaMailProperties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.debug", "true");

        return token;
    }

    public void sendEmail(Mail mensaje) throws MessagingException {
        JavaMailSender headers = createUser(mensaje.getHeaders());
        boolean isHTML =
                mensaje.getHeaders().getMailType().equalsIgnoreCase(MessageType.HTML) ||
                !mensaje.getDetails().getImages().isEmpty();
        MimeMessage message = headers.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, isHTML);

        // Para la funcionalidad del mensaje por HTML.
        if (!isHTML) {
            helper.setText(mensaje.getDetails().getBody());
        }

        helper.setTo(mensaje.getDetails().getSendTo());
        helper.setSubject(mensaje.getDetails().getSubject());
        headers.send(message);
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
            System.out.println(param);
            body = body.replaceAll(param, information[1][col]);
            col++;
        }
        return body;
    }
}
