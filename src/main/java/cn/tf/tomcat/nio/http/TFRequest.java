package cn.tf.tomcat.nio.http;

import java.io.InputStream;

public class TFRequest {

    private String method;
    private String url;


    public TFRequest(InputStream is){
        try{
            String content = "";
            byte[] buff = new byte[1024];
            int len = 0;
            if((len = is.read(buff))>0){
                content = new String(buff,0,len);
            }
            String line = content.split("\\n")[0];
            String[] arr = line.split("\\s");
            this.method =arr[0];
            this.url = arr[1].split("\\?")[0];
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
