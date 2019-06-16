package cn.tf.tomcat.netty.servlet;

import cn.tf.tomcat.netty.http.TFRequest;
import cn.tf.tomcat.netty.http.TFResponse;
import cn.tf.tomcat.netty.http.TFServlet;

public class SecondServlet extends TFServlet {

    public void doGet(TFRequest request, TFResponse response) throws Exception {
        this.doPost(request, response);
    }

    public void doPost(TFRequest request, TFResponse response) throws Exception {
        response.write("This is Second Serlvet");
    }

}
