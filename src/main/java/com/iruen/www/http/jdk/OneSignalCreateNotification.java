package com.iruen.www.http.jdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iruen.www.domain.Message;
import com.iruen.www.helper.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by donghoon on 15. 9. 3..
 */
@Component
public class OneSignalCreateNotification {

    Logger logger = LoggerFactory.getLogger(OneSignalCreateNotification.class);

    ObjectMapper mapper = new ObjectMapper();

    private String one_signal_app_id = Config.getInstance().getProperties("one_signal_app_id");
    private final String URI = "https://onesignal.com/api/v1/notifications";

    public void createnotificationByJDK(String rawMessage) {
        try {
            Message message = mapper.readValue(rawMessage, Message.class);
            String sendMessage = message.getIp() + ": " + "something wrong with " + message.getMessage();

            String contents = "{ " +
                    "\"app_id\"            : \"" + one_signal_app_id + "\", " +
                    "\"contents\"            : {\"en\" : \"" + sendMessage + "\"}, " +
//                    "\"included_segments\" : [ \"Test\" ] " +
                    "\"include_player_ids\" : [ \"5b1a39e4-5503-11e5-bdee-67e2d97b8c56\" ] " +
                    "}";

            String method = "POST";
            String contentType = "application/json";

            URL u = new URL(URI);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", "" + contents.length());
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Authorization", "Basic MDYxZTBiNmMtNGViYi0xMWU1LTllOTUtMWI4YTZiNGQzMmMw");


            OutputStream os = conn.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);
            wr.writeBytes(contents);
            wr.flush();
            wr.close();

            logger.info(conn.toString());
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
