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
    
    public int deleteAll() {
        int deleteCnt = em.createNamedQuery(Holiday.HOLIDAY_DELETE_ALL).executeUpdate();
        return deleteCnt;
    }
}
