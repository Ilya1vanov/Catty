package com.ilya.ivanov.catty_catalog.model.mail;

//import com.ilya.ivanov.catty_catalog.model.Model;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailDriver {
    private static final String LOGIN = "ivan4off@gmail.com";
    private static final String PASSWORD = "tap0f8a9qd";
    private static final String TO = "dj.ivan.off@mail.ru";

    private static final String REPORT_FORMAT = "User %s added %d files.";

    public static void sendReport(List<File> list) {
        String text = String.format(REPORT_FORMAT, /*Model.getUser().getName()*/"guest", list.size());
        for (File file : list)
            text += "\n" + file.getName() + "\t" + file.length();
        send("Report", text);
    }

    public static void sendPropose() {

    }

    private static void send(String title, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(LOGIN, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(LOGIN));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(TO));
            message.setSubject(title);
            message.setText(text);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}