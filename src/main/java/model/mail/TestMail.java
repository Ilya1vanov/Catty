package model.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class TestMail {
    static final String ENCODING = "UTF-8";

    public static void send() throws MessagingException, UnsupportedEncodingException {
        String subject = "Subject";
        String content = "Test";
        String smtpHost="smtp.yandex.ru";
        String address="com.ilya.ivanov@gmail.com";
        String login="com.ilya.ivanov@gmail.com";
        String password="Ivanov123";
        String smtpPort="465";
        sendSimpleMessage (login, password, login, "ivan4off@gmail.com", content, subject, smtpPort, smtpHost);
    }

    public static void sendSimpleMessage(String login, String password, String from, String to, String content, String subject, String smtpPort, String smtpHost)
            throws MessagingException, UnsupportedEncodingException {
        Authenticator auth = new MyAuthenticator(login, password);

        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.host", smtpHost);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(content);
        Transport.send(msg);
    }
}

class MyAuthenticator extends Authenticator {
    private String user;
    private String password;

    MyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        String user = this.user;
        String password = this.password;
        return new PasswordAuthentication(user, password);
    }
}