/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 検索条件
 * @author ryouhei
 */
@XmlRootElement
public class RestSearchConditionJobSheet {
    private Integer client;
    private Integer business;
    private Integer businessSystem;
    private Integer inquiry;
    private String contact;
    private String deal;
    private String occurDateFrom;
    private String occurDateTo;
    private Integer completeSign;
    private String limitDate;
    private String keyword;

    public RestSearchConditionJobSheet() {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getOccurDateFrom() {
        return occurDateFrom;
    }

    public void setOccurDateFrom(String occurDateFrom) {
        this.occurDateFrom = occurDateFrom;
    }

    public String getOccurDateTo() {
        return occurDateTo;
    }

    public void setOccurDateTo(String occurDateTo) {
        this.occurDateTo = occurDateTo;
    }

    public Integer getCompleteSign() {
        return completeSign;
    }

    public void setCompleteSign(Integer completeSign) {
        this.completeSign = completeSign;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "RestSearchConditionJobSheet{" + "client=" + client + ", business=" + business + ", businessSystem=" + businessSystem + ", inquiry=" + inquiry + ", contact=" + contact + ", deal=" + deal + ", occurDateFrom=" + occurDateFrom + ", occurDateTo=" + occurDateTo + ", completeSign=" + completeSign + ", limitDate=" + limitDate + ", keyword=" + keyword + '}';
    }
    
}
