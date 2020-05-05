package db;

import entity.Attachment;
import entity.AttachmentKey;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

/**
 *
 * @author ryouhei
 */
@Stateless
public class AttachmentDb extends TryCatchDb<Attachment> {
    public AttachmentDb() {
        super(Attachment.class);
    }
    
    /**
     * 添付ファイル登録処理
     * @param id
     * @param part 
     */
    public void registAttachment(String id, FormDataBodyPart part) {
        //連番の最大値を取得する。
        TypedQuery<Integer> q = em.createNamedQuery(Attachment.ATTACHMENT_GETMAXSEQNO, Integer.class);
        q.setParameter("id", id);
        Integer maxSeq = q.getSingleResult();
        Integer nextSeq = 1;
        if (maxSeq != null) {
            nextSeq = maxSeq + 1;
        }
        // 登録処理
        try (InputStream in = part.getValueAs(InputStream.class)) {
            byte[] data = IOUtils.toByteArray(in);
            in.read(data);
            String filename = new String(part.getFormDataContentDisposition().getFileName().getBytes("iso-8859-1"), "utf-8");
            Attachment attachment = new Attachment(new AttachmentKey(id, nextSeq), filename, data);
            add(attachment);
            
            System.out.println("filename = " + filename + ", size = " + data.length);
        } catch (IOException ex) {
            Logger.getLogger(AttachmentDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 指定したIDのファイルリストを取得する。
     * @param id
     * @return 
     */
    public List<Attachment> getFileList(String id) {
        TypedQuery<Attachment> q = em.createNamedQuery(Attachment.ATTACHMENT_GETFILELIST, Attachment.class);
        q.setParameter("id", id);
        List<Attachment> fileList = q.getResultList();
        return fileList;
    }
    
    public Attachment getFile(String id, Integer seqNo) {
        TypedQuery<Attachment> q = em.createNamedQuery(Attachment.ATTACHMENT_GETFILE, Attachment.class);
        q.setParameter("id", id);
        q.setParameter("seqNo", seqNo);
        Attachment file = q.getSingleResult();
        return file;
    }
}
