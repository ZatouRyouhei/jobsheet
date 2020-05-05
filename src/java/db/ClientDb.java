package db;

import entity.Client;
import javax.ejb.Stateless;

/**
 *
 * @author ryouhei
 */
@Stateless
public class ClientDb extends TryCatchDb<Client>  {
    public ClientDb() {
        super(Client.class);
    }
}
