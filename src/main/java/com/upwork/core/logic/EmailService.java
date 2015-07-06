package com.upwork.core.logic;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.text.html.HTMLDocument;
import java.util.Properties;

/**
 * Created by jsarafajr on 06.07.15.
 */
@Component


@Service
public class EmailService {

    private Session mailSession;

    private Transport mailTransport;

    public void sendHTML(String subject, String htmlMessage, String ... emails) {
        setProperties();
        MimeMessage message = new MimeMessage(mailSession);

        try {
            for (String email : emails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            message.setSubject(subject);
            message.setContent(htmlMessage, "text/html");

            mailTransport.sendMessage(message, message.getAllRecipients());
            mailTransport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendText(String subject, String textMessage, String ... emails)
    {
        setProperties();
        MimeMessage message = new MimeMessage(mailSession);

        try {
            for (String email : emails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            message.setSubject(subject);
            message.setText(textMessage);

            mailTransport.sendMessage(message, message.getAllRecipients());
            mailTransport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void setProperties() {
        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

        mailSession = Session.getDefaultInstance(emailProperties, null);
        /**
         * Sender's credentials
         * */
        String user = "kpishare.noreply@gmail.com";
        String password = "kpi-share";

        String host = "smtp.gmail.com";
        try {
            mailTransport = mailSession.getTransport("smtp");
            mailTransport.connect(host, user, password);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}