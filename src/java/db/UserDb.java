package db;

import entity.User;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author ryouhei
 */
@Stateless
public class UserDb extends TryCatchDb<User> {
    public UserDb() {
        super(User.class);
    }
    
    /**
     * 並び順の最大値+1を求める
     * @return 
     */
    public Integer getNextSeq() {
        TypedQuery<Integer> q = em.createNamedQuery(User.USER_GETMAXSEQ, Integer.class);
        Integer maxSeqNo = q.getSingleResult();
        Integer nextSeqNo = 1;
        if (maxSeqNo != null) {
            nextSeqNo = maxSeqNo + 1;
        }
        return nextSeqNo;
    }
}
