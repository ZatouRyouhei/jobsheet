package rest.filter;

import db.UserDb;
import entity.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author ryouhei
 */
@RequestScoped
@Authenticate
@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    UserDb userdb;
    
    /**
     * 認証処理
     * Authorizationヘッダーを読み取り、ユーザIDとパスワードを検証する。
     * Authorizationヘッダーは、Basic username:passwordのかたちとなる想定。
     * 
     * @param requestContext
     * @throws IOException 
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");
        System.out.println("*****" + authHeader);
//        MultivaluedMap<String, String> headers = requestContext.getHeaders();
//        headers.forEach((k, v) -> {
//            System.out.println("*****" + k + ":" + v);
//        });
        
        if (authHeader != null) {
            // username:passwordの部分はBase64でエンコードされているので、デコードを行う。
            byte[] basicAuthByte =  Base64.getDecoder().decode(authHeader.replaceAll("Basic\\s", ""));
            String basicAuth = new String(basicAuthByte, StandardCharsets.UTF_8);
            System.out.println("*****" + basicAuth);
            // ユーザIDとパスワードは「:」で区切られているので分解する。
            String[] headerArr = basicAuth.split(":");
            String userName = headerArr[0];
            String password = headerArr[1];
            User user = userdb.login(userName, password);
            if (user != null) {
                // ユーザが取得できればOK
            } else {
                // ユーザが取得できない場合はエラー
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("Not Logged In.")
                            .build()
                );
            }
        } else {
            // ヘッダーがセットされていない場合はエラー
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Not Logged In.")
                        .build()
            );
        }
    }
    
}
