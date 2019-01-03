package com.giga.tms.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.giga.base.BaseVO;
import com.giga.base.LoggerUtility;
import com.giga.base.service.GigaServiceLocator;
import com.giga.exception.DataAccessException;
import com.giga.exception.ServiceException;
import com.giga.tms.ITMSServices;
import com.giga.tms.dao.DOSummaryDao;
import com.giga.tms.util.GenerateTMSReportUtil;
import com.giga.tms.vo.DOSummaryVo;

public class DOSummaryServiceImpl implements DOSummaryService{
	
	DOSummaryDao doSummaryDAO=null;
	private Logger logger=null;
	
	@SuppressWarnings("static-access")
	public DOSummaryServiceImpl() {
		try {
		logger=LoggerUtility.getInstance().getLogger(DOSummaryServiceImpl.class);
			doSummaryDAO= (DOSummaryDao) GigaServiceLocator
					.getService(ITMSServices.daoLayer.DOSUMMARY_DAO);
			
		} catch (ServiceException e) {			
			logger.error("Usage : TMS :: DOSummary : DOSummaryServiceImpl Class : CardMasterServiceImpl() :: "  ,e);
			e.printStackTrace();
		}
	}

	@Override
	public List<BaseVO> driverSearch(String driverCode,String drivername,String idCardNo) throws DataAccessException, ServiceException {
		//logger.debug("Usage : TMS :: DOSummary : DOSummaryServiceImpl Class : driverSearch():Processing.... " );   
		return doSummaryDAO.driverSearch(driverCode, drivername, idCardNo);
	}

	@Override
	public List<BaseVO> customerSearch(String customerCode, String customerName)
			throws ServiceException, DataAccessException {
		//logger.debug("Usage : TMS :: DOSummary : DOSummaryServiceImpl Class : customerSearch():Processing.... " );  
		return doSummaryDAO.customerSearch(customerCode, customerName);
	}

	@Override
	public List<DOSummaryVo> doSummarySearch(Map<String, String> criteriaMap)
			throws ServiceException, DataAccessException {
		//logger.debug("Usage : TMS :: DOSummary : DOSummaryServiceImpl Class : doSummarySearch():Processing.... " );  
		return doSummaryDAO.doSummarySearch(criteriaMap);
	}

	@Override
	public void saveDosummaryDetails(List<DOSummaryVo>  updateDOSmryVOList)
			throws ServiceException, DataAccessException {
		//logger.debug("Usage : TMS :: DOSummary : DOSummaryServiceImpl Class : saveDosummaryDetails() :Processing.... " );  
		doSummaryDAO.saveDosummaryDetails(updateDOSmryVOList);
		
	}
	
	/**
	 * This method is responsible for generating PDF report
	 * @param bookingID
	 * @return
	 * @throws Exception
	 */
	@Override
	
	public byte[] generatePDF(Map<String,String> criteriaMap) throws Exception {
		GenerateTMSReportUtil generateReportUtil = new GenerateTMSReportUtil();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] pdf = null;
		List<BaseVO> voList = null;
		
		String isPDFReport = criteriaMap.get("isPDFReport");
		
		voList = doSummaryDAO.doSummarySearchForReport(criteriaMap);
		try {
			if(voList!=null && voList.size()>0){
				if(isPDFReport.equalsIgnoreCase("TRUE")){
					pdf=generateReportUtil.generatePdf("TMSDOSummeryPDFReport", baos, voList);
				}else{
					pdf = generateReportUtil.generateDoSummaryListingJasperReportXLS("TMSDOSummeryReport", baos,voList);
				}
				//System.out.println("pdf :: "+pdf);
			}else{
				throw new ServiceException("No record exists with this search criteria, please change and search again");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pdf;
	}
}
