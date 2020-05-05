package db;

import entity.Inquiry;
import javax.ejb.Stateless;

/**
 *
 * @author ryouhei
 */
@Stateless
public class InquiryDb extends TryCatchDb<Inquiry> {
    public InquiryDb() {
        super(Inquiry.class);
    }
}
