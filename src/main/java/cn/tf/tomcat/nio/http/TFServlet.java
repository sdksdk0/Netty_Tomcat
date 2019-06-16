package cn.tf.tomcat.nio.http;

public abstract  class TFServlet {

    public void service(TFRequest request, TFResponse response) throws Exception {
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }

    }

    protected abstract void doPost(TFRequest request, TFResponse response) throws Exception;


    protected abstract void doGet(TFRequest request, TFResponse response) throws Exception;
    
    


}
