package com.giga.dms.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import org.apache.log4j.Logger;
import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.giga.dms.util.DMSGConstants;
import com.giga.dms.util.DMSGType;
import com.giga.dms.vo.DMSSearchVO;
import com.giga.dms.vo.DMSWebUploadVO;
import com.giga.dms.vo.KeyValueVo;
import com.giga.dms.vo.WebDataVO;
import com.google.gson.Gson;

public class BookingDMSHelper {

	public static final String DMS_SERVICE_URL = "http://10.60.105.216:9080/DMSServices/GMGDMSServices?wsdl";
	public static final String PROFILE_ACESS_FAILED = "PROFILE_ACC_FAILED";
	public static final String COMPANY_CODE = "COMPANY_CODE";
	public static final String BRANCH_CODE = "BRANCH_CODE";
	public static final String USER_NAME = "USER_NAME";
	public static final String USER_ID = "USER_ID";
	public static final String USER_ROLE = "USER_ROLE";
	public static final String USER_MAIL_ID = "USER_MAIL_ID";
	public static final String USER_PROFILE_MODULE_NAME = "USER_PROFILE_MODULE_NAME";
	public static final String LOGGED_MODULE_NAME = "LOGGED_MODULE_NAME";
	public static final String SYSTEM_LINE = "SYSTEM_LINE";
	public static final String UPLOAD_SUCCESS = "1001";
	public static final String UPLOAD_FAILURE_NOT_ALL_SET = "1002";
	public static final String UPLOAD_FAILURE_BY_ERROR = "1003";
	public static final String SEARCH_BOOKING = "SEARCH_BOOKING";
	public static final String SEARCH_JOB = "SEARCH_JOB";
	public static final String SEARCH_SI = "SEARCH_SI";
	public static final String SEARCH_CLAIM = "SEARCH_CLAIM";
	public static final String SEARCH_BOOKING_CUSTOMER = "SEARCH_BOOKING_CUSTOMER";
	public static final String NO_DOCUMENT_FOUND_ERROR = "NO_DOCUMENT_FOUND_ERROR";
	public static final String DMS_DOCUMENT_TITLE = "DMS_DOCUMENT_TITLE";
	public static final String DMS_DCUMENT_ID = "DMS_DCUMENT_ID";
	public static final String DMS_DOCUMENT_LINE = "DMS_DOCUMENT_LINE";
	public static final String DMS_DOCUMENT_TYPE = "DMS_DOCUMENT_TYPE";
	public static final String DMS_BOOKING_NO = "DMS_BOOKING_NO";
	public static final String DMS_JOB_NO = "DMS_JOB_NO";
	public static final String DMS_SI_NO = "DMS_SI_NO";
	public static final String DMS_CLAIM_NO = "DMS_CLAIM_NO";
	public static final String DMS_SHIPMENT_NO = "DMS_SHIPMENT_NO";
	public static final String DMS_QUOTATION_NO = "DMS_QUOTATION_NO";
	public static final String DMS_INVOICE_NO = "DMS_INVOICE_NO";
	public static final String DMS_PO_NO = "DMS_PO_NO";
	public static final String DMS_DO_NO = "DMS_DO_NO";
	public static final String DMS_LOAD_NO = "DMS_LOAD_NO";
	public static final String DMS_BOOKING_CUSTOMER = "DMS_BOOKING_CUSTOMER";
	public static final String DMS_UPLOAD_FORM_DATE = "DMS_UPLOAD_FORM_DATE";
	public static final String DMS_UPLOAD_TO_DATE = "DMS_UPLOAD_TO_DATE";
	public static final String LOAD_DOC_TYPE = "LOAD_DOC_TYPE";
	public static final String LOAD_BOOKING_PODS = "LOAD_BOOKING_PODS";
	public static final String LOAD_BOOKING_SERVICETYPES = "LOAD_BOOKING_SERVICETYPES";
	public static final String LOAD_DMS_BOOKING_SERVICELINES = "LOAD_DMS_BOOKING_SERVICELINES";
	public static final String LOAD_BOOKING_CARGOTYPES = "LOAD_BOOKING_CARGOTYPES";
	public static final String LOAD_BOOKING_BUSINESSLINE = "LOAD_BOOKING_BUSINESSLINE";
	public static final String LOAD_BOOKINGCUS_AND_BUSINESSLINE = "LOAD_BOOKINGCUS_AND_BUSINESSLINE";
	public static String LOGGED_USER = "";
	public static String LOGGED_USER_NEW = "";
	public static final String DMS_APPROVER = "DMS_APPROVER";
	public static final String SYSTEM_LINE_NEW = "SystemLine";
	public static final String DOCUMENT_LINE = "DocumentLine";
	public static final String DOCUMENT_TYPE = "DocumentType";
	public static final String BOOKING_CUSTOMER = "BookingCustomer";
	public static final String BOOKING_NO = "BookingNo";
	public static final String JOB_NO = "JobNo";
	public static final String SI_NO = "SINo";
	public static final String BUSINESS_LINE = "BusinessLine";
	public static final String SERVICE_TYPE = "ServiceType";
	public static final String SERVICE_LINE = "ServiceLine";
	public static final String PORT_OF_DISCHARGE = "PortOfDischarge";
	public static final String CARGO_TYPE = "CargoType";
	public static final String CLAIM_NO = "ClaimNo";
	public static final String UPLOADED_BY = "UploadBy";
	public static final String DOCUMENT_TITLE = "DocumentTitle";
	public static final String VOYAGE_NO = "VoyageNo";
	public static final String VESSEL_NAME = "VesselName";

	@SuppressWarnings("static-access")
	private static Logger logger = LoggerUtility.getInstance().getLogger(
			BookingDMSHelper.class);

	public static BookingDMSHelper getInstance() {
		return new BookingDMSHelper();
	}

	public static String getUserProfileModuleName(PortletSession session) {
		logger.debug("BookingDMSHelper :: getUserProfileModuleName() :processs.....");
		String profileGroupName = "";
		String loggedModuleName = "";
		String profileModuleName = "";
		String currentCompanyCode = "";
		String currentBranchCode = "";
		String userRole = "";
		String currentUser = "";
		String currentUserEmail = "";
		String currentUserId = "";
		String dmsApperover = "FALSE";
		Boolean profileflag = false;
		Boolean moduleChek = false;
		Map<String, String> pumaMap = new HashMap<String, String>();
		Map<String, String> moduleMap = new HashMap<String, String>();
		try {
			UserProfileAccessUtil gm = UserProfileAccessUtil.getInstance();
			if (gm != null) {
				Map<String, String> userDetails = gm.getCurrentUserDetails();
				if (userDetails != null)
					currentUser = userDetails
							.get(UserProfileAccessUtil.USER_NAME);
				currentCompanyCode = (String) userDetails
						.get(UserProfileAccessUtil.COMPANYNAME);
				currentBranchCode = (String) userDetails
						.get(UserProfileAccessUtil.BRANCH_CODE);
				currentUserEmail = (String) userDetails
						.get(UserProfileAccessUtil.USER_EMAILID);
				currentUserId = userDetails
						.get(UserProfileAccessUtil.USER_ID);
				
				// get the current user Details

				for (com.ibm.portal.um.Group grp : gm.getLoggerInUserGroups()) {
					Map<String, Object> groupAtt = gm.getGroupsAttributes(grp);
					logger.debug("groupAttr : groupAtt Map :: " + groupAtt);
					if (groupAtt != null ? !groupAtt.isEmpty() ? true : false
							: false) {
						profileGroupName = (String) groupAtt.get(groupAtt
								.keySet().iterator().next().toString());
						profileflag = profileGroupName != null ? profileGroupName
								.trim().contains("_") ? true : false : false;
						logger.debug("groupAttr : groupAtt :: "
								+ profileGroupName + "------ profileflag : "
								+ profileflag);

						// module--Selected..
						if (!moduleChek && profileflag) {
							profileModuleName = profileGroupName.split("_")[0];
							loggedModuleName = profileGroupName.split("_")[1];
							moduleMap.put(loggedModuleName.trim(),
									profileGroupName);
						}

						// all moduleType
						if (profileflag) {
							moduleChek = true;
							profileModuleName = profileGroupName.split("_")[0];
							loggedModuleName = profileGroupName.split("_")[1];
							pumaMap.put(loggedModuleName.trim(),
									profileGroupName);
						} else {
							pumaMap.put(profileGroupName.trim(),
									profileGroupName);
						}

						logger.debug("in Group :: profileflag : " + profileflag
								+ "---profileModuleName :: "
								+ profileModuleName);
					}
					logger.debug("in Group :: continue......");
				}
				logger.debug(" profile flag :: " + profileflag
						+ "--pumaMap.size :: " + pumaMap.size());
				if (!pumaMap.isEmpty() && pumaMap.size() > 0) {
					System.out.println("PumaMap :: is :: " + pumaMap);
					if (pumaMap.get("DMS") != null) {
						System.out.println("PumaMap :: is contains : DMS : "
								+ pumaMap);
						profileGroupName = pumaMap.get("DMS");
						profileModuleName = profileGroupName.split("_")[0];
						loggedModuleName = profileGroupName.split("_")[1];
						userRole = gm.getCurrentUserRole(loggedModuleName);
						if (userRole != null
								&& userRole.trim().equalsIgnoreCase("Approver")) {
							dmsApperover = "TRUE";
						}
						logger.debug("PumaMap :: is contains : DMS : { profileModuleName : "
								+ profileModuleName
								+ "},{ loggedModuleName : "
								+ loggedModuleName
								+ "},{ userRole : "
								+ userRole
								+ "} ,{ dmsApperover : "
								+ dmsApperover + "}");
					} else {
						logger.debug("ModuleMap :: is contains :  " + moduleMap);
						for (Map.Entry<String, String> entry : moduleMap
								.entrySet()) {
							profileGroupName = entry.getValue();
							profileModuleName = profileGroupName.split("_")[0];
							loggedModuleName = entry.getKey();
							userRole = gm.getCurrentUserRole(loggedModuleName);
							logger.debug("ModuleMap :: is contains:: { profileModuleName : "
									+ profileModuleName
									+ "},{ loggedModuleName : "
									+ loggedModuleName
									+ "},{ userRole : "
									+ userRole
									+ "},{ dmsApperover : "
									+ dmsApperover + "}");
						}
					}
				}
				if (!moduleChek) {
					logger.debug("profileModule fetching failed.....");
					throw new Exception();
				}

				if (session != null) {
					if (dmsApperover != null && !dmsApperover.equals(""))
						session.setAttribute(DMS_APPROVER, dmsApperover);
					if (profileModuleName != null
							&& !profileModuleName.equals(""))
						session.setAttribute(USER_PROFILE_MODULE_NAME,
								profileModuleName);
					if (currentCompanyCode != null
							&& !currentCompanyCode.equals(""))
						session.setAttribute(COMPANY_CODE, currentCompanyCode);
					if (currentBranchCode != null
							&& !currentBranchCode.equals(""))
						session.setAttribute(BRANCH_CODE, currentBranchCode);
					if (currentUserId != null && !currentUserId.equals("")){
						LOGGED_USER_NEW =currentUserId;
					}	
					if (currentUser != null && !currentUser.equals(""))
						session.setAttribute(USER_NAME, currentUser);
					if (userRole != null && !userRole.equals(""))
						session.setAttribute(USER_ROLE, userRole.toUpperCase());
					if (currentUserEmail != null
							&& !currentUserEmail.equals(""))
						session.setAttribute(USER_MAIL_ID, currentUserEmail);
					if (loggedModuleName != null
							&& !loggedModuleName.equals("")) {
						session.setAttribute(LOGGED_MODULE_NAME,
								loggedModuleName);
					}
				}

			}
			System.out.println("Logged user is :"+BookingDMSHelper.LOGGED_USER);
			System.out.println("BookingDMSHelper :: getUserProfileModuleName() :\n [ profileModuleName : "
					+ profileModuleName
					+ " ]"
					+ "\n [ loggedModuleName : "
					+ loggedModuleName
					+ "]\n "
					+ "[user id : "
					+ currentUserId
					+ "] \n "
					+ "[currentUser : "
					+ currentUser
					+ "]\n [userRole : "
					+ userRole
					+ "]"
					+ "\n [currentCompanyName : "
					+ currentCompanyCode
					+ "]\n [currentBranchCode : " + currentBranchCode + "]");
			// return request;
		} catch (Exception e) {
			logger.error("BookingDMSWebPortlet : checkUserProfileAuthentication : USER Checker Exception From Puma.Exception.............."
					+ e);
			// throw new Exception("PROFILE_ACESS_FAILED",e);
			profileModuleName = PROFILE_ACESS_FAILED;
		}
		return profileModuleName;
	}

	public DMSWebUploadVO populateData(ResourceRequest request,
			DMSGType searchType, String systemLine) {

		
		logger.debug("BookingDMSHelper :: populateData(): Start ::: \n ::[resourceRequest :"
				+ request
				+ "] ::[ DMSGType :searchType: "
				+ searchType
				+ "] :: [systemLine : " + systemLine + "]");
		DMSWebUploadVO dmsSearchVO = null;
		try {
			if (systemLine != null ? systemLine.trim().isEmpty() ? true : false
					: false) {
				return null;
			}
			dmsSearchVO = new DMSWebUploadVO();
			dmsSearchVO.setSystemLine(systemLine.trim());

			switch (searchType) {

			case DMS_BOOKINGNO_SERVICETYPES:
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter("bookingNo_Bk")));
				break;
			case DMS_BOOKINGNO_SERVICELINES:
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter("BOOKING_NO")));
				dmsSearchVO.setServiceType(nullCheckerString(request
						.getParameter("DOC_LINE_VAL")));
				break;
			case DMS_BOOKINGNO_CARGOTYPES:
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter("bookingNo_Bk")));
				break;
			case DMS_BOOKINGNO_PODS:
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter("BOOKING_NO")));
				dmsSearchVO.setServiceType(nullCheckerString(request
						.getParameter("DOC_LINE_VAL")));
				break;
			case DMS_LOAD_BOOKINGCUS_AND_BUSINESSLINE:
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter("bookingNo_Book")));
				break;

			case SEARCH_BOOKING_CUSTOMER:
				break;
			}

		} catch (Exception e) {
			logger.debug("BookingDMSHelper :: populateData : Exception " + e);
			dmsSearchVO = null;
		}
		logger.debug("BookingDMSHelper :: populateData :END :: dmsSearchVO "
				+ dmsSearchVO);
		if (dmsSearchVO != null) {
			logger.debug("BookingDMSHelper :: dmsSearchVO booking no is : "
					+ dmsSearchVO.getBookingNo());
		}
		return dmsSearchVO;
	}

	public DMSSearchVO createSearchVO(ResourceRequest request,
			DMSGType searchType, String systemLine,String branchCode,String companyCode) {
		
		logger.debug("DMSHelper :: createSearchVO(): Start ::: \n ::[resourceRequest :"
				+ request
				+ "] ::[ DMSGType :searchType: "
				+ searchType
				+ "] :: [systemLine : " + systemLine + "]");
		DMSSearchVO dmsSearchVO = null;
		try {
			if (systemLine != null ? systemLine.trim().isEmpty() ? true : false
					: false) {
				return null;
			}
			dmsSearchVO = new DMSSearchVO();
			dmsSearchVO.setSystemLine(systemLine.trim());
			dmsSearchVO.setBranchCode(nullCheckerString(branchCode));
			dmsSearchVO.setCompanyCode(nullCheckerString(companyCode));
			switch (searchType) {
			case SEARCH_BOOKING:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingDateFrom(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_DATE_FROM)));
				dmsSearchVO.setBookingDateTo(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_DATE_TO)));
				dmsSearchVO.setBusinessLine(nullCheckerString(request
						.getParameter(DMSGConstants.P_BUSINESSLINE)));
				dmsSearchVO.setVesselName(nullCheckerString(request
						.getParameter(DMSGConstants.P_VESSELNAME)));
				dmsSearchVO.setEtaDate(nullCheckerString(request
						.getParameter(DMSGConstants.P_ETA_DATE)));
				dmsSearchVO.setVoyageNo(nullCheckerString(request
						.getParameter(DMSGConstants.P_VOYAGENO)));
				break;
			case SEARCH_JOB:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setPortOfDischarge(nullCheckerString(request
						.getParameter(DMSGConstants.P_POD)));
				dmsSearchVO.setJobDateFrom(nullCheckerString(request
						.getParameter(DMSGConstants.P_JOB_DATE_FROM)));
				dmsSearchVO.setJobDateTo(nullCheckerString(request
						.getParameter(DMSGConstants.P_JOB_DATE_TO)));
				break;
			case SEARCH_SI:
				dmsSearchVO.setBookingCustomer(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setPortOfDischarge(nullCheckerString(request
						.getParameter(DMSGConstants.P_POD)));
				dmsSearchVO.setSiDateFrom(nullCheckerString(request
						.getParameter(DMSGConstants.P_SI_DATE_FROM)));
				dmsSearchVO.setSiDateTo(nullCheckerString(request
						.getParameter(DMSGConstants.P_SI_DATE_TO)));
				break;
			case SEARCH_CLAIM:
				dmsSearchVO.setClaimDateFrom(nullCheckerString(request
						.getParameter(DMSGConstants.P_CLAIM_DATE_FROM)));
				dmsSearchVO.setClaimDateTo(nullCheckerString(request
						.getParameter(DMSGConstants.P_CLAIM_DATE_TO)));
				dmsSearchVO.setBookingNo(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_NUMBER)));
				dmsSearchVO.setBookingCustomer(nullCheckerString(request
						.getParameter(DMSGConstants.P_BK_CUSTOMER)));
				dmsSearchVO.setIncidentNo(nullCheckerString(request
						.getParameter(DMSGConstants.P_INCIDENT_NO)));
				break;
			case SEARCH_BOOKING_CUSTOMER:
				dmsSearchVO.setCustomerName(nullCheckerString(request
						.getParameter(DMSGConstants.P_CUSTOMER_NAME)));
				dmsSearchVO.setCustomerCode(nullCheckerString(request
						.getParameter(DMSGConstants.P_CUSTOMER_CODE)));
				break;
			}

		} catch (Exception e) {
			logger.error("DMSHelper :: createSearchVO : Exception " + e);
			dmsSearchVO = null;
		}
		logger.debug("DMSHelper :: createSearchVO :END :: dmsSearchVO "
				+ dmsSearchVO);

		return dmsSearchVO;
	}

	private String nullCheckerString(String str) {
		return str != null ? !str.trim().isEmpty() ? str.trim() : "" : "";
	}

	/**
	 * This method is used to convert Object into JSon String value
	 * 
	 * @param obj
	 * @return
	 */
	public String convertToJson(Object obj) {
		String toJsonString = "";
		Gson gson = null;
		if (obj != null) {
			gson = new Gson();
			toJsonString = gson.toJson(obj);
		}
		logger.debug("DMSHelper :: convertToJson(): toJsonString"
				+ toJsonString);
		return toJsonString;
	}

	/**
	 * This method Used to Sorted the Dopdown values As Map<String,String>
	 * format
	 * 
	 * @category Method sortMapByValue()
	 * @param type_id
	 *            holds set of map instance
	 * @return Map<K, V> List of Map object as Ascending Order
	 */
	public static <K, V> Map<K, V> sortMapByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (((String) o1.getValue()).compareTo((String) o2
						.getValue()));
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext();) {
			Map.Entry<K, V> entry = it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public List<com.giga.dms.vo.KeyValueVo> getDocTypeListVO(
			Map<String, String> listOfDocTypeMap) {
		
		List<KeyValueVo> alKVvo = new ArrayList<KeyValueVo>();
		Set<String> mapKey = listOfDocTypeMap.keySet();
		
		System.out.println("Watch out ! key set is : "+listOfDocTypeMap.keySet());
		
		for (String key : mapKey) {
			KeyValueVo kvVO = new KeyValueVo();
			String val = (String) listOfDocTypeMap.get(key);
			kvVO.setKey(key);
			kvVO.setValue(val);
			alKVvo.add(kvVO);
		}
		
		
		
		return alKVvo;
	}

	public static String softAddToGrid(String docLine, String docType,
			String bookingNo, String jobNo, String siNo, String pod,
			String cargoType, String dgJsonList) {

		List<WebDataVO> listOfObjArrays = new ArrayList<WebDataVO>(0);
		return null;
	}
}
