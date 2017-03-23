package model.mail;


import javax.mail.*;
import java.util.Properties;

/**
 * Send mail using SSL.
 * Created by Илья on 23.03.2017.
 */
class SSLSender extends Sender {
    public SSLSender(String username, String password) {
        super(username, password);

        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
    }

    public void send(String subject, String text, String from, String to) throws RuntimeException{
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        preformSending(subject, text, from, to, session);
    }
}
