package rest.dto;

import entity.Attachment;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ryouhei
 */
@XmlRootElement
public class RestAttachment {
    Integer seqNo;
    String fileName;

    public RestAttachment() {
    }

    public RestAttachment(Attachment attachment) {
        this.seqNo = attachment.getKey().getSeqNo();
        this.fileName = attachment.getFileName();
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
