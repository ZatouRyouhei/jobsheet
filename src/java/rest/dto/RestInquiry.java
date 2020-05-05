package rest.dto;

import entity.Inquiry;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestInquiry {
    private Integer id;
    private String name;

    public RestInquiry() {
    }

    public RestInquiry(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.name = inquiry.getName();
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
