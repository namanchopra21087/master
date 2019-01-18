package com.giga.dms.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.log4j.Logger;

import com.giga.base.LoggerUtility;
import com.giga.base.UserProfileAccessUtil;
import com.ibm.portal.MetaData;
import com.ibm.portal.content.ContentMetaDataModel;
import com.ibm.portal.navigation.NavigationNode;
import com.ibm.portal.navigation.NavigationSelectionModel;
import com.ibm.portal.portlet.service.PortletServiceHome;
import com.ibm.portal.portlet.service.model.ContentMetaDataModelProvider;
import com.ibm.portal.portlet.service.model.NavigationSelectionModelProvider;

public abstract class BaseDMSWebPortlet extends GenericPortlet{
	
	public static final String JSP_FOLDER    = "/_DMSWeb/jsp/";    // JSP folder name

	public static String VIEW_JSP      = "DMSSearchPortletView";         // JSP file name to be rendered on the view mode
	public static final String SESSION_BEAN  = "DMSWebPortletSessionBean";  // Bean name for the portlet session
	public static final String FORM_SUBMIT   = "DMSWebPortletFormSubmit";   // Action name for submit form
	public static final String FORM_TEXT     = "DMSWebPortletFormText";     // Parameter name for the text input

	public static final String VIEW_JSP_PAGE_KEY="com.giga.dms.requestParameter";


	public static final String SEARCH_DOCUMENT_VIEW_JSP = "DMSSearchPortletView"; // Jsp file name to be view of Search DMS document module page
	//public static final String PICTURE_UPLOAD_VIEW_JSP = "DMSPictureUploadPortletView"; // Jsp file name to be view of Picutre Upload DMS document module page
	//public static final String WEB_DOCUMENT_UPLOAD_VIEW_JSP = "WebDocumentUploadPortletView"; // Jsp file name to be view of Picutre Upload DMS document module page
	
	   
	//USER PROFILE INFO PARAMETERS_KEY IN SESSION
	   public static final String PROFILE_ACESS_FAILED="PROFILE_ACESS_FAILED";
	   public static final String USER_PROFILE_MODULE_NAME="USER_PROFILE_MODULE_NAME";
	   public static final String COMPANY_CODE="COMPANY_CODE";
	   public static final String BRANCH_CODE="BRANCH_CODE";
	   public static final String USER_NAME="USER_NAME";
	   public static final String USER_ROLE="USER_ROLE";
	   public static final String USER_MAIL_ID="USER_MAIL_ID";
	   public static final String LOGGED_MODULE_NAME="LOGGED_MODULE_NAME";
	   public static final String DMS_APPROVER="DMS_APPROVER";
	   
	   
	   
	   
	@SuppressWarnings("static-access")
	public static Logger logger = LoggerUtility.getInstance().getLogger(BaseDMSWebPortlet.class); 
    public abstract void initialize();
    
	/**
	 * @see javax.portlet.Portlet#init()
	 */
	public void init() throws PortletException{
		super.init();
		System.out.println("Usage: DMS :BaseDMSWebPortlet :: init() processing... ");
		initialize();
	}
	
	

	@SuppressWarnings("unused")
	public static void setUserProfileInfoIntoSession(PortletSession session)throws Exception{
		logger.debug("BaseDMSWebPortlet :: getUserProfileModuleName() :processs.....");
		  String profileGroupName="";
		  String loggedModuleName="";
		  String profileModuleName="";
		  String currentCompanyCode="";
		  String currentBranchCode="";
		  String userRole="";
		  String currentUser="";
		  String currentUserEmail="";
		  String dmsApperover="FALSE";
		  Boolean profileflag=false;
		  Boolean moduleChek=false;
		  Map<String,String> pumaMap=new HashMap<String,String>();
		  Map<String,String> moduleMap=new HashMap<String,String>();
		  try {
			   UserProfileAccessUtil gm = UserProfileAccessUtil.getInstance();
			if(gm!=null){
				Map<String, String> userDetails = gm.getCurrentUserDetails();
				if(userDetails!=null)
				currentUser = userDetails.get(UserProfileAccessUtil.USER_NAME);
				currentCompanyCode=(String)userDetails.get(UserProfileAccessUtil.COMPANYNAME);
				currentBranchCode=(String)userDetails.get(UserProfileAccessUtil.BRANCH_CODE);
				currentUserEmail=(String)userDetails.get(UserProfileAccessUtil.USER_EMAILID);
				//get the current user Details
				
				for(com.ibm.portal.um.Group grp:gm.getLoggerInUserGroups()){
					 Map<String,Object> groupAtt=gm.getGroupsAttributes(grp);
					 System.out.println("groupAttr : groupAtt Map :: "+groupAtt);
					 if(groupAtt!=null?!groupAtt.isEmpty()?true:false:false){ 
					     profileGroupName=(String)groupAtt.get(groupAtt.keySet().iterator().next().toString());
						 profileflag=profileGroupName!=null?profileGroupName.trim().contains("_")?true:false:false;
						 System.out.println("groupAttr : groupAtt :: "+profileGroupName + "------ profileflag : "+profileflag);
						
						 //module--Selected..
						 if(!moduleChek && profileflag ){
							 profileModuleName= profileGroupName.split("_")[0];
							 loggedModuleName =profileGroupName.split("_")[1];
							 moduleMap.put(loggedModuleName.trim(), profileGroupName);
						 }
						 
						 //all moduleType
						 if(profileflag){
							 moduleChek=true;
							 profileModuleName= profileGroupName.split("_")[0];
							 loggedModuleName =profileGroupName.split("_")[1];
							 pumaMap.put(loggedModuleName.trim(), profileGroupName);
						 }else{
							 pumaMap.put(profileGroupName.trim(), profileGroupName);
						 }
						
						 
						 System.out.println("in Group :: profileflag : "+profileflag +"---profileModuleName :: "+profileModuleName);
					 }
					 System.out.println("in Group :: continue......"); 
				 }
				 System.out.println(" profile flag :: "+profileflag+"--pumaMap.size :: "+pumaMap.size()); 
				if(!pumaMap.isEmpty()&&pumaMap.size()>0){
					System.out.println("PumaMap :: is :: "+ pumaMap );
					if(pumaMap.get("DMS")!=null){
						System.out.println("PumaMap :: is contains : DMS : "+ pumaMap );
						profileGroupName=pumaMap.get("DMS");
						profileModuleName =profileGroupName.split("_")[0];
						loggedModuleName =profileGroupName.split("_")[1];
						userRole= gm.getCurrentUserRole(loggedModuleName);
						if(userRole!=null&&userRole.trim().equalsIgnoreCase("Approver")){
							dmsApperover="TRUE";
						}
						System.out.println("PumaMap :: is contains : DMS : { profileModuleName : "+ profileModuleName+"},{ loggedModuleName : "+loggedModuleName+
								"},{ userRole : "+userRole +"} ,{ dmsApperover : "+dmsApperover+"}" );
					}else{
						System.out.println("ModuleMap :: is contains :  "+ moduleMap );
						for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
							profileGroupName=entry.getValue();
							profileModuleName =profileGroupName.split("_")[0];
							loggedModuleName=entry.getKey();
							userRole= gm.getCurrentUserRole(loggedModuleName);
							System.out.println("ModuleMap :: is contains:: { profileModuleName : "+ profileModuleName+"},{ loggedModuleName : "+loggedModuleName+
									"},{ userRole : "+userRole +"},{ dmsApperover : "+dmsApperover+"}" );
						}
					}
				}
				if(!moduleChek){
					System.out.println("profileModule fetching failed.....");
					throw new Exception(); 
				}
				
				if(session!=null){
					if(dmsApperover!=null && !dmsApperover.equals(""))
						session.setAttribute(DMS_APPROVER, dmsApperover);  
					if(profileModuleName!=null && !profileModuleName.equals(""))
						session.setAttribute(USER_PROFILE_MODULE_NAME, profileModuleName);
					if(currentCompanyCode!=null && !currentCompanyCode.equals(""))
						session.setAttribute(COMPANY_CODE, currentCompanyCode);
					if(currentBranchCode!=null && !currentBranchCode.equals(""))
						session.setAttribute(BRANCH_CODE, currentBranchCode);
					if(currentUser!=null && !currentUser.equals(""))
						session.setAttribute(USER_NAME, currentUser);
					if(userRole!=null && !userRole.equals(""))
						session.setAttribute(USER_ROLE, userRole.toUpperCase());
					if(currentUserEmail!=null && !currentUserEmail.equals(""))
						session.setAttribute(USER_MAIL_ID, currentUserEmail);
					if(loggedModuleName!=null && !loggedModuleName.equals("")){
						session.setAttribute(LOGGED_MODULE_NAME, loggedModuleName);
					}
				}
			
			}
			System.out.println("BaseDMSWebPortlet :: getUserProfileModuleName() :\n [ profileModuleName : "+profileModuleName+" ]" +
					"\n [ loggedModuleName : "+loggedModuleName+"]\n [currentUser : "+currentUser+"]\n [userRole : "+userRole+"]" +
					"\n [currentCompanyName : "+currentCompanyCode+"]\n [currentBranchCode : "+currentBranchCode+"]");
			//return request;
		  } catch (Exception e) { 
			  System.out.println("BaseDMSWebPortlet : checkUserProfileAuthentication : USER Checker Exception From Puma.Exception.............."+e);
			  throw new Exception("PROFILE_ACESS_FAILED",e);
		}
		  
	  }
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	protected static String getRequestedJSPViewPage(RenderRequest request, RenderResponse response){
	    	 String forwardPage = "";
			 PortletServiceHome psh=null;
			 try{
				 System.out.println("BaseDMSWebPortlet :: Requsting page :: Processing... :");
				 Context context = new InitialContext();
				 psh = (PortletServiceHome) context.lookup(ContentMetaDataModelProvider.JNDI_NAME);
				 System.out.println("psh :: "+psh);
				 ContentMetaDataModelProvider provider = (ContentMetaDataModelProvider) psh.getPortletService(ContentMetaDataModelProvider.class);
				 ContentMetaDataModel model = provider.getContentMetaDataModel(request, response);
				 System.out.println("ContentMetaDataModel :: "+model);
				 NavigationSelectionModelProvider navprovider = (NavigationSelectionModelProvider) psh.getPortletService(NavigationSelectionModelProvider.class);
				 NavigationSelectionModel navmodel = navprovider.getNavigationSelectionModel(request, response);
				 NavigationNode selectedNode = (NavigationNode) navmodel.getSelectedNode();
				 System.out.println("selectedNode :: "+selectedNode);
				 if (selectedNode != null) {
					 MetaData md = model.getMetaData(selectedNode);
					 HashMap map=new HashMap();
					 Iterator itr = md.iterator();
					 while(itr.hasNext()){
						 Map.Entry entry = (Map.Entry) itr.next();
						 map.put(entry.getKey(),entry.getValue());
						 System.out.println("request key :: "+entry.getKey()+"=== and values  :"+ entry.getValue()); 
						 if (VIEW_JSP_PAGE_KEY.equals(entry.getKey())) {
							 forwardPage =  entry.getValue().toString();
							 System.out.println("forwardPage :: "+forwardPage);
							 break;
						 }	                   
					 }	              
				 }
			 }catch(Exception e){
				 System.err.println("Exception in reading requested page.................");
				 e.printStackTrace();
			 }
			 return forwardPage;
	     }
	 
	 	// Added on 24 Aug 2015 for Invoice and Quotation filter
	 	protected synchronized boolean isQuotationInvoiceMapped(){
	 		String profileGroupName="";
			try {
				UserProfileAccessUtil gm = UserProfileAccessUtil.getInstance();
				if(gm!=null){
					for(com.ibm.portal.um.Group grp:gm.getLoggerInUserGroups()){
						 Map<String,Object> groupAtt=gm.getGroupsAttributes(grp);
						 if(groupAtt!=null?!groupAtt.isEmpty()?true:false:false){ 
						     profileGroupName=(String)groupAtt.get(groupAtt.keySet().iterator().next().toString());
						     if(profileGroupName.equals("OFS_DMS_INV_QUOT")){
						    	 return true;
						     }
						 }
					}
				}
			} catch (NamingException e) {
				System.out.println("Exception : BaseDMSWebPortlet :: isQuotationInvoiceMapped() ::: "+e);
				e.printStackTrace();
			}
	 		return false;
	 	}
	 
	
/*	 *//**
		 * Process an action request.
		 * 
		 * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse)
		 *//*
		public void processAction(ActionRequest request, ActionResponse response) throws PortletException, java.io.IOException {
			if( request.getParameter(FORM_SUBMIT) != null ) {
				// Set form text in the session bean
				DMSWebPortletSessionBean sessionBean = getSessionBean(request);
				if( sessionBean != null )
					sessionBean.setFormText(request.getParameter(FORM_TEXT));
			}
		}

	*/
	
	/***
	 * This method is used to get the current Portlet session
	 * @param request
	 * @return
	 */
	 protected static PortletSession getCurrentPortletSession(PortletRequest request)
	 {
		 	 PortletSession session = request.getPortletSession();
			 if(session==null){
				 return request.getPortletSession(true);
			 }
			 return session;
	 }
	 /**
		 * This method is used to set values into portlet session scope
		 * @param request
		 * @param parameterType
		 * @param valueType
		 */
	     protected static void setSessionValues(PortletRequest request,String parameterType,Object valueType){
	    	 PortletSession session = request.getPortletSession();
	 		if( session == null ){
	 			System.out.println("session==null :: new session : ");
	 			 session=request.getPortletSession(true);
	 			 session.setAttribute(parameterType,valueType);
	 		}else{
	 			System.out.println("old Session : ");
	 			session.setAttribute(parameterType,valueType);
	 		}
	 		
	     }
	     
	
	/**
	 * This getSessionValues will give the session hold values
	 * @param request
	 * @param typeOfValue
	 * @return
	 */
     protected static Object getSessionValues(PortletRequest request,String typeOfValue){
    	 PortletSession session = request.getPortletSession();
 		if( session == null ){
 			System.out.println("getSessionValues :: Session Null : "+session);
 			return null;
 		}else{
 		 return session.getAttribute(typeOfValue);
 		}
 		
     }
     
     /**
      * *This method is used to set the current Portlet Session time*
      * @param request
      * @param timeInterval
      */
     protected static void setSessionTime(PortletRequest request,int timeInterval){
    	 PortletSession session = request.getPortletSession();
    	 if(session == null ){
    		 session=request.getPortletSession(true);
    		 session.setMaxInactiveInterval(timeInterval);
    	 }else{
    		 session.setMaxInactiveInterval(timeInterval);
    	 }
     }
	

 	/**
 	 * Get SessionBean.
 	 * 
 	 * @param request PortletRequest
 	 * @return DMSWebPortletSessionBean
 	 */
 	protected static DMSWebPortletSessionBean getSessionBean(PortletRequest request) {
 		PortletSession session = request.getPortletSession();
 		if( session == null )
 			return null;
 		DMSWebPortletSessionBean sessionBean = (DMSWebPortletSessionBean)session.getAttribute(SESSION_BEAN);
 		if( sessionBean == null ) {
 			sessionBean = new DMSWebPortletSessionBean();
 			session.setAttribute(SESSION_BEAN,sessionBean);
 		}
 		return sessionBean;
 	}

 	/**
 	 * Returns JSP file path.
 	 * 
 	 * @param request Render request
 	 * @param jspFile JSP file name
 	 * @return JSP file path
 	 */
 	protected static String getJspFilePath(RenderRequest request, String jspFile) {
 		String markup = request.getProperty("wps.markup");
 		if( markup == null )
 			markup = getMarkup(request.getResponseContentType());
 		return JSP_FOLDER + markup + "/" + jspFile + "." + getJspExtension(markup);
 	}

 	/**
 	 * Convert MIME type to markup name.
 	 * 
 	 * @param contentType MIME type
 	 * @return Markup name
 	 */
 	private static String getMarkup(String contentType) {
 		if( "text/vnd.wap.wml".equals(contentType) )
 			return "wml";
         else
             return "html";
 	}

 	/**
 	 * Returns the file extension for the JSP file
 	 * 
 	 * @param markupName Markup name
 	 * @return JSP extension
 	 */
 	private static String getJspExtension(String markupName) {
 		return "jsp";
 	}
}
