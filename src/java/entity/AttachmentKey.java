package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author ryouhei
 */
@Embeddable
public class AttachmentKey implements Serializable {
    public static final int SIZE_ID = 11;
    
    @Column(length=SIZE_ID)
    private String id;
    private Integer seqNo;

    public AttachmentKey() {
    }

    public AttachmentKey(String id, Integer seqNo) {
        this.id = id;
        this.seqNo = seqNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
}
