//
// Generated By:JAX-WS RI IBM 2.2.1-11/28/2011 08:28 AM(foreman)- (JAXB RI IBM 2.2.3-11/28/2011 06:21 AM(foreman)-)
//


package com.giga.dms.services;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "GMGDMSServices", targetNamespace = "http://services.dms.giga.com/", wsdlLocation = "META-INF/wsdl/GMGDMSServices.wsdl")
public class GMGDMSServices extends Service
{
	
    private final static URL GMGDMSSERVICES_WSDL_LOCATION;
    private final static WebServiceException GMGDMSSERVICES_EXCEPTION;
    private final static QName GMGDMSSERVICES_QNAME = new QName("http://services.dms.giga.com/", "GMGDMSServices");

    static {
            GMGDMSSERVICES_WSDL_LOCATION = com.giga.dms.services.GMGDMSServices.class.getResource("/META-INF/wsdl/GMGDMSServices.wsdl");
        WebServiceException e = null;
        if (GMGDMSSERVICES_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/wsdl/GMGDMSServices.wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        GMGDMSSERVICES_EXCEPTION = e;
    }

    public GMGDMSServices() {
        super(__getWsdlLocation(), GMGDMSSERVICES_QNAME);
    }

    public GMGDMSServices(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns DMSServices
     */
    @WebEndpoint(name = "DMSServicesImplPort")
    public DMSServices getDMSServicesImplPort() {
        return super.getPort(new QName("http://services.dms.giga.com/", "DMSServicesImplPort"), DMSServices.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DMSServices
     */
    @WebEndpoint(name = "DMSServicesImplPort")
    public DMSServices getDMSServicesImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://services.dms.giga.com/", "DMSServicesImplPort"), DMSServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (GMGDMSSERVICES_EXCEPTION!= null) {
            throw GMGDMSSERVICES_EXCEPTION;
        }
        return GMGDMSSERVICES_WSDL_LOCATION;
    }

}