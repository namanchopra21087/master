package com.giga.dms.services;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;

public class DMSServicesImplPortProxy{

    protected Descriptor _descriptor;

    public class Descriptor {
        private com.giga.dms.services.GMGDMSServices _service = null;
        private com.giga.dms.services.DMSServices _proxy = null;
        private Dispatch<Source> _dispatch = null;

        public Descriptor() {
            init();
        }

        public Descriptor(URL wsdlLocation, QName serviceName) {
            _service = new com.giga.dms.services.GMGDMSServices(wsdlLocation, serviceName);
            initCommon();
        }

        public void init() {
            _service = null;
            _proxy = null;
            _dispatch = null;
            _service = new com.giga.dms.services.GMGDMSServices();
            initCommon();
        }

        private void initCommon() {
            _proxy = _service.getDMSServicesImplPort();
        }

        public com.giga.dms.services.DMSServices getProxy() {
            return _proxy;
        }

        public Dispatch<Source> getDispatch() {
            if (_dispatch == null ) {
                QName portQName = new QName("http://services.dms.giga.com/", "DMSServicesImplPort");
                _dispatch = _service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

                String proxyEndpointUrl = getEndpoint();
                BindingProvider bp = (BindingProvider) _dispatch;
                String dispatchEndpointUrl = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
                if (!dispatchEndpointUrl.equals(proxyEndpointUrl))
                    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, proxyEndpointUrl);
            }
            return _dispatch;
        }

        public String getEndpoint() {
            BindingProvider bp = (BindingProvider) _proxy;
            return (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }

        public void setEndpoint(String endpointUrl) {
            BindingProvider bp = (BindingProvider) _proxy;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);

            if (_dispatch != null ) {
                bp = (BindingProvider) _dispatch;
                bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
            }
        }

        public void setMTOMEnabled(boolean enable) {
            SOAPBinding binding = (SOAPBinding) ((BindingProvider) _proxy).getBinding();
            binding.setMTOMEnabled(enable);
        }
    }

    public DMSServicesImplPortProxy() {
        _descriptor = new Descriptor();
        _descriptor.setMTOMEnabled(false);
    }

    public DMSServicesImplPortProxy(URL wsdlLocation, QName serviceName) {
        _descriptor = new Descriptor(wsdlLocation, serviceName);
        _descriptor.setMTOMEnabled(false);
    }

    public Descriptor _getDescriptor() {
        return _descriptor;
    }

    public List<DocumentVO> searchDocuments(DocumentVO document) throws ServiceException_Exception {
        return _getDescriptor().getProxy().searchDocuments(document);
    }

    public List<DocumentVO> uploadDocuments(List<DocumentVO> documentCollection) throws ServiceException_Exception {
        return _getDescriptor().getProxy().uploadDocuments(documentCollection);
    }

    public DocumentVO viewDocument(DocumentVO document) throws IOException_Exception, ServiceException_Exception {
        return _getDescriptor().getProxy().viewDocument(document);
    }

    public String deleteDocument(DocumentVO document) throws ServiceException_Exception {
        return _getDescriptor().getProxy().deleteDocument(document);
    }

    public List<DocumentVO> downloadDocuments(List<DocumentVO> documentCollection) throws ServiceException_Exception {
        return _getDescriptor().getProxy().downloadDocuments(documentCollection);
    }

    public byte[] mergeDocuments(DocumentVO document) throws ServiceException_Exception {
        return _getDescriptor().getProxy().mergeDocuments(document);
    }

}