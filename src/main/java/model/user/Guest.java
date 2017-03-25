package model.user;

/**
 * Class represents Guest with read permissions.
 * Created by Илья on 05.03.2017.
 */
public class Guest extends AbstractUser {

    public Guest() {
        super(0, UserType.guest, "Guest", 0);
    }

    /**
     * Do nothing.
     * @param fileSize anything
     */
    @Override
    public void subtractQuota(long fileSize) {}
}
