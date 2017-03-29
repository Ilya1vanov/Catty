package mail;


import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Perform mail sending.
 * Created by Илья on 25.03.2017.
 */
public class LowLevelSMTPEmailService implements EmailService {
    private static final Logger log = Logger.getLogger(LowLevelSMTPEmailService.class);

    /** smtp message format */
    private static final String HELO = "HELO smtp.gmail.com";
    private static final String MAIL_FROM = "MAIL FROM: ";
    private static final String RCPT_TO = "RCPT TO: ";
    private static final String DATA = "DATA ";
    private static final String FROM = "from: ";
    private static final String SUBJECT = "subject: ";
    private static final String TO = "to: ";
    private static final String NEW_LINE = "\n";
    private static final String END = ".";

    /** output buffer */
    private PrintStream output;

    /** input buffer */
    private BufferedReader input;

    /** send giver string by smtp */
    private void send(String str) {
        output.println(str);
        output.flush();
        log.debug("Java sent: " + str);
    }

    /** receive string from smtp */
    private String receive() throws IOException {
        String line = input.readLine();
        log.debug("SMTP response: " + line);
        return line;
    }

    /**
     * @param fromEmail - who sends email message
     * @param toEmail - who gets email message
     * @param subject - subject of email message
     * @param messageText - message content
     */
    @Override
    public void sendMessage(String fromEmail, String toEmail, String subject, String messageText) {
        // try-with-resource
        try (Socket smtp = new Socket("mx.yandex.ru", 25);
             OutputStream os = smtp.getOutputStream();
             InputStream is = smtp.getInputStream()) {
            output = new PrintStream(os);
            input = new BufferedReader(new InputStreamReader(is));

            receive();
            send(HELO);
            receive();
            send(MAIL_FROM + fromEmail);
            receive();
            send(RCPT_TO + toEmail);
            receive();
            send(DATA);
            receive();
            send(FROM + fromEmail);
            send(SUBJECT + subject);
            send(TO + toEmail);
            send(NEW_LINE);
            send(messageText);
            send(NEW_LINE);
            send(END);
            receive();
        } catch (IOException e) {
            // suppress
        }
    }
}
