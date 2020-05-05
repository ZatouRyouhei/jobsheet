package db;

import entity.User;
import javax.ejb.Stateless;

/**
 *
 * @author ryouhei
 */
@Stateless
public class UserDb extends TryCatchDb<User> {
    public UserDb() {
        super(User.class);
    }
}
