package db;

import entity.Holiday;
import javax.ejb.Stateless;

/**
 *
 * @author ryouhei
 */
@Stateless
public class HolidayDb extends TryCatchDb<Holiday> {
    public HolidayDb() {
        super(Holiday.class);
    }
}
