package examples;

import javax.net.SocketFactory;
import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;

/**
 * Example 9: Try with resources
 *
 * @author gideon
 */
public class TryWithResources  {

    public static void main(String... args) throws Exception {
        TryWithResources ex = new TryWithResources();
        ex.test();
    }

    private void test() throws MikrotikApiException, InterruptedException {
        Config config =new Config();
        config.ambilDataLogin();
        try (ApiConnection con = ApiConnection.connect(SocketFactory.getDefault(), config.HOST, ApiConnection.DEFAULT_PORT, 2000)) {
            con.login(config.USERNAME, config.PASSWORD);
            con.execute("/user/add name=eric");
        }
    }
}
