package model.mail;

import javax.mail.*;

/**
 * Send mail using TSL.
 * Created by Илья on 23.03.2017.
 */
class TSLSender extends Sender {
    TSLSender(String username, String password) {
        super(username, password);

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
    }

    void send(String subject, String text, String from, String to) throws RuntimeException{
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        preformSending(subject, text, from, to, session);
    }
}