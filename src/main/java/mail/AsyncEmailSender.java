package mail;

/**
 * Execution stream that performs sending email basic messages to specified
 * list of addresses
 */
class AsyncEmailSender implements EmailSender {

    /** property - who sends email message */
    private final String from;

    /** property - who gets email message */
    private final String to;

    /** property - subject of email message */
    private final String subject;

    /** property - message content */
    private final String messageText;

    /** property - email service */
    private final EmailService service;

    /**
     * constructor of thread object that specifies parameters of email sending
     * @param from - who sends email message
     * @param to - who gets email message
     * @param subject - subject of email message
     * @param messageText - message content
     */
    AsyncEmailSender(EmailService service, String from, String to, String subject, String messageText) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.messageText = messageText;
        this.service = service;
    }

    /**
     * executes email sending with specified parameters
     */
    @Override
    public void run() {
        service.sendMessage(from, to, subject, messageText);
    }
}
