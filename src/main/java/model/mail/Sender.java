package model.mail;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Илья on 23.03.2017.
 */
abstract class Sender {
    protected static final Logger log = LogManager.getLogger(TSLSender.class);

    protected String username;
    protected String password;
    protected Properties props = System.getProperties();

    protected static final String HOST = "smtp.gmail.com";

    /**
     * Set properties for getting connection.
     * @param username - Sender address.
     * @param password - Account pass.
     */
    protected Sender(String username, String password) {
        this.username = username;
        this.password = password;

        props.put("mail.smtp.host", HOST);
        props.put("mail.host", HOST);
        props.put("mail.smtp.auth", "true");

        // debug
//        props.put("mail.smtp.tsl.trust", HOST);
//        props.put("mail.smtp.ssl.trust", HOST);
//        props.put("mail.smtp.user", username);
//        props.put("mail.smtp.password", password);

//        props.put("mail.transport.protocol", "smtp");
    }

    /**
     * Method for sending mail.
     * @param subject - Subj of the mail.
     * @param text - Text of the message.
     * @param to - Recipient address.
     * @param session - Connection session.
     */
    protected final void preformSending(String subject, String text, String from, String to, Session session) throws RuntimeException{
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);

//            Transport transport = session.getTransport("smtp");
//            transport.connect(HOST, username, password);
//            transport.sendMessage(message, message.getAllRecipients());
//            transport.close();

            Transport.send(message);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * method for sending mail with input subject
     * @param subject - the Theme of mail
     * @param text - the text of message
     * @param from - the mail of admin
     * @param to - the mail address
     */
    abstract void send(String subject, String text, String from, String to);
}
