package com.giga.tms.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.giga.base.BaseDAO;
import com.giga.base.BaseEntity;
import com.giga.base.BaseVO;
import com.giga.base.HibernateUtil;
import com.giga.base.LoggerUtility;
import com.giga.exception.DataAccessException;
import com.giga.master.dao.domain.Customer;
import com.giga.master.dao.domain.CustomerAddress;
import com.giga.master.dao.domain.MasterSetup;
import com.giga.tms.dao.domain.Driver;
import com.giga.tms.dao.domain.TmsBooking;
import com.giga.tms.dao.domain.TmsBookingLoadDetail;
import com.giga.tms.dao.domain.TmsDeliveryOrder;
import com.giga.tms.dao.domain.TmsDeliveryOrderChassis;
import com.giga.tms.dao.domain.TmsDeliveryOrderSub;
import com.giga.tms.dao.domain.TmsTransactionStatus;
import com.giga.tms.dao.domain.Truck;
import com.giga.tms.util.DOSummaryConstant;
import com.giga.tms.util.DeliveryOrderConstant;
import com.giga.tms.util.TMSMasterQueries;
import com.giga.tms.util.TMSUtil;
import com.giga.tms.util.TruckMasterConstant;
import com.giga.tms.vo.CustomerVO;
import com.giga.tms.vo.DOSummaryReportVO;
import com.giga.tms.vo.DOSummaryVo;
import com.giga.tms.vo.DeliveryOrderSubDOVO;
import com.giga.util.DateUtil;
@SuppressWarnings({"unchecked","rawtypes","static-access"})
public class DOSummaryDaoImpl extends BaseDAO implements DOSummaryDao{
	Driver driver = null;
	Session session =null;
	TmsDeliveryOrder deliveryOrder=null;
	DOSummaryVo doSummaryVo = null;
	Iterator<BaseEntity> iterator = null;
	TmsBookingLoadDetail tmsBookingLoadDetail = null;
	MasterSetup masterSetUp = null;
	private Logger logger=null;
	TMSUtil tmsUtil = null;
	int serialNoForReport = 0;
	/*Date minDoDate = null;
	Date maxDoDate = null;
	Set<Date> doDateSet = null;*/
	
	public DOSummaryDaoImpl() {
		super();
		tmsUtil = new TMSUtil();
		logger = LoggerUtility.getInstance().getLogger(DOSummaryDaoImpl.class);
		serialNoForReport = 1;
	}
	
	/**
	 * This method is used to retrieve all driver details from the driver table
	 * @param driverCode
	 * @param driverName
	 * @param idCardNo
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public List<BaseVO> driverSearch(String driverCode,
			String driverName,String idCardNo) throws DataAccessException {
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : driverSearch():Processing.... " );   
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		List<BaseVO> list = new ArrayList<BaseVO>();
		
		try {
			String queryString = TMSMasterQueries.SELECT_DRIVER;
			if(driverCode != null && !(driverCode.isEmpty())){
				queryString = queryString + " and d.driverCode LIKE '" + driverCode + "%'";
			}
			if(driverName != null && !(driverName.isEmpty())){
				queryString = queryString + " and d.name like "
						+ "'%" + driverName + "%'";
			}
			if(idCardNo != null && !(idCardNo.isEmpty())){
				queryString = queryString + " and d.idCardNo LIKE '" + idCardNo + "%'";
			}
			Query query = session.createQuery(queryString);
			List<BaseEntity> truckList = query.list();
			
			driver = new Driver();
			for (java.util.Iterator it = truckList.iterator();it.hasNext();){
				doSummaryVo = new DOSummaryVo();
				driver = (Driver) it.next();
				doSummaryVo.setDriverCode(driver.getDriverCode());
				doSummaryVo.setName(driver.getName());
				doSummaryVo.setIdCardNo(driver.getIdCardNo());
				list.add(doSummaryVo);
			}
		} catch (Exception e) {
		logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : driverSearch() Method :: "+
                    "HibernateException, Unable to retrieve driver details..",e);    
			throw new DataAccessException(TruckMasterConstant.RETRIEVAL_ERROR);
			//e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}
	
	/**
	 * This method is used to retrive  the customer details
	 * @param deliveryToCode
	 * @param deliveryToDesc
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public List<BaseVO> customerSearch(String customerCode, String customerName) throws DataAccessException {
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : customerSearch():Processing.... " );  
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		List<BaseVO> list = new ArrayList<BaseVO>();
		CustomerVO customerVO = null;
		try {
			Criteria criteria = session.createCriteria(Customer.class, "c");
			if (!tmsUtil.isNullOrEmpty(customerCode)) {
				criteria = criteria.add(Restrictions.eq("customerCode",customerCode));
			}
			if (!tmsUtil.isNullOrEmpty(customerName)) {
				criteria = criteria.add(Restrictions.like("customerName", "%"+ customerName + "%"));
			}
			List<BaseEntity> customerList = criteria.list();
			Customer customer = null;
			for (java.util.Iterator it = customerList.iterator(); it.hasNext();) {
				customerVO = new CustomerVO();
				customer = (Customer) it.next();
				customerVO.setCustomerId(customer.getCustomerId());
				customerVO.setCustomerCode(customer.getCustomerCode());
				customerVO.setCustomerName(customer.getCustomerName());
				list.add(customerVO);
			}
		} catch (Exception e) {
		logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : customerSearch() Method :: "+
                    "HibernateException, Unable to retrieve customer details..",e);    
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * This method is used to retrive  the dosummary  details
	 * @return
	 * @throws DataAccessException
	 */

	public List<DOSummaryVo>  doSummarySearch(Map<String, String> criteriaMap)throws DataAccessException{
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : doSummarySearch():Processing.... " );  
		List<DOSummaryVo> doSearchlist = new ArrayList<DOSummaryVo>();		
		String driverName=criteriaMap.get("driverName");
		String bookingCustomer=criteriaMap.get("bookingCustomer");
		String deliveredFromDate=criteriaMap.get("fromDate");
		String deliveredToDate=criteriaMap.get("toDate");
		String doCopyStatus=criteriaMap.get("doCopyStatus");
		String branchCode = criteriaMap.get(DeliveryOrderConstant.BRANCH_CODE);

		Session hibernateSession=getSession();
		try {			
			Criteria criteria = HibernateUtil.getSession().createCriteria(TmsDeliveryOrder.class, "do")
					.createAlias("do.driver", "d")
					/*.createAlias("do.customerAddress.customerByAddresseeCodeCustomerId", "ca",  CriteriaSpecification.LEFT_JOIN) // arul added 09Nov
					*/
					.createAlias("do.tmsBookingLoadDetail", "tbl")
					.createAlias("tbl.masterSetupByPickupLocationMasterId", "msp")
					.createAlias("tbl.masterSetupByDeliveryLocationMasterId", "msd")
					.createAlias("tbl.tmsBooking", "tb")
					.createAlias("tb.customer", "tc")
					.createAlias("tb.company", "cm")
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.addOrder(Order.desc("do.doDate"));
			if(!tmsUtil.isNullOrEmpty(branchCode)) {
				criteria.add(Restrictions.eq("cm.branchCode",branchCode));
			}

			if (bookingCustomer != null && !(bookingCustomer.isEmpty())) {
				criteria.add(Restrictions.eq("tc.customerName", bookingCustomer));
			}

			if (driverName != null && !(driverName.trim().isEmpty())) {
				criteria.add(Restrictions.eq("d.name", driverName));
			}

			// Display do date in the range
			if (deliveredFromDate != null && !deliveredFromDate.isEmpty()) {
				if (deliveredToDate != null && !deliveredToDate.isEmpty()) {
					criteria.add(Restrictions.between("do.doDate", DateUtil.getDate(deliveredFromDate,DateUtil.FORMAT_SLASH_DD_MM_YYYY),
							DateUtil.getDate(deliveredToDate,DateUtil.FORMAT_SLASH_DD_MM_YYYY)));
				}
			}

			if(doCopyStatus!=null){
				if(doCopyStatus.equals("Y")){
					criteria.add(Restrictions.isNotNull("doCopyReceivedDate"));
				}
				else if(doCopyStatus.equals("N")){
					criteria.add(Restrictions.isNull("doCopyReceivedDate"));
				}
			}
			List<TmsDeliveryOrder> dolist = (List<TmsDeliveryOrder>) criteria.list();
			for (TmsDeliveryOrder dor : dolist) {
				doSummaryVo=new DOSummaryVo();
				doSummaryVo = setDeliveryOrderToVO(dor);
				doSearchlist.add(doSummaryVo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : doSummarySearch() Method :: "+
					"HibernateException, Unable to retrieve dosummary details..",e);  
			throw new DataAccessException(DOSummaryConstant.RETRIEVAL_ERROR);
		} finally {
			HibernateUtil.closeSession(hibernateSession);
		}
		return doSearchlist;
	}
	
	@Override
	public List<BaseVO> doSummarySearchForReport(Map<String,String> criteriaMap)throws DataAccessException{
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : doSummarySearchForReport():Processing.... " ); 
		System.out.println("inside doSummarySearchForReport()");
		serialNoForReport = 1;
		List<BaseVO> doSearchlist = new ArrayList<BaseVO>();		
		String doDateFrom=criteriaMap.get("doDateFrom");
		String doDateTo=criteriaMap.get("doDateTo");
		String doCustomer=criteriaMap.get("doCustomer");
		String doDriver=criteriaMap.get("doDriver");
		String doTruck=criteriaMap.get("doTruck");
		String doVendor=criteriaMap.get("doVendor");
		String doStatusId=criteriaMap.get("doStatusId");
		Long doStatusIdLong = Long.parseLong(doStatusId);
		//String datePattern = DeliveryOrderConstant.CALENDAR_DATE_PATTERN;
		String branchCode = criteriaMap.get("branchCode");
		String companyCode = criteriaMap.get("companyCode");
		System.out.println("branchCode in daoImpl :: "+branchCode+" companyCode : "+companyCode);

		Session hibernateSession=getSession();
		try {			
			Criteria criteria = HibernateUtil.getSession().createCriteria(TmsDeliveryOrder.class, "do")
					.createAlias("do.driver", "d" , CriteriaSpecification.LEFT_JOIN)
					.createAlias("do.tmsBookingLoadDetail", "tbl")
					.createAlias("tbl.tmsBooking", "tb")
					.createAlias("tb.customer", "tc")
					.createAlias("do.truck", "tr" ,  CriteriaSpecification.LEFT_JOIN)
					.createAlias("do.vendor", "ve" ,  CriteriaSpecification.LEFT_JOIN)
					.createAlias("do.company", "cm")
					.createAlias("do.tmsTransactionStatus", "tts")
					.addOrder(Order.asc("do.doDate"));
			//.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			if(!tmsUtil.isNullOrEmpty(branchCode)) {
				criteria.add(Restrictions.eq("cm.branchCode",branchCode));
			}

			if (doCustomer != null && !(doCustomer.trim().isEmpty())) {
				criteria = criteria.add(Restrictions.eq("tc.customerCode", doCustomer));
			}

			if (doDriver != null && !(doDriver.trim().isEmpty())) {
				criteria = criteria.add(Restrictions.eq("d.driverCode", doDriver));
			}

			if (doTruck != null && !(doTruck.trim().isEmpty())) {
				criteria = criteria.add(Restrictions.eq("tr.truckCode", doTruck));
			}

			if (doVendor != null && !(doVendor.trim().isEmpty())) {
				criteria = criteria.add(Restrictions.eq("ve.vendorCode", doVendor));
			}

			if (doStatusIdLong != null && doStatusIdLong != 0L ) {
				criteria = criteria.add(Restrictions.eq("tts.tfStatusId", doStatusIdLong));
			}
			if (doDateFrom != null && !doDateFrom.isEmpty()) {
				if (doDateTo != null && !doDateTo.isEmpty()) {
					criteria.add(Restrictions.between("do.doDate", DateUtil.getDate(doDateFrom,DateUtil.FORMAT_SLASH_DD_MM_YYYY),
							DateUtil.getDate(doDateTo,DateUtil.FORMAT_SLASH_DD_MM_YYYY)));
				}

			}

			List<TmsDeliveryOrder> dolist = (List<TmsDeliveryOrder>) criteria.list();
			
			for (TmsDeliveryOrder dor : dolist) {
				DOSummaryReportVO doSumRepVo = new DOSummaryReportVO();
				doSumRepVo = setDeliveryOrderToReportVO(dor);
				doSumRepVo.setToDate(doDateTo);
				doSumRepVo.setFromDate(doDateFrom);
				String cmpName = "NEXUS MEGA CARRIERS SDN BHD";
				if(companyCode.contains("KK")){
					cmpName = "KK CAR TERMINAL";
				}
				doSumRepVo.setLoggedInUserCompany(cmpName);
				doSumRepVo.setUserId(criteriaMap.get("currentUserName"));
				doSumRepVo.setTotalDOCount(String.valueOf(dolist.size()));
				doSearchlist.add(doSumRepVo);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : doSummarySearchForReport() Method :: "+
					"HibernateException, Unable to retrieve dosummary details..",e);  
			throw new DataAccessException(DOSummaryConstant.RETRIEVAL_ERROR);
		} finally {
			HibernateUtil.closeSession(hibernateSession);
		}
		return doSearchlist;
	}
	
	private DOSummaryReportVO setDeliveryOrderToReportVO (TmsDeliveryOrder dor){
		DOSummaryReportVO doSummaryReportVo = new DOSummaryReportVO();
		//slNo
		doSummaryReportVo.setSlNo(String.valueOf(serialNoForReport));
		serialNoForReport++;
		//doType
		doSummaryReportVo.setDoType("MAIN");
		//doNumber
		doSummaryReportVo.setDoNumber(dor.getDoNumber());
		//manualDo
		doSummaryReportVo.setManualDo(dor.getManualDo() != null ? dor.getManualDo() : "");
		//pickUpLocation & deliveryLocation
		MasterSetup ms = dor.getMasterSetupByPickupLocationMasterId();
		if(ms != null){
			doSummaryReportVo.setPickupLocation(ms.getSetupDescription());
		}
		ms = dor.getMasterSetupByDeliveryLocationMasterId();
		if(ms != null){
			doSummaryReportVo.setDeliveryLocation(ms.getSetupDescription());
		}
		//pickUpDate and deliveryDate
		String pickupDate = DateUtil.getDateAsString(
				DateUtil.getDate(dor.getPickupDate().toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),
				DateUtil.FORMAT_SLASH_DD_MM_YYYY);
		doSummaryReportVo.setMainDOPickupDate(pickupDate);
		
		String deliveryDate = DateUtil.getDateAsString(
				DateUtil.getDate(dor.getDeliveredDate().toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),
				DateUtil.FORMAT_SLASH_DD_MM_YYYY);
		doSummaryReportVo.setMainDODeliveryDaate(deliveryDate);
		//DoDate
		Date doDate = dor.getDoDate();
		String subDoDate = "";
		if (doDate != null) {
			//doDateSet.add(doDate);
			subDoDate = DateUtil.getDateAsString(
					DateUtil.getDate(doDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),
					DateUtil.FORMAT_SLASH_DD_MM_YYYY);
			doSummaryReportVo.setDoDate(subDoDate);
		}else{
			doSummaryReportVo.setDoDate("");
		}
		if(dor.getTmsBookingLoadDetail().getRequestedPickupDate() != null)
			doSummaryReportVo.setPlanLoadDate(new TMSUtil().toDateString(DateUtil.FORMAT_SLASH_DD_MM_YYYY,dor.getTmsBookingLoadDetail().getRequestedPickupDate()));
		//truckRegistrationNumber and truckCode
		Truck truck = null;
		truck = dor.getTruck();
		String truckRegNo = "";
		String truckCode = "";
		String trucktype = "";
		if(truck != null){
			truckRegNo = truck.getRegistrationNo() != null ? truck.getRegistrationNo() : "";
			doSummaryReportVo.setTruckRegNo(truckRegNo);
			truckCode = truck.getTruckCode() != null ? truck.getTruckCode() : "";
			doSummaryReportVo.setTruckCode(truckCode);
			if(truck.getMasterSetupByTruckTypeMasterId()!=null){
			doSummaryReportVo.setTruckType(truck.getMasterSetupByTruckTypeMasterId().getSetupDescription());
			trucktype = truck.getMasterSetupByTruckTypeMasterId().getSetupDescription();
			}
		}
		//billingCustomer
		CustomerAddress billingCustomer = dor.getCustomerAddress();
		billingCustomer = dor.getCustomerAddress();
		String billingCustName = "";
		if(billingCustomer != null){
			Customer customer = billingCustomer.getCustomerByAddresseeCodeCustomerId();
			//long customerAddId = billingCustomer.getCustomerAddressId();
			if(customer != null){
				billingCustName = customer.getCustomerName();
				doSummaryReportVo.setBillingCustomerName(customer.getCustomerName());
			}else{
				billingCustName = billingCustomer.getCompanyName();
				doSummaryReportVo.setBillingCustomerName(billingCustomer.getCompanyName());
			}
		}
		//driverId( ID for display purpose, but value should be code)
		Driver driver = null;
		driver = dor.getDriver();
		String driverId = "";
		if(driver != null){
			//driverId = driver.getDriverCode() != null ? String.valueOf(driver.getDriverCode()) : "";
			driverId = driver.getName();
			doSummaryReportVo.setDriverId(driver.getName());
		}
		//doRecievedDate
		Date doCopyRcvdDate = dor.getDoCopyReceivedDate();
		String doCpyRcvdDate = ""; 
		if (doCopyRcvdDate != null) {
			doCpyRcvdDate = DateUtil.getDateAsString(
					DateUtil.getDate(doCopyRcvdDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),DateUtil.FORMAT_SLASH_DD_MM_YYYY);
			doSummaryReportVo.setDoRcvdDate(doCpyRcvdDate);
		}else{
			doSummaryReportVo.setDoRcvdDate("");
		}
		//doStatus
		TmsTransactionStatus status = dor.getTmsTransactionStatus();
		if(status != null){
			doSummaryReportVo.setDoStatus(status.getTfStatus());
		}
		//totalUnits
		doSummaryReportVo.setTotalUnits(dor.getAdditonalDrops() != null ? String.valueOf(dor.getAdditonalDrops()) : "");
		Set<TmsDeliveryOrderChassis> tempSet = dor.getTmsDeliveryOrderChassises();
		if(tempSet != null && tempSet.size() >0){
			doSummaryReportVo.setTotalUnits(String.valueOf(tempSet.size()));
		}else{
			doSummaryReportVo.setTotalUnits("0");
		}
		//region
		TmsBookingLoadDetail tmsBookingLoadDetail = null;
		tmsBookingLoadDetail = dor.getTmsBookingLoadDetail();
		if(tmsBookingLoadDetail != null){
			MasterSetup msrtSetup = tmsBookingLoadDetail.getMasterSetupByBookingTypeMasterId();
			if(msrtSetup != null){
				doSummaryReportVo.setRegion(msrtSetup.getSetupDescription());
			}
		}
		//UserId, FromDate and ToDate will have to set at service
		
		//subDOList
		Set<TmsDeliveryOrderSub> tmsDeliveryOrderSubs = dor.getTmsDeliveryOrderSubs();
		List<DeliveryOrderSubDOVO> subDOList = new ArrayList<DeliveryOrderSubDOVO>();
		DeliveryOrderSubDOVO subDOVO = null;
		if(tmsDeliveryOrderSubs != null && !tmsDeliveryOrderSubs.isEmpty()){
			for (Iterator iterator = tmsDeliveryOrderSubs.iterator(); iterator.hasNext();) {
				TmsDeliveryOrderSub subDOEntity = (TmsDeliveryOrderSub) iterator.next();
				
				subDOVO = new DeliveryOrderSubDOVO();
				//slNo
				/*subDOVO.setSubDOSlNo(String.valueOf(serialNoForReport));
				serialNoForReport++;*/
				//type
				subDOVO.setType("SUB-DO");
				//subDONumber
				System.out.println("sub do ::::::::;"+subDOEntity.getSubDoNumber());
				subDOVO.setSubDONumber(subDOEntity.getSubDoNumber());
				//manualDo
				subDOVO.setManualDO(subDOEntity.getManualDo() != null ? subDOEntity.getManualDo() : "");
				//pickupLocation
				MasterSetup pickupLoc = subDOEntity.getMasterSetupByPickupLocationMasterId();
				if(pickupLoc != null){
					subDOVO.setPickupLocationDesc(pickupLoc.getSetupDescription());
				}
				//deliveryLocation
				MasterSetup delLoc = subDOEntity.getMasterSetup();
				if(delLoc != null){
					subDOVO.setDeliveryLocationDesc(delLoc.getSetupDescription());
				}
				//pickup and delivery date
				subDOVO.setSubDOPickupDate(pickupDate);
				String subDoDelDate = DateUtil.getDateAsString(
						DateUtil.getDate(subDOEntity.getDeliveryDate().toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),
						DateUtil.FORMAT_SLASH_DD_MM_YYYY);
				subDOVO.setSubDODeliveryDate(subDoDelDate);
				//units
				Set<TmsDeliveryOrderChassis> chassisList = subDOEntity.getTmsDeliveryOrderChassises();
				if(chassisList != null){
					subDOVO.setUnits(String.valueOf(chassisList.size()));
				}else{
					subDOVO.setUnits("0");
				}
				//subDODate
				subDOVO.setSubDODate(subDoDate);
				//subDOTruck
				subDOVO.setSubDOTruck(truckRegNo);
				subDOVO.setSubDOTrucktype(trucktype);				
				//subDOTruckCode
				subDOVO.setSubDOTruckCode(truckCode);
				//subDODriver
				subDOVO.setSubDODriver(driverId);
				//subDOBillingCust
				subDOVO.setSubDOBillingCust(billingCustName);
				//subDOReceived Date
				subDOVO.setSubDORcvdDate(doCpyRcvdDate);
				
				subDOList.add(subDOVO);
			}
		}
		//sorting subDoList
		Collections.sort(subDOList, new Comparator<DeliveryOrderSubDOVO>() {
	        @Override public int compare(DeliveryOrderSubDOVO vo1, DeliveryOrderSubDOVO vo2) {
	            return vo1.getSubDONumber().compareTo(vo2.getSubDONumber());
	        }

	    });
		for (Iterator iterator = subDOList.iterator(); iterator.hasNext();) {
			DeliveryOrderSubDOVO vo = (DeliveryOrderSubDOVO) iterator.next();
			vo.setSubDOSlNo(String.valueOf(serialNoForReport));
			serialNoForReport++;
		}
		//subDOList
		doSummaryReportVo.setSubDOVOList(subDOList);
		
		return doSummaryReportVo;
	}
	
	/**
	 * This private method is used to retrieve the dosummary details 
	 * from  TmsDeliveryOrder , TmsBookingLoadDetailTable,TmsBooking
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	
	private DOSummaryVo setDeliveryOrderToVO(TmsDeliveryOrder dor) {
		try{
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : setDeliveryOrderToVO ():Processing.... " );  
		long doId = dor.getDoId();
		if(doId != 0){
			doSummaryVo.setDoId(String.valueOf(doId));
		}
		//DoNo
		doSummaryVo.setDoNo(dor.getDoNumber());
		//LoadNO
		doSummaryVo.setLoadNo(dor.getTmsBookingLoadDetail().getBookingLoadNumber());

		//Date doCopyRecvdDate = dor.getDoCopyReceivedDate();

		Date doCopyRecvdDate = dor.getDoCopyReceivedDate();
		if (doCopyRecvdDate != null) {
					doSummaryVo.setDocopyRecived(DateUtil.getDateAsString(DateUtil
					.getDate(doCopyRecvdDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),DateUtil.FORMAT_SLASH_DD_MM_YYYY));
		}

		//PickUpDate
		Date pickUpDate = dor.getPickupDate();
		if (pickUpDate != null) {
			doSummaryVo.setPickupDate(DateUtil.getDateAsString(DateUtil
					.getDate(pickUpDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),DateUtil.FORMAT_SLASH_DD_MM_YYYY));
		}
		//DeliveryDate
		Date deliveredDate = dor.getDeliveredDate();
		if (deliveredDate != null) {
			doSummaryVo.setDeliveryDate(DateUtil.getDateAsString(DateUtil
					.getDate(deliveredDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),DateUtil.FORMAT_SLASH_DD_MM_YYYY));
		}
		//DoDate
		Date doDate = dor.getDoDate();
		if (doDate != null) {
			doSummaryVo.setDoDate(DateUtil.getDateAsString(
					DateUtil.getDate(doDate.toString(),DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),DateUtil.FORMAT_SLASH_DD_MM_YYYY));
		}

		//Additional Drops
		Byte additionalDrops = dor.getAdditonalDrops();
		if(additionalDrops != null){
			doSummaryVo.setAddtionalDrop(additionalDrops.toString());
		}
		
		if(dor.getDoNumber()!=null && dor.getDoNumber().length()>0){
				logger.debug("getCustomer().getCustomerName()    "+dor.getCustomerAddress().getCustomer().getCustomerName());
				if(dor.getCustomerAddress().getCustomerByAddresseeCodeCustomerId()!=null){
				logger.debug("dor.getCustomerAddress().getCustomerByAddresseeCodeCustomerId().getCustomerName() "+dor.getCustomerAddress().getCustomerByAddresseeCodeCustomerId().getCustomerName());
				doSummaryVo.setBillingCustomer(dor.getCustomerAddress().getCustomerByAddresseeCodeCustomerId().getCustomerName());
				}
				else{
				logger.debug("dor.getCustomerAddress().getCompanyName() "+dor.getCustomerAddress().getCompanyName());
				doSummaryVo.setBillingCustomer(dor.getCustomerAddress().getCompanyName());
				}
		}
		
		tmsBookingLoadDetail = new TmsBookingLoadDetail();
		tmsBookingLoadDetail = dor.getTmsBookingLoadDetail();
		if(tmsBookingLoadDetail != null){
			/*//Additional Drops
			Byte additionalDrops = tmsBookingLoadDetail.getAdditonalDrops();
			if(additionalDrops != null){
				doSummaryVo.setAddtionalDrop(additionalDrops.toString());
			}*/
			//PickUp Location
			masterSetUp = new MasterSetup();
			masterSetUp = tmsBookingLoadDetail.getMasterSetupByPickupLocationMasterId();
			if(masterSetUp != null){
				doSummaryVo.setPickupLocation(masterSetUp.getSetupDescription());
			}
			//Delivery Location
			masterSetUp = new MasterSetup();
			masterSetUp = tmsBookingLoadDetail.getMasterSetupByDeliveryLocationMasterId();
			if(masterSetUp != null){
				doSummaryVo.setDeliveryLocation(masterSetUp.getSetupDescription());
			}
			//Booking
			TmsBooking tmsBooking =  new TmsBooking();
			tmsBooking = tmsBookingLoadDetail.getTmsBooking();
			if(tmsBooking != null){
				//Customer Name
				Customer customer = new Customer();
				customer = tmsBooking.getCustomer();
				if(customer != null){
					doSummaryVo.setCustomerName(customer.getCustomerName());
				}
				//Booking Date
				Date bookingDate = tmsBooking.getBookingDate();
				if (bookingDate != null) {
					doSummaryVo.setBookingDate(DateUtil.getDateAsString(
							DateUtil.getDate(bookingDate.toString(),
									DateUtil.FORMAT_HIPHEN_YYYY_MM_DD),
									DateUtil.FORMAT_SLASH_DD_MM_YYYY));
				}
			}
		}
		//totalUnits -- no of chassis to a DO
		Set<TmsDeliveryOrderChassis> chassisList = dor.getTmsDeliveryOrderChassises();
		Integer totalUnits;
		if(chassisList != null){
			totalUnits = chassisList.size();
		}else{
			totalUnits = 0;
		}
		doSummaryVo.setTotalUnits(totalUnits.toString());
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug("error while setting to do summary ::::::::::::::::::",e);
		}
		return doSummaryVo;
	}
	
	/**
	 * This method will save/Update the docopyRecived Date in the DB 
	 * @param
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public void saveDosummaryDetails(List<DOSummaryVo> updateDOSmryVOList)
			throws DataAccessException {
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : saveDosummaryDetails():Processing.... " );  
		//DOSummaryVo localVo = null;
		Session hibernateSession = getSession();
		Transaction trans = hibernateSession.beginTransaction();
		TmsDeliveryOrder doEntity = new TmsDeliveryOrder();
		try {
			for (DOSummaryVo doVO : updateDOSmryVOList) {
				
				doEntity = (TmsDeliveryOrder)hibernateSession.get(TmsDeliveryOrder.class,Long.parseLong(doVO.getDoId()));
			
				doEntity.setDoCopyReceivedDate(DateUtil.getDate(doVO.getDocopyRecived(),DateUtil.FORMAT_SLASH_DD_MM_YYYY));
				
			}
			hibernateSession.saveOrUpdate(doEntity);
			trans.commit();
			
			System.out.println(TmsDeliveryOrder.class.getName()+ "..Update()...");
		
			UpdatePlanningDetails(updateDOSmryVOList); //  arul added
		}catch (Exception e) {
			//e.printStackTrace();
			logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : saveDosummaryDetails() Method :: "+
                    "HibernateException",e); 
			throw new DataAccessException(DOSummaryConstant.SAVE_ERROR);
		
		} finally {
			HibernateUtil.closeSession(hibernateSession);
		}

	}

	public void UpdatePlanningDetails(List<DOSummaryVo> updateDOSmryVOList) throws DataAccessException {
		logger.debug("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : saveDosummaryDetails():Processing.... ");
		// DOSummaryVo localVo = null;
		Session hibernateSession = getSession();
		Transaction trans = hibernateSession.beginTransaction();
		TmsDeliveryOrder doEntity = new TmsDeliveryOrder();
		try {
			for (DOSummaryVo doVO : updateDOSmryVOList) {

				doEntity = (TmsDeliveryOrder) hibernateSession.get(TmsDeliveryOrder.class,
						Long.parseLong(doVO.getDoId()));

				// doEntity.setDoCopyReceivedDate(DateUtil.getDate(doVO.getDocopyRecived(),DateUtil.FORMAT_SLASH_DD_MM_YYYY));

				/*
				 * String doCopyRecvdDateString =
				 * headerMap.get(DeliveryOrderConstant.DO_COPY_RECEIVED_DATE);
				 * String manualDO =
				 * headerMap.get(DeliveryOrderConstant.MANUAL_DO); String
				 * bookingLoadIdStr =
				 * headerMap.get(DeliveryOrderConstant.BOOKING_LOAD_ID);
				 */
				TMSPlanningLoadDaoImpl impl = new TMSPlanningLoadDaoImpl();
				HashMap<String, String> para = new HashMap<String, String>();
				para.put("receviedDate",
						DateUtil.getDateAsString(doEntity.getDoCopyReceivedDate(), DateUtil.FORMAT_SLASH_DD_MM_YYYY));
				para.put("ManualDO", doEntity.getManualDo());
				para.put("LoadId", String.valueOf(doEntity.getTmsBookingLoadDetail().getBookingLoadId()));

				Boolean result = impl.updateLoadDetails(para);

				System.out.println("Saved Successfully" + "updateLoadDetails::::::::::::::::" + result);

			}
			// hibernateSession.saveOrUpdate(doEntity);
			trans.commit();

			System.out.println(TmsDeliveryOrder.class.getName() + "..Update()...");

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("Usage : TMS :: DOSummary : DOSummaryDaoImpl Class : saveDosummaryDetails() Method :: "
					+ "HibernateException", e);
			throw new DataAccessException(DOSummaryConstant.SAVE_ERROR);

		} finally {
			HibernateUtil.closeSession(hibernateSession);
		}

	}
	
}

