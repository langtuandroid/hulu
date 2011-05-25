import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class SimplestServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

//        server.setHandler(new HelloHandler());    //for test

//        ResourceHandler publicDocs = new ResourceHandler();
//        publicDocs.setResourceBase("c:/xampp/apache/htdocs"); //www, htdocs
//        String webappPath = "C:/Users/ADMIN/Downloads/Compressed/jetty-distribution-7.4.1.v20110513/webapps/test.war";
//        String contextPath = "/webapp";
//        WebAppContext webapp = new WebAppContext(webappPath, contextPath);
//        HandlerList hl = new HandlerList();
//        hl.setHandlers(new Handler[] { publicDocs, webapp });
//        server.setHandler(hl);
        
        ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context0.setContextPath("/ctx0");
        context0.addServlet(new ServletHolder(new HelloServlet()),"/*");
        context0.addServlet(new ServletHolder(new HelloServlet("Buongiorno Mondo")),"/it/*");
        context0.addServlet(new ServletHolder(new HelloServlet("Bonjour le Monde")),"/fr/*");
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context0 });
        server.setHandler(contexts);
        
        server.start();
        server.join();
    }
}
