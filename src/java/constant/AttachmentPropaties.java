package constant;

/**
 * 添付ファイル保存についての設定クラス
 * 利用するサーバごとに設定を変更してください。
 * @author ryouhei
 */
public class AttachmentPropaties {
    // 添付ファイルの保存モード（DB_MODE:DBに保存、DISK_MODE:サーバのフォルダに保存）
    public static final AttachmentMode ATTACHMENT_MODE = AttachmentMode.DISK_MODE;
    // DISK_MODEの場合のベース保存フォルダ
    public static final String BASE_DIR = "C:/jobsheet_attachement/";
    
    public enum AttachmentMode {
        DB_MODE,
        DISK_MODE
    }
}
