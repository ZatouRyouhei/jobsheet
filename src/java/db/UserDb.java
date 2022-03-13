package db;

import entity.User;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
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
    
    /**
     * ログイン処理
     * @param userId
     * @param password
     * @return 
     */
    public User login(String userId, String password) {
        TypedQuery<User> q = em.createNamedQuery(User.USER_LOGIN, User.class);
        q.setParameter("userId", userId);
        q.setParameter("password", password);
        User resultUser = null;
        try {
            resultUser = q.getSingleResult();
        } catch (NoResultException ex) {
            // 結果がないとき
        }
        return resultUser;
    }
}
