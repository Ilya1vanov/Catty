package model.quotatimer;

import model.Model;
import model.user.AbstractUser;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Schedule quota reset every midnight.
 *     Ensures the normal operation of business logic.<br>
 * <br>
 * Created by Илья on 26.03.2017.
 */
public class QuotaTimer {
    private static final Logger log = Logger.getLogger(QuotaTimer.class);

    private static QuotaTimer ourInstance = new QuotaTimer();

    ScheduledExecutorService executor;
    ScheduledFuture future;

    public static QuotaTimer getInstance() {
        return ourInstance;
    }

    private QuotaTimer() {
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("Quota Timer");
            return thread;
        });
    }

    /**
     * Run task: reset current user's quota every midnight.
     * Subsequent call has no effect.
     */
    public void start() {
        if (future == null) {
            Calendar midnight = Calendar.getInstance();
            midnight.set(Calendar.HOUR_OF_DAY, 23);
            midnight.set(Calendar.MINUTE, 59);
            midnight.set(Calendar.SECOND, 59);

            future = executor.scheduleAtFixedRate(() -> {
                AbstractUser user = Model.getUser();
                if (user != null) {
                    user.resetQuota();
                }
            }, midnight.getTimeInMillis() - Calendar.getInstance().getTimeInMillis(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), TimeUnit.MILLISECONDS);

            log.info("User's quota reset scheduled at " + midnight.getTime());
        }
    }
}
