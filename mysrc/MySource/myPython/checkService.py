import wlstModule

from com.bea.wli.sb.management.configuration import SessionManagementMBean
from com.bea.wli.sb.management.configuration import ALSBConfigurationMBean
from com.bea.wli.config import Ref
from java.lang import String

admin_server="govwls1d.gov.dnvr"
admin_server_port="7604"
user="OSBdDevsDeployer"
pword="Rubidium37Rb"
domain="OSBDomain"

connect(user,pword,'t3://'+admin_server+':'+admin_server_port)
domainRuntime()
sessionName = String("SessionScript"+Long(System.currentTimeMillis()).toString())
print('....SessionName is: ',sessionName)

sessionBean = findService(SessionManagementMBean.NAME,SessionManagementMBean.TYPE)
print('...SessionBean is: ',sessionBean)
sessionBean.createSession(sessionName)
print(String('...Session was created ...').concat(sessionName))


