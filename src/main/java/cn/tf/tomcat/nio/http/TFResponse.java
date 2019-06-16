package cn.tf.tomcat.nio.http;

import java.io.IOException;
import java.io.OutputStream;

public class TFResponse {

    private OutputStream out;

    public TFResponse(OutputStream os){
        this.out= os;
    }

    public void write(String context) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(context);
        out.write(sb.toString().getBytes());
    }
}
