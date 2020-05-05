package db;

import entity.Business;
import javax.ejb.Stateless;

/**
 *
 * @author ryouhei
 */
@Stateless
public class BusinessDb extends TryCatchDb<Business> {
    public BusinessDb() {
        super(Business.class);
    }
}
