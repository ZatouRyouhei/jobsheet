package rest.dto;

import entity.Attachment;
import entity.JobSheet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 検索結果用DTO
 * @author ryouhei
 */
@XmlRootElement
public class RestSearchJobSheet {
    // ID
    private String id;
    // 顧客
    private RestClient client;
    // システム
    private RestBusinessSystem businessSystem;
    // 問合せ区分
    private RestInquiry inquiry;
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
    private RestUser contact;
    // 完了期限 yyyy-MM-dd
    private String limitDate;
    //対応者
    private RestUser deal;
    // 完了日 yyyy-MM-dd
    private String completeDate;
    //対応詳細
    private String support;
    // 対応時間
    private Double responseTime;
    // 添付ファイルリスト
    private List<RestAttachment> fileList = new ArrayList<>();

    public RestSearchJobSheet() {
    }

    public RestSearchJobSheet(JobSheet jobSheet, List<Attachment> attachementList) {
        this.id = jobSheet.getId();
        this.client = new RestClient(jobSheet.getClient());
        this.businessSystem = new RestBusinessSystem(jobSheet.getBusinessSystem());
        this.inquiry = new RestInquiry(jobSheet.getInquiry());
        this.department = jobSheet.getDepartment();
        this.person = jobSheet.getPerson();
        this.occurDate = new SimpleDateFormat("yyyy-MM-dd").format(jobSheet.getOccurDateTime());
        this.occurTime = new SimpleDateFormat("HH:mm").format(jobSheet.getOccurDateTime());
        this.title = jobSheet.getTitle();
        this.content = jobSheet.getContent();
        this.contact = new RestUser(jobSheet.getContact().getId(), jobSheet.getContact().getName());
        this.limitDate = new SimpleDateFormat("yyyy-MM-dd").format(jobSheet.getLimitDate());
        this.deal = new RestUser(jobSheet.getDeal().getId(), jobSheet.getDeal().getName());
        if (jobSheet.getCompleteDate() != null) {
            this.completeDate = new SimpleDateFormat("yyyy-MM-dd").format(jobSheet.getCompleteDate());
        } else {
            this.completeDate = "";
        }
        this.support = jobSheet.getSupport();
        this.responseTime = jobSheet.getResponseTime();
        attachementList.forEach(attachment -> this.fileList.add(new RestAttachment(attachment)));
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RestClient getClient() {
        return client;
    }

    public void setClient(RestClient client) {
        this.client = client;
    }

    public RestBusinessSystem getBusinessSystem() {
        return businessSystem;
    }

    public void setBusinessSystem(RestBusinessSystem businessSystem) {
        this.businessSystem = businessSystem;
    }

    public RestInquiry getInquiry() {
        return inquiry;
    }

    public void setInquiry(RestInquiry inquiry) {
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

    public RestUser getContact() {
        return contact;
    }

    public void setContact(RestUser contact) {
        this.contact = contact;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public RestUser getDeal() {
        return deal;
    }

    public void setDeal(RestUser deal) {
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

    public List<RestAttachment> getFileList() {
        return fileList;
    }

    public void setFileList(List<RestAttachment> fileList) {
        this.fileList = fileList;
    }
    
}
