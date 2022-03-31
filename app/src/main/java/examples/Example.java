package examples;

import javax.net.SocketFactory;
import me.legrange.mikrotik.ApiConnection;

/**
 *
 * @author gideon
 */
 public abstract class Example {
     
    public void connect() throws Exception {
        con = ApiConnection.connect(SocketFactory.getDefault(), Config.HOST, ApiConnection.DEFAULT_PORT, 2000);
        con.login(Config.USERNAME, Config.PASSWORD);
    }

    public void disconnect() throws Exception {
        con.close();
    }
    
    protected ApiConnection con;
    
}
