import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import controller.DataController;
import model.Model;
import model.user.AbstractUser;
import view.View;

public class Catty extends Application {
    private static final Logger log = Logger.getLogger(Catty.class);

    private static final Timer timer = new Timer();

    static {
        // reset user's quota every midnight
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
				AbstractUser user = Model.getUser();
            	if (user != null) {
					user.resetQuota();
				}
            }
        }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

        log.info("User's quota reset scheduled at " + today.getTime());
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
//	    MailDriver.sendProposal(Arrays.asList("1", "2", "#"));

        try {
            Executors.newSingleThreadExecutor().submit(() -> {
                DataController.dao.connect("src/main/resources/db");
                return true;
            });
        } catch (Exception e) {
            // terminate
            throw e;
        }

        View.hideAllAndShow("login");
	}

    @Override
    public void stop() throws Exception {
        timer.cancel();
        DataController.dao.disconnect();
    }

	public static void main(String[] args) {
		launch(args);
	}
}