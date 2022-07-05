package examples;

import javax.net.SocketFactory;
import me.legrange.mikrotik.ApiConnection;

/**
 *
 * @author gideon
 */
 public abstract class Example {
     
    public void connect() throws Exception {
        Config config =new Config();
        config.ambilDataLogin();
        con = ApiConnection.connect(SocketFactory.getDefault(), config.HOST, ApiConnection.DEFAULT_PORT, 2000);
        con.login(config.USERNAME, config.PASSWORD);
    }

    public void disconnect() throws Exception {
        con.close();
    }
    
    protected ApiConnection con;
    
}
