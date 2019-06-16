package cn.tf.tomcat.nio;

import cn.tf.tomcat.nio.http.TFRequest;
import cn.tf.tomcat.nio.http.TFResponse;
import cn.tf.tomcat.nio.http.TFServlet;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** 基于NIO版本的tomcat
 *
 *     //1、配置好启动端口
 *     //2、读取web.xml
 *     //3、读取配置,url-pattern
 *     //4、http请求
 *     //5、从协议中拿到url
 *     //6、调用实例化对象的service方法，执行具体的doGet/doPost方法
 *     //7、request/response
 *
 */
public class TFTomcat {

    private int port = 8080;
    private ServerSocket server;
    private Map<String, TFServlet> servletMapping = new HashMap<String,TFServlet>();

    private Properties webProperties = new Properties();

    //加载web.Properties文件,同时初始化 ServletMapping对象
    private void init(){
        try{
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF+"web.properties");
            webProperties.load(fis);
            for(Object k :webProperties.keySet()){
                String key = k.toString();
                if(key.endsWith(".url")){
                    String servletName = key.replaceAll("\\.url$","");
                    String url = webProperties.getProperty(key);
                    String className = webProperties.getProperty(servletName+".className");
                    TFServlet obj = (TFServlet) Class.forName(className).newInstance();
                    servletMapping.put(url,obj);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start(){
        init();
        try {
            server = new ServerSocket(this.port);
            System.out.println("Tomcat启动成功,端口是:"+this.port);
            while(true){
                Socket client = server.accept();
                process(client);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void process(Socket client) throws Exception {
        InputStream is = client.getInputStream();
        OutputStream os =client.getOutputStream();
        TFRequest request = new TFRequest(is);
        TFResponse response = new TFResponse(os);
        String url = request.getUrl();
        if(servletMapping.containsKey(url)){
            servletMapping.get(url).service(request,response);
        }else{
            response.write("this "+url+"404 Not Found");
        }
        os.flush();
        os.close();
        is.close();
        client.close();

    }

    public static void main(String[] args) {
        new TFTomcat().start();
    }

}
