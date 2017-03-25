package model.mail;


import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Илья on 25.03.2017.
 */
public class LowLevelSMTPEmailService implements EmailService {
    private static final Logger log = Logger.getLogger(LowLevelSMTPEmailService.class);

    /** smtp message sormat */
    private static String HELO = "HELO smtp.gmail.com";
    private static String MAIL_FROM = "MAIL FROM: ";
    private static String RCPT_TO = "RCPT TO: ";
    private static String DATA = "DATA ";
    private static String FROM = "from: ";
    private static String SUBJECT = "subject: ";
    private static String TO = "to: ";
    private static String NEW_LINE = "\n";
    private static String END = ".";

    /** output buffer */
    private PrintStream ps;

    /** input buffer */
    private BufferedReader bufferedReader;

    /** send giver string by smtp */
    private void send(String str) {
        ps.println(str);      // посылка строки на SMTP
        ps.flush();           // очистка буфера
        log.debug("Java sent: " + str);
    }

    /** receive string from smtp */
    private String receive() throws IOException {
        return bufferedReader.readLine();
    }

    /**
     * @param fromEmail - who sends email message
     * @param toEmail - who gets email message
     * @param subject - subject of email message
     * @param messageText - message content
     * @return True if and only if mail delivered
     */
    @Override
    public void sendMessage(String fromEmail, String toEmail, String subject, String messageText) {
        // try-with-resource
        try (Socket smtp = new Socket("mx.yandex.ru", 25)) {
            OutputStream os = smtp.getOutputStream();
            ps = new PrintStream(os);
            InputStream is = smtp.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));

            receive();
            send(HELO);
            log.debug("SMTP respons: " + receive());
            send(MAIL_FROM + fromEmail);
            log.debug("SMTP respons: " + receive());
            send(RCPT_TO + toEmail);
            log.debug("SMTP respons: " + receive());
            send(DATA);
            log.debug("SMTP respons: " + receive());
            send(FROM + fromEmail);
            send(SUBJECT + subject);
            send(TO + toEmail);
            send(NEW_LINE);
            send(messageText);
            send(NEW_LINE);
            send(END);
            log.debug("SMTP respons: " + receive());
        } catch (IOException e) {
            // suppress
        }
    }
}
