package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 業務日誌
 * @author ryouhei
 */

@NamedQueries({
    @NamedQuery (
            name = JobSheet.JOBSHEET_GETMAXID,
            query =   "SELECT MAX(js.id)"
                    + "  FROM JobSheet js"
                    + " WHERE js.id LIKE :idHeader"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_USERCHECK,
            query =   "SELECT js"
                    + "  FROM JobSheet js"
                    + " WHERE js.contact.id = :userId"
                    + "    OR js.deal.id = :userId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_STATS_OCCUR,
            query =   "SELECT COUNT(js)"
                    + "  FROM JobSheet js"
                    + " WHERE js.occurDateTime >= :occurDateFrom"
                    + "   AND js.occurDateTime <= :occurDateTo"
                    + "   AND js.businessSystem.id = :systemId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_STATS_COMPLETE,
            query =   "SELECT COUNT(js)"
                    + "  FROM JobSheet js"
                    + " WHERE js.completeDate >= :completeDateFrom"
                    + "   AND js.completeDate <= :completeDateTo"
                    + "   AND js.completeDate IS NOT NULL"
                    + "   AND js.businessSystem.id = :systemId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_COUNT_LEFT,
            query =   "SELECT COUNT(js)"
                    + "  FROM JobSheet js"
                    + " WHERE js.completeDate IS NULL"
                    + "   AND js.businessSystem.id = :systemId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_SUM_TIME,
            query =   "SELECT SUM(js.responseTime)"
                    + "  FROM JobSheet js"
                    + " WHERE js.completeDate >= :completeDateFrom"
                    + "   AND js.completeDate <= :completeDateTo"
                    + "   AND js.completeDate IS NOT NULL"
                    + "   AND js.responseTime IS NOT NULL"
                    + "   AND js.businessSystem.id = :systemId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_SYSTEMCHECK,
            query =   "SELECT js"
                    + "  FROM JobSheet js"
                    + " WHERE js.businessSystem.id = :systemId"
    ),
    @NamedQuery (
            name = JobSheet.JOBSHEET_CLIENTCHECK,
            query =   "SELECT js"
                    + "  FROM JobSheet js"
                    + " WHERE js.client.id = :clientId"
    )
})
@Entity
@Table(name="t_job_sheet")
@Cacheable(false)
public class JobSheet implements Serializable {
    public static final String JOBSHEET_GETMAXID = "JobSheet_getMaxId";
    public static final String JOBSHEET_USERCHECK = "JOBSHEET_USERCHECK";
    public static final String JOBSHEET_SYSTEMCHECK = "JOBSHEET_SYSTEMCHECK";
    public static final String JOBSHEET_CLIENTCHECK = "JOBSHEET_CLIENTCHECK";
    public static final String JOBSHEET_STATS_OCCUR = "JOBSHEET_STATS_OCCUR";
    public static final String JOBSHEET_STATS_COMPLETE = "JOBSHEET_STATS_COMPLETE";
    public static final String JOBSHEET_COUNT_LEFT = "JOBSHEET_COUNT_LEFT";
    public static final String JOBSHEET_SUM_TIME = "JOBSHEET_SUM_TIME";
    
    public static final int SIZE_ID = 11;
    public static final int SIZE_DEPARTMENT = 50;
    public static final int SIZE_PERSON = 50;
    public static final int SIZE_TITLE = 50;
    public static final int SIZE_CONTENT = 500;
    public static final int SIZE_SUPPORT = 500;
    
    // ID YYYY-MM-連番3桁
    @Id
    @Column(length=SIZE_ID)
    private String id;
    // 顧客
    private Client client;
    // システム
    private BusinessSystem businessSystem;
    // 問合せ区分
    private Inquiry inquiry;
    // 問合せ部署
    @Column(length=SIZE_DEPARTMENT)
    private String department;
    // 担当者
    @Column(length=SIZE_PERSON)
    private String person;
    //発生日時
    @Temporal(TemporalType.TIMESTAMP)
    private Date occurDateTime;
    // タイトル
    @Column(length=SIZE_TITLE)
    private String title;
    // 詳細
    @Column(length=SIZE_CONTENT)
    private String content;
    // 窓口
    private User contact;
    // 完了期限
    @Temporal(TemporalType.DATE)
    private Date limitDate;
    //対応者
    private User deal;
    // 完了日
    @Temporal(TemporalType.DATE)
    private Date completeDate;
    //対応詳細
    @Column(length=SIZE_SUPPORT)
    private String support;
    // 対応時間
    private Double responseTime;

    public JobSheet() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BusinessSystem getBusinessSystem() {
        return businessSystem;
    }

    public void setBusinessSystem(BusinessSystem businessSystem) {
        this.businessSystem = businessSystem;
    }

    public Inquiry getInquiry() {
        return inquiry;
    }

    public void setInquiry(Inquiry inquiry) {
        this.inquiry = inquiry;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Date getOccurDateTime() {
        return occurDateTime;
    }

    public void setOccurDateTime(Date occurDateTime) {
        this.occurDateTime = occurDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public User getDeal() {
        return deal;
    }

    public void setDeal(User deal) {
        this.deal = deal;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }
    
}
