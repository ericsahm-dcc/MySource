
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;

import com.bea.wli.sb.management.configuration.ALSBConfigurationMBean;
import com.bea.wli.sb.management.configuration.SessionManagementMBean;
import com.bea.wli.sb.management.configuration.ProxyServiceConfigurationMBean;

import com.bea.wli.sb.util.Refs;
import com.bea.wli.config.Ref;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;


public class MBeanManager {

    private static MBeanServerConnection connection;
    private static JMXConnector jmxConn;
    private static SessionManagementMBean sessionBean;

    public static void main(String[] args) {
       //connect("OSBdDevDeployer","Rubidium37Rb","govwls1d.gov.dnvr:7604")
       //
       try{
	  DisableOSBProxyService("DCCRouteCourtCaseESB","RouteCourtCaseKeyProxyService");
	}catch(Exception exp){
	   System.out.println(exp.getMessage());
	}

    }

    public static void DisableOSBProxyService(String ProjectName, String ProxyServiceName) throws IOException {

        try{
            jmxConn = initConnection("govwls1d.gov.dnvr",7604,"OSBdDevsDeployer","Rubidium37Rb");
            System.out.println("JMX Connection Successful");
            
            MBeanServerConnection mbeanConn = jmxConn.getMBeanServerConnection();
            System.out.println("MBean Connection Successful");
            
            DomainRuntimeServiceMBean domainMBean = (DomainRuntimeServiceMBean) MBeanServerInvocationHandler.newProxyInstance(mbeanConn, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
            System.out.println("DomainMBean Created"); 
           
	    sessionBean = (SessionManagementMBean) domainMBean.findService(SessionManagementMBean.NAME,SessionManagementMBean.TYPE, null);
	    System.out.println("SessionMBean Created");

	    if (!sessionBean.sessionExists("mySession")){ 
	        sessionBean.createSession("mySession");
		System.out.println("new mySession");
	    }
	    System.out.println("mySesson Bean Created");


            ProxyServiceConfigurationMBean proxyMBean = (ProxyServiceConfigurationMBean) domainMBean.findService(ProxyServiceConfigurationMBean.NAME+"."+"mysession", "ProxyServiceConfigurationMBean", null);
            System.out.println("ProxyBean Created");
            
            Ref projectName = Refs.makeParentRef("DCCRouteCourtCaseEBS/");
            
            Ref proxyRef = Refs.makeProxyRef(projectName, "RouteCourtCaseKeyProxyService");
            System.out.println("Proxy Ref Created");
	    System.out.println(proxyRef.getFullName());

	    if (proxyMBean.isEnabled(proxyRef)){
                proxyMBean.disableService(proxyRef);
                System.out.println("Disabled the Proxy Service");
	    }

            sessionBean.discardSession("mySession");
	    System.out.println("Session Discarded");
	    jmxConn.close();
	    System.out.println("JMX Connection Closed");
        }catch(Exception exp){
           	System.out.println("Exception Occurred: "); 
		System.out.println(exp.getMessage());	
		jmxConn.close();
        }
    }
 
 /*   
    public static void initConnection(String hostname, String portString, String username, String password) throws IOException, MalformedURLException {

         String protocol = "t3";
         Integer portInteger = Integer.valueOf(portString);
         int port = portInteger.intValue();
         String jndiroot = "/jndi/";
         String mserver = "weblogic.management.mbeanservers.domainruntime";
         JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,
         jndiroot + mserver);

         Hashtable h = new Hashtable();
         h.put(Context.SECURITY_PRINCIPAL, username);
         h.put(Context.SECURITY_CREDENTIALS, password);
         h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,"weblogic.management.remote");
         h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
         connector = JMXConnectorFactory.connect(serviceURL, h);
         connection = connector.getMBeanServerConnection();
      }
 */   

    public static JMXConnector initConnection(String hostname, int port, String username, String password) throws IOException,MalformedURLException{
      JMXServiceURL serviceURL = new JMXServiceURL("t3", hostname, port, "/jndi/" + DomainRuntimeServiceMBean.MBEANSERVER_JNDI_NAME);

      Hashtable<String, String> h = new Hashtable<String, String>();
      h.put(Context.SECURITY_PRINCIPAL, username);
      h.put(Context.SECURITY_CREDENTIALS, password);
      h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
      
      return JMXConnectorFactory.connect(serviceURL, h);
      
    }
    
}
