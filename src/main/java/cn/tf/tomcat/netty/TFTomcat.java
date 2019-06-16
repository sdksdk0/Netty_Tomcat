package cn.tf.tomcat.netty;


import cn.tf.tomcat.netty.http.TFServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Netty版本的tomcat
 *
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
            FileInputStream fis = new FileInputStream(WEB_INF+"netty_web.properties");
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

    private void run(){
        //Netty封装了NIO，Reactor模型，Boss，worker
        // Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{

            ServerBootstrap server = new ServerBootstrap();
            //链式编程
            server.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class) //// 主线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() { //子线程处理类

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 编码器
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            //解码器
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            socketChannel.pipeline().addLast(new TFTomcatHandler(servletMapping));
                        }
                    })
            .option(ChannelOption.SO_BACKLOG,128) // 针对主线程的配置 分配线程最大数量 128
            .childOption(ChannelOption.SO_KEEPALIVE,true); // 针对子线程的配置 保持长连接

            ChannelFuture f = server.bind(port).sync();
            System.out.println("Tomcat启动成功,端口是:"+this.port);
            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void start() {
        init();
        run();
    }

    public static void main(String[] args) {
        new TFTomcat().start();
    }

}
