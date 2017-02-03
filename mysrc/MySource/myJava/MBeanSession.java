import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.jmx.MBeanServerInvocationHandler;
import com.bea.wli.sb.management.configuration.ALSBConfigurationMBean;
import com.bea.wli.sb.management.configuration.SessionManagementMBean;

public class MBeanSesssion {

static public void main(String[] args) throws Exception {

// get the jmx connector
     JMXConnector conn = initConnection("localhost", 7001, "weblogic", "weblogic");
  
// get mbean connection
     MBeanServerConnection mbconn = conn.getMBeanServerConnection();
  
// get domain service mbean. This is the topmost mbean
     DomainRuntimeServiceMBean domainService = (DomainRuntimeServiceMBean) MBeanServerInvocationHandler.newProxyInstance(mbconn, new ObjectName(DomainRuntimeServiceMBean.OBJECT_NAME));
  
  
// obtain session management mbean to create a session.
// This mbean instance can be used more than once to
// create/discard/commit many sessions
     SessionManagementMBean sm = (SessionManagementMBean) domainService.findService(SessionManagementMBean.NAME,SessionManagementMBean.TYPE, null);
 
 // create a session
    sm.createSession("mysession");

 // obtain the ALSBConfigurationMBean instance that operates on the session that has
 // just been created. Notice that the name of the mbean contains the session name.
    ALSBConfigurationMBean alsbSession = (ALSBConfigurationMBean) domainService.findService(ALSBConfigurationMBean.NAME + "." + "mysession",ALSBConfigurationMBean.TYPE, null);

// Perform updates or read operations in the session using alsbSession


// activate changes performed in the session
   sm.activateSession("mysession", "description");
 

// Obtain MBean for peforming read only operations. Notice that the name
// of the mbean for the core data does not contain any session name.
   ALSBConfigurationMBean alsbCore = (ALSBConfigurationMBean) domainService.findService(ALSBConfigurationMBean.NAME, ALSBConfigurationMBean.TYPE, null);


// Perform read-only operations on core data using alsbCore


     conn.close();
 }
 


    public static JMXConnector initConnection(String hostname, int port, String username, String password) throws IOException,MalformedURLException {
                                                                                                                                                                     JMXServiceURL serviceURL = new JMXServiceURL("t3", hostname, port, "/jndi/" + DomainRuntimeServiceMBean.MBEANSERVER_JNDI_NAME);
                                                                                                                                                                     Hashtable<String, String> h = new Hashtable<String, String>();
                                                                                                                                                                     h.put(Context.SECURITY_PRINCIPAL, username);
                                                                                                                                                                     h.put(Context.SECURITY_CREDENTIALS, password);
                                                                                                                                                                     h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
                                                                                                                                                                     return JMXConnectorFactory.connect(serviceURL, h);
                                                                                                                                                                }
                                                                                                                                                             }

