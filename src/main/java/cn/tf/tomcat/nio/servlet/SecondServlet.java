package cn.tf.tomcat.nio.servlet;

import cn.tf.tomcat.nio.http.TFRequest;
import cn.tf.tomcat.nio.http.TFResponse;
import cn.tf.tomcat.nio.http.TFServlet;

public class SecondServlet extends TFServlet {

    public void doGet(TFRequest request, TFResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(TFRequest request, TFResponse response) throws Exception {
        response.write("This is Second Serlvet");
    }

}
