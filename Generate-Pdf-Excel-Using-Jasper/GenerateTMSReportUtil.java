package com.giga.tms.util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Session;
import org.hibernate.impl.SessionFactoryImpl;

import com.giga.base.BaseVO;
import com.giga.base.HibernateUtil;
import com.giga.base.LoggerUtility;
import com.giga.exception.DataAccessException;
import com.giga.master.dao.MasterDAOImpl;
import com.giga.tms.dao.TMSQuotationDaoImpl;
import com.giga.tms.vo.DOSummaryReportVO;
import com.giga.tms.vo.DailyAssignmentMultipleGatePassReportVO;
import com.giga.tms.vo.DailySummaryVo;
import com.giga.tms.vo.DeliveryOrderReportVO;
import com.giga.tms.vo.DeliveryOrderSubDOVO;
import com.giga.tms.vo.QuotationCustomerAddressVO;
import com.giga.tms.vo.QuotationServiceDetailsVO;
import com.giga.tms.vo.QuotationVO;
import com.giga.tms.vo.TmsCNoteVO;
import com.giga.tms.vo.TmsCustomerCreditNoteDetailsVO;
import com.giga.tms.vo.TmsInvoiceDetailVO;
import com.giga.tms.vo.TmsInvoiceReportVO;
import com.giga.tms.vo.TmsInvoiceVO;
import com.giga.util.CommonUtil;
import com.giga.util.GenerateReportUtil;
import com.giga.util.GigaBaseReport;
import com.giga.util.IBusinessConstant;
import com.google.gson.Gson;

@SuppressWarnings({"rawtypes","unchecked"})
public class GenerateTMSReportUtil extends GigaBaseReport {
	
	private static Logger logger = LoggerUtility
			.getLogger(GenerateTMSReportUtil.class);
	private static Date SST_DATE = MasterDAOImpl.getSstDate();
	
	
		public byte[] generateQuotationReport( Map<String, Object> parameterMap, String jasperReportName1,String jasperReportName2,String jasperReportName3, ByteArrayOutputStream outputStream) throws DataAccessException, ParseException {
		
		logger.info("generateQuotationReport  --- start --");
		logger.info("parameterMap  :::::::::::::: "+parameterMap);
		System.out.println("generateQuotationReport  --- start");
		TMSQuotationDaoImpl impl = new TMSQuotationDaoImpl();
		QuotationConverter util = new QuotationConverter();
		Integer diffInDays = null;
		
		HashMap<String,String> result = new HashMap<String, String>();
		result = impl.QuotationConfrimedUser((String)parameterMap.get("QUOTATION_NO"));
		parameterMap.put("PREPARED_BY",result.get("name"));
		parameterMap.put("EMAILID",result.get("email"));
		
		QuotationCustomerAddressVO customerAddressVO;
		Date date=null;
		Double taxAmt=0.0;
		QuotationVO quotatinVO = impl.getQuotationDetail((String)parameterMap.get("QUOTATION_NO"));
		List<com.giga.tms.vo.QuotationServiceDetailsPdfVO> qServiceDetailsPdf = impl.getQuotaionPDFValues((String)parameterMap.get("QUOTATION_NO"));//impl.getQuotaionPDFValues((String)parameterMap.get("QUOTATION_NO"));
		
		if(quotatinVO.getQuotationGeneralDetailsVO()!=null){
			date=TMSUtil.convertToDdMMYYYYDate(quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidFrom());
		}
		
		System.out.println("branch Code is >>> "+quotatinVO.getQuotationGeneralDetailsVO().getBranchCode());
		String taxFlag=null;
		for(QuotationServiceDetailsVO detVO:quotatinVO.getServiceDetailsList()){
			if(IBusinessConstant.GST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
				taxFlag=IBusinessConstant.GST_APPLICABLE;
				break;
			}else if(IBusinessConstant.SST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
				taxFlag=IBusinessConstant.SST_APPLICABLE;
				break;
			}
		}
		
		if(quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo()!=null && !quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo().isEmpty()){
		Date quotationValidTo =util.convertToDate(quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo());
		Date currentDate = Calendar.getInstance().getTime();
		diffInDays = (int) ((quotationValidTo.getTime() - currentDate
				.getTime()) / (1000 * 60 * 60 * 24));
		}
		
		if(quotatinVO.getQuotationGeneralDetailsVO().getIsNewCustomer().equalsIgnoreCase("YES")){
			
			customerAddressVO = quotatinVO.getNewCust();
			
		}else{
			String customerAdsId = quotatinVO.getQuotationGeneralDetailsVO().getCustomerAddressId();
			Map<String,String> searchCriteria = new HashMap<String,String>();
			searchCriteria.put("CONTACT_PERSION_ID",customerAdsId);
			System.out.println("CONTACT_PERSION_ID is >>>"+searchCriteria);
			logger.info("CONTACT_PERSION_ID is "+searchCriteria);
			customerAddressVO = (QuotationCustomerAddressVO) impl.getCustomerAddressDetail(searchCriteria);
		}
		logger.info(" customerAddressVO  >>"+customerAddressVO);
		
		Session session = HibernateUtil.getSession();		
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		Connection connection = null;
		JRPdfExporter exporter = new JRPdfExporter();
		java.util.List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		
	   try {
			connection = sessionFactory.getConnectionProvider().getConnection();
			System.out.println("latest changes >>> Connection is getting "+connection);
			String reportLocation1 = "/resources/"+ jasperReportName1 +".jrxml";
			String reportLocation2 = "/resources/"+ jasperReportName2 +".jrxml";
			String reportLocation3 = "/resources/"+ jasperReportName3 +".jrxml";
				
			logger.info("reportLocation1 "+reportLocation1+" reportLocation2"+reportLocation2+" reportLocation3"+reportLocation3);
			System.out.println("reportLocation1 "+reportLocation1+" reportLocation2"+reportLocation2+" reportLocation3"+reportLocation3);
			
			if("YES".equalsIgnoreCase(quotatinVO.getQuotationGeneralDetailsVO().getIsNewCustomer())){
				
				parameterMap.put("customerName", customerAddressVO.getNewCustName());
			}else{			
				parameterMap.put("customerName", quotatinVO.getQuotationGeneralDetailsVO().getCustomerName());			
			}
			
			parameterMap.put("contactPerson", customerAddressVO.getContactPerson());
			parameterMap.put("diffInDays",(diffInDays!=null?diffInDays.toString():""));
			parameterMap.put("address1", customerAddressVO.getAddress1());
			parameterMap.put("address2", customerAddressVO.getAddress2());
			parameterMap.put("city", customerAddressVO.getCityDesc());
			parameterMap.put("country", customerAddressVO.getCountryDesc());
			parameterMap.put("email", customerAddressVO.getEmail());
			parameterMap.put("postCode", customerAddressVO.getPostCode());
			parameterMap.put("state", customerAddressVO.getStateDesc());
			parameterMap.put("fax1", (customerAddressVO.getFax1()!=null ? customerAddressVO.getFax1().toString():"" ));
			parameterMap.put("fax2", (customerAddressVO.getFax2()!=null ? customerAddressVO.getFax2().toString():"" ));
			parameterMap.put("fax3", (customerAddressVO.getFax3()!=null ? customerAddressVO.getFax3().toString():"" ));
			parameterMap.put("phone1", (customerAddressVO.getPhone1()!=null ? customerAddressVO.getPhone1().toString():"" ));
			parameterMap.put("phone2", (customerAddressVO.getPhone2()!=null ? customerAddressVO.getPhone2().toString():"" ));
			parameterMap.put("phone3", (customerAddressVO.getPhone3()!=null ? customerAddressVO.getPhone3().toString():"" ));
			parameterMap.put("prepared_by", ((String)parameterMap.get("PREPARED_BY")!=null ? (String)parameterMap.get("PREPARED_BY"):"" ));
			parameterMap.put("designation", ((String)parameterMap.get("DESIGNATION")!=null ? (String)parameterMap.get("DESIGNATION"):"" ));
			parameterMap.put("useremailId", ((String)parameterMap.get("EMAILID")!=null ? (String)parameterMap.get("EMAILID"):"" ));
			parameterMap.put("TAX_FLAG",taxFlag!=null?taxFlag:IBusinessConstant.NEITHER_GST_SST_APPLICABLE);
			//parameterMap.put("teamAndCondition", teamAndCondition);
			//(String)parameterMap.get("QUOTATION_NO")
			logger.info("parameterMap  :::::::::::::: "+parameterMap);
			System.out.println("<<<<parameterMap>>>>"+parameterMap);
			
			JasperDesign generalDetails = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation1));
		
			JasperReport jasperReport1 = JasperCompileManager.compileReport(generalDetails);
		
			JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
				
			JasperDesign services = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation2));
			
			
			JasperReport jasperReport2 = JasperCompileManager.compileReport(services);
			
			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport2, parameterMap,  new JRBeanCollectionDataSource(qServiceDetailsPdf));
			//JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
			//System.out.println("jasperReport2: after"+jasperReport2);
			
			JasperDesign termsAndCondn = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation3));
			//System.out.println("termsAndCondn>>>>"+termsAndCondn);
			
			JasperReport jasperReport3 = JasperCompileManager.compileReport(termsAndCondn);
			
			parameterMap.remove("SUBREPORT_DIR");
			JasperPrint jasperPrint3 = JasperFillManager.fillReport(jasperReport3, parameterMap, connection);	
			
			
			int pageNo1 = getPageCount(jasperPrint1);
			int pageNo2 = getPageCount(jasperPrint2);
			int pageNo3 = getPageCount(jasperPrint3);
			
			logger.info("pageNo1 "+pageNo1+" pageNo2"+pageNo2+" pageNo3"+pageNo3);
			System.out.println("pageNo1: "+pageNo1+" pageNo2 :"+pageNo2+" pageNo3 :"+pageNo3);
		
			int total = pageNo1+pageNo2+pageNo3;
		  
			logger.info("total"+total);
		  
			parameterMap.put("PageNo" , total);
			parameterMap.put("PageNo1" , pageNo1);
			parameterMap.put("PageNo2" , pageNo2);
			parameterMap.put("PageNo3" , pageNo3);
		  
			JasperPrint jprint4 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
			JasperPrint jprint5 = JasperFillManager.fillReport(jasperReport2, parameterMap,  new JRBeanCollectionDataSource(qServiceDetailsPdf));
		//  JasperPrint jprint5 = JasperFillManager.fillReport(jasperReport2, parameterMap, connection);
			JasperPrint jprint6 = JasperFillManager.fillReport(jasperReport3, parameterMap, connection);
			jprintlist.add(jprint4);
			jprintlist.add(jprint5);
			jprintlist.add(jprint6);
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
			logger.info("generateQuotationReport  --- end");
			System.out.println("generateQuotationReport  --- end");
				
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error("Usage: Schedule : GenerateReport.generateJasperReport() :: Report Could not be generated :::", e);
        	throw new DataAccessException("DataAccessErrorCodeMsg");	
		} finally {
			GenerateReportUtil.close(connection);
		}
		return outputStream.toByteArray();
		
	}


	public static int getPageCount(JasperPrint jasperPrint) {
    if (jasperPrint != null) {
          return ((JasperPrint) jasperPrint).getPages().size();
    }
    return 0;
	} 
	
public JRPdfExporter generateQuotationReport( Map<String, Object> parameterMap, String jasperReportName1,String jasperReportName2,String jasperReportName3, OutputStream outputStream) throws DataAccessException, ParseException {
		
		logger.info("generateQuotationReport  --- start");
		logger.info("parameterMap  :::::::::::::: "+parameterMap);
		System.out.println("generateQuotationReport  --- start");
		TMSQuotationDaoImpl impl = new TMSQuotationDaoImpl();
		QuotationConverter util = new QuotationConverter();
		Integer diffInDays = null;
		
		
		
		QuotationCustomerAddressVO customerAddressVO;
		
		QuotationVO quotatinVO = impl.getQuotationDetail((String)parameterMap.get("QUOTATION_NO"));
		//String branchCode = quotatinVO.getQuotationGeneralDetailsVO().getBranchCode();
		System.out.println("branch Code is "+quotatinVO.getQuotationGeneralDetailsVO().getBranchCode());
		List<com.giga.tms.vo.QuotationServiceDetailsPdfVO> qServiceDetailsPdf = impl.getQuotaionPDFValues((String)parameterMap.get("QUOTATION_NO"));
	
		System.out.println("qServiceDetailsPdf>>>>>"+qServiceDetailsPdf);
		
		//Ram
		/*String taxFlag=null;
		for(QuotationServiceDetailsVO detVO:quotatinVO.getServiceDetailsList()){
			if(IBusinessConstant.GST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
				taxFlag=IBusinessConstant.GST_APPLICABLE;
				break;
			}else if(IBusinessConstant.SST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
				taxFlag=IBusinessConstant.SST_APPLICABLE;
				break;
			}
		}*/
		//Ram
	
		if(quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo()!=null && !quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo().isEmpty()){
		Date quotationValidTo =util.convertToDate(quotatinVO.getQuotationGeneralDetailsVO().getQuotationValidTo());
		Date currentDate = Calendar.getInstance().getTime();
		diffInDays = (int) ((quotationValidTo.getTime() - currentDate
				.getTime()) / (1000 * 60 * 60 * 24));
		}
		
		if(quotatinVO.getQuotationGeneralDetailsVO().getIsNewCustomer().equalsIgnoreCase("YES")){
			
			customerAddressVO = quotatinVO.getNewCust();
			
		}else{
			String customerAdsId = quotatinVO.getQuotationGeneralDetailsVO().getCustomerAddressId();
			Map<String,String> searchCriteria = new HashMap<String,String>();
			searchCriteria.put("CONTACT_PERSION_ID",customerAdsId);
			System.out.println("CONTACT_PERSION_ID is >>"+searchCriteria);
			logger.info("CONTACT_PERSION_ID is "+searchCriteria);
			customerAddressVO = (QuotationCustomerAddressVO) impl.getCustomerAddressDetail(searchCriteria);
		}
		logger.info(" customerAddressVO  "+customerAddressVO);
		
		Session session = HibernateUtil.getSession();		
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		Connection connection = null;
		JRPdfExporter exporter = new JRPdfExporter();
		java.util.List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		try {
			connection = sessionFactory.getConnectionProvider().getConnection();
			System.out.println("changes latest one");
			System.out.println("Connection is getting "+connection);
			String reportLocation1 = "/resources/"+ jasperReportName1 +".jrxml";
			String reportLocation2 = "/resources/"+ jasperReportName2 +".jrxml";
			String reportLocation3 = "/resources/"+ jasperReportName3 +".jrxml";
			
		logger.info("reportLocation1 "+reportLocation1+" reportLocation2"+reportLocation2+" reportLocation3"+reportLocation3);
		
		if(quotatinVO.getQuotationGeneralDetailsVO().getIsNewCustomer().equalsIgnoreCase("YES")){
			
			parameterMap.put("customerName", customerAddressVO.getNewCustName());
		}else{			
			parameterMap.put("customerName", quotatinVO.getQuotationGeneralDetailsVO().getCustomerName());			
		}
		
		parameterMap.put("contactPerson", customerAddressVO.getContactPerson());
		parameterMap.put("diffInDays",(diffInDays!=null?diffInDays.toString():""));
		parameterMap.put("address1", customerAddressVO.getAddress1());
		parameterMap.put("address2", customerAddressVO.getAddress2());
		parameterMap.put("city", customerAddressVO.getCityDesc());
		parameterMap.put("country", customerAddressVO.getCountryDesc());
		parameterMap.put("email", customerAddressVO.getEmail());
		parameterMap.put("postCode", customerAddressVO.getPostCode());
		parameterMap.put("state", customerAddressVO.getStateDesc());
		parameterMap.put("fax1", (customerAddressVO.getFax1()!=null ? customerAddressVO.getFax1().toString():"" ));
		parameterMap.put("fax2", (customerAddressVO.getFax2()!=null ? customerAddressVO.getFax2().toString():"" ));
		parameterMap.put("fax3", (customerAddressVO.getFax3()!=null ? customerAddressVO.getFax3().toString():"" ));
		parameterMap.put("phone1", (customerAddressVO.getPhone1()!=null ? customerAddressVO.getPhone1().toString():"" ));
		parameterMap.put("phone2", (customerAddressVO.getPhone2()!=null ? customerAddressVO.getPhone2().toString():"" ));
		parameterMap.put("phone3", (customerAddressVO.getPhone3()!=null ? customerAddressVO.getPhone3().toString():"" ));
		parameterMap.put("prepared_by", ((String)parameterMap.get("PREPARED_BY")!=null ? (String)parameterMap.get("PREPARED_BY"):"" ));
		parameterMap.put("designation", ((String)parameterMap.get("DESIGNATION")!=null ? (String)parameterMap.get("DESIGNATION"):"" ));
		parameterMap.put("useremailId", ((String)parameterMap.get("EMAILID")!=null ? (String)parameterMap.get("EMAILID"):"" ));
		//Ram
		/*parameterMap.put("TAX_FLAG",taxFlag!=null?taxFlag:IBusinessConstant.NEITHER_GST_SST_APPLICABLE);*/
		//Ram
		//parameterMap.put("teamAndCondition", teamAndCondition);
		//(String)parameterMap.get("QUOTATION_NO")
		logger.info(":::::::: parameterMap  :::::::::::::: "+parameterMap);
		System.out.println("<<<<<<parameterMap>>>>>>>>>>>>>>"+parameterMap);
		
			JasperDesign generalDetails = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation1));
			
			System.out.println("<<<<<generalDetails>>>>"+generalDetails);
			
			JasperReport jasperReport1 = JasperCompileManager.compileReport(generalDetails);
			JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
			
			System.out.println("jasper print 1 is completed>>>>>>");
			
			
			JasperDesign services = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation2));
			
			System.out.println("services after>>>>>>>"+services);
			
			JasperReport jasperReport2 = JasperCompileManager.compileReport(services);
			System.out.println("jasperReport:"+jasperReport2);
			System.out.println("before JRBeanCollectionDataSource "+qServiceDetailsPdf);
			JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport2, parameterMap,  new JRBeanCollectionDataSource(qServiceDetailsPdf));
		
		//	JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
			
			JasperDesign termsAndCondn = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation3));
			JasperReport jasperReport3 = JasperCompileManager.compileReport(termsAndCondn);
			parameterMap.remove("SUBREPORT_DIR");
			JasperPrint jasperPrint3 = JasperFillManager.fillReport(jasperReport3, parameterMap, connection);	
			
			int pageNo1 = getPageCount(jasperPrint1);
			int pageNo2 = getPageCount(jasperPrint2);
			int pageNo3 = getPageCount(jasperPrint3);
			  
		logger.info("pageNo1 "+pageNo1+" pageNo2"+pageNo2+" pageNo3"+pageNo3);
		System.out.println("pageNo1: "+pageNo1+" pageNo2 :"+pageNo2+" pageNo3 :"+pageNo3);
		
			  int total = pageNo1+pageNo2+pageNo3;
			  
			  logger.info("total"+total);
			  
			  parameterMap.put("PageNo" , total);
			  parameterMap.put("PageNo1" , pageNo1);
			  parameterMap.put("PageNo2" , pageNo2);
			  parameterMap.put("PageNo3" , pageNo3);
			  
			  JasperPrint jprint4 = JasperFillManager.fillReport(jasperReport1, parameterMap, connection);
			  // JasperPrint jprint5 = JasperFillManager.fillReport(jasperReport2, parameterMap, connection);
			  JasperPrint jprint5 = JasperFillManager.fillReport(jasperReport2, parameterMap,  new JRBeanCollectionDataSource(qServiceDetailsPdf));
			  JasperPrint jprint6 = JasperFillManager.fillReport(jasperReport3, parameterMap, connection);
			  jprintlist.add(jprint4);
			  jprintlist.add(jprint5);
			  jprintlist.add(jprint6); 
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
			logger.info("generateQuotationReport  --- end");
			System.out.println("generateQuotationReport  --- end");
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error("Usage: Schedule : GenerateReport.generateJasperReport() :: Report Could not be generated :::", e);
        	throw new DataAccessException("DataAccessErrorCodeMsg");	
		} finally {
			GenerateReportUtil.close(connection);
		}
		return exporter;
		
	}
	
	public  byte[] getCNDNPdf(String jasperReportName1, ByteArrayOutputStream stream, List list) throws DataAccessException  {
		System.out.println("Inside Jasper"+jasperReportName1);
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jPrintHome =null;
		Double taxAmt=0.0;
		String taxFlag=null;
		JRPdfExporter exporter = new JRPdfExporter();
		Map<String,Object> parameterMap=new HashMap<String,Object>();
		
		try {
			
			TmsCNoteVO tmsCNoteVo=(TmsCNoteVO)list.get(0);
				for(TmsCustomerCreditNoteDetailsVO vo:tmsCNoteVo.getCustomerCreditNoteDetailsVO() ){
					taxAmt = taxAmt + Double.parseDouble(vo.getTaxAmount());
				}
			String reportLocation1 = "/resources/"+ jasperReportName1 +".jrxml";
			System.out.println("reportLocation"+reportLocation1);
			for(TmsCustomerCreditNoteDetailsVO vo:tmsCNoteVo.getCustomerCreditNoteDetailsVO()){
				if(IBusinessConstant.GST_APPLICABLE.equals(CommonUtil.getTaxFlag(vo.getTaxAmountId()))){
					taxFlag=IBusinessConstant.GST_APPLICABLE;
					break;
				}else if(IBusinessConstant.SST_APPLICABLE.equals(CommonUtil.getTaxFlag(vo.getTaxAmountId()))){
					taxFlag=IBusinessConstant.SST_APPLICABLE;
					break;
				}
			}
			//Tax_Flag to segregate GST and SST details.
			parameterMap.put("TAX_FLAG",taxFlag!=null?taxFlag:IBusinessConstant.NEITHER_GST_SST_APPLICABLE);
			JasperReport jasperReport1 = super.getJasperReport(reportLocation1);
			jPrintHome = JasperFillManager.fillReport(jasperReport1,parameterMap, new JRBeanCollectionDataSource(list));
			jprintlist.add(jPrintHome);
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporter.exportReport();
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage: Schedule : GenerateReport.getCNDNPdf() :: Report Could not be generated :::", e);
			throw new DataAccessException("DataAccessErrorCodeMsg");	
		} 
		finally {
		}
		return stream.toByteArray();
	}
	
	public  JRPdfExporter getGeneratePdf(String reportName,String jasperReportName, OutputStream outputStream, List list) throws DataAccessException  {
		System.out.println("Inside Jasper"+jasperReportName);
		
		JRPdfExporter exporter = new JRPdfExporter();
		try {
			String reportLocation = "/resources/"+ jasperReportName +".jrxml";
			//String  repName="TMS"+reportName;
			System.out.println("reportLocation"+reportLocation);
			JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, new JRBeanCollectionDataSource(list));
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage: Schedule : GenerateReport.getGeneratePdf() :: Report Could not be generated :::", e);
	    	throw new DataAccessException("DataAccessErrorCodeMsg");	
		} finally {
		}
		return exporter;
	}
	
	public  JRPdfExporter getInvoicePdf(String jasperReportName1,String jasperReportName2, String path, List list) throws DataAccessException  {
		System.out.println("Inside Jasper"+jasperReportName1);
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jPrintHome =null;
		JasperPrint jPrintAttach =null;
		JRPdfExporter exporter = new JRPdfExporter();
		try {
			String reportLocation1 = "/resources/"+ jasperReportName1 +".jrxml";
			System.out.println("reportLocation"+reportLocation1);
			JasperReport jasperReport1 = super.getJasperReport(reportLocation1);
			/*JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);*/
			jPrintHome = JasperFillManager.fillReport(jasperReport1,null, new JRBeanCollectionDataSource(list));
			jprintlist.add(jPrintHome);
			
			if(jasperReportName2!=null){
				String reportLocation2 = "/resources/"+ jasperReportName2 +".jrxml";
				//JasperDesign jasperDesign2 = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation2));
				JasperReport jasperReport2= super.getJasperReport(reportLocation2);
				jPrintAttach = JasperFillManager.fillReport(jasperReport2,null, new JRBeanCollectionDataSource(list));
				jprintlist.add(jPrintAttach);
			}
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
			exporter.exportReport();
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage: Invoice : GenerateTMSReportUtil :: getInvoicePdf() :: Report Could not be generated :::", e);
			throw new DataAccessException("Report Could not be generated");	
		} 
		finally {
		}
		return exporter;
	}
	
	public  byte[] getInvoicePdf(String jasperReportName1,String jasperReportName2, ByteArrayOutputStream stream, List list) throws DataAccessException  {
		System.out.println("Inside Jasper"+jasperReportName1+" List="+new Gson().toJson(list));
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jPrintHome =null;
		JasperPrint jPrintAttach =null;
		JRPdfExporter exporter = new JRPdfExporter();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		Date date=null;
		Double taxAmt=0.0;
		for(TmsInvoiceVO vo : (List<TmsInvoiceVO>)list){
			if(vo.getInvoiceDateBox()!=null){
			date=TMSUtil.convertToDdMMYYYYDate(vo.getInvoiceDateBox());
			}
			if(vo.getTaxAmt()!=null){
			taxAmt=taxAmt+Double.parseDouble(vo.getTaxAmt());
			}
		}
		try {
			String reportLocation1 = "/resources/"+ jasperReportName1 +".jrxml";
			System.out.println("reportLocation"+reportLocation1);
			JasperReport jasperReport1 = super.getJasperReport(reportLocation1);
			TmsInvoiceVO vo=(TmsInvoiceVO)list.get(0);
			String taxFlag=null;
			for(TmsInvoiceDetailVO detVO : vo.getDetails()){
				if(IBusinessConstant.GST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
					taxFlag=IBusinessConstant.GST_APPLICABLE;
					break;
				}else if(IBusinessConstant.SST_APPLICABLE.equals(CommonUtil.getTaxFlag(detVO.getTaxMasterId()))){
					taxFlag=IBusinessConstant.SST_APPLICABLE;
					break;
				}
			}
			/*JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);*/
			paramMap.put("TAX_FLAG",taxFlag!=null?taxFlag:IBusinessConstant.NEITHER_GST_SST_APPLICABLE);
			
			jPrintHome = JasperFillManager.fillReport(jasperReport1,paramMap, new JRBeanCollectionDataSource(list));
			jprintlist.add(jPrintHome);
			
			if(jasperReportName2!=null){
				String reportLocation2 = "/resources/"+ jasperReportName2 +".jrxml";
				//JasperDesign jasperDesign2 = JRXmlLoader.load(getClass().getResourceAsStream(reportLocation2));
				JasperReport jasperReport2= super.getJasperReport(reportLocation2);
				jPrintAttach = JasperFillManager.fillReport(jasperReport2,paramMap, new JRBeanCollectionDataSource(list));
				jprintlist.add(jPrintAttach);
			}
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporter.exportReport();
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage: Invoice : GenerateTMSReportUtil :: getInvoicePdf() :: Report Could not be generated :::", e);
			throw new DataAccessException("Report Could not be generated");	
		} 
		finally {
		}
		return stream.toByteArray();
	}

	/**
	 * This method is responsible for generating report for TMSBooking
	 * @param jasperReportName
	 * @param stream
	 * @param list
	 * @return
	 * @throws DataAccessException
	 */
	public  byte[] generatePdf(String jasperReportName, ByteArrayOutputStream stream, List list) throws DataAccessException  {
		System.out.println("Inside Jasper"+jasperReportName);
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		JasperPrint jPrintHome =null;
		JRPdfExporter exporter = new JRPdfExporter();
		try {
			String reportLocation = "/resources/"+ jasperReportName +".jrxml";
			System.out.println("reportLocation"+reportLocation);
			JasperReport jasperReport1 = super.getJasperReport(reportLocation);
			jPrintHome = JasperFillManager.fillReport(jasperReport1,null, new JRBeanCollectionDataSource(list));
			jprintlist.add(jPrintHome);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jprintlist);	
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			exporter.exportReport();
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage: Schedule : GenerateReport.getCNDNPdf() :: Report Could not be generated :::", e);
			throw new DataAccessException("DataAccessErrorCodeMsg");	
		} 
		finally {
		}
		return stream.toByteArray();
	}
	
	/**
	 * Generate DO and SubDo pdf report
	 * @param jasperReportName
	 * @param path
	 * @param list
	 */
	public void generateDeliveryOrderPDF(String jasperReportName, String path,String deliveryNumber,String dropLocationId,List list) {
		JRPdfExporter exporter = new JRPdfExporter();
		JasperReport jasperReport;
		Map<String,Object> parameterMap=new HashMap<String,Object>();
		try {
			parameterMap.put("DRIVER_SIGN_IMAGE", "D:/Temp/TMS/DELIVERY_ORDER/"+deliveryNumber+".jpg");
			parameterMap.put("RECEIVER_SIGN_IMAGE", "D:/Temp/TMS/DELIVERY_ORDER/"+deliveryNumber+"-"+dropLocationId+".jpg");
			jasperReport = (JasperReport) JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("/resources"+jasperReportName+".jasper"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JRBeanCollectionDataSource(list));
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public byte[]  generateJasperReportXLS(String jasperReportName, ByteArrayOutputStream outputStream, List reportList) {
		 JRXlsxExporter exporter = new JRXlsxExporter();
	    try {
	    	String reportLocation = "/resources/"+ jasperReportName +".jrxml";
	    	JasperDesign jasperDesign = JRXmlLoader.load(getClass().getClassLoader().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, new JRBeanCollectionDataSource(reportList));
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 10000);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
	    	} catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	    	} finally {
	    	}
	    return outputStream.toByteArray();
		}
	public byte[] generateDailySummaryXLS(List<DailySummaryVo> dataList,String from,String to){
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	
	try{
		Collections.sort(dataList);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		HSSFFont boldFont = workbook.createFont();
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		HSSFCellStyle bstyle=workbook.createCellStyle();
		bstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		bstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		bstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		bstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		bstyle.setFont(boldFont);
		
		HSSFCellStyle b1style=workbook.createCellStyle();
		b1style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		b1style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		b1style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		b1style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		b1style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		b1style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		b1style.setFont(boldFont);
		
		HSSFCellStyle bgColor=workbook.createCellStyle();
		bgColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		bgColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
		bgColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
		bgColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		bgColor.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		bgColor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//bgColor.setDataFormat(fmt.getFormat("@"));
		bgColor.setFont(boldFont);
		
		HSSFCellStyle style=workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		//style.setDataFormat(fmt.getFormat("@"));
		
	   HSSFSheet sheet = workbook.createSheet("Daily Summary");
	   sheet.setDisplayGridlines(false);
	   sheet.setAutoFilter(CellRangeAddress.valueOf("B1:K1"));
		
	   sheet.setColumnWidth(0, 1200); //No
	   sheet.setColumnWidth(1, 5000); //Customer Code
	   sheet.setColumnWidth(2, 10000); //Customer Name
	   sheet.setColumnWidth(3, 5000); //Requested Pickup Date
	   sheet.setColumnWidth(4, 3000); //Driver Id
	   sheet.setColumnWidth(5, 10000); //Driver Name
	   sheet.setColumnWidth(6, 7000); //Pickup Zone
	   sheet.setColumnWidth(7, 7000); //Delivery Zone
	   sheet.setColumnWidth(8, 5000); //Truck Reg No
	   sheet.setColumnWidth(9, 4000); //Truck Type
	   sheet.setColumnWidth(10, 4000); //Empty Run
	   
	   HSSFRow row = sheet.createRow(0);
	   row.setHeight((short)300);
	   
	   HSSFCell cell = null;
	 
	    row.setHeight((short)300);
	    
		cell = row.createCell(0);
		cell.setCellValue("S.No");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(1);
		cell.setCellValue("Customer Code");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(2);
		cell.setCellValue("Customer Name");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(3);
		cell.setCellValue("Requested Pickup Date");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(4);
		cell.setCellValue("Driver Id");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(5);
		cell.setCellValue("Driver Name");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(6);
		cell.setCellValue("Pickup Zone");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(7);
		cell.setCellValue("Delivery Zone");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(8);
		cell.setCellValue("Truck Reg No");
		cell.setCellStyle(bgColor);

		cell = row.createCell(9);
		cell.setCellValue("Truck Type");
		cell.setCellStyle(bgColor);
		
		cell = row.createCell(10);
		cell.setCellValue("Empty Run");
		cell.setCellStyle(bgColor);
	    
	    //String tempCustCode = "";
	    int rowCnt = 0,sno=1;
		for (DailySummaryVo obj : dataList) {
			 rowCnt = rowCnt+1;
			 row = sheet.createRow(rowCnt);
			 row.setHeight((short)300);
				
				cell = row.createCell(0);cell.setCellValue(sno);cell.setCellStyle(style);
				cell = row.createCell(1);cell.setCellValue(obj.getCustomerCode());cell.setCellStyle(style);
				cell = row.createCell(2);cell.setCellValue(obj.getCustomerName());cell.setCellStyle(style);
				cell = row.createCell(3);cell.setCellValue(obj.getRequestedPickupDate());cell.setCellStyle(style);
			    cell = row.createCell(4);cell.setCellValue(obj.getDriverID());cell.setCellStyle(style);cell.setCellType(Cell.CELL_TYPE_STRING);
			    cell = row.createCell(5);cell.setCellValue(obj.getDriverName());cell.setCellStyle(style);
			    cell = row.createCell(6);cell.setCellValue(obj.getPickupLocation());cell.setCellStyle(style);
			    cell = row.createCell(7);cell.setCellValue(obj.getDeliveryLocation());cell.setCellStyle(style);
			    cell = row.createCell(8);cell.setCellValue(obj.getTruckRegNumber());cell.setCellStyle(style);
			    cell = row.createCell(9);cell.setCellValue(obj.getTruckType());cell.setCellStyle(style);
			    cell = row.createCell(10);cell.setCellValue(obj.getIsEmptyRun());cell.setCellStyle(style);
			
			sno++;
		}
		
			workbook.write(outByteStream);
		} catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
			try {
				outByteStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Daily Summary Report exporting..!");
		return outByteStream.toByteArray();
	}
	
	public byte[]  generateDoSummaryExcel(String jasperReportName, ByteArrayOutputStream outputStream, List reportList) {
		System.out.println("GenerateDoSummaryExcel: Report is getting generated");
		 JRXlsxExporter exporter = new JRXlsxExporter();
		 Map<String,Object>  parameters= new HashMap<String,Object>();
	    try {
	    	String reportLocation = "/resources/"+ jasperReportName +".jrxml";
	    	JasperDesign jasperDesign = JRXmlLoader.load(getClass().getClassLoader().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			parameters.put(JRParameter.IS_IGNORE_PAGINATION,true);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, new JRBeanCollectionDataSource(reportList));
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET, 1048576);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
	    	} catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	    	} finally {
	    	}
	    return outputStream.toByteArray();
		}
	
	public byte[]  generateDoSummaryListingJasperReportXLS(String jasperReportName, ByteArrayOutputStream outputStream, List reportList) {
		// JRXlsxExporter exporter = new JRXlsxExporter();
		 byte[] outArray = null;
		 try {
			logger.debug("generateDoSummaryListingJasperReportXLS:::::::::::::::"); 
	    	StringBuffer sb = new StringBuffer();
	    	int no = 1;
	        if (!reportList.isEmpty()){
	        	List<DOSummaryReportVO> list = new ArrayList<DOSummaryReportVO>();
	        	list = reportList;
	        	
			        sb.append("<table border='1' width='100%'><thead><tr>");
			        sb.append("<th>No</th>");
				    sb.append("<th>Type</th>");
				    sb.append("<th>DO No</th>");                                  
				    sb.append("<th>Manual</th>");
				    sb.append("<th>Collection</th>"); 
				    sb.append("<th>Delivery</th>");
				    sb.append("<th>Collection Date</th>");
				    sb.append("<th>DO Date</th>");
				    sb.append("<th>DO Copy Recevied date</th>");
				    sb.append("<th>Truck No</th>");                                  
				    sb.append("<th>Truck Code</th>");
				    sb.append("<th>Truck Type</th>"); 
				    sb.append("<th>Billing Customer</th>");
				    sb.append("<th>Driver</th>");
				    sb.append("<th>Status </th>");					    
				    sb.append("<th>Unit</th>");
				    sb.append("<th>Region</th>");  
				    sb.append("</tr></thead><tbody>");  
				 	
				    for (DOSummaryReportVO vo: list) {
				    	sb.append("<tr><td align='center'>");
						sb.append(no++); // no 
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoType()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoNumber()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getManualDo()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getPickupLocation()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDeliveryLocation()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getPlanLoadDate()); // Plan Load Date
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoDate()); // DO Date
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoRcvdDate()!=null ? vo.getDoRcvdDate():"" ); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckRegNo());// wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckCode()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckType()!=null ? vo.getTruckType():""); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getBillingCustomerName()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDriverId()!=null ? vo.getDriverId():""); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoStatus()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTotalUnits()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getRegion()); // wr no
						sb.append("</td></tr>");
						
							   for (DeliveryOrderSubDOVO voSub: vo.getSubDOVOList()) {
									sb.append("<tr><td align='center'>");
									sb.append(no++); // no 
									sb.append("</td><td align='center'>");
									sb.append(voSub.getType()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getSubDONumber()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getManualDO()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getPickupLocationDesc()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getDeliveryLocationDesc()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getPlanLoadDate()); // Plan Load Date
									sb.append("</td><td align='center'>");
									sb.append(voSub.getSubDODate()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getDoRcvdDate()!=null ? vo.getDoRcvdDate():"" ); // wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getTruckRegNo());// wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getTruckCode()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getTruckType()!=null ? vo.getTruckType():""); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getSubDOBillingCust()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(vo.getDriverId()!=null ? vo.getDriverId():""); // wr no
									sb.append("</td><td align='center'>");
									sb.append(""); // wr no
									sb.append("</td><td align='center'>");
									sb.append(voSub.getUnits()); // wr no
									sb.append("</td><td align='center'>");
									sb.append(""); // wr no
									sb.append("</td></tr>");
							   
							   }
						
				 	}
					sb.append("</table>");
			 		
					 outArray = sb.toString().getBytes();
	        }
	    } catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	           logger.error("error in invoice report ::::::::::::::::",e);
	    } 
	    return outArray;
	   
		}
	
	
	
/*	
 * old pattern commented by arul.
 * 
 * public byte[]  generateInvoiceJasperReportXLS(String jasperReportName, ByteArrayOutputStream outputStream, List reportList) {
		 JRXlsxExporter exporter = new JRXlsxExporter();
	    try {
	    	String reportLocation = "/resources/"+ jasperReportName +".jrxml";
	    	JasperDesign jasperDesign = JRXmlLoader.load(getClass().getClassLoader().getResourceAsStream(reportLocation));
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, new JRBeanCollectionDataSource(reportList));
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			//exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);			
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);			
			exporter.exportReport();
	    } catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	    } 
	    return outputStream.toByteArray();
	}*/
	
	
	public byte[]  generateInvoiceJasperReportXLS(String jasperReportName, ByteArrayOutputStream outputStream, List reportList) {
		 //JRXlsxExporter exporter = new JRXlsxExporter();
		 byte[] outArray = null;
		 try {
			logger.debug("generateInvoiceJasperReportXLS:::::::::::::::"); 
	    	TmsInvoiceReportVO vo = new TmsInvoiceReportVO();
	    	StringBuffer sb = new StringBuffer();
	    	int no = 1;
	        if (!reportList.isEmpty()){
	        	List<TmsInvoiceReportVO> list = new ArrayList<TmsInvoiceReportVO>();
	        	list = reportList;
	        	
	        	
			        sb.append("<table border='1' width='100%'><thead><tr>");
						
			        sb.append("<th>No</th>");
				    sb.append("<th>Invoice No</th>");
				    sb.append("<th>Date</th>");                                  
				    sb.append("<th>Customer Name</th>");
				    sb.append("<th>Cust Code</th>"); 
				    sb.append("<th>Term</th>"); 
				    sb.append("<th>DO No.</th>");
				    sb.append("<th>Ref No.</th>");                                  
				    sb.append("<th>Manual DO</th>");
				    sb.append("<th>DO Date</th>"); 
				    sb.append("<th>DO Copy Rev</th>");
				    sb.append("<th>Truck</th>");
				    sb.append("<th>Size </th>");					    
				    sb.append("<th>Type</th>");
				    sb.append("<th>Driver Name</th>");      
				    sb.append("<th>Driver Id</th>");
				    sb.append("<th>Toll Charges </th>"); 
				    sb.append("<th>UOM</th>");
				    sb.append("<th>UNIT</th>");
				    sb.append("<th>Pickup Loc </th>");					    
				    sb.append("<th>Delivery Loc</th>");
				    sb.append("<th>Amount</th>");      
				    sb.append("<th>Status</th>");
				    
				    sb.append("</tr></thead><tbody>");  
				 	for (java.util.Iterator it = list.iterator(); it.hasNext();) {
						vo = new TmsInvoiceReportVO();
				    	vo = (TmsInvoiceReportVO) it.next();
				    	
				    	sb.append("<tr><td align='center'>");
						sb.append(no++); // no 
						sb.append("</td><td align='center'>");
						sb.append(vo.getInvoiceNo()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getInvoiceDate()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getCustomerName()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getCustomerCode()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getPaymentTerm()!=null ? vo.getPaymentTerm():"" ); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoNo()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getReferenceNo()!=null ? vo.getReferenceNo():"" ); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getManualDO()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDoDate()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDocopyReceviedDate()!=null ? vo.getDocopyReceviedDate():""); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckRegNo()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckSize()!=null ? vo.getTruckSize():""); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTruckType()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDriverName()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDriver()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getTollCharges()!=null ? vo.getTollCharges():""); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getUom()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getUnits()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getPickupZone()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getDeliveryZone()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getAmount()); // wr no
						sb.append("</td><td align='center'>");
						sb.append(vo.getStatus()); // wr no
						sb.append("</td></tr>");
				 		
				 	}
					sb.append("</table>");
			 		
				    logger.debug("generateInvoiceJasperReportXLS:::::::::::::::"+sb.toString());
					
					 outArray = sb.toString().getBytes();
					 logger.debug("generateInvoiceJasperReportXLS::::::::::::test:::"+outArray);
	        }
	    } catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	           logger.error("error in invoice report ::::::::::::::::",e);
	    } 
	    return outArray;
	}
	
	public byte[]  generateJasperReportPettyCashReport(String jasperReportName, ByteArrayOutputStream outputStream, List pettyCashList) {

	    JRPdfExporter exporter = new JRPdfExporter();
	    try {
	           String reportLocation = "/resources/" + jasperReportName + ".jrxml";
	           
	           System.out.println("reportLocation is "+reportLocation);
	           JasperDesign jasperDesign = JRXmlLoader.load(getClass().getClassLoader().getResourceAsStream(reportLocation));
	           JasperReport jasperReport1 = JasperCompileManager.compileReport(jasperDesign);
	           JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1,null, new JRBeanCollectionDataSource(pettyCashList));
	           exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint1);	
	           exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
	           exporter.exportReport();
	    } catch (Exception e) {
	           e.printStackTrace();
	           System.out.println("Error in generate Report..."+e);
	    } finally {
	    }
	    return outputStream.toByteArray();

	}
	


/**
 * Method for generating pdf. Accepts the report name, byte array output
 * stream and the list of objects.
 * 
 * @param jasperReportName - String
 * @param stream - ByteArrayOutputStream
 * @param list - List
 * @return byte[]
 */ 
public byte[] generateMergedJasperReport(String jasperReportName, ByteArrayOutputStream stream, List<DailyAssignmentMultipleGatePassReportVO> list) {
	System.out.println("Inside Jasper" + jasperReportName+" with :"+list.size());
	JRPdfExporter exporter = new JRPdfExporter();
	List<JasperPrint> jasperPrintList=new ArrayList<JasperPrint>();
	try {
		String reportLocation = "/resources/" + jasperReportName + ".jrxml";
		JasperDesign jasperDesign = JRXmlLoader.load(getClass().getClassLoader().getResourceAsStream(reportLocation));
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		if(list!=null){
		   for(DailyAssignmentMultipleGatePassReportVO baseVO:list){
			   DailyAssignmentMultipleGatePassReportVO vo=baseVO;
			   Map<String,Object> parameterMap=vo.getMap();
			   List<BaseVO> voList=vo.getBaseVoList();
			   JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, new JRBeanCollectionDataSource(voList));
			   jasperPrintList.add(jasperPrint);
		   }
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
		exporter.exportReport();
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
	}
	return stream.toByteArray();
  }	
}