package Command_execution;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Exp {
    public static String sendPostRequest(String url, String postData) throws IOException {
        // 创建URL对象
        URL obj = new URL(url);

        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // 设置请求方法为POST
        connection.setRequestMethod("POST");

        // 启用输入输出流
        connection.setDoOutput(true);

        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/json");

        // 获取输出流，将参数写入请求体
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
            wr.write(postDataBytes);
        }

        // 获取响应代码
        int responseCode = connection.getResponseCode();
        System.out.println("响应代码: " + responseCode);

        // 读取服务器响应
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }
}
