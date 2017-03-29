package mail;

import org.apache.log4j.Logger;
//import org.springframework.mail.MailException;
//import org.springframework.mail.MailSender;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;


import java.util.List;
import java.util.concurrent.*;

public class MailDriver {
    private static final Logger log = Logger.getLogger(MailDriver.class);

    private static final String LOGIN = "com.ilya.ivanov@gmail.com";
    private static final String PASSWORD = "Ivanov123";
    private static final String FROM = LOGIN;
    private static final String TO = "com.ilya.ivanov@yandex.com";

    private static final String SUBJECT_PREFIX = "Catty - ";

    private static final String REPORT_SUBJECT = "Activity report";
    private static final String REPORT_FORMAT = "User %s added %d file(s):\n";

    private static final String PROPOSAL_SUBJECT = "Proposal for adding";
    private static final String PROPOSAL_FORMAT = "Guest offered %d files for adding:\n";

    private static final EmailService service = new LowLevelSMTPEmailService();

    private static final ExecutorService executor = Executors.newFixedThreadPool(2, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("AsyncEmailSender");
        return thread;
    });

    public static void sendProposal(List<String> files) {
        executor.submit(
                new AsyncEmailSender(
                        service,
                        FROM,
                        TO,
                        SUBJECT_PREFIX + PROPOSAL_SUBJECT,
                        String.format(PROPOSAL_FORMAT, files.size()) + bodyBuilder(files)
                )
        );
    }

    public static void sendReport(String user, List<String> files) {
        executor.submit(
                new AsyncEmailSender(
                        service,
                        FROM,
                        TO,
                        SUBJECT_PREFIX + REPORT_SUBJECT,
                        String.format(REPORT_FORMAT, user, files.size()) + bodyBuilder(files)
                )
        );
    }

    private static String bodyBuilder(List<String> files) {
        StringBuilder body = new StringBuilder("");
        for (String file : files)
            body.append(file).append("\n");
        return body.toString();
    }
}