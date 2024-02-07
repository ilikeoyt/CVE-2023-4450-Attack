import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class GetBytes {
    public String getbytes(Class<?> clazz) {
        try {
            // 获取类的字节码
            InputStream inputStream = clazz.getResourceAsStream(clazz.getSimpleName() + ".class");

            // 检查输入流是否为null，如果为null则资源未找到
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + clazz.getSimpleName() + ".class");
            }

            // 读取输入流中的字节并存储到字节数组中
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] bytecode = buffer.toByteArray();

            // 将字节码进行 Base64 编码
            String base64Encoded = Base64.getEncoder().encodeToString(bytecode);

            // 输出 Base64 编码后的字节码
            return base64Encoded;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生异常";
        }
    }
}

