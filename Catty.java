package com.ilya.ivanov.catty_catalog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.ilya.ivanov.catty_catalog.controller.DataController;
import com.ilya.ivanov.catty_catalog.model.Model;
import com.ilya.ivanov.catty_catalog.model.user.AbstractUser;
import com.ilya.ivanov.catty_catalog.resources.Recource;
import com.ilya.ivanov.catty_catalog.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Catty extends Application {
    private static final Logger log = Logger.getLogger(Catty.class);

	static Timer timer = new Timer();

    static {
        // reset user's quota every midnight
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);

        timer.schedule(new TimerTask() {
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
    public void stop() throws Exception {
        DataController.dao.disconnect();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		DataController.dao.connect("src/com/ilya/ivanov/catty_catalog/resources/db");

        View.hideAllAndShow("login");
	}

	public static void main(String[] args) {
		launch(args);
	}
}