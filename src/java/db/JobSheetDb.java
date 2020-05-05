package db;

import entity.BusinessSystem;
import entity.JobSheet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import rest.dto.RestSearchConditionJobSheet;

/**
 *
 * @author ryouhei
 */
@Stateless
public class JobSheetDb extends TryCatchDb<JobSheet> {
    public JobSheetDb() {
        super(JobSheet.class);
    }
    
    /**
     * 次のIDを生成する。
     * @return 
     */
    public String getNextId() {
        Date now = new Date();
        String idHeader = new SimpleDateFormat("yyyy-MM").format(now);
        TypedQuery<String> q = em.createNamedQuery(JobSheet.JOBSHEET_GETMAXID, String.class);
        q.setParameter("idHeader", idHeader + "%");
        String maxId = q.getSingleResult();
        String nextSeq = "001";
        if (maxId != null) {
            int maxSeqNo = Integer.parseInt(maxId.substring(8));
            int nextSeqNo = maxSeqNo + 1;
            nextSeq = String.format("%03d", nextSeqNo);
        }
        
        return idHeader + "-" + nextSeq;
    }
    
    /**
     * 業務日誌検索
     * @param condition
     * @return 
     */
    public List<JobSheet> getJobSheetList(RestSearchConditionJobSheet condition) throws ParseException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT j");
        query.append("  FROM JobSheet j");
        query.append(" WHERE 1 = 1");
        if (condition.getClient() != 0) {
            query.append(" AND j.client.id = :client");
        }
        if (condition.getBusiness() != 0) {
            query.append(" AND j.businessSystem.business.id = :business");
        }
        if (condition.getBusinessSystem() != 0) {
            query.append(" AND j.businessSystem.id = :businessSystem");
        }
        if (condition.getInquiry() != 0) {
            query.append(" AND j.inquiry.id = :inquiry");
        }
        if (StringUtils.isNotEmpty(condition.getContact())) {
            query.append(" AND j.contact.id = :contact");
        }
        if (StringUtils.isNotEmpty(condition.getDeal())) {
            query.append(" AND j.deal.id = :deal");
        }
        if (StringUtils.isNotEmpty(condition.getOccurDateFrom())) {
            query.append(" AND j.occurDateTime >= :occurDateFrom");
        }
        if (StringUtils.isNotEmpty(condition.getOccurDateTo())) {
            query.append(" AND j.occurDateTime <= :occurDateTo");
        }
        if (condition.getCompleteSign() == 1) {
            query.append(" AND j.completeDate IS NOT NULL");
        }
        if (condition.getCompleteSign() == 2) {
            query.append(" AND j.completeDate IS NULL");
        }
        if (StringUtils.isNotEmpty(condition.getLimitDate())) {
            query.append(" AND j.limitDate = :limitDate");
        }
        if (StringUtils.isNotEmpty(condition.getKeyword())) {
            query.append(" AND (j.title LIKE :keyword OR j.content LIKE :keyword )");
        }
        query.append(" ORDER BY j.id DESC");
        
        TypedQuery<JobSheet> q = em.createQuery(query.toString(), JobSheet.class);
        if (condition.getClient() != 0) {
            q.setParameter("client", condition.getClient());
        }
        if (condition.getBusiness() != 0) {
            q.setParameter("business", condition.getBusiness());
        }
        if (condition.getBusinessSystem() != 0) {
            q.setParameter("businessSystem", condition.getBusinessSystem());
        }
        if (condition.getInquiry() != 0) {
            q.setParameter("inquiry", condition.getInquiry());
        }
        if (StringUtils.isNotEmpty(condition.getContact())) {
            q.setParameter("contact", condition.getContact());
        }
        if (StringUtils.isNotEmpty(condition.getDeal())) {
            q.setParameter("deal", condition.getDeal());
        }
        if (StringUtils.isNotEmpty(condition.getOccurDateFrom())) {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date occurDateFrom = sdFormat.parse(condition.getOccurDateFrom() + " 00:00");
            q.setParameter("occurDateFrom", occurDateFrom);
        }
        if (StringUtils.isNotEmpty(condition.getOccurDateTo())) {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date occurDateTo = sdFormat.parse(condition.getOccurDateTo() + " 23:59");
            q.setParameter("occurDateTo", occurDateTo);
        }
        if (StringUtils.isNotEmpty(condition.getLimitDate())) {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date limitDate = sdFormat.parse(condition.getLimitDate());
            q.setParameter("limitDate", limitDate);
        }
        if (StringUtils.isNotEmpty(condition.getKeyword())) {
            q.setParameter("keyword", "%" + condition.getKeyword() + "%");
        }
        List<JobSheet> jobSheetList = q.getResultList();
        return jobSheetList;
    }
    
    /**
     * 指定のユーザIDが業務日誌で使用されているか確認
     * @param userId
     * @return true:使用されている、false:使用されていない
     */
    public boolean checkUser(String userId) {
        TypedQuery<JobSheet> q = em.createNamedQuery(JobSheet.JOBSHEET_USERCHECK, JobSheet.class);
        q.setParameter("userId", userId);
        List<JobSheet> jobSheetList = q.getResultList();
        if (jobSheetList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 指定のシステムIDが業務日誌で使用されているか確認
     * @param systemId
     * @return true:使用されている、false:使用されていない
     */
    public boolean checkSystem(Integer systemId) {
        TypedQuery<JobSheet> q = em.createNamedQuery(JobSheet.JOBSHEET_SYSTEMCHECK, JobSheet.class);
        q.setParameter("systemId", systemId);
        List<JobSheet> jobSheetList = q.getResultList();
        if (jobSheetList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 指定したシステムの指定期間の発生件数をカウントする。
     * @param system
     * @param fromDate
     * @param toDate
     * @return 
     */
    public Integer statsOccur(BusinessSystem system, Date fromDate, Date toDate) {
        TypedQuery<Long> q = em.createNamedQuery(JobSheet.JOBSHEET_STATS_OCCUR, Long.class);
        q.setParameter("occurDateFrom", fromDate);
        q.setParameter("occurDateTo", toDate);
        q.setParameter("systemId", system.getId());
        Integer result = 0;
        Long count = q.getSingleResult();
        if (count != null) {
            result = count.intValue();
        }
        return result;
    }
    
    /**
     * 指定したシステムの指定期間の完了件数をカウントする。
     * @param system
     * @param fromDate
     * @param toDate
     * @return 
     */
    public Integer statsComplete(BusinessSystem system, Date fromDate, Date toDate) {
        TypedQuery<Long> q = em.createNamedQuery(JobSheet.JOBSHEET_STATS_COMPLETE, Long.class);
        q.setParameter("completeDateFrom", fromDate);
        q.setParameter("completeDateTo", toDate);
        q.setParameter("systemId", system.getId());
        Integer result = 0;
        Long count = q.getSingleResult();
        if (count != null) {
            result = count.intValue();
        }
        return result;
    }
    
    /**
     * 指定したシステムの未完了の問合せ件数をカウントする。
     * @param system
     * @return 
     */
    public Integer countLeft(BusinessSystem system) {
        TypedQuery<Long> q = em.createNamedQuery(JobSheet.JOBSHEET_COUNT_LEFT, Long.class);
        q.setParameter("systemId", system.getId());
        Integer result = 0;
        Long count = q.getSingleResult();
        if (count != null) {
            result = count.intValue();
        }
        return result;
    }
}
