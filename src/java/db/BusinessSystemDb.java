package db;

import entity.BusinessSystem;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author ryouhei
 */
@Stateless
public class BusinessSystemDb extends TryCatchDb<BusinessSystem> {
    public BusinessSystemDb() {
        super(BusinessSystem.class);
    }
    
    /**
     * 指定された業務IDが、システムに紐づいているか確認
     * @param businessId
     * @return true:使用されている、false:使用されていない
     */
    public boolean checkBusiness(Integer businessId) {
        TypedQuery<BusinessSystem> q = em.createNamedQuery(BusinessSystem.BUSINESSSYSTEM_BUSINESSCHECK, BusinessSystem.class);
        q.setParameter("businessId", businessId);
        List<BusinessSystem> businessSystemList = q.getResultList();
        if (businessSystemList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
