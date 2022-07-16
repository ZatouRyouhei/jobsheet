package db;

import constant.AttachmentPropaties;
import entity.Attachment;
import entity.AttachmentKey;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
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
        // 添付ファイルモードによって処理を切り替える
        if (AttachmentPropaties.ATTACHMENT_MODE == AttachmentPropaties.AttachmentMode.DB_MODE) {
            // 添付ファイルのデータをDBに登録する場合。
            try (InputStream in = part.getValueAs(InputStream.class)) {
                byte[] data = IOUtils.toByteArray(in);
                in.read(data);
                String filename = new String(part.getFormDataContentDisposition().getFileName().getBytes("iso-8859-1"), "utf-8");
                Attachment attachment = new Attachment(new AttachmentKey(id, nextSeq), filename, data);
                add(attachment);
            } catch (IOException ex) {
                Logger.getLogger(AttachmentDb.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (AttachmentPropaties.ATTACHMENT_MODE == AttachmentPropaties.AttachmentMode.DISK_MODE) {
            // サーバのフォルダに保存する場合。
            // ファイルを配置するフォルダを作成する。　ベースフォルダがない場合はベースフォルダごと作成する。
            Path putDir = Paths.get(AttachmentPropaties.BASE_DIR + id + "/" + nextSeq);
            if (Files.notExists(putDir)) {
                try {
                    Files.createDirectories(putDir);
                } catch (IOException ex) {
                    Logger.getLogger(AttachmentDb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // ファイルをフォルダに配置する。
            try (InputStream in = part.getValueAs(InputStream.class)) {
                // DBにデータを登録する。（添付ファイルはDBには入れずにサーバに保存する。）
                String filename = new String(part.getFormDataContentDisposition().getFileName().getBytes("iso-8859-1"), "utf-8");
                Attachment attachment = new Attachment(new AttachmentKey(id, nextSeq), filename);
                add(attachment);

                // フォルダに配置する。
                byte[] data = IOUtils.toByteArray(in);
                Path filePath = Paths.get(putDir.toString(), filename);
                try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(filePath))) {
                    bos.write(data, 0, data.length);
                }
            } catch (IOException ex) {
                Logger.getLogger(AttachmentDb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * 添付ファイル削除処理
     * @param file 
     */
    public void deleteAttachment(Attachment file) {
        // DBから削除する。
        delete(file);
        // DISK_MODEの場合、サーバのファイルを削除する。
        if (AttachmentPropaties.ATTACHMENT_MODE == AttachmentPropaties.AttachmentMode.DISK_MODE) {
            try {
                // ファイル削除
                Path filePath = Paths.get(AttachmentPropaties.BASE_DIR + file.getKey().getId() + "/" + file.getKey().getSeqNo() + "/" + file.getFileName());
                Files.deleteIfExists(filePath);
                // 連番フォルダ削除
                Path seqDirPath = Paths.get(AttachmentPropaties.BASE_DIR + file.getKey().getId() + "/" + file.getKey().getSeqNo());
                Files.deleteIfExists(seqDirPath);
                // 番号フォルダが空の場合は削除
                File idDir = new File(AttachmentPropaties.BASE_DIR + file.getKey().getId());
                if (idDir.isDirectory()) {
                    if (idDir.list().length > 0) {
                        // フォルダが空でない場合は削除しない
                    } else {
                        // フォルダが空の場合は削除する
                        Path idDirPath = idDir.toPath();
                        Files.deleteIfExists(idDirPath);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(AttachmentDb.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    
    /**
     * 添付ファイルのバイト列を取得する。
     * @param file
     * @return 
     */
    public byte[] getByteFile(Attachment file) {
        Path filePath = Paths.get(AttachmentPropaties.BASE_DIR + file.getKey().getId() + "/" + file.getKey().getSeqNo() + "/" + file.getFileName());
        if (Files.exists(filePath)) {
            try {
                byte[] byteFile = Files.readAllBytes(filePath);
                return byteFile;
            } catch (IOException ex) {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }
}
