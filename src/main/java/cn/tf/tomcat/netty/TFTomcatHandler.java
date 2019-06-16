package cn.tf.tomcat.netty;

import cn.tf.tomcat.netty.http.TFRequest;
import cn.tf.tomcat.netty.http.TFResponse;
import cn.tf.tomcat.netty.http.TFServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

public class TFTomcatHandler extends ChannelInboundHandlerAdapter {
    private Map<String, TFServlet> servletMapping = new HashMap<String,TFServlet>();


    public TFTomcatHandler(Map<String, TFServlet> servletMapping) {
        this.servletMapping = servletMapping;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest req = (HttpRequest) msg;

            TFRequest request = new TFRequest(ctx,req);
            TFResponse response = new TFResponse(ctx,req);
            // 实际业务处理
            String url = request.getUrl();

            if(servletMapping.containsKey(url)){
                servletMapping.get(url).service(request, response);
            }else{
                response.write("this "+url+"404 Not Found");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
