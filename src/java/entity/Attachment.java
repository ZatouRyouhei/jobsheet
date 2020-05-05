package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 添付ファイル
 * @author ryouhei
 */
@NamedQueries({
    @NamedQuery (
            name = Attachment.ATTACHMENT_GETMAXSEQNO,
            query =   "SELECT MAX(a.key.seqNo)"
                    + "  FROM Attachment a"
                    + " WHERE a.key.id = :id"
    ),
    @NamedQuery (
            name = Attachment.ATTACHMENT_GETFILELIST,
            query =   "SELECT a"
                    + "  FROM Attachment a"
                    + " WHERE a.key.id = :id"
    ),
    @NamedQuery (
            name = Attachment.ATTACHMENT_GETFILE,
            query =   "SELECT a"
                    + "  FROM Attachment a"
                    + " WHERE a.key.id = :id"
                    + "   AND a.key.seqNo = :seqNo"
    )
})
@Entity
@Table(name="t_attachment")
@Cacheable(false)
public class Attachment implements Serializable {
    public static final String ATTACHMENT_GETMAXSEQNO = "ATTACHMENT_GETMAXSEQNO";
    public static final String ATTACHMENT_GETFILELIST = "ATTACHMENT_GETFILELIST";
    public static final String ATTACHMENT_GETFILE = "ATTACHMENT_GETFILE";
    
    @EmbeddedId
    private AttachmentKey key;
    private String fileName;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    private byte[] attachFile;

    public Attachment() {
    }

    public Attachment(AttachmentKey key, String fileName, byte[] attachFile) {
        this.key = key;
        this.fileName = fileName;
        this.attachFile = attachFile;
    }

    public AttachmentKey getKey() {
        return key;
    }

    public void setKey(AttachmentKey key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(byte[] attachFile) {
        this.attachFile = attachFile;
    }
}
