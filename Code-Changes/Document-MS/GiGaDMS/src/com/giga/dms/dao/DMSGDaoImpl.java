package com.giga.dms.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.giga.base.HibernateUtil;
import com.giga.base.LoggerUtility;
import com.giga.dms.dao.domain.DmsDocumentAuditHistory;
import com.giga.dms.dao.domain.DmsUserAccess;
import com.giga.dms.services.AESencrp;
import com.giga.dms.util.DMSGConstants;
import com.giga.dms.util.DMSGType;
import com.giga.dms.vo.DMSDocumentAuditHistoryVO;
import com.giga.dms.vo.DMSResultObject;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.exception.DataAccessException;
import com.giga.master.dao.domain.Company;
import com.giga.master.dao.domain.DocumentType;
import com.giga.master.dao.domain.MasterSetup;
import com.giga.ofs.dao.domain.Booking;
import com.giga.ofs.dao.domain.BookingDetail;
import com.giga.ofs.dao.domain.Claim;
import com.giga.ofs.dao.domain.CreditDebitNote;
import com.giga.ofs.dao.domain.Invoice;
import com.giga.ofs.dao.domain.Job;
import com.giga.ofs.dao.domain.PurchaseOrder;
import com.giga.ofs.dao.domain.Quotation;
import com.giga.ofs.dao.domain.ShippingInstruction;
import com.giga.ofs.dao.domain.ShippingSchedule;
import com.giga.tms.dao.domain.TmsBooking;
import com.giga.tms.dao.domain.TmsBookingCommonServiceDetail;
import com.giga.tms.dao.domain.TmsBookingLoadDetail;
import com.giga.tms.dao.domain.TmsCreditDebitNote;
import com.giga.tms.dao.domain.TmsDeliveryOrder;
import com.giga.tms.dao.domain.TmsInsuranceClaim;
import com.giga.tms.dao.domain.TmsInvoice;
import com.giga.tms.dao.domain.TmsLoadPlanning;
import com.giga.tms.dao.domain.TmsPurchaseOrder;
import com.giga.tms.dao.domain.TmsQuotation;
import com.giga.util.DateUtil;
import com.giga.yms.dao.domain.YmsBooking;
import com.giga.yms.dao.domain.YmsCreditDebitNote;
import com.giga.yms.dao.domain.YmsDeliveryNote;
import com.giga.yms.dao.domain.YmsInsuranceClaim;
import com.giga.yms.dao.domain.YmsInvoice;
import com.giga.yms.dao.domain.YmsPullingOrder;
import com.giga.yms.dao.domain.YmsPurchaseOrder;
import com.giga.yms.dao.domain.YmsQuotation;
import com.giga.yms.dao.domain.YmsShipment;


public class DMSGDaoImpl  implements DMSGDao{
	
	@SuppressWarnings("static-access")
	private static Logger logger = LoggerUtility.getInstance().getLogger(DMSGDaoImpl.class); 
	private static final String DMS_DOC_LINE_TYPE="DOCUMENT LINE";
	//"GS","FF","NT","GC","CU","VR"
		private static String COMPANY_CODE_GIGA="GS";
		private static String COMPANY_CODE_NEXUS="FF";
		private static String COMPANY_CODE_NMC="NT";
		private static String COMPANY_CODE_GCT="GC";
		private static String COMPANY_CODE_CUSTOMER="CU";
		private static String COMPANY_CODE_VENDOR="VR";
		private static String DMS_ACC_USER_NAME="DMS_ACC_USER_NAME";
		private static String DMS_ACC_USER_PASS="DMS_ACC_USER_PASS";
		private static String DMS_ACC_URL="DMS_ACC_URL";
	@Override
	public Map<String, String> loadDocumentLine(String systemLine)
			throws DataAccessException {
		 
		
		if(systemLine!=null?systemLine.isEmpty()?true:false:true){
			return new HashMap<String,String>();
		}
		
		Session session = null;
		Map<String,String> documentLineMap=null;
		try {
			session = HibernateUtil.getSession();
			documentLineMap=new HashMap<String,String>();
			Criteria criteria = session.createCriteria(MasterSetup.class,"ms");
			criteria.createAlias("ms.masterType", "mt",CriteriaSpecification.INNER_JOIN);
			criteria.add(Restrictions.eq("mt.typeDescription",DMS_DOC_LINE_TYPE));

			/*criteria.setProjection(Projections.projectionList()
					.add(Projections.property("ms.masterId"))
					.add(Projections.property("ms.setupCode"))
					.add(Projections.property("ms.setupDescription")));*/
			@SuppressWarnings("unchecked")
			List<MasterSetup> msList=criteria.list();
			 if(msList!=null?!msList.isEmpty()?true:false:false){
				 for(MasterSetup ms: msList){
					 String masterId=ms.getMasterId()!=null?String.valueOf(ms.getMasterId()).trim():"";
					 String setupCode=ms.getSetupCode()!=null?String.valueOf(ms.getSetupCode()).trim():"";
					 String setupDesc=ms.getSetupDescription()!=null?String.valueOf(ms.getSetupDescription()).trim():"";
					 String mapKey=masterId+":"+setupCode;
					 documentLineMap.put(mapKey, setupDesc);
				 }
			 }
			
			
		} catch (HibernateException he) {
			he.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
			logger.debug("DMADao:loadDocumentLine:"+documentLineMap);
		}
		return documentLineMap;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, String> loadDocumentType(Map<String,String> docTypeDetailsMap)
			throws DataAccessException {
		
		Session session = null;
		Map<String,String> documentTypeMap=null;
		 Map<String,String> docMapTypeOrder=new HashMap<String,String>();
		if(docTypeDetailsMap!=null?docTypeDetailsMap.isEmpty()?true:false:true){
			return new HashMap<String,String>();
		}
		 String systemLine=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_SYSTEMLINE);
		 String documentLine=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_DOCLINE);
		 String comp_Group_code=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_COMPANY_GROUP_CODE);
		 String comp_code=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_USER_COMPANY_CODE);
		 logger.debug("DMSDaoImpl ::loadDocumentType(-) :: docTypeDetailsMap  : "+docTypeDetailsMap);
		 if(!nullEmptyCheckerStringFlag(systemLine)&&!nullEmptyCheckerStringFlag(documentLine)){
			 logger.debug("DMSDaoImpl ::loadDocumentType(-) :: comp_code code is empty due to that, values list of documentsType fetching failed..  : ");
			 return new HashMap<String,String>();
		 }
		try {
			//String companyCodeMaster=getCompanyNameOfMaster(comp_code,comp_Group_code);
			 if(!nullEmptyCheckerStringFlag(comp_code)){
				 return new HashMap<String,String>();
			 }
			session = HibernateUtil.getSession();
			documentTypeMap=new LinkedHashMap<String,String>();
			Criteria criteria = session.createCriteria(DocumentType.class,"dt");
			criteria.createAlias("dt.masterSetupBySystemLineMasterId", "msSysLine",CriteriaSpecification.INNER_JOIN);
			criteria.createAlias("dt.masterSetupByDocumentLineMasterId", "msDocLine",CriteriaSpecification.INNER_JOIN);
			if(nullEmptyCheckerStringFlag(systemLine)){
				   criteria.add(Restrictions.eq("msSysLine.setupDescription",systemLine.trim()));
			}
			if(nullEmptyCheckerStringFlag(documentLine)){
				   criteria.add(Restrictions.eq("msDocLine.setupCode",documentLine.trim()));
			}
			//COMPANY_CODE_GIGA,COMPANY_CODE_NEXUS,COMPANY_CODE_NMC,COMPANY_CODE_GCT,COMPANY_CODE_CUSTOMER,COMPANY_CODE_VENDOR
			if(nullEmptyCheckerStringFlag(comp_code)){
				 if(comp_code.trim().equals(COMPANY_CODE_GIGA))
					 criteria.add(Restrictions.eq("dt.isGigaSearch",true));
				 if(comp_code.trim().equals(COMPANY_CODE_NEXUS))
					 criteria.add(Restrictions.eq("dt.isNexusSearch",true));
				 if(comp_code.trim().equals(COMPANY_CODE_NMC))
					 criteria.add(Restrictions.eq("dt.isNmcSearch",true));
				 if(comp_code.trim().equals(COMPANY_CODE_GCT))
					 criteria.add(Restrictions.eq("dt.isGctSearch",true));
				 if(comp_code.trim().equals(COMPANY_CODE_CUSTOMER))
				    criteria.add(Restrictions.eq("dt.isCustomerSearch",true));
				 if(comp_code.trim().equals(COMPANY_CODE_VENDOR))
					 criteria.add(Restrictions.eq("dt.isVendorSearch",true));
				 
			}
			logger.debug("DMSDaoImpl ::loadDocumentType(-) :11");
			List<DocumentType> docType=criteria.list();
			 if(docType!=null?!docType.isEmpty()?true:false:false){
				 for(DocumentType dt: docType){
					 String docTypeId=String.valueOf(dt.getDocumentTypeId());
					 String docTypeDesc=dt.getDocumentType()!=null?String.valueOf(dt.getDocumentType()).trim():"";
					 documentTypeMap.put(docTypeId, docTypeDesc);
				 }
			 }
			
			 docMapTypeOrder=getMasAsOrder(documentTypeMap);
			
		} catch (HibernateException he) {
			he.printStackTrace();
			logger.error("DMSDaoImpl ::loadDocumentType(-): HibernateException: "+he);
		} catch (Exception e) {
			logger.error("DMSDaoImpl ::loadDocumentType(-): HibernateException: "+e);
		} finally {
			HibernateUtil.closeSession(session);
			 logger.debug("DMSDaoImpl ::loadDocumentType(-) ::"+documentTypeMap);
		}
		return docMapTypeOrder;
	}
	/*
    private String getCompanyNameOfMaster(String companyCode,String compBranchCode) throws Exception{
         Session session = null;
         String companyName="";
         String companyCodeMaster="";
        if(!nullEmptyCheckerStringFlag(companyCode))
        {
        	throw new Exception("COMPANY_CODE_NOT_FOUND_ERROR",new Exception());
        }
    try {
    	   session=HibernateUtil.getSession();
    	   
    	   MasterSetup masterSetupEntity=(MasterSetup)session.createCriteria(MasterSetup.class,"ms")
    			   .createAlias("ms.masterType", "mt",CriteriaSpecification.INNER_JOIN).add(Restrictions.eq("mt.typeDescription", "COMPANY CODE"))
    	   			.add(Restrictions.eq("ms.setupCode", valueNullEmptyChecker(companyCode))).uniqueResult();
    	   
    	   if(masterSetupEntity!=null){
				companyName=valueNullEmptyChecker(masterSetupEntity.getSetupDescription());
				companyCodeMaster=valueNullEmptyChecker(masterSetupEntity.getSetupCode());
			}
    		logger.debug("Usage :: DMSDAOImpl : getCompanyNameOfMaster :companyCodeMaster:"+companyCodeMaster+"-- companyName :"+ companyName);
			return companyCodeMaster;
		 } catch (HibernateException he) {
			he.printStackTrace();
			logger.error("DMSDaoImpl ::getCompanyNameOfMaster(-): HibernateException: "+he);
			throw new Exception("COMPANY_CODE_FETCH_ERROR",new Exception());
		} catch (Exception e) {
			logger.error("DMSDaoImpl ::getCompanyNameOfMaster(-): Exception: "+e);
			throw new Exception("COMPANY_CODE_FETCH_ERROR",new Exception());
		} finally {
			HibernateUtil.closeSession(session);
			 
		}
    	
    }*/
    
    private String getCompanyIDByBCodeAndCCode(String companyCode,String compBranchCode) throws Exception{
        Session session = null;
        String companyName=null;
        String companyCodeMaster=null;
        String companyID=null;
       if(!nullEmptyCheckerStringFlag(companyCode)&&!nullEmptyCheckerStringFlag(compBranchCode))
       {
       	throw new Exception("COMPANY_CODE_AND_BRANCH_CODE_NOT_FOUND_ERROR",new Exception());
       }
   try {
   	   session=HibernateUtil.getSession();
   	   
   	 
   	  Criteria cri=session.createCriteria(Company.class,"comp")
   			   		.createAlias("comp.masterSetupByCompanyMasterId", "ms",CriteriaSpecification.INNER_JOIN)
   			   		.createAlias("ms.masterType", "mt").
   			   		add(Restrictions.eq("mt.typeDescription", "COMPANY CODE"))
   			   		.add(Restrictions.eq("ms.setupCode", valueNullEmptyChecker(companyCode)));
   	    if(nullEmptyCheckerStringFlag(compBranchCode)){
   	    	         cri.add(Restrictions.eq("comp.branchCode", valueNullEmptyChecker(compBranchCode)));
   	    }
   	     
   	     Company companyEntity=(Company)cri.uniqueResult();		
   			if(companyEntity!=null?companyEntity.getMasterSetupByCompanyMasterId()!=null?true:false:false){
   				companyName=valueNullEmptyChecker(companyEntity.getMasterSetupByCompanyMasterId().getSetupDescription());
   				companyCodeMaster=valueNullEmptyChecker(companyEntity.getMasterSetupByCompanyMasterId().getSetupCode());
   				companyID=valueNullEmptyChecker(String.valueOf(companyEntity.getCompanyId()));
   				
   			}
			logger.debug("Usage :: DMSDAOImpl : getCompanyIDByBCodeAndCCode :BranchCode :: "+companyEntity.getBranchCode()+"\n companyCodeMaster:"+companyCodeMaster+"-- companyName :"+ companyName + "--companyID :  "+companyID);
			return companyID;
		 } catch (HibernateException he) {
			he.printStackTrace();
			logger.error("DMSDaoImpl ::getCompanyIDByBCodeAndCCode(-): HibernateException: "+he);
			throw new Exception("COMPANY_ID_FETCH_ERROR",new Exception());
		} catch (Exception e) {
			logger.error("DMSDaoImpl ::getCompanyIDByBCodeAndCCode(-): Exception: "+e);
			throw new Exception("COMPANY_ID_FETCH_ERROR",new Exception());
		} finally {
			HibernateUtil.closeSession(session);
			 
		}
   }

    @SuppressWarnings("unchecked")
	@Override
	public Map<String, String> loadDocumentTypeUpload(Map<String,String> docTypeDetailsMap)
			throws DataAccessException {
		
		Session session = null;
		Map<String,String> documentTypeMap=null;
		Map<String,String> docMapTypeOrder=new HashMap<String,String>();
		if(docTypeDetailsMap!=null?docTypeDetailsMap.isEmpty()?true:false:true){
			return new HashMap<String,String>();
		}
		 String systemLine=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_SYSTEMLINE);
		 String documentLine=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_DOCLINE);
		 String comp_Group_code=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_COMPANY_GROUP_CODE);
		 String comp_code=docTypeDetailsMap.get(DMSGConstants.DOCTYPE_USER_COMPANY_CODE);
		 logger.debug("DMSDaoImpl ::loadDocumentTypeUpload(-) :: docTypeDetailsMap  : "+docTypeDetailsMap);
		 if(!nullEmptyCheckerStringFlag(systemLine)&&!nullEmptyCheckerStringFlag(documentLine)){
			 return new HashMap<String,String>();
		 }
		try {
			//String companyCodeMaster=getCompanyNameOfMaster(comp_code,comp_Group_code);
			 if(!nullEmptyCheckerStringFlag(comp_code)){
				 logger.debug("DMSDaoImpl ::loadDocumentTypeUpload(-) :: comp_code code is empty due to that, values list of documentsType fetching failed..  : ");
				 return new HashMap<String,String>();
			 }
			session = HibernateUtil.getSession();
			documentTypeMap=new LinkedHashMap<String,String>();
			Criteria criteria = session.createCriteria(DocumentType.class,"dt");
			criteria.createAlias("dt.masterSetupBySystemLineMasterId", "msSysLine",CriteriaSpecification.INNER_JOIN);
			criteria.createAlias("dt.masterSetupByDocumentLineMasterId", "msDocLine",CriteriaSpecification.INNER_JOIN);
			if(nullEmptyCheckerStringFlag(systemLine)){
				   criteria.add(Restrictions.eq("msSysLine.setupDescription",systemLine.trim()));
			}
			if(nullEmptyCheckerStringFlag(documentLine)){
				   criteria.add(Restrictions.eq("msDocLine.setupCode",documentLine.trim()));
			}
			//COMPANY_CODE_GIGA,COMPANY_CODE_NEXUS,COMPANY_CODE_NMC,COMPANY_CODE_GCT,COMPANY_CODE_CUSTOMER,COMPANY_CODE_VENDOR
			if(nullEmptyCheckerStringFlag(comp_code)){
				 if(comp_code.trim().equals(COMPANY_CODE_GIGA))
					 criteria.add(Restrictions.eq("dt.isGigaUpload",true));
				 if(comp_code.trim().equals(COMPANY_CODE_NEXUS))
					 criteria.add(Restrictions.eq("dt.isNexusUpload",true));
				 if(comp_code.trim().equals(COMPANY_CODE_NMC))
					 criteria.add(Restrictions.eq("dt.isNmcUpload",true));
				 if(comp_code.trim().equals(COMPANY_CODE_GCT))
					 criteria.add(Restrictions.eq("dt.isGctUpload",true));
				 if(comp_code.trim().equals(COMPANY_CODE_CUSTOMER))
				    criteria.add(Restrictions.eq("dt.isCustomerUpload",true));
				 if(comp_code.trim().equals(COMPANY_CODE_VENDOR))
					 criteria.add(Restrictions.eq("dt.isVendorUpload",true));
				 
			}			
			
			logger.debug("DMSDaoImpl ::loadDocumentTypeUpload(-) :11");
			@SuppressWarnings("unchecked")
			List<DocumentType> docType=criteria.list();
			 if(docType!=null?!docType.isEmpty()?true:false:false){
				 for(DocumentType dt: docType){
					 String docTypeId=String.valueOf(dt.getDocumentTypeId());
					 String docTypeDesc=dt.getDocumentType()!=null?String.valueOf(dt.getDocumentType()).trim():"";
					 documentTypeMap.put(docTypeId, docTypeDesc);
					 logger.debug("B4 :: "+"Key :"+docTypeId+" Value : "+docTypeDesc);
				 }
			 }
			 docMapTypeOrder=getMasAsOrder(documentTypeMap);			 
		} catch (HibernateException he) {
			he.printStackTrace();
			logger.error("DMSDaoImpl ::loadDocumentTypeUpload(-): HibernateException: "+he);
		} catch (Exception e) {
			logger.error("DMSDaoImpl ::loadDocumentTypeUpload(-): HibernateException: "+e);
		} finally {
			HibernateUtil.closeSession(session);
			 logger.debug("DMSDaoImpl ::loadDocumentTypeUpload(-) ::"+documentTypeMap);
		}
		return docMapTypeOrder;
	}	
	@Override
	public List<DMSSearchVO> getSearchDetails(DMSGType searchType,DMSSearchVO searchParamVO) throws DataAccessException {
		logger.debug("DMSDaoImpl ::getSearchDetails(-,-,-): process Start ::searchType  :"+searchType+"\n ::: DMSSearchVO : "+searchParamVO); 
		try {
			
				if(nullChekerMap(searchParamVO)){
				 logger.debug("DMSDaoImpl ::getSearchDetails(-,-,-): searchParamVO contains Null ,unable to process search :: searchType"+searchType);
					   return new ArrayList<DMSSearchVO>();
				  }else{
					  if(searchParamVO.getSystemLine()==null?true:searchParamVO.getSystemLine().trim().isEmpty()?true:false){
						logger.debug("DMSDaoImpl ::getSearchDetails(-,-,-): getSystemLine isEmpty or Null");
					  return new ArrayList<DMSSearchVO>();  
					 }
				  }
				if(searchType.equals(DMSGType.SEARCH_BOOKING)){
					return searchBookingDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_JOB)){
					return searchJobDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_SI)){
					return searchSiDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_CLAIM)){
					return searchClaimDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_SHIPMENT)){
					return searchShipmentDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_INVOICE)){
					return searchInvoiceDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_QUOTATION)){
					return searchQuotationDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_PO)){
					return searchPoDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_DO)){
					return searchDoDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_LOAD)){
					return searchLoadDetials(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_BOOKING_CUSTOMER)){
					return searchBookingCustomerDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_CREDITNOTE)){
					return searchCreditNoteDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_DEBITNOTE)){
					return searchDebitNoteDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_DELIVERYNOTE)){
					return searchDeliveryNoteDetails(searchParamVO);
				}else
				if(searchType.equals(DMSGType.SEARCH_PULLINGORDER)){
					return searchPullingOrderDetails(searchParamVO);
				}
		} catch (DataAccessException e) {
			
			throw new DataAccessException(e);
		}catch(Exception e){
			throw new DataAccessException(e);
		}
		return null;
	}
	
	

	private List<DMSSearchVO> searchDeliveryNoteDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("searchDeliveryNoteDetails >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*********");
		String systemLine=searchParamVO.getSystemLine().trim();
		 if(systemLine!=null && systemLine.equalsIgnoreCase("YMS")){
				  return searchYmsDeliveryNote(searchParamVO);
			  }
		return null;
		
	}

	private List<DMSSearchVO> searchYmsDeliveryNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchYmsDeliveryNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchYmsDeliveryNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(YmsDeliveryNote.class,"ymsDlNote")
		  		 .createAlias("ymsDlNote.ymsShipment", "ss",CriteriaSpecification.LEFT_JOIN)
		  		 .createAlias("ymsDlNote.company","comp")
					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		  	 		criteria.addOrder(Order.desc("ymsDlNote.delDate"));
		  	 		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getDeliveryNoteNo())){
					   logger.debug("getDeliveryNoteNo : "+searchParamVO.getDeliveryNoteNo());
					   criteria.add(Restrictions.eq("ymsDlNote.delNumber",searchParamVO.getDeliveryNoteNo().trim()));
				   }
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentNo())){
		  			 criteria.add(Restrictions.eq("ss.shipmentNumber",searchParamVO.getShipmentNo().trim()));
		  		   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getDeliveryNoteType())){
					   criteria.createAlias("ymsDlNote.masterSetup","dlType",CriteriaSpecification.INNER_JOIN)
					   .add(Restrictions.ilike("dlType.setupDescription",searchParamVO.getDeliveryNoteType().trim(),MatchMode.ANYWHERE));
				   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getCnDateForm())&&nullEmptyCheckerStringFlag(searchParamVO.getCnDateTo())){
					   logger.debug("SearchYmsDeliveryNote : fromDate :: "+searchParamVO.getDeliveryNoteDateFrom()+
					   		"toDate :: "+searchParamVO.getDeliveryNoteDateTo());
					   criteria.add(Restrictions.between("ymsDlNote.delDate",getUtilDate(searchParamVO.getCnDateForm()), getUtilDateEnd(searchParamVO.getCnDateTo())));
				   }
				   
				   List<YmsDeliveryNote> ymsDelNoteList=criteria.list();
				   if(ymsDelNoteList!=null&&!ymsDelNoteList.isEmpty()){
					   
					   for(YmsDeliveryNote ymsDlNote:ymsDelNoteList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("YMS");
						   dmsVo.setDeliveryNoteNo(valueNullEmptyChecker(ymsDlNote.getDelNumber()));
						   
						   if(ymsDlNote.getYmsShipment()!=null){
							   dmsVo.setShipmentNo(valueNullEmptyChecker(ymsDlNote.getYmsShipment().getShipmentNumber()));
						   }else{
							   dmsVo.setShipmentNo(""); 
						   }
						   dmsVo.setDeliveryNoteType(valueNullEmptyChecker(ymsDlNote.getMasterSetup().getSetupDescription()));
						   dmsVo.setDeliveryNoteDateTo(dateCheckerAsGetString(ymsDlNote.getDelDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsDeliveryNote :  data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsDeliveryNote :  data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsDeliveryNote :  searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	private List<DMSSearchVO> searchPullingOrderDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("searchCreditNoteDetails >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*********");
		String systemLine=searchParamVO.getSystemLine().trim();
		 if(systemLine!=null && systemLine.equalsIgnoreCase("YMS")){
		  return searchYmsPullingOrder(searchParamVO);
		 }
		return null;
		
	}

	private List<DMSSearchVO> searchYmsPullingOrder(DMSSearchVO searchParamVO) {
		
		logger.debug("searchYmsPullingOrder >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchYmsPullingOrder : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(YmsPullingOrder.class,"ymsPlO")
		  		 .createAlias("ymsPlO.customer", "cus",CriteriaSpecification.INNER_JOIN)
		  		 .createAlias("ymsPlO.ymsBooking", "ymsBk",CriteriaSpecification.LEFT_JOIN)
		  		 .createAlias("ymsPlO.company","comp")
					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		  	 		criteria.addOrder(Order.desc("ymsPlO.plDate"));
		  	 		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getPullingOrderNo())){
					   logger.debug("getPullingOrderNo : "+searchParamVO.getPullingOrderNo());
					   criteria.add(Restrictions.eq("ymsPlO.plDate",searchParamVO.getPullingOrderNo().trim()));
				   }
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
		  			 criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getCustomerName().trim(),MatchMode.ANYWHERE));
		  		   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
					   	 criteria.add(Restrictions.eq("ymsBk.customerName",searchParamVO.getBookingNo().trim()));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getPloDateFrom()) && nullEmptyCheckerStringFlag(searchParamVO.getPloDateTo())){
					   logger.debug("getPloDateFrom :fromDate :: "+searchParamVO.getPloDateFrom()+"--toDate :: "+searchParamVO.getPloDateTo());
					   criteria.add(Restrictions.between("ymsPlO.plDate",getUtilDate(searchParamVO.getPloDateFrom()), getUtilDateEnd(searchParamVO.getPloDateTo())));
				   }
				   
				   List<YmsPullingOrder> ymsPloList=criteria.list();
				   if(ymsPloList!=null && !ymsPloList.isEmpty()){
					   
					   for(YmsPullingOrder ymsPlo:ymsPloList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("YMS");
						   dmsVo.setPullingOrderNo(valueNullEmptyChecker(ymsPlo.getPlNumber()));
						   if(ymsPlo.getCustomer()!=null){
							   dmsVo.setCustomerName(valueNullEmptyChecker(ymsPlo.getCustomer().getCustomerName()));
						   }else{
							   dmsVo.setCustomerName(""); 
						   }
						   if(ymsPlo.getYmsBooking()!=null){
							   dmsVo.setBookingNo(valueNullEmptyChecker(ymsPlo.getYmsBooking().getBookingNumber()));
						   }else{
							   dmsVo.setBookingNo(""); 
						   }
						   dmsVo.setPloDateTo(dateCheckerAsGetString(ymsPlo.getPlDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsPullingOrder: data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsPullingOrder : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsPullingOrder : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	private List<DMSSearchVO> searchCreditNoteDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("searchCreditNoteDetails >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*********");
		String systemLine=searchParamVO.getSystemLine().trim();
		  if(systemLine.equalsIgnoreCase("OFS")?true:false){
			  return searchOfsCreditNote(searchParamVO);
		  }else
		  if(systemLine.equalsIgnoreCase("TMS")?true:false){
			  return searchTmsCreditNote(searchParamVO);
		  }else
			  if(systemLine.equalsIgnoreCase("YMS")?true:false){
				  return searchYmsCreditNote(searchParamVO);
			  }
		return null;
		
	}

	private List<DMSSearchVO> searchYmsCreditNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchYmsCreditNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchYmsCreditNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(YmsCreditDebitNote.class,"Cr_Db");
		  		 criteria.createAlias("Cr_Db.customer", "blCus",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.createAlias("Cr_Db.ymsCdNoteDetails", "ymsCdDtl",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.createAlias("ymsCdDtl.ymsShipment", "ss",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","CN"));
		  	 	criteria.createAlias("Cr_Db.company","comp")
					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		  	 		criteria.addOrder(Order.desc("Cr_Db.cdNoteDate"));
		  	 		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getCreditNoteNo())){
					   logger.debug("getCreditNoteNo : "+searchParamVO.getCreditNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getCreditNoteNo().trim()));
				   }
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentNo())){
		  			/* DetachedCriteria mani=DetachedCriteria.forClass(YmsCdNoteDetail.class, "ymsCdDtl")
		  			 	 .createAlias("ymsCdDtl.ymsShipment", "ss",CriteriaSpecification.INNER_JOIN)
						 .setProjection(Projections.projectionList()
									.add(Projections.max("movement.movementDate")));*/
				   	 criteria.add(Restrictions.eq("ss.shipmentNumber",searchParamVO.getShipmentNo().trim()));
		  		   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("blCus.customerName",searchParamVO.getBillingCustomer().trim(),MatchMode.ANYWHERE));
				   }
				    
				   if(nullEmptyCheckerStringFlag(searchParamVO.getCnDateForm())&&nullEmptyCheckerStringFlag(searchParamVO.getCnDateTo())){
					   logger.debug("CreditNote :fromDate :: "+searchParamVO.getCnDateForm()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateForm().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getCnDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getCnDateForm()), getUtilDateEnd(searchParamVO.getCnDateTo())));
				   }
				   
				   List<YmsCreditDebitNote> ofsCnList=criteria.list();
				   if(ofsCnList!=null?!ofsCnList.isEmpty()?true:false:false){
					   
					   for(YmsCreditDebitNote ymsCN:ofsCnList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("YMS");
						   dmsVo.setCreditNoteNo(valueNullEmptyChecker(ymsCN.getCdNoteNumber()));
						   
						   if(ymsCN.getYmsCdNoteDetails()!=null && ymsCN.getYmsCdNoteDetails().iterator().hasNext()){
							   dmsVo.setShipmentNo(valueNullEmptyChecker(ymsCN.getYmsCdNoteDetails().iterator().next().getYmsShipment().getShipmentNumber()));
						   }else{
							   dmsVo.setShipmentNo(""); 
						   }
						   dmsVo.setInvoiceNo(valueNullEmptyChecker(ymsCN.getYmsInvoice().getInvoiceNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(ymsCN.getCustomerAddress()!=null?ymsCN.getCustomerAddress().getCustomer().getCustomerName():""));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(ymsCN.getCustomer().getCustomerName()));
						   dmsVo.setCnDateTo(dateCheckerAsGetString(ymsCN.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsCreditNote : CreditNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsCreditNote : CreditNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsCreditNote : CreditNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}

	private List<DMSSearchVO> searchOfsCreditNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchOfsCreditNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchOfsCreditNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(CreditDebitNote.class,"Cr_Db");
		  		 criteria.createAlias("Cr_Db.customerByBillingCustomerId", "blCus",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.createAlias("Cr_Db.shippingSchedule", "ss",CriteriaSpecification.LEFT_JOIN);  
		  		 criteria.createAlias("Cr_Db.customerByBookingCustomerId", "bkCus",CriteriaSpecification.INNER_JOIN);
		  		 criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","CN"));		  		    
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getCreditNoteNo())){
					   logger.debug("getCreditNoteNo : "+searchParamVO.getCreditNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getCreditNoteNo().trim()));
				   }
				  		 if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentNo())){
						   	 criteria.add(Restrictions.eq("ss.shipmentNumber",searchParamVO.getShipmentNo().trim()));
					   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("blCus.customerName","%"+searchParamVO.getBillingCustomer().trim()+"%"));
				   }
				   
				 /*  if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("bkCus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }*/
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getCnDateForm())&&nullEmptyCheckerStringFlag(searchParamVO.getCnDateTo())){
					   logger.debug("CreditNote :fromDate :: "+searchParamVO.getCnDateForm()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateForm().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getCnDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getCnDateForm()), getUtilDateEnd(searchParamVO.getCnDateTo())));
				   }
				   
				   List<CreditDebitNote> ofsCnList=criteria.list();
				   if(ofsCnList!=null?!ofsCnList.isEmpty()?true:false:false){
					   
					   for(CreditDebitNote ofsCn:ofsCnList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("OFS");
						   dmsVo.setCreditNoteNo(valueNullEmptyChecker(ofsCn.getCdNoteNumber()));
						   dmsVo.setShipmentNo(valueNullEmptyChecker(ofsCn.getShippingSchedule()!=null?ofsCn.getShippingSchedule().getShipmentNumber():""));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(ofsCn.getCustomerByBookingCustomerId().getCustomerName()));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(ofsCn.getCustomerByBillingCustomerId().getCustomerName()));
						   dmsVo.setCnDateTo(dateCheckerAsGetString(ofsCn.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOfsCreditNote : CreditNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOfsCreditNote : CreditNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOfsCreditNote : CreditNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	private List<DMSSearchVO> searchTmsCreditNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchTmsCreditNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchTmsCreditNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(TmsCreditDebitNote.class,"Cr_Db");
		  		   criteria.createAlias("Cr_Db.tmsInvoice", "inv",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("inv.bookingCustomer", "bkCus",CriteriaSpecification.INNER_JOIN);
		  		   criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","CN"));
		  		    
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getCreditNoteNo())){
					   logger.debug("getCreditNoteNo : "+searchParamVO.getCreditNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getCreditNoteNo().trim()));
				   }
				  	 if(nullEmptyCheckerStringFlag(searchParamVO.getInvoiceNo())){
						   	 criteria.add(Restrictions.eq("inv.invoiceNumber",searchParamVO.getInvoiceNo().trim()));
					 }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("inv.billingCustomerName","%"+searchParamVO.getBillingCustomer().trim()+"%"));
				   }
				   
				 /*  if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("inv.bookingCustomer.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }*/
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getCnDateForm())&&nullEmptyCheckerStringFlag(searchParamVO.getCnDateTo())){
					   logger.debug("CreditNote :fromDate :: "+searchParamVO.getCnDateForm()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateForm().trim())+"\n" +
					   		"CreditNote :toDate :: "+searchParamVO.getCnDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getCnDateForm()), getUtilDateEnd(searchParamVO.getCnDateTo())));
				   }
				   
				   List<TmsCreditDebitNote> tmsCnList=criteria.list();
				   if(tmsCnList!=null?!tmsCnList.isEmpty()?true:false:false){
					   
					   for(TmsCreditDebitNote tmsCn:tmsCnList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("TMS");
						   dmsVo.setCreditNoteNo(valueNullEmptyChecker(tmsCn.getCdNoteNumber()));
						   dmsVo.setInvoiceNo(valueNullEmptyChecker(tmsCn.getTmsInvoice().getInvoiceNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(tmsCn.getTmsInvoice().getBookingCustomer().getCustomerName()));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(tmsCn.getTmsInvoice().getBillingCustomerName()));
						   dmsVo.setCnDateTo(dateCheckerAsGetString(tmsCn.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);  
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchTmsCreditNote : DebitNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchTmsCreditNote : DebitNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchTmsCreditNote : DebitNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	private List<DMSSearchVO> searchDebitNoteDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("searchDebitNoteDetails >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*********");
		String systemLine=searchParamVO.getSystemLine().trim();
		  if(systemLine.equalsIgnoreCase("OFS")?true:false){
			  return searchOfsDebitNote(searchParamVO);
		  }else
		  if(systemLine.equalsIgnoreCase("TMS")?true:false){
			  return searchTmsDebitNote(searchParamVO);
		  }else
			  if(systemLine.equalsIgnoreCase("YMS")?true:false){
				  return searchYmsDebitNote(searchParamVO);
			  }
		return null;
	}

	private List<DMSSearchVO> searchOfsDebitNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchOfsDebitNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchOfsDebitNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(CreditDebitNote.class,"Cr_Db");
		  		   criteria.createAlias("Cr_Db.customerByBillingCustomerId", "blCus",CriteriaSpecification.LEFT_JOIN);
		  		   criteria.createAlias("Cr_Db.shippingSchedule", "ss",CriteriaSpecification.LEFT_JOIN);  
		  		   criteria.createAlias("Cr_Db.customerByBookingCustomerId", "bkCus",CriteriaSpecification.INNER_JOIN);
		  	
		  		    criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","DN"));
		  		    
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getDebitNoteNo())){
					   logger.debug("getDebitNoteNo : "+searchParamVO.getDebitNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getDebitNoteNo().trim()));
				   }
			  		 if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentNo())){
					   	 criteria.add(Restrictions.eq("ss.shipmentNumber",searchParamVO.getShipmentNo().trim()));
				   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("blCus.customerName","%"+searchParamVO.getBillingCustomer().trim()+"%"));
				   }
				   
				 /*  if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("bkCus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }*/
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getDnDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getDnDateTo())){
					   logger.debug("DebitNote :fromDate :: "+searchParamVO.getDnDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDnDateTo().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getDnDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getDnDateFrom()), getUtilDateEnd(searchParamVO.getDnDateTo())));
				   }
				   
				   List<CreditDebitNote> ofsDnList=criteria.list();
				   if(ofsDnList!=null?!ofsDnList.isEmpty()?true:false:false){
					   
					   for(CreditDebitNote ofsDn:ofsDnList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setDebitNoteNo(valueNullEmptyChecker(ofsDn.getCdNoteNumber()));
						   dmsVo.setShipmentNo(valueNullEmptyChecker(ofsDn.getShippingSchedule()!=null?ofsDn.getShippingSchedule().getShipmentNumber():""));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(ofsDn.getCustomerByBookingCustomerId().getCustomerName()));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(ofsDn.getCustomerByBillingCustomerId().getCustomerName()));
						   dmsVo.setDnDateTo(dateCheckerAsGetString(ofsDn.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOfsDebitNote : DebitNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOfsDebitNote : DebitNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOfsDebitNote : DebitNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	private List<DMSSearchVO> searchTmsDebitNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchTmsDebitNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(TmsCreditDebitNote.class,"Cr_Db");
		  		   criteria.createAlias("Cr_Db.tmsInvoice", "inv",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("inv.bookingCustomer", "bkCus",CriteriaSpecification.INNER_JOIN);
		  		   criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","DN"));
		  		    
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getDebitNoteNo())){
					   logger.debug("getDebitNoteNo : "+searchParamVO.getDebitNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getDebitNoteNo().trim()));
				   }
				  	 if(nullEmptyCheckerStringFlag(searchParamVO.getInvoiceNo())){
						   	 criteria.add(Restrictions.eq("inv.invoiceNumber",searchParamVO.getInvoiceNo().trim()));
					 }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("inv.billingCustomerName",searchParamVO.getBillingCustomer().trim(),MatchMode.ANYWHERE));
				   }
				   
				 /*  if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("bkCus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }*/
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getDnDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getDnDateTo())){
					   logger.debug("CreditNote :fromDate :: "+searchParamVO.getDnDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDnDateTo().trim())+"\n" +
					   		"CreditNote :toDate :: "+searchParamVO.getDnDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getDnDateFrom()), getUtilDateEnd(searchParamVO.getDnDateTo())));
				   }
				   
				   List<TmsCreditDebitNote> tmsDnList=criteria.list();
				   if(tmsDnList!=null?!tmsDnList.isEmpty()?true:false:false){
					   for(TmsCreditDebitNote tmsDn:tmsDnList){
						   
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setDebitNoteNo(valueNullEmptyChecker(tmsDn.getCdNoteNumber()));
						   dmsVo.setInvoiceNo(valueNullEmptyChecker(tmsDn.getTmsInvoice().getInvoiceNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(tmsDn.getTmsInvoice().getCustomer()!=null?tmsDn.getTmsInvoice().getCustomer().getCustomerName():""));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(tmsDn.getTmsInvoice().getBillingCustomerName()));
						   dmsVo.setDnDateTo(dateCheckerAsGetString(tmsDn.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchTmsCreditNote : DebitNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchTmsCreditNote : DebitNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchTmsCreditNote : DebitNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}



	private List<DMSSearchVO> searchYmsDebitNote(DMSSearchVO searchParamVO) {
		
		logger.debug("searchYmsDebitNote >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("DMSDaoImpl ::searchYmsDebitNote : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(YmsCreditDebitNote.class,"Cr_Db");
		  		 criteria.createAlias("Cr_Db.customer", "blCus",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.createAlias("Cr_Db.ymsCdNoteDetails", "ymsCdDtl",CriteriaSpecification.LEFT_JOIN);
		  		 criteria.createAlias("ymsCdDtl.ymsShipment", "ss",CriteriaSpecification.INNER_JOIN);
		  		 criteria.add(Restrictions.eq("Cr_Db.creditDebitFlag","DN"));
		  	 		criteria.createAlias("Cr_Db.company","comp")
					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		  	 		criteria.addOrder(Order.desc("Cr_Db.cdNoteDate"));
		  	 		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
		  		   if(nullEmptyCheckerStringFlag(searchParamVO.getCreditNoteNo())){
					   logger.debug("getCreditNoteNo : "+searchParamVO.getCreditNoteNo());
					   criteria.add(Restrictions.eq("Cr_Db.cdNoteNumber",searchParamVO.getCreditNoteNo().trim()));
				   }
				  		 if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentNo())){
						   	 criteria.add(Restrictions.eq("ss.shipmentNumber",searchParamVO.getShipmentNo().trim()));
					   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
					   	 criteria.add(Restrictions.ilike("blCus.customerName",searchParamVO.getBillingCustomer().trim(),MatchMode.ANYWHERE));
				   }
				   
				 /*  if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("bkCus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }*/
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getCnDateForm())&&nullEmptyCheckerStringFlag(searchParamVO.getCnDateTo())){
					   logger.debug("CreditNote :fromDate :: "+searchParamVO.getCnDateForm()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateForm().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getCnDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getCnDateTo().trim()));
					   criteria.add(Restrictions.between("Cr_Db.cdNoteDate",getUtilDate(searchParamVO.getCnDateForm()), getUtilDateEnd(searchParamVO.getCnDateTo())));
				   }
				   
				   List<YmsCreditDebitNote> ofsCnList=criteria.list();
				   if(ofsCnList!=null&&!ofsCnList.isEmpty()){
					   
					   for(YmsCreditDebitNote ymsCN:ofsCnList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setSystemLine("YMS");
						   dmsVo.setDebitNoteNo(valueNullEmptyChecker(ymsCN.getCdNoteNumber()));
						   
						   if(ymsCN.getYmsCdNoteDetails()!=null && ymsCN.getYmsCdNoteDetails().iterator().hasNext()){
							   dmsVo.setShipmentNo(valueNullEmptyChecker(ymsCN.getYmsCdNoteDetails().iterator().next().getYmsShipment().getShipmentNumber()));
						   }else{
							   dmsVo.setShipmentNo(""); 
						   }
						   dmsVo.setInvoiceNo(valueNullEmptyChecker(ymsCN.getYmsInvoice().getInvoiceNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(ymsCN.getCustomer().getCustomerName()));
						   dmsVo.setBillingCustomer(valueNullEmptyChecker(ymsCN.getCustomerAddress().getCustomer().getCustomerName()));
						   dmsVo.setDnDateTo(dateCheckerAsGetString(ymsCN.getCdNoteDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsDebitNote : CreditNote : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsDebitNote : CreditNote : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsDebitNote : CreditNote : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	/***  ****************************Booking Search*************************
	 *  
	 * @param searchParamVO
	 * @return
	 */
	private List<DMSSearchVO> searchBookingDetails(DMSSearchVO searchParamVO) {
		
		String systemLine=searchParamVO.getSystemLine().trim();
		  if(systemLine.equalsIgnoreCase("OFS")){
			  return searchOfsBookingSearch(searchParamVO);
		  }else
			if(systemLine.equalsIgnoreCase("TMS")){
			  return searchTmsBookingSearch(searchParamVO);
			}else
				if(systemLine.equalsIgnoreCase("YMS")){
					return searchYmsBookingSearch(searchParamVO);
		  }
		  return null;
	}
	
	private List<DMSSearchVO> searchYmsBookingSearch(DMSSearchVO searchParamVO) {
		
		logger.debug("DMSDaoImpl ::searchYmsBookingSearch : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  Criteria criteria=session.createCriteria(YmsBooking.class,"bk");
		  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("bk.masterSetupByBusinessLineMasterId", "ms",CriteriaSpecification.INNER_JOIN);  
		  		   criteria.createAlias("ms.masterType", "mt",CriteriaSpecification.INNER_JOIN);
		  		   criteria.add(Restrictions.eq("mt.typeDescription","BUSINESS LINE"));
		  		   criteria = criteria.createAlias("bk.company","comp")
		  		   					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
		  		   					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
		  		   					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		  		    criteria.createAlias("bk.vessel","ves", Criteria.LEFT_JOIN);
		  			criteria.addOrder(Order.desc("bk.bookingDate"));
					criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   	
					
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBusinessLine())){
					   	 criteria.add(Restrictions.eq("ms.setupDescription",searchParamVO.getBusinessLine().trim()));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getBookingCustomer().trim(),MatchMode.ANYWHERE));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVesselName())){
					   criteria.add(Restrictions.ilike("ves.vesselName",searchParamVO.getVesselName().trim(),MatchMode.ANYWHERE));					   
				   }
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVoyageNo())){
					   criteria.add(Restrictions.ilike("bk.voyageNumber",searchParamVO.getVoyageNo().trim(),MatchMode.ANYWHERE));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getEtaDate())){
					   logger.debug("Booking :EtaDate :: "+searchParamVO.getEtaDate()+"\n :: sql Date :"+getUtilDate(searchParamVO.getEtaDate()));
					   criteria.add(Restrictions.between("bk.eta",getUtilDate(searchParamVO.getEtaDate()), getUtilDateEnd(searchParamVO.getEtaDate())));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getBookingDateTo())){
					   logger.debug("Booking :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateFrom().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateTo().trim()));
					   criteria.add(Restrictions.between("bk.bookingDate",getUtilDate(searchParamVO.getBookingDateFrom()), getUtilDateEnd(searchParamVO.getBookingDateTo())));
				   }
				   
				   List<YmsBooking> bookingList=criteria.list();
				   if(bookingList!=null&&!bookingList.isEmpty()){
					   
					   for(YmsBooking bk:bookingList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setBookingNo(valueNullEmptyChecker(bk.getBookingNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
						   dmsVo.setBookingDate(dateCheckerAsGetString(bk.getBookingDate()));
						   dmsVo.setBusinessLine(valueNullEmptyChecker(bk.getMasterSetupByBusinessLineMasterId().getSetupDescription()));
						   dmsVo.setEtaDate(dateCheckerAsGetString(bk.getEta()));
						   if(bk.getVessel()!=null){
							dmsVo.setVesselName(valueNullEmptyChecker(bk.getVessel().getVesselName()));								
						   }
						   dmsVo.setVoyageNo(valueNullEmptyChecker(bk.getVoyageNumber()));
						   
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsBookingSearch : booking : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsBookingSearch : booking : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsBookingSearch : booking : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}


	/***  ****************************OFS Booking Search*************************
	 * 
	 * @param searchParamVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DMSSearchVO> searchOfsBookingSearch(DMSSearchVO searchParamVO){
		logger.debug("DMSDaoImpl ::searchOfsBookingSearch : START: searchParamVO :"+searchParamVO);
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
		  session=HibernateUtil.getSession();  
		  
		  Criteria criteria=session.createCriteria(Booking.class,"bk");
		  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("bk.masterSetup", "ms",CriteriaSpecification.INNER_JOIN);  
		  		   criteria.createAlias("ms.masterType", "mt",CriteriaSpecification.INNER_JOIN);
		  		   criteria.add(Restrictions.eq("mt.typeDescription","BUSINESS LINE"));
		  		   
		  		   criteria = criteria.createAlias("bk.masterSetupByCreatedByCompanyMasterId","cpy")
		  		   					.createAlias("cpy.masterType","cmt")                   
		  		   					.add(Restrictions.eq("cmt.typeDescription", "COMPANY CODE"))
		  		   					.add(Restrictions.eq("cpy.setupCode", searchParamVO.getCompanyCode()));
		  		   criteria = criteria.createAlias("bk.shippingSchedule","ss", Criteria.LEFT_JOIN)
		  		   						.createAlias("ss.vessel", "ves", Criteria.LEFT_JOIN);
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBusinessLine())){
					   	 criteria.add(Restrictions.eq("ms.setupDescription",searchParamVO.getBusinessLine().trim()));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   logger.debug("getBookingCustomer : "+searchParamVO.getBookingCustomer());
					   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVesselName())){
					   criteria.add(Restrictions.or(
							   Restrictions.ilike("ves.vesselName","%"+searchParamVO.getVesselName().trim()+"%")
							   , Restrictions.ilike("bk.vesselName","%"+searchParamVO.getVesselName().trim()+"%")));					   
				   }
				   
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVoyageNo())){
					   criteria.add(Restrictions.or(
							   Restrictions.ilike("ss.voyageNumber","%"+searchParamVO.getVoyageNo().trim()+"%")
							   , Restrictions.ilike("bk.voyageNumber","%"+searchParamVO.getVoyageNo().trim()+"%")));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getEtaDate())){
					   logger.debug("Booking :EtaDate :: "+searchParamVO.getEtaDate()+"\n :: sql Date :"+getUtilDate(searchParamVO.getEtaDate()));
					   criteria.add(Restrictions.between("bk.ETA",getUtilDate(searchParamVO.getEtaDate()), getUtilDateEnd(searchParamVO.getEtaDate())));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getBookingDateTo())){
					   logger.debug("Booking :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateFrom().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateTo().trim()));
					   criteria.add(Restrictions.between("bk.bookingDate",getUtilDate(searchParamVO.getBookingDateFrom()), getUtilDateEnd(searchParamVO.getBookingDateTo())));
				   }
				   
				   List<Booking> bookingList=criteria.list();
				   if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
					   
					   for(Booking bk:bookingList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setBookingNo(valueNullEmptyChecker(bk.getBookingNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
						   dmsVo.setBookingDate(dateCheckerAsGetString(bk.getBookingDate()));
						   dmsVo.setBusinessLine(valueNullEmptyChecker(bk.getMasterSetup().getSetupDescription()));
						   dmsVo.setEtaDate(dateCheckerAsGetString(bk.getETA()));
						   if(bk.getShippingSchedule()!=null){
							   if(bk.getShippingSchedule().getVessel()!=null){
								   if(bk.getShippingSchedule().getVessel().getVesselName()!=null){
									   dmsVo.setVesselName(valueNullEmptyChecker(bk.getShippingSchedule().getVessel().getVesselName()));
								   }
							   }
						   }
						   else{
						   dmsVo.setVesselName(valueNullEmptyChecker(bk.getVesselName()));
						   }
						   if(bk.getShippingSchedule()!=null){
							   if(bk.getShippingSchedule().getVoyageNumber()!=null){
								   dmsVo.setVoyageNo(valueNullEmptyChecker(bk.getShippingSchedule().getVoyageNumber()));								   
							   }
						   }
						   else{
						   dmsVo.setVoyageNo(valueNullEmptyChecker(bk.getVoyageNumber()));
						   }
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOfsBookingSearch : booking : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOfsBookingSearch : booking : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOfsBookingSearch : booking : searchParamVO : "+searchParamVO+"\n dmsSearchVolist : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}
/** /***  ****************************TMSBooking Search*************************
 * 
 * @param searchParamVO
 * @return
 */
	private List<DMSSearchVO> searchTmsBookingSearch(DMSSearchVO searchParamVO){
		Session session=null;
		logger.debug("DMSDaoImpl ::searchTmsBookingSearch : searchParamVO :"+searchParamVO);
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		 
		try{
		  session=HibernateUtil.getSession();
		  Criteria criteria=session.createCriteria(TmsBooking.class,"bk");
		  		   criteria.createAlias("bk.masterSetup", "ms",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("ms.masterType", "mt",CriteriaSpecification.INNER_JOIN);
		  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
		  		   criteria.add(Restrictions.eq("mt.typeDescription","BUSINESS LINE"));
		  		   criteria.createAlias("bk.company", "comp",CriteriaSpecification.INNER_JOIN);
		  		   
		  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
		  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
		  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
		  			}
					 if(nullEmptyCheckerStringFlag(searchParamVO.getBusinessLine())){
					   criteria.add(Restrictions.eq("ms.setupDescription",searchParamVO.getBusinessLine().trim()));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVesselName())){
					   criteria.add(Restrictions.ilike("bk.vessleName","%"+searchParamVO.getVesselName().trim()+"%"));
				   }
				   
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getVoyageNo())){
					   criteria.add(Restrictions.eq("bk.voyageNumber",searchParamVO.getVoyageNo().trim()));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getEtaDate())){
					   logger.debug("Booking :EtaDate :: "+searchParamVO.getEtaDate()+"\n :: sql Date :"+getUtilDate(searchParamVO.getEtaDate()));
					   criteria.add(Restrictions.between("bk.etaDate",getUtilDate(searchParamVO.getEtaDate()), getUtilDateEnd(searchParamVO.getEtaDate())));
				   }
				   
				   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getBookingDateTo())){
					   logger.debug("Booking :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateFrom().trim())+"\n" +
					   		"Booking :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateTo().trim()));
					   criteria.add(Restrictions.between("bk.bookingDate",getUtilDate(searchParamVO.getBookingDateFrom()), getUtilDateEnd(searchParamVO.getBookingDateTo())));
				   }
				   
				   @SuppressWarnings("unchecked")
				List<TmsBooking> bookingList=criteria.list();
				   if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
					   
					   for(TmsBooking bk:bookingList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setBookingNo(valueNullEmptyChecker(bk.getBookingNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
						   dmsVo.setBookingDate(dateCheckerAsGetString(bk.getBookingDate()));
						   dmsVo.setBusinessLine(valueNullEmptyChecker(bk.getMasterSetup().getSetupDescription()));
						   dmsVo.setVesselName(valueNullEmptyChecker(bk.getVessleName()));
						   dmsVo.setEtaDate(dateCheckerAsGetString(bk.getEtaDate()));
						   dmsVo.setVoyageNo(valueNullEmptyChecker(bk.getVoyageNumber()));
						   logger.debug("BranchCode :: "+bk.getCompany().getBranchCode()+"---CompanyCode ::"+bk.getCompany().getMasterSetupByCompanyMasterId().getSetupCode()+"\n Company Name :: "+valueNullEmptyChecker(bk.getCompany().getMasterSetupByCompanyMasterId().getSetupDescription())+"--company ID :: "+bk.getCompany().getCompanyId());
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
				   
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchBookingCustomerDetails : booking : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchBookingCustomerDetails : booking : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchBookingCustomerDetails : booking : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}
	
	/** ******************** Job Details Search **********************************
	 * 	
	 * @param searchParamVO
	 * @return
	 */
		private List<DMSSearchVO> searchJobDetails(DMSSearchVO searchParamVO) {
			
			Session session=null;
			logger.debug("DMSDaoImpl ::searchJobDetails : searchParamVO :"+searchParamVO);
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();;
			try{
			  session=HibernateUtil.getSession();
			  Criteria criteria=session.createCriteria(Job.class,"job");
			  		   criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			  		   criteria.createAlias("job.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
			  		   criteria.createAlias("bk.bookingDetails", "bkdtl",CriteriaSpecification.LEFT_JOIN);
			  		   criteria.createAlias("bkdtl.port", "disPort",CriteriaSpecification.LEFT_JOIN);
			  		   				   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
					   }
					   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
						   criteria.add(Restrictions.eq("bk.bookingNumber",searchParamVO.getBookingNo().trim()));
					   }
					   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getPortOfDischarge())){
						   criteria.add(Restrictions.ilike("disPort.portName","%"+searchParamVO.getPortOfDischarge().trim()+"%"));
					   }
					   if(nullEmptyCheckerStringFlag(searchParamVO.getJobDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getJobDateTo())){
						   logger.debug("Booking :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getJobDateFrom())+"\n" +
						   		"Booking :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getJobDateTo()));
						   criteria.add(Restrictions.between("job.jobDate",getUtilDate(searchParamVO.getJobDateFrom()), getUtilDateEnd(searchParamVO.getJobDateTo())));
					   }
					   
					   @SuppressWarnings("unchecked")
					List<Job> jobList=criteria.list();
					   if(jobList!=null?!jobList.isEmpty()?true:false:false){
						   
						   for(Job jb:jobList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setJobNo(valueNullEmptyChecker(jb.getJobNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(jb.getBooking().getCustomer().getCustomerName()));
							   dmsVo.setBookingNo(valueNullEmptyChecker(jb.getBooking().getBookingNumber()));
							   String portOfDischar=jb.getBooking().getBookingDetails()!=null?!jb.getBooking().getBookingDetails().isEmpty()?((BookingDetail)jb.getBooking().getBookingDetails().iterator().next()).getPort()!=null?((BookingDetail)jb.getBooking().getBookingDetails().iterator().next()).getPort().getPortName():"":"":"";
							   dmsVo.setPortOfDischarge(valueNullEmptyChecker(portOfDischar));
							   dmsVo.setJobDateTo(dateCheckerAsGetString(jb.getJobDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
					   
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchJobDetails :  : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchJobDetails :  : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchJobDetails :  : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
		
/** *******************************  Si search ***************************************
 * 
 * @param searchParamVO
 * @return
 */
		private List<DMSSearchVO> searchSiDetails(DMSSearchVO searchParamVO) {
			
			Session session=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			logger.debug("DMSDaoImpl ::searchSiDetails : searchParamVO :"+searchParamVO);
			try{
			  session=HibernateUtil.getSession();
			  Criteria criteria=session.createCriteria(ShippingInstruction.class,"si");
			  		   criteria.createAlias("si.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);//
			  		   criteria.createAlias("si.bookingDetail", "bkdtl",CriteriaSpecification.INNER_JOIN);
			  		   criteria.createAlias("bkdtl.port", "disPort",CriteriaSpecification.LEFT_JOIN);
			  		   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
					   }
					  
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
						   criteria.add(Restrictions.eq("bk.bookingNumber",searchParamVO.getBookingNo().trim()));
					   }
					   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getPortOfDischarge())){
						   criteria.add(Restrictions.ilike("disPort.portName","%"+searchParamVO.getPortOfDischarge().trim()+"%"));
					   }
					   
					   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getSiDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getSiDateTo())){
						   logger.debug("Si :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getSiDateFrom())+"\n" +
						   		"Booking :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getSiDateTo()));
						  criteria.add(Restrictions.between("si.siDate",getUtilDate(searchParamVO.getSiDateFrom()), getUtilDateEnd(searchParamVO.getSiDateTo())));
					   }
					   
					@SuppressWarnings("unchecked")
					List<ShippingInstruction> siList=criteria.list();
					   if(siList!=null?!siList.isEmpty()?true:false:false){
						   
						   for(ShippingInstruction si:siList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setSiNo(valueNullEmptyChecker(si.getSiNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(si.getBooking().getCustomer().getCustomerName()));
							   dmsVo.setBookingNo(valueNullEmptyChecker(si.getBooking().getBookingNumber()));
							   String portOfDischarge=si.getBookingDetail()!=null?si.getBookingDetail().getPort()!=null?si.getBookingDetail().getPort().getPortName():"":"";
							   dmsVo.setPortOfDischarge(valueNullEmptyChecker(portOfDischarge));
							   dmsVo.setSiDateTo(dateCheckerAsGetString(si.getSiDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
					   
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchSiDetails : : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchSiDetails : : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchSiDetails : : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
		
	/*** *********************************** Claim search *********************************
	 * 	
	 * @param searchParamVO
	 * @return
	 */
		private List<DMSSearchVO> searchClaimDetails(DMSSearchVO searchParamVO) {
			
			String systemLine=searchParamVO.getSystemLine().trim();
			  if(systemLine.equalsIgnoreCase("OFS")){
				  return searchOfsClaimSearch(searchParamVO);
			  }if(systemLine.equalsIgnoreCase("TMS")){
				  return searchTmsClaimSearch(searchParamVO);
			  }
			  else
				  if(systemLine.equalsIgnoreCase("YMS")){
				 return searchYmsClaimSearch(searchParamVO);
			  }
			return null;
		}
		/*** ************************* OFS Claim Search *************************
		 * 
		 * @param searchParamVO
		 * @return
		 */
		private List<DMSSearchVO> searchOfsClaimSearch(DMSSearchVO searchParamVO){
			logger.debug("DMSDaoImpl ::searchOfsClaimSearch : searchParamVO :"+searchParamVO);
			Session session=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
			  session=HibernateUtil.getSession();
			  Criteria criteria=session.createCriteria(Claim.class,"clm");
			  		   criteria.createAlias("clm.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  		   criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);//
			  		   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
					   }
					  
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
						   criteria.add(Restrictions.eq("bk.bookingNumber",searchParamVO.getBookingNo().trim()));
					   }
					   
					   if(nullEmptyCheckerStringFlag(searchParamVO.getClaimDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getClaimDateTo())){
						   logger.debug("Claim :fromDate :: "+searchParamVO.getClaimDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateFrom())+"\n" +
						   		"Claim :toDate :: "+searchParamVO.getClaimDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateTo()));
						   criteria.add(Restrictions.between("clm.claimDate",getUtilDate(searchParamVO.getClaimDateFrom()), getUtilDateEnd(searchParamVO.getClaimDateTo())));
					   }
					   
					   @SuppressWarnings("unchecked")
					List<Claim> claimList=criteria.list();
					   if(claimList!=null?!claimList.isEmpty()?true:false:false){
						   
						   for(Claim claim:claimList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setClaimNo(valueNullEmptyChecker(claim.getClaimNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(claim.getBooking().getCustomer().getCustomerName()));
							   dmsVo.setBookingNo(valueNullEmptyChecker(claim.getBooking().getBookingNumber()));
							   dmsVo.setClaimDateTo(dateCheckerAsGetString(claim.getClaimDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
					   
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchOfsClaimSearch :  : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchOfsClaimSearch :  : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchOfsClaimSearch :  : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
			
		}
		
		/*** ************************* TMS Claim Search *************************
		 * 
		 * @param searchParamVO
		 * @return
		 */
		private List<DMSSearchVO> searchTmsClaimSearch(DMSSearchVO searchParamVO){
			Session session=null;
			Criteria criteria=null;
			logger.debug("DMSDaoImpl :: searchTmsClaimSearch : START : searchParamVO :: "+searchParamVO);
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(TmsInsuranceClaim.class,"tmsClm");
			           criteria.createAlias("tmsClm.customer", "cus",CriteriaSpecification.INNER_JOIN);
			           criteria.createAlias("tmsClm.tmsIncident", "tmsInc",CriteriaSpecification.INNER_JOIN);
			           criteria.createAlias("tmsClm.company", "comp",CriteriaSpecification.INNER_JOIN);
			           
			           if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
			  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
			  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
			  			}
			            
						if(nullEmptyCheckerStringFlag(searchParamVO.getIncidentNo())){
							   criteria.add(Restrictions.eq("tmsInc.incidentNumber",searchParamVO.getIncidentNo().trim()));
						}
						if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
  						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getCustomerName().trim()+"%"));
  					    }	
    					if(nullEmptyCheckerStringFlag(searchParamVO.getClaimDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getClaimDateTo())){
  						   logger.debug("claim :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateFrom())+"\n" +
  						   		"claim :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateTo()));
  						 criteria.add(Restrictions.between("tmsClm.claimDate",getUtilDate(searchParamVO.getClaimDateFrom()), getUtilDateEnd(searchParamVO.getClaimDateTo())));
  					     }
    				  @SuppressWarnings("unchecked")
					List<TmsInsuranceClaim> clList=criteria.list();
  					   if(clList!=null?!clList.isEmpty()?true:false:false){
  						   
  						   for(TmsInsuranceClaim tCl:clList){
  							   DMSSearchVO dmsVo=new DMSSearchVO();
  							   dmsVo.setClaimNo(valueNullEmptyChecker(tCl.getClaimNo()));
  							   dmsVo.setBookingNo(tCl.getTmsIncident().getTmsDeliveryOrder()!=null?tCl.getTmsIncident().getTmsDeliveryOrder().getTmsBookingLoadDetail().getTmsBooking().getBookingNumber():"");
  							   dmsVo.setBookingCustomer(tCl.getTmsIncident().getTmsDeliveryOrder()!=null?tCl.getTmsIncident().getTmsDeliveryOrder().getTmsBookingLoadDetail().getTmsBooking().getCustomer().getCustomerName():"");
  							   dmsVo.setClaimDateTo(dateCheckerAsGetString(tCl.getClaimDate()));
  							   dmsVo.setIncidentNo(valueNullEmptyChecker(tCl.getTmsIncident().getIncidentNumber()));
  							   dmsSearchVolist.add(dmsVo);    
  						   }
  					   }
			  	    
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchTmsClaimSearch : claim : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchTmsClaimSearch : claim : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchTmsClaimSearch : claim : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
		/*** ************************* TMS Claim Search *************************
		 * 
		 * @param searchParamVO
		 * @return
		 */
		private List<DMSSearchVO> searchYmsClaimSearch(DMSSearchVO searchParamVO){
			Session session=null;
			Criteria criteria=null;
			logger.debug("DMSDaoImpl :: searchYmsClaimSearch : START : searchParamVO :: "+searchParamVO);
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(YmsInsuranceClaim.class,"ymsClm");
			           criteria.createAlias("ymsClm.customer", "cus",CriteriaSpecification.INNER_JOIN);
			           criteria.createAlias("ymsClm.ymsIncident", "ymsInc",CriteriaSpecification.INNER_JOIN);
			           criteria.createAlias("ymsClm.company", "comp",CriteriaSpecification.INNER_JOIN)
			            .createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
	   					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
	   					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
			           criteria.addOrder(Order.desc("ymsClm.claimDate"));
			           criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
			           if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getBookingCustomer().trim(),MatchMode.ANYWHERE));
					   }					  
					   if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
						   criteria.createAlias("ymsClm.masterSetup","claimType",CriteriaSpecification.INNER_JOIN);
		   				   criteria.add(Restrictions.ilike("claimType.setupDescription",searchParamVO.getBookingCustomer().trim(),MatchMode.ANYWHERE));
					   }
			       	   if(nullEmptyCheckerStringFlag(searchParamVO.getIncidentNo())){
			       		   criteria.add(Restrictions.eq("ymsInc.incidentNumber",searchParamVO.getIncidentNo().trim()));
					   }
						/*if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
  						   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getCustomerName().trim(),MatchMode.ANYWHERE));
  					    }	*/
    					if(nullEmptyCheckerStringFlag(searchParamVO.getClaimDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getClaimDateTo())){
  						   logger.debug("claim :fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateFrom())+"\n" +
  						   		"claim :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getClaimDateTo()));
  						 criteria.add(Restrictions.between("ymsClm.claimDate",getUtilDate(searchParamVO.getClaimDateFrom()), getUtilDateEnd(searchParamVO.getClaimDateTo())));
  					     }
    				@SuppressWarnings("unchecked")
					List<YmsInsuranceClaim> clList=criteria.list();
  					   if(clList!=null&&!clList.isEmpty()){  						   
  						   for(YmsInsuranceClaim ycl:clList){
  							   DMSSearchVO dmsVo=new DMSSearchVO();
  							   dmsVo.setClaimNo(valueNullEmptyChecker(ycl.getClaimNumber()));
  							   dmsVo.setBookingNo(valueNullEmptyChecker(ycl.getMasterSetup().getSetupDescription()));
  							   dmsVo.setBookingCustomer(valueNullEmptyChecker(ycl.getCustomer().getCustomerName()));
  							   dmsVo.setClaimDateTo(dateCheckerAsGetString(ycl.getClaimDate()));
  							   dmsVo.setIncidentNo(valueNullEmptyChecker(ycl.getYmsIncident().getIncidentNumber()));
  							   dmsSearchVolist.add(dmsVo);    
  						   }
  					   }
			  	    
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchYmsClaimSearch : claim : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchYmsClaimSearch : claim : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchYmsClaimSearch : claim : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
	/*** ************************************* Shipment Search ********************************
	 * 	
	 * @param searchParamVO
	 * @return List<DMSSearchVO>
	 * Modified date : 29-07-2016 by rajanikant
	 */
	@SuppressWarnings("unchecked")
	private List<DMSSearchVO> searchShipmentDetails(DMSSearchVO searchParamVO) {
		
		
		List<DMSSearchVO> dmsSearchVoList=new ArrayList<DMSSearchVO>();
		String systemLine=searchParamVO.getSystemLine().trim();
		logger.debug("DMSDaoImpl :: searchShipmentDetails : START : searchParamVO ::systemLine : "+systemLine);
		logger.debug("DMSDaoImpl :: searchShipmentDetails : START : searchParamVO ::systemLine : "+systemLine);
		  if(systemLine.equalsIgnoreCase("OFS")){
			  dmsSearchVoList=searchOfsShipmentDetails(searchParamVO);
		  }else
		  if(systemLine.equalsIgnoreCase("YMS")){
			  dmsSearchVoList=searchYmsShipmentDetails(searchParamVO);  
		  }
		return dmsSearchVoList;
	}
		 

	private List<DMSSearchVO> searchYmsShipmentDetails(DMSSearchVO searchParamVO) {
		logger.debug("DMSDaoImpl :: searchYmsShipmentDetails : START : searchParamVO :: "+searchParamVO);
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(YmsShipment.class,"ss"); 
		           	criteria.createAlias("ss.company","comp")
	   					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
	   					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
	   					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
		            criteria.createAlias("ss.ymsShipmentAuditHistories", "shAudHs",CriteriaSpecification.LEFT_JOIN);
		           	criteria.addOrder(Order.desc("shAudHs.actionDate"));
					criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   
		             if(nullEmptyCheckerStringFlag(searchParamVO.getVesselName())){
		            	   criteria.createAlias("ss.vessel", "vesl",CriteriaSpecification.INNER_JOIN);
						   criteria.add(Restrictions.ilike("vesl.vesselName",searchParamVO.getVesselName().trim(),MatchMode.ANYWHERE));
					   }
					  if(nullEmptyCheckerStringFlag(searchParamVO.getVoyageNo())){
						   criteria.add(Restrictions.eq("ss.voyageNumber",searchParamVO.getVoyageNo().trim()));
					   }
		            if(nullEmptyCheckerStringFlag(searchParamVO.getEtaDate())){
						   criteria.add(Restrictions.between("ss.eta",getUtilDate(searchParamVO.getEtaDate()), getUtilDateEnd(searchParamVO.getEtaDate())));
					     }	
		            
					 if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getShipmentDateTo())){
						   logger.debug("shipment :fromDate :: "+searchParamVO.getShipmentDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getShipmentDateFrom())+"\n" +
						   		"shipment :toDate :: "+searchParamVO.getShipmentDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getShipmentDateTo()));
						
						 criteria.add(Restrictions.between("shAudHs.actionDate",getUtilDate(searchParamVO.getShipmentDateFrom()), getUtilDateEnd(searchParamVO.getShipmentDateTo())));
					     }
				  List<YmsShipment> shList=criteria.list();
					   if(shList!=null && !shList.isEmpty()){
						   
						   for(YmsShipment ss:shList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setShipmentNo(valueNullEmptyChecker(ss.getShipmentNumber()));
							   dmsVo.setVesselName(valueNullEmptyChecker(ss.getVessel()!=null?ss.getVessel().getVesselName():""));
							   dmsVo.setVoyageNo(valueNullEmptyChecker(ss.getVoyageNumber()));
							   dmsVo.setEtaDate(dateCheckerAsGetString(ss.getEta()));
							   dmsVo.setShipmentDateTo(dateCheckerAsGetString(ss.getYmsShipmentAuditHistories().iterator().next().getActionDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
		  	    
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchYmsShipmentDetails : shipping scheduler : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchYmsShipmentDetails : shipping scheduler : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchYmsShipmentDetails : shipping scheduler : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}
		private List<DMSSearchVO> searchOfsShipmentDetails(DMSSearchVO searchParamVO) {
			logger.debug("DMSDaoImpl :: searchOfsShipmentDetails : START : searchParamVO :: "+searchParamVO);
			Session session=null;
			Criteria criteria=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(ShippingSchedule.class,"ss");
			           
			             if(nullEmptyCheckerStringFlag(searchParamVO.getVesselName())){
			            	   criteria.createAlias("ss.vessel", "vesl",CriteriaSpecification.LEFT_JOIN);
							   criteria.add(Restrictions.ilike("vesl.vesselName","%"+searchParamVO.getVesselName().trim()+"%"));
						   }
						  if(nullEmptyCheckerStringFlag(searchParamVO.getVoyageNo())){
							   criteria.add(Restrictions.eq("ss.voyageNumber",searchParamVO.getVoyageNo().trim()));
						   }
			            if(nullEmptyCheckerStringFlag(searchParamVO.getEtaDate())){
  						   //criteria.add(Restrictions.eq("ss.departureEta",getUtilDate(searchParamVO.getEtaDate())));//getUtilDateEnd
  						   criteria.add(Restrictions.between("ss.departureEta",getUtilDate(searchParamVO.getEtaDate()), getUtilDateEnd(searchParamVO.getEtaDate())));
  					     }	
			            
    					 if(nullEmptyCheckerStringFlag(searchParamVO.getShipmentDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getShipmentDateTo())){
  						   logger.debug("shipment :fromDate :: "+searchParamVO.getShipmentDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getShipmentDateFrom())+"\n" +
  						   		"shipment :toDate :: "+searchParamVO.getShipmentDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getShipmentDateTo()));
  						//   criteria.add(Restrictions.ge("ss.shipmentDate",getUtilDate(searchParamVO.getShipmentDateFrom())));
  						//   criteria.add(Restrictions.le("ss.shipmentDate",getUtilDate(searchParamVO.getShipmentDateTo())));
  						 criteria.add(Restrictions.between("ss.shipmentDate",getUtilDate(searchParamVO.getShipmentDateFrom()), getUtilDateEnd(searchParamVO.getShipmentDateTo())));
  					     }
    				  List<ShippingSchedule> shList=criteria.list();
  					   if(shList!=null?!shList.isEmpty()?true:false:false){
  						   
  						   for(ShippingSchedule ss:shList){
  							   DMSSearchVO dmsVo=new DMSSearchVO();
  							   dmsVo.setShipmentNo(valueNullEmptyChecker(ss.getShipmentNumber()));
  							   dmsVo.setVesselName(valueNullEmptyChecker(ss.getVessel()!=null?ss.getVessel().getVesselName():""));
  							   dmsVo.setVoyageNo(valueNullEmptyChecker(ss.getVoyageNumber()));
  							   dmsVo.setEtaDate(dateCheckerAsGetString(ss.getDepartureEta()));
  							   dmsVo.setShipmentDateTo(dateCheckerAsGetString(ss.getShipmentDate()));
  							   dmsSearchVolist.add(dmsVo);    
  						   }
  					   }
			  	    
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchOfsShipmentDetails : shipping scheduler : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchOfsShipmentDetails : shipping scheduler : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchOfsShipmentDetails : shipping scheduler : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
/*** ******************************** Invoice Search ************************************
 * 
 * @param searchParamVO
 * @return
 */
		private List<DMSSearchVO> searchInvoiceDetails(DMSSearchVO searchParamVO) {
			
			String systemLine=searchParamVO.getSystemLine().trim();
			  if(systemLine.equalsIgnoreCase("OFS")){
				  return searchOfsInvoiceDetails(searchParamVO);
			  }else
				  if(systemLine.equalsIgnoreCase("TMS")){
				  return searchTmsInvoiceDetails(searchParamVO);
			  }else
				  if(systemLine.equalsIgnoreCase("YMS")){
					  return searchYmsInvoiceDetails(searchParamVO);
				  }
				  else{
					  return new ArrayList<DMSSearchVO>();
				  }
		}
		
	

	private List<DMSSearchVO> searchOfsInvoiceDetails(DMSSearchVO searchParamVO) {
	
		logger.debug("DMSDaoImpl :: searchOFSInvoiceDetails : START : searchParamVO :: "+searchParamVO);
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(Invoice.class,"inv");
		           criteria.createAlias("inv.customer", "cus",CriteriaSpecification.INNER_JOIN);
		           if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
					     }		
		            
					 if(nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateTo())){
						   logger.debug("searchOFSInvoiceDetails :fromDate :: "+searchParamVO.getInvoiceDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateFrom())+"\n" +
						   		"claim :toDate :: "+searchParamVO.getInvoiceDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateTo()));
						   //criteria.add(Restrictions.ge("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateFrom())));
						  // criteria.add(Restrictions.le("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateTo())));
						   criteria.add(Restrictions.between("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateFrom()), getUtilDateEnd(searchParamVO.getInvoiceDateTo())));
					     }
				  List<Invoice> invList=criteria.list();
					   if(invList!=null?!invList.isEmpty()?true:false:false){
						   
						   for(Invoice inv:invList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setInvoiceNo(valueNullEmptyChecker(inv.getInvoiceNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(inv.getCustomer().getCustomerName()));
							   dmsVo.setInvoiceDateTo(dateCheckerAsGetString(inv.getInvoiceDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
		  	     
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOFSInvoiceDetails : invoice : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOFSInvoiceDetails : invoice : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOFSInvoiceDetails : invoice : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
}
	
	private List<DMSSearchVO> searchTmsInvoiceDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("DMSDaoImpl :: searchTMSInvoiceDetails : START : searchParamVO :: "+searchParamVO);
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(TmsInvoice.class,"tmsInv");
		           criteria.createAlias("tmsInv.customer", "cus",CriteriaSpecification.LEFT_JOIN);
		           criteria.createAlias("tmsInv.company", "comp",CriteriaSpecification.INNER_JOIN);
		           
		  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
		  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
		  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
		  			}
		           if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
					     }		
		            
					 if(nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateTo())){
						   logger.debug("searchTMSInvoiceDetails :fromDate :: "+searchParamVO.getInvoiceDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateFrom())+"\n" +
						   		"searchTMSInvoiceDetails :toDate :: "+searchParamVO.getInvoiceDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateTo()));
						   //criteria.add(Restrictions.ge("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateFrom())));
						  // criteria.add(Restrictions.le("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateTo())));
						   criteria.add(Restrictions.between("tmsInv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateFrom()), getUtilDateEnd(searchParamVO.getInvoiceDateTo())));
					     }
				  List<TmsInvoice> invList=criteria.list();
					   if(invList!=null?!invList.isEmpty()?true:false:false){
						   
						   for(TmsInvoice inv:invList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setInvoiceNo(valueNullEmptyChecker(inv.getInvoiceNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(inv.getCustomer()!=null?inv.getCustomer().getCustomerName():""));
							   dmsVo.setInvoiceDateTo(dateCheckerAsGetString(inv.getInvoiceDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
		  	     
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchTMSInvoiceDetails : invoice : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchTMSInvoiceDetails : invoice : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchTMSInvoiceDetails : invoice : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}

    


private List<DMSSearchVO> searchYmsInvoiceDetails(DMSSearchVO searchParamVO) {

	logger.debug("DMSDaoImpl :: searchYmsInvoiceDetails : START : searchParamVO :: "+searchParamVO);
	Session session=null;
	Criteria criteria=null;
	List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
	try{
			   session=HibernateUtil.getSession();
	           criteria=session.createCriteria(YmsInvoice.class,"inv");
	           criteria.createAlias("inv.customer", "cus",CriteriaSpecification.INNER_JOIN);
	           criteria = criteria.createAlias("inv.company","comp")
	   			.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
	   			.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
	   			.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
	            criteria.addOrder(Order.desc("inv.invoiceDate"));
	   			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);  
	   			if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
					   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getBookingCustomer().trim(),MatchMode.ANYWHERE));
				     }		
	            
				 if(nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getInvoiceDateTo())){
					   logger.debug("searchYmsInvoiceDetails :fromDate :: "+searchParamVO.getInvoiceDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateFrom())+"\n" +
					   		"claim :toDate :: "+searchParamVO.getInvoiceDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getInvoiceDateTo()));
					   criteria.add(Restrictions.between("inv.invoiceDate",getUtilDate(searchParamVO.getInvoiceDateFrom()), getUtilDateEnd(searchParamVO.getInvoiceDateTo())));
				     }
			  List<YmsInvoice> invList=criteria.list();
				   if(invList!=null&&!invList.isEmpty()){					   
					   for(YmsInvoice inv:invList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setInvoiceNo(valueNullEmptyChecker(inv.getInvoiceNumber()));
						   dmsVo.setBookingCustomer(valueNullEmptyChecker(inv.getCustomer().getCustomerName()));
						   dmsVo.setInvoiceDateTo(dateCheckerAsGetString(inv.getInvoiceDate()));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
	  	     
			   
	}catch(HibernateException he){
		logger.error("DMSDaoImpl :: searchYmsInvoiceDetails : invoice : data fetches problem due to DB exception",he);
	}catch(Exception e){
		logger.error("DMSDaoImpl :: searchYmsInvoiceDetails : invoice : data fetches problem due to exception",e);
	}finally{
		HibernateUtil.closeSession(session);
		logger.debug("DMSDaoImpl :: searchYmsInvoiceDetails : invoice : "+dmsSearchVolist);
	}
	return dmsSearchVolist;
}

	/*** ************* Quotation Details ********************
	 * 	
	 * @param searchParamVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DMSSearchVO> searchQuotationDetails(DMSSearchVO searchParamVO) {
		
		
		List<DMSSearchVO> dmsSearchVoList=new ArrayList<DMSSearchVO>();
		String systemLine=searchParamVO.getSystemLine().trim();
		logger.debug("DMSDaoImpl :: searchQuotationDetails : START : searchParamVO ::systemLine : "+systemLine);
		  if(systemLine.equalsIgnoreCase("OFS")?true:false){
			  dmsSearchVoList=searchOfsQuotationDetails(searchParamVO);
		  }else
		  if(systemLine.equalsIgnoreCase("TMS")?true:false){
			  dmsSearchVoList=searchTmsQuotationDetails(searchParamVO);  
		  }else
			  if(systemLine.equalsIgnoreCase("YMS")?true:false){
				  dmsSearchVoList=searchYmsQuotationDetails(searchParamVO);  
			  }
		return dmsSearchVoList;
	}
	



private List<DMSSearchVO> searchOfsQuotationDetails(
			DMSSearchVO searchParamVO) {
		
	 logger.debug("DMSDaoImpl :: searchOfsQuotationDetails : Quotation : start :: ");
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(Quotation.class,"quo");
		             if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
		        		   criteria.createAlias("quo.customer", "cus",CriteriaSpecification.INNER_JOIN);
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getCustomerName().trim()+"%"));
					    }		
		            if(nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateTo())){
		            	 logger.debug("Quotation :fromDate :: "+searchParamVO.getQuotationDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateFrom())+
		            			 "Quotation :toDate :: "+searchParamVO.getQuotationDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateTo()));
					 criteria.add(Restrictions.between("quo.quotationDate",getUtilDate(searchParamVO.getQuotationDateFrom()), getUtilDateEnd(searchParamVO.getQuotationDateTo())));
		            }
					 
			    @SuppressWarnings("unchecked")
				List<Quotation> quoList=criteria.list();
					   if(quoList!=null?!quoList.isEmpty()?true:false:false){
						   
						   for(Quotation quo:quoList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setQuotationNo(valueNullEmptyChecker(quo.getQuotationNumber()));
							  // dmsVo.setQuotationDateFrom(dateCheckerAsGetString(quo.getQuotationValidFrom()));
							   dmsVo.setQuotationDateTo(dateCheckerAsGetString(quo.getQuotationDate()));
							   dmsVo.setCustomerName(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerName():""));
							   dmsVo.setCustomerCode(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerCode():""));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOfsQuotationDetails : Quotation : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOfsQuotationDetails : Quotation : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOfsQuotationDetails : Quotation : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}



private List<DMSSearchVO> searchTmsQuotationDetails(
			DMSSearchVO searchParamVO) {
	logger.debug("DMSDaoImpl :: searchTmsQuotationDetails : Quotation : start :: ");
	Session session=null;
	Criteria criteria=null;
	List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
	try{
			   session=HibernateUtil.getSession();
	           criteria=session.createCriteria(TmsQuotation.class,"tmsQ");
	           criteria.createAlias("tmsQ.company", "comp",CriteriaSpecification.INNER_JOIN);
	  		   
	  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
	  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
	  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
	  			}
	           if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
	        	   	   criteria.createAlias("tmsQ.customer", "cus",CriteriaSpecification.LEFT_JOIN);
					   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getCustomerName().trim()+"%"));
				     }		
	            if(nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateTo())){
	            	 logger.debug("Quotation :fromDate :: "+searchParamVO.getQuotationDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateFrom())+
	            			 "Quotation :toDate :: "+searchParamVO.getQuotationDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateTo()));
				 criteria.add(Restrictions.between("tmsQ.quotationDate",getUtilDate(searchParamVO.getQuotationDateFrom()), getUtilDateEnd(searchParamVO.getQuotationDateTo())));
	            }
				 
	            
				   @SuppressWarnings("unchecked")
				List<TmsQuotation> quoList=criteria.list();
				   if(quoList!=null&&!quoList.isEmpty()){
					   
					   for(TmsQuotation quo:quoList){
						   DMSSearchVO dmsVo=new DMSSearchVO();
						   dmsVo.setQuotationNo(valueNullEmptyChecker(quo.getQuotationNumber()));
						   dmsVo.setQuotationDateFrom(dateCheckerAsGetString(quo.getQuotationValidFrom()));
						   dmsVo.setQuotationDateTo(dateCheckerAsGetString(quo.getQuotationValidTo()));
						   dmsVo.setCustomerName(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerName():""));
						   dmsVo.setCustomerCode(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerCode():""));
						   dmsSearchVolist.add(dmsVo);    
					   }
				   }
			   
	}catch(HibernateException he){
		logger.error("DMSDaoImpl :: searchTmsQuotationDetails : Quotation : data fetches problem due to DB exception",he);
	}catch(Exception e){
		logger.error("DMSDaoImpl :: searchTmsQuotationDetails : Quotation : data fetches problem due to exception",e);
	}finally{
		HibernateUtil.closeSession(session);
		logger.debug("DMSDaoImpl :: searchTmsQuotationDetails : Quotation : "+dmsSearchVolist);
	}
	return dmsSearchVolist;
	}


	/**
	 * Search YMS Quotation
	 * @param searchParamVO
	 * @return
	 */
	
	private List<DMSSearchVO> searchYmsQuotationDetails(
				DMSSearchVO searchParamVO) {
			
		 logger.debug("DMSDaoImpl :: searchYmsQuotationDetails : Quotation : start :: ");
			Session session=null;
			Criteria criteria=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(YmsQuotation.class,"quo");
				       	criteria.createAlias("quo.company","comp")
	   					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
	   					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
	   					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
				       	criteria.addOrder(Order.desc("quo.quotationDate"));
						criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);   	
			             if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
			        		   criteria.createAlias("quo.customer", "cus",CriteriaSpecification.INNER_JOIN);
							   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getCustomerName().trim(),MatchMode.ANYWHERE));
						    }		
			            if(nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getQuotationDateTo())){
			            	 logger.debug("Quotation :fromDate :: "+searchParamVO.getQuotationDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateFrom())+
			            			 "Quotation :toDate :: "+searchParamVO.getQuotationDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getQuotationDateTo()));
						 criteria.add(Restrictions.between("quo.quotationDate",getUtilDate(searchParamVO.getQuotationDateFrom()), getUtilDateEnd(searchParamVO.getQuotationDateTo())));
			            }
						 
				    @SuppressWarnings("unchecked")
					List<YmsQuotation> quoList=criteria.list();
						   if(quoList!=null&&!quoList.isEmpty()){
							   
							   for(YmsQuotation quo:quoList){
								   DMSSearchVO dmsVo=new DMSSearchVO();
								   dmsVo.setQuotationNo(valueNullEmptyChecker(quo.getQuotationNumber()));
								  // dmsVo.setQuotationDateFrom(dateCheckerAsGetString(quo.getQuotationValidFrom()));
								   dmsVo.setQuotationDateTo(dateCheckerAsGetString(quo.getQuotationDate()));
								   dmsVo.setCustomerName(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerName():""));
								   dmsVo.setCustomerCode(valueNullEmptyChecker(quo.getCustomer()!=null?quo.getCustomer().getCustomerCode():""));
								   dmsSearchVolist.add(dmsVo);    
							   }
						   }
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchYmsQuotationDetails : Quotation : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchYmsQuotationDetails : Quotation : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchYmsQuotationDetails : Quotation : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}

/** *************************************TMS LoadDetails Search ******************************
  * 
  * @param searchParamVO
  * @return
  */

	private List<DMSSearchVO> searchLoadDetials(DMSSearchVO searchParamVO) {
		
		Session session=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		String systemLine=searchParamVO.getSystemLine().trim();
		logger.debug("DMSDaoImpl :: searchLoadDetials : START : searchParamVO :: "+searchParamVO);
		 try{
			 if(systemLine.equalsIgnoreCase("TMS")?true:false){
			 session=HibernateUtil.getSession();
			 Criteria cri=session.createCriteria(TmsLoadPlanning.class,"lp");
			 cri.createAlias("lp.truck", "tk", CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("tk.masterSetupByTruckTypeMasterId", "tkType", CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("lp.tmsBookingLoadDetail", "bkld",CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("bkld.tmsBooking", "bk",CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("bkld.masterSetupByPickupLocationMasterId", "pkLM",CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("bkld.masterSetupByDeliveryLocationMasterId", "dlLM",CriteriaSpecification.INNER_JOIN);
			 cri.createAlias("lp.company", "comp",CriteriaSpecification.INNER_JOIN);
	  		   
	  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
	  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
	  			    cri.add(Restrictions.eq("comp.companyId", branchId));
	  			}
			 if(nullEmptyCheckerStringFlag(searchParamVO.getBookingCustomer())){
			 	    cri.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBookingCustomer().trim()+"%"));
			 }
			 if(nullEmptyCheckerStringFlag(searchParamVO.getPickupLocation())){
			 	    cri.add(Restrictions.ilike("pkLM.setupDescription","%"+searchParamVO.getPickupLocation().trim()+"%"));
			 }
			 if(nullEmptyCheckerStringFlag(searchParamVO.getDeliveryLocation())){
			 	    cri.add(Restrictions.ilike("dlLM.setupDescription","%"+searchParamVO.getDeliveryLocation().trim()+"%"));
			 }
			 if(nullEmptyCheckerStringFlag(searchParamVO.getTruckType())){
			 	    cri.add(Restrictions.ilike("tkType.setupDescription","%"+searchParamVO.getTruckType().trim()+"%"));
			 }
			 if(nullEmptyCheckerStringFlag(searchParamVO.getBookingDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getBookingDateTo())){
						   logger.debug("load Bk:fromDate :: "+searchParamVO.getBookingDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateFrom())+"\n" +
						   		"load Bk :toDate :: "+searchParamVO.getBookingDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getBookingDateTo()));
						  // cri.add(Restrictions.ge("inv.invoiceDate",getUtilDate(searchParamVO.getBookingDateFrom())));
						   //cri.add(Restrictions.le("ss.invoiceDate",getUtilDate(searchParamVO.getBookingDateTo())));
						   cri.add(Restrictions.between("bk.bookingDate",getUtilDate(searchParamVO.getBookingDateFrom()), getUtilDateEnd(searchParamVO.getBookingDateTo())));
			   }
				  List<TmsLoadPlanning> loadList=cri.list();
					   if(loadList!=null?!loadList.isEmpty()?true:false:false){
						   for(TmsLoadPlanning load:loadList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setLoadNo(valueNullEmptyChecker(load.getTmsBookingLoadDetail().getBookingLoadNumber()));
							   dmsVo.setBookingNo(valueNullEmptyChecker(load.getTmsBookingLoadDetail().getTmsBooking().getBookingNumber()));
							   dmsVo.setBookingCustomer(valueNullEmptyChecker(load.getTmsBookingLoadDetail().getTmsBooking().getCustomer().getCustomerName()));
							   dmsVo.setBookingDate(dateCheckerAsGetString(load.getTmsBookingLoadDetail().getTmsBooking().getBookingDate()));
							   dmsVo.setPickupLocation(valueNullEmptyChecker(load.getTmsBookingLoadDetail().getMasterSetupByPickupLocationMasterId().getSetupDescription()));
							   dmsVo.setDeliveryLocation(valueNullEmptyChecker(load.getTmsBookingLoadDetail().getMasterSetupByDeliveryLocationMasterId().getSetupDescription()));
							   dmsVo.setTruckType(valueNullEmptyChecker(load.getTruck().getMasterSetupByTruckTypeMasterId().getSetupDescription()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
		  	    
			 }
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchLoadDetials :  : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchLoadDetials :  : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchLoadDetials :  : "+dmsSearchVolist);
		}
	    
		return dmsSearchVolist;
	}

	
	private List<DMSSearchVO> searchPoDetails(DMSSearchVO searchParamVO) throws Exception {
		
		List<DMSSearchVO> dmsSearchVoList=new ArrayList<DMSSearchVO>();
		String systemLine=searchParamVO.getSystemLine().trim();
		logger.debug("DMSDaoImpl :: searchPoDetails : START : searchParamVO ::systemLine : "+systemLine);
		  if(systemLine.equalsIgnoreCase("OFS")){
			  dmsSearchVoList=searchOfsPODetails(searchParamVO);
		  }else
		  if(systemLine.equalsIgnoreCase("TMS")){
			  dmsSearchVoList=searchTmsPODetails(searchParamVO);  
		  }else
			  if(systemLine.equalsIgnoreCase("YMS")){
				  dmsSearchVoList=searchYmsPODetails(searchParamVO);  
			  }
		return dmsSearchVoList;	
	}
	
	private List<DMSSearchVO> searchOfsPODetails(DMSSearchVO searchParamVO) {
		
		logger.debug("DMSDaoImpl :: searchOfsPODetails : start ... : ");
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(PurchaseOrder.class,"po");
		           criteria.createAlias("po.vendor", "vdor",CriteriaSpecification.INNER_JOIN);
		           if(nullEmptyCheckerStringFlag(searchParamVO.getVendorName())){
					   criteria.add(Restrictions.ilike("vdor.vendorName","%"+searchParamVO.getVendorName().trim()+"%"));
				   }		
		         
		           if(nullEmptyCheckerStringFlag(searchParamVO.getPoDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getPoDateTo())){
					   logger.debug("PO :fromDate :: "+searchParamVO.getPoDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateFrom())+"\n" +
					   		"PO :toDate :: "+searchParamVO.getPoDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateTo()));
					   criteria.add(Restrictions.between("po.poDate",getUtilDate(searchParamVO.getPoDateFrom()), getUtilDateEnd(searchParamVO.getPoDateTo())));
		            } 
				  @SuppressWarnings("unchecked")
				List<PurchaseOrder> poList=criteria.list();
					   if(poList!=null?!poList.isEmpty()?true:false:false){
						   for(PurchaseOrder po:poList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setPoNo(valueNullEmptyChecker(po.getPoNumber()));
							   dmsVo.setPoDateTo(dateCheckerAsGetString(po.getPoDate()));
							   dmsVo.setVendorName(valueNullEmptyChecker(po.getVendor().getVendorName()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchOfsPODetails :  : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchOfsPODetails :  : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchOfsPODetails :  : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}
		private List<DMSSearchVO> searchTmsPODetails(DMSSearchVO searchParamVO) {
		
			logger.debug("DMSDaoImpl :: searchTmsPODetails : start : ");
			Session session=null;
			Criteria criteria=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(TmsPurchaseOrder.class,"tmsPo");
			           criteria.createAlias("tmsPo.company", "comp",CriteriaSpecification.INNER_JOIN);
			  		   
			  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
			  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
			  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
			  			}
			           criteria.createAlias("tmsPo.vendorByVendorId", "vdor",CriteriaSpecification.INNER_JOIN);
			              if(nullEmptyCheckerStringFlag(searchParamVO.getVendorName())){
						   criteria.add(Restrictions.ilike("vdor.vendorName","%"+searchParamVO.getVendorName().trim()+"%"));
					   }		
			         
			         if(nullEmptyCheckerStringFlag(searchParamVO.getPoDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getPoDateTo())){
						   logger.debug("Tms PO :fromDate :: "+searchParamVO.getPoDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateFrom())+"\n" +
						   		"Tms PO :toDate :: "+searchParamVO.getPoDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateTo()));
						   criteria.add(Restrictions.between("tmsPo.poDate",getUtilDate(searchParamVO.getPoDateFrom()), getUtilDateEnd(searchParamVO.getPoDateTo())));
			            } 
					
						  @SuppressWarnings("unchecked")
						List<TmsPurchaseOrder> tmsPoList=criteria.list();
						   if(tmsPoList!=null?!tmsPoList.isEmpty()?true:false:false){
							   
							   for(TmsPurchaseOrder poT:tmsPoList){
								   DMSSearchVO dmsVo=new DMSSearchVO();
								   dmsVo.setPoNo(valueNullEmptyChecker(poT.getPoNumber()));
								   dmsVo.setPoDateTo(dateCheckerAsGetString(poT.getPoDate()));
								   dmsVo.setVendorName(valueNullEmptyChecker(poT.getVendorByVendorId().getVendorName()));
								   dmsSearchVolist.add(dmsVo);    
							   }
						   }
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchTmsPODetails :  : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchTmsPODetails :  : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchTmsPODetails :  : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
	}


		private List<DMSSearchVO> searchYmsPODetails(DMSSearchVO searchParamVO) {
			
			logger.debug("DMSDaoImpl :: searchYmsPODetails : start ... : ");
			Session session=null;
			Criteria criteria=null;
			List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
			try{
					   session=HibernateUtil.getSession();
			           criteria=session.createCriteria(YmsPurchaseOrder.class,"po");
			           criteria.createAlias("po.vendor", "vdor",CriteriaSpecification.INNER_JOIN);
			           criteria = criteria.createAlias("po.company","comp")
		   					.createAlias("comp.masterSetupByCompanyMasterId","ms",CriteriaSpecification.INNER_JOIN)                   
		   					.add(Restrictions.eq("ms.setupCode", searchParamVO.getCompanyCode()))
		   					.add(Restrictions.eq("comp.branchCode", searchParamVO.getBranchCode()));
			           criteria.addOrder(Order.desc("po.poDate"));
			           criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);  
			           if(nullEmptyCheckerStringFlag(searchParamVO.getVendorName())){
						   criteria.add(Restrictions.ilike("vdor.vendorName","%"+searchParamVO.getVendorName().trim()+"%"));
					   }		
			         
			           if(nullEmptyCheckerStringFlag(searchParamVO.getPoDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getPoDateTo())){
						   logger.debug("PO :fromDate :: "+searchParamVO.getPoDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateFrom())+"\n" +
						   		"PO :toDate :: "+searchParamVO.getPoDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getPoDateTo()));
						   criteria.add(Restrictions.between("po.poDate",getUtilDate(searchParamVO.getPoDateFrom()), getUtilDateEnd(searchParamVO.getPoDateTo())));
			            } 
					  @SuppressWarnings("unchecked")
					List<YmsPurchaseOrder> poList=criteria.list();
						   if(poList!=null?!poList.isEmpty()?true:false:false){
							   for(YmsPurchaseOrder po:poList){
								   DMSSearchVO dmsVo=new DMSSearchVO();
								   dmsVo.setPoNo(valueNullEmptyChecker(po.getPoNumber()));
								   dmsVo.setPoDateTo(dateCheckerAsGetString(po.getPoDate()));
								   dmsVo.setVendorName(valueNullEmptyChecker(po.getVendor().getVendorName()));
								   dmsSearchVolist.add(dmsVo);    
							   }
						   }
					   
			}catch(HibernateException he){
				logger.error("DMSDaoImpl :: searchYmsPODetails :  : data fetches problem due to DB exception",he);
			}catch(Exception e){
				logger.error("DMSDaoImpl :: searchYmsPODetails :  : data fetches problem due to exception",e);
			}finally{
				HibernateUtil.closeSession(session);
				logger.debug("DMSDaoImpl :: searchYmsPODetails :  : "+dmsSearchVolist);
			}
			return dmsSearchVolist;
		}
	
	/** **************************** DO Search *********************** * 
	 * @param searchParamVO
	 * @return
	 */
	private List<DMSSearchVO> searchDoDetails(DMSSearchVO searchParamVO) {
		
		List<DMSSearchVO> dmsSearchVoList=new ArrayList<DMSSearchVO>();
		String systemLine=searchParamVO.getSystemLine().trim();
		logger.debug("DMSDaoImpl :: searchDoDetails : START : searchParamVO ::systemLine : "+systemLine);
		  if(systemLine.equalsIgnoreCase("OFS")?true:false){
			  //...if need
		  }else
		  if(systemLine.equalsIgnoreCase("TMS")?true:false){
			  dmsSearchVoList=searchTmsDODetails(searchParamVO);  
		  }
		return dmsSearchVoList;
	}
	




	private List<DMSSearchVO> searchTmsDODetails(DMSSearchVO searchParamVO) {
		
		Session session=null;
		Criteria criteria=null;
		logger.debug("DMSDaoImpl :: searchTmsDODetails : START : searchParamVO :: "+searchParamVO);
		List<DMSSearchVO> dmsSearchVolist=new ArrayList<DMSSearchVO>();
		try{
				   session=HibernateUtil.getSession();
		          criteria=session.createCriteria(TmsDeliveryOrder.class,"dvOdr");
		           criteria.createAlias("dvOdr.customerAddress", "cusAdd",CriteriaSpecification.INNER_JOIN);
		           criteria.createAlias("cusAdd.customer", "cus",CriteriaSpecification.INNER_JOIN);
		           
		           criteria.createAlias("dvOdr.masterSetupByPickupLocationMasterId", "doPkLM",CriteriaSpecification.INNER_JOIN);
		           criteria.createAlias("dvOdr.masterSetupByDeliveryLocationMasterId", "doDkLM",CriteriaSpecification.INNER_JOIN);
		           
		           criteria.createAlias("dvOdr.tmsBookingLoadDetail", "tmsBkLD",CriteriaSpecification.INNER_JOIN);
		           criteria.createAlias("tmsBkLD.tmsBooking", "tmsBk",CriteriaSpecification.INNER_JOIN);
		           
		           criteria.createAlias("dvOdr.company", "comp",CriteriaSpecification.INNER_JOIN);
		  		    if(nullEmptyCheckerStringFlag(searchParamVO.getBranchCode()) &&nullEmptyCheckerStringFlag(searchParamVO.getCompanyCode())){
		  			    Long branchId= Long.parseLong(getCompanyIDByBCodeAndCCode(searchParamVO.getCompanyCode(),searchParamVO.getBranchCode()));
		  			    criteria.add(Restrictions.eq("comp.companyId", branchId));
		  			}
		           
		           if(nullEmptyCheckerStringFlag(searchParamVO.getBillingCustomer())){
						   criteria.add(Restrictions.ilike("cus.customerName","%"+searchParamVO.getBillingCustomer().trim()+"%"));
					     }	
		           if(nullEmptyCheckerStringFlag(searchParamVO.getPickupLocation())){
					   criteria.add(Restrictions.ilike("doPkLM.setupDescription","%"+searchParamVO.getPickupLocation().trim()+"%"));
				     }
		           if(nullEmptyCheckerStringFlag(searchParamVO.getDeliveryLocation())){
					   criteria.add(Restrictions.ilike("doDkLM.setupDescription","%"+searchParamVO.getDeliveryLocation().trim()+"%"));
				     }
		           if(nullEmptyCheckerStringFlag(searchParamVO.getBookingNo())){
					   criteria.add(Restrictions.like("tmsBk.bookingNumber",searchParamVO.getBookingNo().trim()));
				     }
		           if(nullEmptyCheckerStringFlag(searchParamVO.getLoadNo())){
					   criteria.add(Restrictions.eq("tmsBkLD.bookingLoadNumber",searchParamVO.getLoadNo().trim()));
				     }
		           if(nullEmptyCheckerStringFlag(searchParamVO.getPickupDate())){
					   //criteria.add(Restrictions.eq("dvOdr.pickupDate",getUtilDate(searchParamVO.getPickupDate())));
					   criteria.add(Restrictions.between("dvOdr.pickupDate",getUtilDate(searchParamVO.getPickupDate()), getUtilDateEnd(searchParamVO.getPickupDate())));
				     }
		           if(nullEmptyCheckerStringFlag(searchParamVO.getDeliveryDate())){
					  //criteria.add(Restrictions.eq("dvOdr.deliveredDate",getUtilDate(searchParamVO.getDeliveryDate())));
					   criteria.add(Restrictions.between("dvOdr.deliveredDate",getUtilDate(searchParamVO.getDeliveryDate()), getUtilDateEnd(searchParamVO.getDeliveryDate())));
				     }
		            
					 if(nullEmptyCheckerStringFlag(searchParamVO.getDoDateFrom())&&nullEmptyCheckerStringFlag(searchParamVO.getDoDateTo())){
						   logger.debug("DO :fromDate :: "+searchParamVO.getDoDateFrom()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDoDateFrom())+"\n" +
						   		"DO :toDate :: "+searchParamVO.getDoDateTo()+"\n :: sql Date :"+getUtilDate(searchParamVO.getDoDateTo()));
						   criteria.add(Restrictions.between("dvOdr.doDate",getUtilDate(searchParamVO.getDoDateFrom()), getUtilDateEnd(searchParamVO.getDoDateTo())));
					     }
				  @SuppressWarnings("unchecked")
				List<TmsDeliveryOrder> delvOdrList=criteria.list();
					   if(delvOdrList!=null?!delvOdrList.isEmpty()?true:false:false){
						   
						   for(TmsDeliveryOrder dOv:delvOdrList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setDoNo(valueNullEmptyChecker(dOv.getDoNumber()));
							   dmsVo.setBillingCustomer(valueNullEmptyChecker(dOv.getCustomerAddress().getCustomer().getCustomerName()));
							   dmsVo.setPickupLocation(valueNullEmptyChecker(dOv.getMasterSetupByPickupLocationMasterId().getSetupDescription()));
							   dmsVo.setDeliveryLocation(valueNullEmptyChecker(dOv.getMasterSetupByDeliveryLocationMasterId().getSetupDescription()));
							   dmsVo.setBookingNo(valueNullEmptyChecker(dOv.getTmsBookingLoadDetail().getTmsBooking().getBookingNumber()));
							   dmsVo.setLoadNo(valueNullEmptyChecker(dOv.getTmsBookingLoadDetail().getBookingLoadNumber()));
							   dmsVo.setPickupDate(dateCheckerAsGetString(dOv.getPickupDate()));
							   dmsVo.setDeliveryDate(dateCheckerAsGetString(dOv.getDeliveredDate()));
							   dmsVo.setDoDateTo(dateCheckerAsGetString(dOv.getDoDate()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchTmsDODetails :  : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchTmsDODetails :  : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchTmsDODetails :  : "+dmsSearchVolist);
		}
		return dmsSearchVolist;
	}



	/*** ************************* Booking Customer ********************************
	 * 
	 * @param searchParamVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DMSSearchVO> searchBookingCustomerDetails(DMSSearchVO searchParamVO) {
		
		logger.debug("DMSDaoImpl :: searchBookingCustomerDetails : START : searchParamVO :: "+searchParamVO);
		Session session=null;
		Criteria criteria=null;
		List<DMSSearchVO> dmsSearchVolist=null;
		List<DMSSearchVO> dmsSearchVolist2=null;
		@SuppressWarnings("rawtypes")
		Class className=null;
		String systemLine=searchParamVO.getSystemLine().trim();
		  if(systemLine.equalsIgnoreCase("OFS")?true:false){
			  className=Booking.class;
		  }else
			  
			  if(systemLine.equalsIgnoreCase("TMS")?true:false){
			  className=TmsBooking.class;
			  }else
				  if(systemLine.equalsIgnoreCase("YMS")?true:false){
					  className=YmsBooking.class;
					  }
		  
		try{
			dmsSearchVolist=new ArrayList<DMSSearchVO>();
				   session=HibernateUtil.getSession();
		           criteria=session.createCriteria(className,"bk");		           
		           criteria.createAlias("bk.customer", "cus",CriteriaSpecification.INNER_JOIN);
		           if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerName())){
						   criteria.add(Restrictions.ilike("cus.customerName",searchParamVO.getCustomerName().trim(),MatchMode.ANYWHERE));
					     }		
		           if(nullEmptyCheckerStringFlag(searchParamVO.getCustomerCode())){
					   criteria.add(Restrictions.eq("cus.customerCode",searchParamVO.getCustomerCode().trim()));
				     }
			    if(systemLine.equalsIgnoreCase("OFS")){ 
				  List<Booking> bkList=criteria.list();
					   if(bkList!=null&&!bkList.isEmpty()){
						   
						   for(Booking bk:bkList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setCustomerName(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
							   dmsVo.setCustomerCode(valueNullEmptyChecker(bk.getCustomer().getCustomerCode()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
				 } 
			    else
				  if(systemLine.equalsIgnoreCase("TMS")){
					  
					  List<TmsBooking> bkList=criteria.list();
					   if(bkList!=null&&!bkList.isEmpty()){
						   
						   for(TmsBooking bk:bkList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setCustomerName(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
							   dmsVo.setCustomerCode(valueNullEmptyChecker(bk.getCustomer().getCustomerCode()));
							   dmsSearchVolist.add(dmsVo);    
						   }
					   }
			    }else
			    	if(systemLine.equalsIgnoreCase("YMS")){
			    		 List<YmsBooking> bkList=criteria.list();
			    		   for(YmsBooking bk:bkList){
							   DMSSearchVO dmsVo=new DMSSearchVO();
							   dmsVo.setCustomerName(valueNullEmptyChecker(bk.getCustomer().getCustomerName()));
							   dmsVo.setCustomerCode(valueNullEmptyChecker(bk.getCustomer().getCustomerCode()));
							   dmsSearchVolist.add(dmsVo);    
						   }
			       }
			    dmsSearchVolist2=getUniqueBookingCustomer(dmsSearchVolist);
				   
		}catch(HibernateException he){
			logger.error("DMSDaoImpl :: searchBookingCustomerDetails :  : data fetches problem due to DB exception",he);
		}catch(Exception e){
			logger.error("DMSDaoImpl :: searchBookingCustomerDetails :  : data fetches problem due to exception",e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: searchBookingCustomerDetails :  : "+dmsSearchVolist2);
		}
		return dmsSearchVolist2;
		  
	}

   private List<DMSSearchVO> getUniqueBookingCustomer(List<DMSSearchVO> dmsSearchVoList){
	   List<DMSSearchVO> uniqueDmsSVO=null;
	   if(dmsSearchVoList!=null && !dmsSearchVoList.isEmpty()){
		   uniqueDmsSVO=new ArrayList<DMSSearchVO>();
		   Set<String> s1=new HashSet<String>();
		      for(DMSSearchVO dmsSVo: dmsSearchVoList){
		    	  if(!s1.contains(dmsSVo.getCustomerCode().trim())){
		    	         s1.add(dmsSVo.getCustomerCode());
		    	         DMSSearchVO dVO=new DMSSearchVO();
		    	         dVO.setCustomerName(dmsSVo.getCustomerName().trim());
		    	         dVO.setCustomerCode(dmsSVo.getCustomerCode().trim());
		    	         uniqueDmsSVO.add(dVO);
		    	  }
		      }
	   }else{
		   uniqueDmsSVO=new ArrayList<DMSSearchVO>();
	   }
	 return uniqueDmsSVO;
   }
	
   private java.util.Date getUtilDate(String date){
	   if(date==null?true:date.trim().isEmpty()?true:false)return null;
	   return DateUtil.getDate(date.trim(), DateUtil.FORMAT_SLASH_DD_MM_YYYY);
   }
   private java.util.Date getUtilDateEnd(String date){
	   if(date==null?true:date.trim().isEmpty()?true:false)return null;

		Date dateEndTime=DateUtil.getDate(date,DateUtil.FORMAT_SLASH_DD_MM_YYYY);
		dateEndTime.setHours(23);
		dateEndTime.setMinutes(59);
		dateEndTime.setSeconds(59);
		
	   return dateEndTime;
   }
	
   
	private boolean nullChekerMap(Object object){
		return object==null?true:false;
	} 
	
	private boolean nullEmptyCheckerStringFlag(String str){
		return str!=null?str.trim().isEmpty()?false:true:false;
	}
	private String valueNullEmptyChecker(String str){
		return str!=null?str.trim().isEmpty()?"":str:"";
	}
	private String dateCheckerAsGetString(Date dt){
	  return dt!=null?DateUtil.getDateAsString(dt, DateUtil.FORMAT_SLASH_DD_MM_YYYY):"";	
	}
	



	@Override
	public DMSWebUploadVO populateDetails(DMSGType searchType, DMSWebUploadVO searchParam) throws DataAccessException {

		if(nullChekerMap(searchParam)){
			 logger.debug("DMSDaoImpl ::populateDetails(-,-): searchParam contains Null ,unable to process search :: searchType"+searchType);
			 return new DMSWebUploadVO();
		}else{
			if(searchType.equals(DMSGType.DMS_BOOKINGNO_PODS)){
				return loadBookingPODS(searchParam);
			}
			else if(searchType.equals(DMSGType.DMS_BOOKINGNO_SERVICETYPES)){
				return loadBookingSerTypes(searchParam);
			}
			else if(searchType.equals(DMSGType.DMS_BOOKINGNO_SERVICELINES)){
				return loadBookingSerLines(searchParam);
			}			
			else if(searchType.equals(DMSGType.DMS_BOOKINGNO_CARGOTYPES)){
				return loadBookingCargoTypes(searchParam);
			}
			else if(searchType.equals(DMSGType.DMS_LOAD_BOOKINGCUS_AND_BUSINESSLINE)){
				return loadBookingCustomerNBussLine(searchParam);
			}
		}
		return null;
	}
	
	//Below method will list all associated service lines associated to service type
	private DMSWebUploadVO loadBookingSerLines(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::loadBookingSerLines : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		Map<String,String> serLinesArray = new HashMap<String,String>(0);
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<BookingDetail> bookingList = new ArrayList<BookingDetail>(0);	  
			  
			  Criteria criteria=session.createCriteria(BookingDetail.class,"bkd");
			  criteria.createAlias("bkd.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  criteria.createAlias("bkd.masterSetupByServiceTypeMasterId", "sl",CriteriaSpecification.INNER_JOIN);
			  if(searchParam.getBookingNo() != null){
				  criteria.add(Restrictions.eq("bk.bookingNumber",searchParam.getBookingNo()));					  
			  }
			  if(searchParam.getServiceType()!=null && searchParam.getServiceType() !=""){
				  criteria.add(Restrictions.eq("sl.masterId",Long.parseLong(searchParam.getServiceType())));
			  }			  
			  bookingList=criteria.list();
			  
			  if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
				  for(BookingDetail bookingDtl : bookingList){
					  serLinesArray.put(String.valueOf(bookingDtl.getMasterSetupByServiceLineMasterId().getMasterId()), 
							  bookingDtl.getMasterSetupByServiceLineMasterId().getSetupDescription());
				  }
				  dmsSearchVoObj.setServiceLines(serLinesArray);
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
			Criteria criteria = session
						.createCriteria(TmsBookingCommonServiceDetail.class, "commSer")
						.createAlias("commSer.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
						.createAlias("commSer.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
						.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()))
						.add(Restrictions.eq("st.masterId",Long.parseLong(searchParam.getServiceType())));
			
			Criteria criteria2 = session
					.createCriteria(TmsBookingLoadDetail.class, "loadDetails")
					.createAlias("loadDetails.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
					.createAlias("loadDetails.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
					.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()))
					.add(Restrictions.eq("st.masterId",Long.parseLong(searchParam.getServiceType())));

			List<TmsBookingCommonServiceDetail> csvList = (List<TmsBookingCommonServiceDetail>) criteria.list();
			List<TmsBookingLoadDetail> loadDtlsList = (List<TmsBookingLoadDetail>) criteria2.list();

			for (TmsBookingCommonServiceDetail comSv : csvList) {
				serLinesArray.put(String.valueOf(comSv.getMasterSetupByServiceLineMasterId()
							.getMasterId()), comSv
							.getMasterSetupByServiceLineMasterId()
							.getSetupDescription());
			}
			for (TmsBookingLoadDetail loadDtl : loadDtlsList) {
				serLinesArray.put(String.valueOf(loadDtl.getMasterSetupByServiceLineMasterId()
						.getMasterId()), loadDtl
						.getMasterSetupByServiceLineMasterId()
						.getSetupDescription());
			}
			dmsSearchVoObj.setServiceLines(serLinesArray);					
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingSerLines : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingSerLines : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingSerLines : session closed ");
		}
		
		logger.debug("DMSDAOImpl :: Service Lines are : "+dmsSearchVoObj);
		return dmsSearchVoObj;
	}	
	
	//Below method will list all associated service types associated to booking
	@SuppressWarnings("unchecked")
	private DMSWebUploadVO loadBookingSerTypes(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::loadBookingSerTypes : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		Map<String,String> serTypesArray = new HashMap<String,String>(0);
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<BookingDetail> bookingList = new ArrayList<BookingDetail>(0);		  
			  
			  Criteria criteria=session.createCriteria(BookingDetail.class,"bkd");
			  criteria.createAlias("bkd.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  criteria.add(Restrictions.eq("bk.bookingNumber",searchParam.getBookingNo()));			  
			  bookingList=criteria.list();
			  
			  if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
				  for(BookingDetail bookingDtl : bookingList){
					  serTypesArray.put(String.valueOf(bookingDtl.getMasterSetupByServiceTypeMasterId().getMasterId()), 
							  bookingDtl.getMasterSetupByServiceTypeMasterId().getSetupDescription());
				  }
				  dmsSearchVoObj.setServiceTypes(serTypesArray);
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
				Criteria criteria = session
							.createCriteria(TmsBookingCommonServiceDetail.class, "commSer")
							.createAlias("commSer.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
							.createAlias("commSer.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
							.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()));
				
				Criteria criteria2 = session
						.createCriteria(TmsBookingLoadDetail.class, "loadDetails")
						.createAlias("loadDetails.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
						.createAlias("loadDetails.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
						.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()));

				List<TmsBookingCommonServiceDetail> csvList = (List<TmsBookingCommonServiceDetail>) criteria.list();
				List<TmsBookingLoadDetail> loadDtlsList = (List<TmsBookingLoadDetail>) criteria2.list();

				for (TmsBookingCommonServiceDetail comSv : csvList) {
						  serTypesArray.put(String.valueOf(comSv.getMasterSetupByServiceTypeMasterId()
								.getMasterId()), comSv
								.getMasterSetupByServiceTypeMasterId()
								.getSetupDescription());
				}
				for (TmsBookingLoadDetail loadDtl : loadDtlsList) {
					  serTypesArray.put(String.valueOf(loadDtl.getMasterSetupByServiceTypeMasterId()
							.getMasterId()), loadDtl
							.getMasterSetupByServiceTypeMasterId()
							.getSetupDescription());
			}
			dmsSearchVoObj.setServiceTypes(serTypesArray);					
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingSerTypes : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingSerTypes : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingSerTypes : session closed ");
		}
		return dmsSearchVoObj;
	}
	
	
	//PODS should be populated based on service type selected
	@SuppressWarnings("unchecked")
	private DMSWebUploadVO loadBookingPODS(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::searchBookingPODS : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		Map<String,String> podArray = new HashMap<String,String>(0);
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<BookingDetail> bookingList = new ArrayList<BookingDetail>(0);		  
			  
			  Criteria criteria=session.createCriteria(BookingDetail.class,"bkd");
			  criteria.createAlias("bkd.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  criteria.createAlias("bkd.masterSetupByServiceTypeMasterId", "st",CriteriaSpecification.INNER_JOIN);
			  
			  criteria.add(Restrictions.eq("bk.bookingNumber",searchParam.getBookingNo()));
			  if(searchParam.getServiceType()!=null && searchParam.getServiceType() !=""){
				  criteria.add(Restrictions.eq("st.masterId",Long.parseLong(searchParam.getServiceType())));
			  }
			  
			  bookingList=criteria.list();
			  
			  if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
				  for(BookingDetail bookingDtl : bookingList){
					  
					  podArray.put(String.valueOf(bookingDtl.getPort().getPortId()), 
							  		bookingDtl.getPort().getPortName());
				  }
				  dmsSearchVoObj.setPods(podArray);
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
				Criteria criteria2 = session
					.createCriteria(TmsBookingLoadDetail.class, "loadDetails")
					.createAlias("loadDetails.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
					.createAlias("loadDetails.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
					.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()))
					.add(Restrictions.eq("st.masterId",Long.parseLong(searchParam.getServiceType())));

				List<TmsBookingLoadDetail> loadDtlsList = (List<TmsBookingLoadDetail>) criteria2.list();

				for (TmsBookingLoadDetail loadDtl : loadDtlsList) {
					podArray.put(String.valueOf(loadDtl.getPort().getPortId()), loadDtl
						.getPort().getPortName());
				}
				dmsSearchVoObj.setPods(podArray);
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingPODS : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingPODS : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingPODS : session closed ");
		}
		return dmsSearchVoObj;
	}	

	//PODS should be populated based on service type selected
	@SuppressWarnings("unchecked")
	private DMSWebUploadVO loadBookingCargoTypes(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::loadBookingCargoTypes : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		Map<String,String> cargoTypesArray = new HashMap<String,String>(0);
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<BookingDetail> bookingList = new ArrayList<BookingDetail>(0);		  
			  
			  Criteria criteria=session.createCriteria(BookingDetail.class,"bkd");
			  criteria.createAlias("bkd.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  criteria.add(Restrictions.eq("bk.bookingNumber",searchParam.getBookingNo()));
			  bookingList=criteria.list();
			  
			  if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
				  for(BookingDetail bookingDtl : bookingList){
					  cargoTypesArray.put(String.valueOf(bookingDtl.getMasterSetupByCargoTypeMasterId().getMasterId()), 
							  bookingDtl.getMasterSetupByCargoTypeMasterId().getSetupDescription());
				  }
				  dmsSearchVoObj.setCargoTypes(cargoTypesArray);
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
				Criteria criteria2 = session
						.createCriteria(TmsBookingLoadDetail.class, "loadDetails")
						.createAlias("loadDetails.tmsBooking", "tmsBk",Criteria.INNER_JOIN)
						.createAlias("loadDetails.masterSetupByServiceTypeMasterId", "st", Criteria.INNER_JOIN)
						.add(Restrictions.eq("tmsBk.bookingNumber",searchParam.getBookingNo()));
				
				List<TmsBookingLoadDetail> loadDtlsList = (List<TmsBookingLoadDetail>) criteria2.list();
				for (TmsBookingLoadDetail loadDtl : loadDtlsList) {
					cargoTypesArray.put(String.valueOf(loadDtl.getMasterSetupByCargoTypeMasterId()
							.getMasterId()), loadDtl
							.getMasterSetupByCargoTypeMasterId()
							.getSetupDescription());
				}
				dmsSearchVoObj.setCargoTypes(cargoTypesArray);				
				
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingCargoTypes : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingCargoTypes : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingCargoTypes : session closed ");
		}
		return dmsSearchVoObj;
	}

	//Below method will load business line of associated booking
	@SuppressWarnings("unchecked")
	private DMSWebUploadVO loadBookingBusinessLine(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::loadBookingBusinessLine : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		String busLine = "";
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<BookingDetail> bookingList = new ArrayList<BookingDetail>(0);		  
			  
			  Criteria criteria=session.createCriteria(BookingDetail.class,"bkd");
			  criteria.createAlias("bkd.booking", "bk",CriteriaSpecification.INNER_JOIN);
			  
			  bookingList=criteria.list();
			  if(bookingList !=null && bookingList.get(0).getBooking() !=null){
				  dmsSearchVoObj.setBusinessLine(bookingList.get(0).getBooking().getMasterSetup().getSetupDescription());
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
			  //TMS Search
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingBusinessLine : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingBusinessLine : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingBusinessLine : session closed ");
		}
		return dmsSearchVoObj;
	}	

	//Business Line and Booking Customer should be populated based on booking number retreived from IPC
	@SuppressWarnings("unchecked")
	private DMSWebUploadVO loadBookingCustomerNBussLine(DMSWebUploadVO searchParam){
		Session session=null;
		
		logger.debug("DMSDaoImpl ::loadBookingCustomerNBussLine : searchParam :"+searchParam);
		
		DMSWebUploadVO dmsSearchVoObj = new DMSWebUploadVO();
		String bookingCus = null;
		String bussLine = null;
	
		try{
		  session=HibernateUtil.getSession();
		  
		  String systemLine=searchParam.getSystemLine().trim();
		  
		  if(systemLine.equalsIgnoreCase("OFS")){
			  
			  List<Booking> bookingList = new ArrayList<Booking>(0);
			  
			  Criteria criteria=session.createCriteria(Booking.class,"bk");
			  criteria.createAlias("bk.customer", "bkCus",CriteriaSpecification.INNER_JOIN);
			  criteria.createAlias("bk.masterSetup", "bkBussLine",CriteriaSpecification.INNER_JOIN);
			  criteria.add(Restrictions.eq("bk.bookingNumber",searchParam.getBookingNo()));
			  bookingList=criteria.list();
			  
			  if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
				  for(Booking booking : bookingList){
					  bookingCus = booking.getCustomer().getCustomerName();
					  bussLine = booking.getMasterSetup().getSetupDescription();
				  }
				  dmsSearchVoObj.setBookingCustomer(bookingCus);
				  dmsSearchVoObj.setBusinessLine(bussLine);
			  }
		  }else if(systemLine.equalsIgnoreCase("TMS")){
			  	List<TmsBooking> bookingList = new ArrayList<TmsBooking>(0);
			  	
				Criteria criteria = session
						.createCriteria(TmsBooking.class, "B")
						.createAlias("B.masterSetup", "BMS", Criteria.LEFT_JOIN)
						.createAlias("B.customer", "BC", Criteria.LEFT_JOIN)
						.add(Restrictions.eq("B.bookingNumber", searchParam.getBookingNo()));
						
				bookingList=criteria.list();
				
				if(bookingList!=null?!bookingList.isEmpty()?true:false:false){
					for(TmsBooking booking : bookingList){
						bookingCus = booking.getCustomer().getCustomerName();
						bussLine = booking.getMasterSetup().getSetupDescription();
					}
					dmsSearchVoObj.setBookingCustomer(bookingCus);
					dmsSearchVoObj.setBusinessLine(bussLine);
				}				
				
		  }else if(systemLine.equalsIgnoreCase("YMS")){
			  //YMS Search
		  }			   
		}catch(HibernateException he){
			logger.debug("DMSDaoImpl :: loadBookingCustomerNBussLine : Hibernate Exception araised "+he);
		}catch(Exception e){
			logger.debug("DMSDaoImpl :: loadBookingCustomerNBussLine : Generic Exception araised "+e);
		}finally{
			HibernateUtil.closeSession(session);
			logger.debug("DMSDaoImpl :: loadBookingCustomerNBussLine : session closed ");
		}
		return dmsSearchVoObj;
	}

	/**
	 * This method is responsible for logging the document upload events into the DB
	 * @param auditHistoryVo
	 * @return
	 */
	public  DMSResultObject logToDMSDocumnetHistory(List<DMSDocumentAuditHistoryVO> auditHistoryVoList){
		logger.debug("Usage:: DMS : DMSGDaoImpl : logToDMSDocumnetHistory() : ENTERING");
		Session session = null;
		Transaction tran = null;
		DMSResultObject dmsResultObj = new DMSResultObject();
		dmsResultObj.setResultFlag(false);
		
		if(auditHistoryVoList != null){
			try{
				session = HibernateUtil.getSession();
				tran = session.beginTransaction();
				for(DMSDocumentAuditHistoryVO auditHistoryVo:auditHistoryVoList){
				DmsDocumentAuditHistory dmsAuditHistoryEntity = new DmsDocumentAuditHistory();
				dmsAuditHistoryEntity.setDocumentNumber(valueNullEmptyChecker(auditHistoryVo.getDocumentNumber()));
				dmsAuditHistoryEntity.setActionBy(valueNullEmptyChecker(auditHistoryVo.getActionBy()));
				dmsAuditHistoryEntity.setActionDate(new Date());
				dmsAuditHistoryEntity.setDocumentNumber(valueNullEmptyChecker(auditHistoryVo.getDocumentNumber()));
				dmsAuditHistoryEntity.setSourceSystem(valueNullEmptyChecker(auditHistoryVo.getSourceSystem()));
				dmsAuditHistoryEntity.setRemarks(valueNullEmptyChecker(auditHistoryVo.getRemarks()));
				session.save(dmsAuditHistoryEntity);
				}
				tran.commit();
				dmsResultObj.setResultFlag(true);
				dmsResultObj.setResultMessage("Action successfully logged into the DB");
			}catch(Exception e){
				tran.rollback();
				e.printStackTrace();
				dmsResultObj.setResultMessage("Failed to log Action into the DB");
				logger.error("Error ::Usage:: DMS : DMSGDaoImpl : logToDMSDocumnetHistory() :", e);
			}finally{
				if(session != null){
					HibernateUtil.closeSession(session);
				}
			}
		}else{
			dmsResultObj.setResultMessage("Failed to log Action into the DB : due to auditHistoryVo is null");
		}
		logger.debug("Usage:: DMS : DMSGDaoImpl : logToDMSDocumnetHistory() : EXITING");
		return dmsResultObj;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,String> getMasAsOrder(Map<String,String> docTypeMap){
		  Map<String,String> mapUnique=getUniqueMap(docTypeMap);
		  logger.debug("mapUnique : "+mapUnique);
		 List<Map.Entry> a = new ArrayList<Map.Entry>(mapUnique.entrySet());
		 Collections.sort(a,
		          new Comparator() {
		              public int compare(Object o1, Object o2) {
		                  Map.Entry e1 = (Map.Entry) o1;
		                  Map.Entry e2 = (Map.Entry) o2;
		                  return ((Comparable) e1.getValue()).compareTo(e2.getValue());
		              }
		          });
		 mapUnique.clear();
		 Map<String,String> orderMap=new HashMap<String,String>();
		 for (Map.Entry e : a) {
			 orderMap.put((String)e.getKey(),(String)e.getValue());
			 logger.debug("After :: "+"Key :"+(String)e.getKey()+" Value : "+(String)e.getValue());
		}
	  return orderMap;
	}
	public static <K, V> Map<K, V> getUniqueMap(Map<K, V> m) {
	    Map<K, V> map = new HashMap<K, V>();
	    Map<V, K> tmpMap = new HashMap<V, K>();
	    for(Map.Entry<K, V> entry : m.entrySet()) {
	        if (!tmpMap.containsKey(entry.getValue())) {
	            tmpMap.put(entry.getValue(), entry.getKey());
	        }
	    }
	    for(Map.Entry<V, K> entry : tmpMap.entrySet()) {
	        map.put(entry.getValue(), entry.getKey());
	    }
	    return map;
	}
	  public Map<String, String> getDmsAccessDetials(String userRole){
			//DMS_ACC_USER_NAME,DMS_ACC_USER_PASS,DMS_ACC_URL
		  logger.debug("DMS :: DMSGDaoImpl :: getDmsAccessDetials() :: DMS Access fetching start : "+userRole);
		  Map<String, String> accDtlMap=null;
		  Boolean isEncypt=false;
		  userRole=getUserRole(userRole);
		   if(nullEmptyCheckerStringFlag(userRole)){
			   logger.debug("new User Role : "+userRole);
			   accDtlMap=new HashMap<String,String>();
			   Session session=null;
			   Transaction tx=null;
			    try {
			 		 session=HibernateUtil.getSession();
			 		 Criteria cri=session.createCriteria(DmsUserAccess.class,"dmsUAcc");
					           cri.add(Restrictions.eq("dmsUAcc.role",userRole.trim().toUpperCase()));
					    DmsUserAccess dmsUA=(DmsUserAccess)cri.uniqueResult();
					    if(dmsUA!=null){
					    	if(!dmsUA.isIsEncripted()){ 
					    		isEncypt=false;
					    		String userName=dmsUA.getUserName();
					    		String password=dmsUA.getPassword();
					    		Long uacId=dmsUA.getDuaId();
					    		logger.debug("DMS :: DMSGDaoImpl :getDmsAccessDetials(): befor Encryption :[ ( DMSUrl : "+dmsUA.getDmsUrl()+" ),( UserName : "+dmsUA.getUserName()+"),(Pass : "+dmsUA.getPassword()+"),(isEncrypt :"+dmsUA.isIsEncripted()+"),(modifiedDate :"+dmsUA.getModifiedDate()+"),(Role : "+dmsUA.getRole()+")]");
					    		tx=session.beginTransaction();
					    		dmsUA.setIsEncripted(true);
					    		dmsUA.setUserName(AESencrp.encrypt(userName));
					    		dmsUA.setPassword(AESencrp.encrypt(password));
					    		dmsUA.setModifiedDate(DateUtil.getCurrentDate());
					    		session.update(dmsUA);
					    	    tx.commit();
					    	    tx=session.beginTransaction();
					    	    DmsUserAccess dmsAcc=(DmsUserAccess)session.load(DmsUserAccess.class,uacId);
					    	      if(dmsAcc.isIsEncripted()){
					    	    	  isEncypt=true;
					    	    	   accDtlMap.put(DMS_ACC_URL,valueNullEmptyChecker(dmsAcc.getDmsUrl()));
									   accDtlMap.put(DMS_ACC_USER_NAME,valueNullEmptyChecker(dmsAcc.getUserName()));
									   accDtlMap.put(DMS_ACC_USER_PASS, valueNullEmptyChecker(dmsAcc.getPassword())); 
					    	      }
					    	      logger.debug("DMS :: DMSGDaoImpl :getDmsAccessDetials():: process Encrypting..then :[ ( DMSUrl : "+dmsAcc.getDmsUrl()+" ),( UserName : "+dmsAcc.getUserName()+"),(Pass : "+dmsAcc.getPassword()+"),(isEncrypt :"+dmsAcc.isIsEncripted()+"),(modifiedDate :"+dmsAcc.getModifiedDate()+"),(Role : "+dmsAcc.getRole()+")]");
					    	     tx.commit();			    		
					    	}else{ 
					    		isEncypt=true;
					    		   accDtlMap.put(DMS_ACC_URL,valueNullEmptyChecker(dmsUA.getDmsUrl()));
								   accDtlMap.put(DMS_ACC_USER_NAME,valueNullEmptyChecker(dmsUA.getUserName()));
								   accDtlMap.put(DMS_ACC_USER_PASS, valueNullEmptyChecker(dmsUA.getPassword()));
						    	      logger.debug("DMS :: DMSGDaoImpl :getDmsAccessDetials(): Already Encrypted :[ ( DMSUrl : "+dmsUA.getDmsUrl()+" ),( UserName : "+dmsUA.getUserName()+"),(Pass : "+dmsUA.getPassword()+"),(isEncrypt :"+dmsUA.isIsEncripted()+"),(modifiedDate :"+dmsUA.getModifiedDate()+"),(Role : "+dmsUA.getRole()+")]");
					    	}
					    	 
					    }else{
					    	logger.debug("DMS :: DMSGDaoImpl :: getDmsAccessDetials() ::Unable to fetch the DMS User Access detials form Database::\n " +
					    			" Warng :: No data available in Database please insert the data into Database");
					    	
					    	/*DmsUserAccess dmsAcc=new DmsUserAccess();
					    	dmsAcc.setDmsUrl("http://10.60.105.216:9080/DMSServices/GMGDMSServices?wsdl");
					    	dmsAcc.setIsEncripted(false);
					    	dmsAcc.setRole(userRole.toUpperCase().trim());
					    	dmsAcc.setUserName(userRole);
					    	dmsAcc.setPassword(userRole.equalsIgnoreCase("Author")?"Password1":"Password2");
					    	dmsAcc.setModifiedDate(DateUtil.getCurrentDate());
					    	tx=session.beginTransaction();
					    	session.save(dmsAcc);
					    	tx.commit();*/
					    	
					    }
			 	}catch(Exception e){			 		
			 		if(tx!=null){
			 				tx.rollback();
			 		}
			 		e.printStackTrace();
			 		logger.debug("DMS :: DMSGDaoImpl :: getDmsAccessDetials() ::  Exception : Access fetching failed.. :"+e);
				}finally{
					if(session != null){
						HibernateUtil.closeSession(session);
					}
				}
                 if(!isEncypt){
                	logger.debug("DMS :: DMSGDaoImpl :: DMS Access details fetching failed.. the Map is : "+accDtlMap);
                 }			   
		   }
		   logger.debug("DMS :: DMSGDaoImpl :: getDmsAccessDetials() :: End:  accDtlMap :  "+accDtlMap);
		  return accDtlMap;
	  }
	  private String getUserRole(String userRole){
		  
		  if(userRole!=null && (userRole.trim().equalsIgnoreCase("Author")||userRole.trim().equalsIgnoreCase("Approver"))){
			  return userRole;
		  }else{
			  return "AUTHOR";
		  }
	  }

/*
	public static void main(String[] args) {
		try {
			//new DMSDaoImpl().getSearchDetails(DMSType.SEARCH_BOOKING, null);
   			//logger.debug(DateUtil.getDate("20/02/2015", DateUtil.FORMAT_SLASH_DD_MM_YYYY));
			Map<String,String> docTypeDetailsMap=new HashMap<String,String>();
			 docTypeDetailsMap.put(DMSGConstants.DOCTYPE_SYSTEMLINE,"TMS");
			 docTypeDetailsMap.put(DMSGConstants.DOCTYPE_DOCLINE,"ST-DOC");
			 docTypeDetailsMap.put(DMSGConstants.DOCTYPE_COMPANY_GROUP_CODE,"");
			 docTypeDetailsMap.put(DMSGConstants.DOCTYPE_USER_COMPANY_CODE,"FF");
			//new DMSDaoImpl().loadDocumentType(docTypeDetailsMap);
			// new DMSDaoImpl().getCompanyNameOfMaster("GC","");
			 DMSSearchVO dmsSearchVo=new DMSSearchVO();
			 //booking no search-----------------------------------------
			 //dmsSearchVo.setSystemLine("TMS");
			 dmsSearchVo.setBookingCustomer("a");
			 dmsSearchVo.setBookingDateFrom("21/04/2015");
			 dmsSearchVo.setBookingDateTo("25/05/2015");
			 dmsSearchVo.setCompanyCode("NT");
			 dmsSearchVo.setBranchCode("MB");
			 
			// dmsSearchVo.setBusinessLine("DOMESTIC EXPORT");//INTERNATIONAL IMPORT,DOMESTIC EXPORT
			 //dmsSearchVo.setVesselName("a");
			// dmsSearchVo.setEtaDate("11/03/2015");
			// dmsSearchVo.setVoyageNo("043");
			 
			 //job Search................................................................
			 dmsSearchVo.setBookingCustomer("a");
			 dmsSearchVo.setBookingNo("GSBK101400001");
			 dmsSearchVo.setPortOfDischarge("KOTA");
			 dmsSearchVo.setJobDateFrom("18/10/2014");
			 dmsSearchVo.setJobDateTo("18/10/2014");
			 
			 // Si search................................................................
			 dmsSearchVo.setBookingCustomer("");
			 dmsSearchVo.setBookingNo("");
			 dmsSearchVo.setPortOfDischarge("KUCHING");
			 dmsSearchVo.setSiDateFrom("");
			 dmsSearchVo.setSiDateTo("");
			 
			 
			 // Claim Search...................
			 //dmsSearchVo.setSystemLine("TMS");
			 //dmsSearchVo.setClaimDateFrom("20/04/2014");
			 //dmsSearchVo.setClaimDateTo("20/05/2015");
			 //dmsSearchVo.setBookingNo("GSBK021500106");
			 //dmsSearchVo.setBookingCustomer("");
			 //dmsSearchVo.setIncidentNo("NTCRIL15030026");
			    
			 //shipment search----------------------------------------
			 dmsSearchVo.setSystemLine("OFS");
			 dmsSearchVo.setShipmentDateFrom("10/03/2015");//20/02/2015
			 dmsSearchVo.setShipmentDateTo("30/04/2015");//20/03/2015
			 dmsSearchVo.setVesselName("");
			 dmsSearchVo.setVoyageNo("");
			 dmsSearchVo.setEtaDate("29/03/2015");
			//Invoice search------------------------------------------------
			dmsSearchVo.setSystemLine("OFS"); 
			 dmsSearchVo.setInvoiceDateFrom("05/05/2014");
			 dmsSearchVo.setInvoiceDateTo("04/05/2015");
			 dmsSearchVo.setBookingCustomer("");
			 //Quotation Search---------------------------------------------------
			dmsSearchVo.setSystemLine("OFS");
			 dmsSearchVo.setQuotationDateFrom("05/05/2014");
			 dmsSearchVo.setQuotationDateTo("05/05/2015");
			 dmsSearchVo.setCustomerName("");
			 
			 //PO search-------------------------------------------------
			 dmsSearchVo.setSystemLine("TMS");
			 dmsSearchVo.setPoDateFrom("18/02/2015");
			 dmsSearchVo.setPoDateTo("18/03/2015");
			 dmsSearchVo.setVendorName("a");
			 //DO search-----------------------------------------------
			 dmsSearchVo.setSystemLine("TMS");
			 dmsSearchVo.setBillingCustomer("a");
			 dmsSearchVo.setPickupLocation("AIR KEROH INDUSTRIAL AREA");
			 dmsSearchVo.setDeliveryLocation("");
			 dmsSearchVo.setBookingNo("");
			 dmsSearchVo.setLoadNo("NTCRL150325002");
			 dmsSearchVo.setPickupDate("25/03/2015");
			 dmsSearchVo.setDeliveryDate("27/03/2015");
			 dmsSearchVo.setDoDateFrom("30/01/2015");
			 dmsSearchVo.setDoDateTo("30/04/2015");
			 //Load Number Search----------------------------------------
			    dmsSearchVo.setSystemLine("TMS");
			    dmsSearchVo.setBookingCustomer("");
			    dmsSearchVo.setBookingDateFrom("07/04/2015");
			    dmsSearchVo.setBookingDateTo("04/05/2015");
			    dmsSearchVo.setPickupLocation("");
			    dmsSearchVo.setDeliveryLocation("");
			    dmsSearchVo.setTruckType("");
			 //Booking Customer------------------------------------------------------------------------
			 dmsSearchVo.setSystemLine("TMS");			 dmsSearchVo.setCustomerName("a");
			 dmsSearchVo.setCustomerCode("C000430");
			 dmsSearchVo.setSystemLine("TMS"); 
			 dmsSearchVo.setCompanyCode("NT");
			 dmsSearchVo.setBranchCode("CR");  
			 dmsSearchVo.setDoDateFrom("28/05/2015");
			 dmsSearchVo.setDoDateTo("28/05/2015");
			// dmsSearchVo.setPoDateFrom("29/04/2015");
			 //dmsSearchVo.setPoDateTo("29/05/2015"); 
			// dmsSearchVo.setVendorName("K.K REGIONAL NAVIGATION SDN BH");
			 
			 //creditNote
			 dmsSearchVo.setSystemLine("TMS");	
			 dmsSearchVo.setCnDateForm("11/01/2015");
			 dmsSearchVo.setCnDateTo("11/05/2015");
			 DMSSearchVO dmsSearchVo=new DMSSearchVO();
			 dmsSearchVo.setSystemLine("TMS");			 
			// dmsSearchVo.setCustomerName("SIME DARBY INDUSTRIAL SDN BHD");
			// dmsSearchVo.setCustomerCode("C000430");
			 dmsSearchVo.setSystemLine("TMS"); 
			 dmsSearchVo.setCompanyCode("NT");
			 dmsSearchVo.setBranchCode("CR");  
			 
			 List<DMSSearchVO> dmsSearchVO=new DMSGDaoImpl().getSearchDetails(DMSGType.SEARCH_BOOKING_CUSTOMER,dmsSearchVo);
			 logger.debug("Main :: dmsSearchVO :Val : "+dmsSearchVO);
			logger.debug("Main :: dmsSearchVO :SIZE : "+dmsSearchVO.size()); 
			 //new DMSGDaoImpl().getCompanyIDByBCodeAndCCode("NT","MB");
			 //logger.debug("Date :"+new DMSDaoImpl().getUtilDateEnd("28/10/2014"));
			new DMSGDaoImpl().getDmsAccessDetials("Approver");
		} catch (Exception e) { 
			
			e.printStackTrace();
		}
	}*/
	
}


