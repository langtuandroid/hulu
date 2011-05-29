import java.io.FileInputStream;

import org.cometd.java.annotation.AnnotationCometdServlet;
import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class SimplestServer {
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		
        XmlConfiguration configuration = new XmlConfiguration(new FileInputStream("jetty.xml"));
        configuration.configure(server);
        
//		QueuedThreadPool qtp = new QueuedThreadPool();
//		qtp.setMinThreads(5);
//		qtp.setMaxThreads(200);
//		server.setThreadPool(qtp);

		// server.setHandler(new HelloHandler()); //for test

		// ResourceHandler publicDocs = new ResourceHandler();
		// publicDocs.setResourceBase("c:/xampp/apache/htdocs"); //www, htdocs
		// String webappPath =
		// "C:/Users/ADMIN/Downloads/Compressed/jetty-distribution-7.4.1.v20110513/webapps/test.war";
		// String contextPath = "/webapp";
		// WebAppContext webapp = new WebAppContext(webappPath, contextPath);
		// HandlerList hl = new HandlerList();
		// hl.setHandlers(new Handler[] { publicDocs, webapp });
		// server.setHandler(hl);

		// ServletContextHandler context0 = new
		// ServletContextHandler(ServletContextHandler.SESSIONS);
		// context0.setContextPath("/ctx0");
		// context0.addServlet(new ServletHolder(new HelloServlet()),"/*");
		// context0.addServlet(new ServletHolder(new
		// HelloServlet("Buongiorno Mondo")),"/it/*");
		// context0.addServlet(new ServletHolder(new
		// HelloServlet("Bonjour le Monde")),"/fr/*");
		// ContextHandlerCollection contexts = new ContextHandlerCollection();
		// contexts.setHandlers(new Handler[] { context0 });
		// server.setHandler(contexts);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		server.setHandler(contexts);
		ServletContextHandler context = new ServletContextHandler(contexts,
				"/", ServletContextHandler.SESSIONS);

		ServletHolder dftServlet = context
				.addServlet(DefaultServlet.class, "/");
		dftServlet.setInitOrder(1);

		// Cometd servlet
		CometdServlet cometdServlet = new AnnotationCometdServlet();
		ServletHolder comet = new ServletHolder(cometdServlet);
		context.addServlet(comet, "/cometd/*");
		comet.setInitParameter("timeout", "20000");
		comet.setInitParameter("interval", "100");
		comet.setInitParameter("maxInterval", "10000");
		comet.setInitParameter("multiFrameInterval", "5000");
		comet.setInitParameter("logLevel", "1");
//		comet.setInitParameter("services", "org.cometd.examples.ChatService");
//		comet.setInitParameter("transports", "org.cometd.server.websocket.WebSocketTransport");
		comet.setInitOrder(2);

		ServletHolder demo = context.addServlet(CometdDemoServlet.class,
				"/demo");
		demo.setInitOrder(3);

		server.start();
		server.join();
	}
}
