package rest.dto;

import entity.JobSheet;
import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestJobSheet {
    // ID
    private String id;
    // 顧客
    private Integer client;
    // 業務
    private Integer business;
    // システム
    private Integer businessSystem;
    // 問合せ区分
    private Integer inquiry;
    // 問合せ部署
    private String department;
    // 担当者
    private String person;
    // 発生日 yyyy-MM-dd
    private String occurDate;
    // 発生時間 HH:mm
    private String occurTime;
    // タイトル
    private String title;
    // 詳細
    private String content;
    // 窓口
    private String contact;
    // 完了期限 yyyy-MM-dd
    private String limitDate;
    //対応者
    private String deal;
    // 完了日 yyyy-MM-dd
    private String completeDate;
    //対応詳細
    private String support;
    // 対応時間
    private Double responseTime;

    public RestJobSheet() {
    }

    public RestJobSheet(JobSheet jobsheet) {
        this.id = jobsheet.getId();
        this.client = jobsheet.getClient().getId();
        this.business = jobsheet.getBusinessSystem().getBusiness().getId();
        this.businessSystem = jobsheet.getBusinessSystem().getId();
        this.inquiry = jobsheet.getInquiry().getId();
        this.department = jobsheet.getDepartment();
        this.person = jobsheet.getPerson();
        this.occurDate = new SimpleDateFormat("yyyy-MM-dd").format(jobsheet.getOccurDateTime());
        this.occurTime = new SimpleDateFormat("HH:mm").format(jobsheet.getOccurDateTime());
        this.title = jobsheet.getTitle();
        this.content = jobsheet.getContent();
        this.contact = jobsheet.getContact().getId();
        this.limitDate = new SimpleDateFormat("yyyy-MM-dd").format(jobsheet.getLimitDate());
        this.deal = jobsheet.getDeal().getId();
        this.completeDate = new SimpleDateFormat("yyyy-MM-dd").format(jobsheet.getCompleteDate());
        this.support = jobsheet.getSupport();
        this.responseTime = jobsheet.getResponseTime();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getBusiness() {
        return business;
    }

    public void setBusiness(Integer business) {
        this.business = business;
    }

    public Integer getBusinessSystem() {
        return businessSystem;
    }

    public void setBusinessSystem(Integer businessSystem) {
        this.businessSystem = businessSystem;
    }

    public Integer getInquiry() {
        return inquiry;
    }

    public void setInquiry(Integer inquiry) {
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

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
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
