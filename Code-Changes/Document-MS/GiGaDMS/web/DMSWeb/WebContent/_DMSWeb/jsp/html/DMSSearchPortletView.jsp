<%@page session="false" contentType="text/html" pageEncoding="ISO-8859-1" import="java.util.*,javax.portlet.*,com.giga.dms.controller.*" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>          
<%@taglib uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model" prefix="portlet-client-model" %>        
<portlet:defineObjects/>
<portlet-client-model:init>
      <portlet-client-model:require module="ibm.portal.xml.*"/>
      <portlet-client-model:require module="ibm.portal.portlet.*"/>   
</portlet-client-model:init>  

	<link href= "/css/easyui.css"	rel="stylesheet"/>
	<link href="/css/icon.css"	rel="stylesheet"/>
	<link href="/css/demo.css"	rel="stylesheet"/>
	<link href="/css/button.css" rel="stylesheet"/> 
	<link href="/css/DMSSearchPopUp.css" rel="stylesheet"/> 
	
	<!--
	<script type="text/javascript"	src="/js/jquery.min.js"></script>
	<script type="text/javascript"	src="/js/jquery.easyui.min.js"></script>
	<script type="text/javascript"	src="/js/barcodePopup.js"></script>
	<script type="text/javascript"	src="/js/datagrid-filter.js"></script>
	<script type="text/javascript"	src="/js/validationBCP.js"></script>   
	-->  
	
	  
<script type="text/javascript"	src= "/js/jquery.min.js"></script> 
<script type="text/javascript"	src= "/js/jquery.easyui.min.js"></script>
<script type="text/javascript"	src= "/js/datagrid-filter.js"></script>
<script type="text/javascript"	src= "/js/datagrid-bufferview.js"></script>
<script type="text/javascript"  src="/js/DMSSearchPopUp.js"></script> 
<script type="text/javascript"  src="/js/CommonScriptFunctions.js"></script> 
<script type="text/javascript"  src="/js/gigasessionvalidator.js"></script> 


<style>
	  .systemLineLbl,.systemLineCss{  
	   display : none; 
	  } 
	  /** booking..css **/ 
	  
	  .tms_show{display:none;}
	[field="priviewIconId"] input[type="text"]{ display:none;}
	[field="downLoadId"] input[type="text"]{ display:none;}
	[field="deleteId"] input[type="text"]{ display:none;}
	[field="dmsId"] input[type="text"]{ display:none;}	


	[field="priview"] input[type="text"]{ display:none;}
	[field="download"] input[type="text"]{ display:none;}
	[field="deleteD"] input[type="text"]{ display:none;}

	img { cursor: pointer; cursor: hand; }
</style> 

<body>
      <span id="OneAndMore_msg" style="display: none"><font><B>One or more mandatory fields are required</B></font></span>  
      <span id="SearchSuccess_msg" style="display: none"><font><B>Documents searching completed successfully</B></font></span>
	  <span id="NoReusultReturn_msg" style="display: none"><font><B>No result return, please change the
	  search criteria</B></font></span> 
	  <span id="SearchFailed_msg" style="display: none"><font><B>Document searching failed, please try again</B></font></span>
	  <span id="DeleteFailed_msg" style="display: none"><font><B>Document delete failed, please try again</B></font></span>
	  <span id="DownloadFailed_msg" style="display: none"><font><B>Document download failed, please try again</B></font></span>
	  <span id="ViewFailed_msg" style="display: none"><font><B>Document view failed, please try again</B></font></span>
	  
	  <span id="DataLoadingFalid_msg" style="display: none"><font><B>Unable to load the master data, please contact with administrator </B></font></span>
	  <span id="InvalidUser_msg" style="display: none"><font><B>Unauthorized login user, please login with valid credentials</B></font></span>
	<!--  Success Message Configuration -->    
	<div id="successMessages" title="Success Details"
		style="width: 938px; text-align: left; border-color: #95B8E7; display: none; border-style: solid; border-width: 1px;">
		<div style="width: 938px; text-align: right;"></div>
		<table class="easyui-panel"	style="width: 938px; background-color: #EFF5FF">  
			<tr>  
				<td style="width: 90%; text-align: left;"> 
					<div>
						<b>Message</b>
					</div>
				</td>
				<td style="width: 10%; text-align: right;">
					<div>
						<img border="1" onClick="sucessHide();"
							src="/images/close-icon.png"
							id="closeSuccessDiv" width="17" height="17" class="closeErrorBox">
					</div>
				</td>
			</tr>
		</table> 
		<div id="successMsg" style="color: green"></div>
	</div>
<!-- ------------------------Success Messages Content Bar of Barcode Printing END----------------------------------------- -->
<!-- ------------------------Error Messages Content Bar of Barcode Printing START----------------------------------------- -->
	<div id="errorMsg" title="Error Details"
		style="width: 938px; text-align: left; border-color: #95B8E7; display: none; border-style: solid; border-width: 1px;">

		<div style="width: 938px; text-align: right;"></div>
		<table class="easyui-panel" style="width: 938px; background-color: #EFF5FF"> 
			<tr>
				<td style="width: 90%; text-align: left;">
					<div>      
						<b>Message</b>
					</div>
				</td>
				<td style="width: 20%; text-align: right;">
					<div>
						<img border="1" onClick="errorHide();"
							src= "/images/close-icon.png"
							id="errorclose" width="17" height="17" class="closeErrorBox">
					</div>
				</td>
			</tr>
		</table>        

		<div id="errorConent" style="color: red; background:#f0f0f0;padding:0px 2px 6px;"></div>

	</div> 
	<div style="margin: 5px 0;"></div>
<!-- ------------------------Error Messages Content Bar of Barcode Printing END----------------------------------------- -->



<div class="easyui-panel" title="Search Document" style="width:940px; background: linear-gradient(#F5F5F5,#F5F5F5)">
<div style="padding:1px 0 2px 0px" >
<table width="100%"border="0"  bgcolor="#F5F5F5"> 
<!-- ------------------------------------ROW 1--------------------------------  -->
 <tr>
    <td width="15%" height="25" align="right"><div class="systemLineLbl">System Line&nbsp;&nbsp;: <span class="madantFld">*</span></div></td>
    <td width="15%">
     <span class="systemLineCss"> <select
									style="width: 136px;" class="easyui-combobox requiredGMG"
									name="systemLineOpt" id="systemLineOpt">
										<option value="">--Select a value--</option>
										<c:forEach var="systemLine" items="${systemLineList}">
											<option value="${systemLine.key}">${systemLine.value}</option>
										</c:forEach>

								</select>
							</span>
    </td>   
    <td width="12%"></td>
    <td width="15%"></td>
    <td width="12%"></td>
    <td width="20%"></td> 
  </tr>   
  <!-- ------------------------------------ROW 2--------------------------------  -->
  <tr>
    <td width="15%" height="25" align="right"><div>Document Line&nbsp;&nbsp;: <span class="madantFld">*</span></div></td>
    <td width="15%">
     <span class="documentLineOpt"> <select
									style="width: 136px;" class="easyui-combobox requiredFieldDMSS"
									name="documentLineOpt" id="documentLineOpt">
										<option value="">--Select a value--</option>
										<c:forEach var="documentType" items="${documentLineList}">
											<option value="${documentType.key}">${documentType.value}</option>
										</c:forEach>


								</select>
							</span>
    </td>   
    <td width="12%" height="25" align="right"><div>Document Type&nbsp;&nbsp;:<span class="madantFld"></span></td>
    <td width="15%">
	 <span class="documentTypeCSS"> <select
									style="width: 136px;" class="easyui-combobox"
									name="documentTypeOpt" id="documentTypeOpt" >
										<option value="">--Select a value--</option>
								</select>
							</span>
	</td>  
    <td width="12%"></td>
    <td width="20%"></td> 
  </tr>    
  <!-- ------------------------------------ROW 3--------------------------------  -->
  <tr>
    <td width="15%" height="25" align="right"><div>Booking No&nbsp;&nbsp;: </div></td> 
    <td width="15%">
     <span class="bookingNoCss spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="bookingNoTxt" id="bookingNoTxt"  /><img class="clearImg" id="BookingNoEreaser"src= "/images/img_clear.gif" title="To Clear Booking No"/><img
									id="bookingNosearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup bookingNoDMSSearchPopup disableCss"
									src="/images/search.png" /> </span>
    </td>  
    <td width="12%" height="25" align="right"><div>Job No&nbsp;&nbsp;:</div></td>
    <td width="15%">
	<span class="jobNoCss spnRqrdTrue">   
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="jobNoTxt" id="jobNoTxt"/><img class="clearImg" id="JobNoEreaser"src= "/images/img_clear.gif" title="To Clear Job No"/><img
									id="jobNoSearch" 
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup jobNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
	</td>
    <td width="12%" height="25" align="right"><div>SI No&nbsp;&nbsp;:</div></td> 
    <td width="20%">
	<span class="siNoCss spnRqrdTrue"> 
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup enabledByOFS"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="siNoTxt" id="siNoTxt" /><img class="clearImg" id="SiNoEreaser"src= "/images/img_clear.gif" title="To Clear SI No. "/><img
									id="siNumberSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup siNoDMSSearchPopup enabledByOFS disableCss"
									src="/images/search.png" /> </span>
	</td> 
  </tr>  
  <!-- ------------------------------------ROW 4--------------------------------  -->
 <tr>  
    <td width="15%" height="25" align="right"><div>Claim No&nbsp;&nbsp;: </div></td>
    <td width="15%">
     <span class="claimNoCss spnRqrdTrue"> 
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="claimNoTxt" id="claimNoTxt" /><img class="clearImg" id="ClaimNoEreaser"src= "/images/img_clear.gif" title="To Clear Claim No"/><img
									id="claimNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup claimNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
    </td>   
    <td width="12%" height="25" align="right"><div>Shipment No&nbsp;&nbsp;:</div></td>
    <td width="15%">
	<span class="shipmentNoCss spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text" 
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="shipmentNoTxt" id="shipmentNoTxt"  /><img class="clearImg" id="ShipmentNoEreaser"src= "/images/img_clear.gif" title="To Clear Shipment No."/><img
									id="shipmentNoSeach"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup shipmentNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
	</td>
    <td width="12%" height="25" align="right"><div>Invoice No&nbsp;&nbsp;:</div></td>
    <td width="20%">
	<span class="invoiceNoCss spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="invoiceNoTxt" id="invoiceNoTxt"  /><img class="clearImg" id="InvoiceNoEreaser"src= "/images/img_clear.gif" title="To Clear Invoice No."/><img
									id="invoiceNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup invoiceNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
	</td> 
  </tr>  
  <!-- ------------------------------------ROW 5--------------------------------  -->
 <tr>  
    <td width="15%" height="25" align="right"><div>Quotation No&nbsp;&nbsp;: </div></td>
    <td width="15%">
     <span class="quotationNoCss spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="quotationNoTxt" id="quotationNoTxt"/><img class="clearImg" id="QuotationNoEreaser"src= "/images/img_clear.gif" title="To Clear Quotation No."/><img
									id="quotationNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup quotationNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
    </td>     
    <td width="12%" height="25" align="right"><div>PO No&nbsp;&nbsp;:</div></td>
    <td width="15%"> 
	<span class="poNoCSS spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="poNoTxt" id="poNoTxt"  /><img class="clearImg" id="PONoEreaser"src= "/images/img_clear.gif" title="To Clear PO No."/><img
									id="poNoSearchPopup"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup poNoDMSSearchPopup disableCss"
									src="/images/search.png" /> </span>
	</td>
    <td width="12%" height="25" align="right"><div>DO No&nbsp;&nbsp;:</div></td>
    <td width="20%"> 
	<span class="doNoCss spnRqrdTrue">
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="doNoTxt" id="doNoTxt" /><img class="clearImg" id="DONoEreaser"src= "/images/img_clear.gif" title="To Clear DO No."/><img
									id="doNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup doNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>
    </td>   
	</td>    
  </tr>  
  <!-- ------------------------------------ROW 6--------------------------------  -->
 <tr> 
    <td width="15%" height="25" align="right"><div>Load No&nbsp;&nbsp;: </div></td>
    <td width="15%">
     <span class="loadNoCss spnRqrdTrue">     
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup requiredGMG"
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="loadNoTxt" id="loadNoTxt" /><img class="clearImg" id="LoadNoEreaser"src= "/images/img_clear.gif" title="To Clear Load No."/><img  
									id="loadNoSearch"     
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup loadNoDMSSearchPopup disableCss"
									src="/images/search.png" /></span>   
    </td>    	   
    <td width="12%" height="25" align="right"><div>Booking Customer&nbsp;&nbsp;:</div></td>
    <td width="15%">   
	<span class="bookingCustCss spnRqrdTrue">    
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup bkCustomerCssDMS" 
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="bookingCustTxt" id="bookingCustTxt" maxlength="256"/><img class="clearImg" id="BkCustomerEreaser"src= "/images/img_clear.gif" title="To Clear Booking No."/><img
									id="bookingCustSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup bookingCustomerDMSSearchPopup customerSarchIconDMS disableCss"
									src="/images/search.png" /></span>
	</td>
    <td width="12%" height="25" align="right">Credit Note&nbsp;&nbsp;: </td>
    <td width="20%"> 
		<span class="creditNoteCss spnRqrdTrue">    
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup creditNoteCssDMS" 
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="creditNoteTxt" id="creditNoteTxt" maxlength="256"/><img class="clearImg" id="CreditNoteEreaser"src= "/images/img_clear.gif" title="To Clear Credit Note"/><img
									id="creditNoteSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup creditNoteDMSSearchPopup creditNoteSarchIconDMS disableCss"
									src="/images/search.png" /></span>
	</td> 
  </tr>    
   <!-- ------------------------------------ROW 7--------------------------------  -->
 <tr> 
    <td width="15%" height="25" align="right">Debit Note&nbsp;&nbsp;: </td>
    <td width="20%"> 
		<span class="debitNoteCss spnRqrdTrue">    
									<!-- readonly="readonly" --> <input type="text"
									class="customSPopup debitNoteCssDMS" 
									style="width: 99px; background-color: #F8F8FF; border: none"
									name="debitNoteTxt" id="debitNoteTxt" maxlength="256"/><img class="clearImg" id="DebitNoteEreaser"src= "/images/img_clear.gif" title="To Clear Debit Note"/><img
									id="debitNoteSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup debitNoteDMSSearchPopup debitNoteSarchIconDMS disableCss"
									src="/images/search.png" /></span>
	</td> 
    <td width="15%" height="25" align="right"><div>Upload Date From&nbsp;&nbsp;: <span class="madantFld">*</span></div></td>
    <td width="15%">
    <span class="uploadDateFromTxt"> <input type="text" 
									class="easyui-datebox loadFromDate requiredFieldDMSS" value=""
									data-options="formatter:myformatter,parser:myparser"
									validType="validDate " style="width: 136px;"
									name="uploadDateFromTxt" id="uploadDateFromTxt" /><span>(dd/mm/yyyy)</span>
    </td>    
    <td width="12%" height="25" align="right"><div>Upload Date To&nbsp;&nbsp;:<span class="madantFld">*</span></div></td>
    <td width="15%">  
	<span class="uploadDateToTxt"> <input type="text"
									class="easyui-datebox LoadTodaysDate requiredFieldDMSS" value=""
									data-options="formatter:myformatter,parser:myparser"
									validType="validDate " style="width: 136px;"
									name="uploadDateToTxt" id="uploadDateToTxt" /><span>(dd/mm/yyyy)</span>
	</td>
  <!--  <td width="12%" height="25" align="right"></td>
    <td width="20%">  
	</td> -->
  </tr>    
 	  </tr>
      		   <tr id="showsYMS"> 
			   
							<td width="15%" height="25" align="right"><div>Delivery Note No. &nbsp;&nbsp;: </div></td>
							<td width="15%">
							 <span class="deliveryNoCss spnRqrdTrue">     
															<!-- readonly="readonly" --> <input type="text"
															class="customSPopup requiredGMG"
															style="width: 99px; background-color: #F8F8FF; border: none"
															name="deliveryNoteTxt" id="deliveryNoteTxt" /><img class="clearImg" id="deliveryNoteNoEreaser"src= "/images/img_clear.gif" title="To Clear DeliveryNote No."/><img  
															id="deliveryNoteNoSearch"     
															style="background-color: #F8F8FF; border: none;"
															class="serachPopup deliveryNoteNoDMSSearchPopup "
															src="/images/search.png" title="To Search DeliveryNote No."/></span>   
							</td>    	   
							<td width="12%" height="25" align="right"><div>Pulling Order No.&nbsp;&nbsp;:</div></td>
							<td width="15%">   
							<span class="pullingOrderNoCss spnRqrdTrue">  
															<!-- readonly="readonly" --> <input type="text"
															class="customSPopup" 
															style="width: 99px; background-color: #F8F8FF; border: none"
															name="pullingOrderNoTxt" id="pullingOrderNoTxt" maxlength="256"/><img class="clearImg" id="pullingOrderNoEreaser" src= "/images/img_clear.gif" title="To Clear Pulling Order No."/><img
															id="pullingOrderNoSearch"
															style="background-color: #F8F8FF; border: none;"
															class="serachPopup pullingOrderNoDMSSearchPopup "
															src="/images/search.png" title="To Search Pulling Order No."/></span>
							</td>
							<td width="12%" height="25" align="right"></td>
							<td width="20%"></td> 
				</tr>
                <tr>
               <td colspan="8"><div class="bottomButtons"> 
                    <a href="#" id="searchBtnDMS" class="button blue bigrounded disableCss searchBtnDMS">Search</a>
                     <a href="#" id="clearBtnDMS" name="clearBtnDMS" class="button blue bigrounded disableCss clearBtnBCP">Clear</a> </div></td>
                </tr>
</table>
</div>
</div> 
<div style="margin:1px 0;"></div>  
	<div class="gridTbl" id="dmsSearchDivId">
					<!-- url: 'datagrid_Claim_ActivityDetails.json',view:bufferview, -->
					<table id="dmsSearchTbldg" title="Search Results" 
						class="easyui-datagrid" style="width: 940px; height: 368px"
						data-options="method: 'get',
						 fitColumns: false,
						 singleSelect: true, 
						 rownumbers: true,
						 multisort:true,
						 pagination:true,
						 pageSize:10">
						<thead> 
							<tr>
								
								<th data-options="field:'documentLine',width:100 ,align:'left'">Document Line</th>
								<th data-options="field:'documentType',width:120 ,align:'left'">Document Type</th>
								<th data-options="field:'bookingNo',width:120 ,align:'left'">Booking No</th>
								<th data-options="field:'jobNo' ,width:100,align:'left'">Job No</th>
								<th data-options="field:'siNo' ,width:100,align:'left'">SI No</th>
								<th data-options="field:'claimNo' ,width:100,align:'left'">Claim No</th>
								<th  data-options="field:'shipmentNo' ,width:150,align:'left'">Shipment No</th>
								<th  data-options="field:'invoiceNo' ,width:120,align:'left'">Invoice No</th>
								<th  data-options="field:'quotationNo' ,width:120,align:'left'">Quotation No</th>
								<th  data-options="field:'poNo' ,width:120,align:'left'">PO No</th>
								<th  data-options="field:'doNo' ,width:120,align:'left'">DO No</th>
								<th  data-options="field:'loadNo' ,width:100,align:'left'">Load No</th>
								<th  data-options="field:'bookingCustomer' ,width:120,align:'left'">Booking Customer</th>
								<th  data-options="field:'creditNoteNo' ,width:120,align:'left'">CreditNote No</th>
								<th  data-options="field:'debitNoteNo' ,width:120,align:'left'">DebitNote No</th>
								<th  data-options="field:'deliveryNoteNo' ,width:120,align:'left'">DeliveryNote No</th>
								<th  data-options="field:'pullingOrderNo' ,width:120,align:'left'">PullingOrder No</th>
								<th  data-options="field:'documentTitle' ,width:180,align:'left'">Document Title</th>
								<th  data-options="field:'uploadedBy' ,width:180,align:'left'">Upload By</th>
								<th  data-options="field:'uploadedDate',width:120,align:'left'">Upload Date</th>
								<th width="30" id="priviewIconId" class="priviewIconCss" data-options="field:'priview',width:10,align:'center'" formatter="priviewIconFormatter"></th>
								<th width="30" id="downLoadId" class="downloadIconCss" data-options="field:'download',width:10 ,align:'center'" formatter="downLoadIconFormatter"></th>
								<th width="30" id="deleteId" class="deleteIconCss" data-options="field:'deleteD',width:10 ,align:'center'" formatter="deleteIconFormatter"></th>
								<th width="01" id="dmsId" class="dmsId" data-options="field:'dmsId',width:0 ,align:'center'" hidden="true"></th>
								<th width="01" id="systemLine" class="systemLine" data-options="field:'systemLine',width:120,align:'left'"  hidden="true"></th>
							</tr> 
						</thead>  
					</table>   
				</div>      
    
 <form id="DMSSerchDocumentForm" name="DMSSerchDocumentForm" target="targetPreviewWindow">		
		<input type="hidden" name="TypeOfProcess" id="TypeOfProcess"/>
		<input type="hidden" name="DMS_DOCUMENT_TITLE" id="DMS_DOCUMENT_TITLE"/>
		<input type="hidden" name="DMS_DCUMENT_ID" id="DMS_DCUMENT_ID"/>
		<input type="hidden" name="DMS_DOCUMENT_LINE" id="DMS_DOCUMENT_LINE"/>
		<input type="hidden" name="DMS_DOCUMENT_TYPE" id="DMS_DOCUMENT_TYPE"/>
		<input type="hidden" name="DMS_BOOKING_NO" id="DMS_BOOKING_NO"/>
		<input type="hidden" name="DMS_JOB_NO" id="DMS_JOB_NO"/>
		<input type="hidden" name="DMS_SI_NO" id="DMS_SI_NO"/>
		<input type="hidden" name="DMS_CLAIM_NO" id="DMS_CLAIM_NO"/>
		<input type="hidden" name="DMS_SHIPMENT_NO" id="DMS_SHIPMENT_NO"/>
		<input type="hidden" name="DMS_QUOTATION_NO" id="DMS_QUOTATION_NO"/>
		<input type="hidden" name="DMS_INVOICE_NO" id="DMS_INVOICE_NO"/>
		<input type="hidden" name="DMS_PO_NO" id="DMS_PO_NO"/>
		<input type="hidden" name="DMS_DO_NO" id="DMS_DO_NO"/>
		<input type="hidden" name="DMS_LOAD_NO" id="DMS_LOAD_NO"/>
		<input type="hidden" name="DMS_BOOKING_CUSTOMER" id="DMS_BOOKING_CUSTOMER"/>
		<input type="hidden" name="DMS_UPLOAD_FORM_DATE" id="DMS_UPLOAD_FORM_DATE"/>
		<input type="hidden" name="DMS_UPLOAD_TO_DATE" id="DMS_UPLOAD_TO_DATE"/>
		<input type="hidden" name="SYSTEM_LINE" id="SYSTEM_LINE"/>
		
 </form>
	 
    
<%-- <form id="viewDocumentFormDMS" name="viewDocumentFormDMS"  method="post" action="<%=renderResponse.encodeURL(renderRequest.getContextPath()+"/_OFSClaimWeb/jsp/html/PicturePreviewPopUp.jsp")%>" target="targetPreviewWindow">		
		<input type="hidden" name="dmsDocID" id="dmsDocID"/>
		<input type="hidden" name="userRole" id="userRole"/>
		<input type="hidden" name="processType" id="processType"/>
 </form>  --%>
  <!-- -------------------hidden Form field for printing PDF values------------------------- -->
     
    
 <%@ include file="DMSBookingSearchPopup.jspf"%>     
 <%@ include file="DMSJobSearchPopup.jspf"%>   
 <%@ include file="DMSSiSearchPopup.jspf"%> 
 <%@ include file="DMSClaimSearchPopup.jspf"%>   
 <%@ include file="DMSShipmentSearchPopup.jspf"%>     
 <%@ include file="DMSInvoiceSearchPopup.jspf"%>   
 <%@ include file="DMSQuotationSearchPopup.jspf"%>  
 <%@ include file="DMSPoSearchPopup.jspf"%>
 <%@ include file="DMSDoSearchPopup.jspf"%> 
 <%@ include file="DMSLoadNoSearchPopup.jspf"%>
 <%@ include file="DMSBookingCustomerSearchPopup.jspf"%>
 <%@ include file="DMSDebitNoteSearchPopup.jspf"%>
 <%@ include file="DMSCreditNoteSearchPopup.jspf"%>
 <%@ include file="DMSDeliveryNoteSearchPopup.jspf"%>
 <%@ include file="DMSPullingOrderSearchPopup.jspf"%>
 
 
 
 
   
 	    
<script type="text/javascript">
	//data-options="formatter:myformatter,parser:myparser"
	//System Line, Document Line, Document Type, Booking No, JobNo, SI No,Claim No, Shipment No, Invoice No, Quotation No, PO No, DO No, Load No, Booking Customer, Upload Date From , Upload Date To 
console.log(">>>>> script running start ...... ");  
console.log("reading js.......................")
var loginModuelType="";
var docuementLineSelect="";
var deleteFlag=false;
var onclickChekFlag=true;
//CONSTANTS FOR SYSTEM LINE AND DOCUMENT LINE
var  DOC_LINE_TYPE_SYSTEM_OUTPUT="SYSTEM OUTPUT";
var  DOC_LINE_TYPE_OTHERS="OTHERS";
var  DOC_LINE_TYPE_CLAIMS="CLAIMS";
var dmsApprover="";	  


var PROCESS_TYPE_SEARCH="SEARCH_DMS_DETAILS";
var PROCESS_TYPE_DELETE="DELET_DMS_DETAILS";
var PROCESS_TYPE_PRIVIEW="PRIVIEW_DMS_DETAILS";
var PROCESS_TYPE_DOWNLOAD="DOWNLOAD_DMS_DETAILS";
//parameter constants names
 
//common-- param constant
	var P_BK_CUSTOMER= "P_BK_CUSTOMER";
	var P_BK_NUMBER= "P_BK_NUMBER";
	var P_POD= "P_POD";
	var P_VOYAGENO= "P_VOYAGENO";
	var P_VESSELNAME= "P_VESSELNAME";
	var P_ETA_DATE= "P_ETA_DATE";
	var P_PICKUPLOCATION= "P_PICKUPLOCATION"; 
	var P_SHIPMENT_NO= "P_SHIPMENT_NO";
	var P_BILLING_CUSTOMER= "P_BILLING_CUSTOMER";
	
//booking-- param constant 
	var P_BK_DATE_FROM= "P_BK_DATE_FROM";
	var P_BK_DATE_TO= "P_BK_DATE_TO";
	var P_BUSINESSLINE= "P_BUSINESSLINE";
 //job-- param constant	
	var P_JOB_DATE_FROM= "P_JOB_DATE_FROM";
	var P_JOB_DATE_TO= "P_JOB_DATE_TO";

//si-- param constant		
	var P_SI_DATE_FROM= "P_SI_DATE_FROM";
	var P_SI_DATE_TO= "P_SI_DATE_TO";

//claim-- param constant			
	var P_CLAIM_DATE_FROM= "P_CLAIM_DATE_FROM";
	var P_CLAIM_DATE_TO= "P_CLAIM_DATE_TO";
	var P_INCIDENT_NO= "P_INCIDENT_NO";
//shipment-- param constant			
	var P_SHIPMNT_DATE_FROM= "P_SHIPMNT_DATE_FROM";
	var P_SHIPMNT_DATE_TO= "P_SHIPMNT_DATE_TO";
	
//invoice-- param constant	
	var P_INVOICE_NO= "P_INVOICE_NO";	
	var P_INVOICE_DATE_FROM= "P_INVOICE_DATE_FROM";
	var P_INVOICE_DATE_TO= "P_INVOICE_DATE_TO";

//quot-- param constant			
	var P_QUOT_DATE_FROM= "P_QUOT_DATE_FROM";
	var P_QUOT_DATE_TO= "P_QUOT_DATE_TO";
	
//po-- param constant		
	var P_PO_DATE_FROM= "P_PO_DATE_FROM";
	var P_PO_DATE_TO= "P_PO_DATE_TO"; 
 
//load-- param constant			
	var P_VENDOR_NAME= "P_VENDOR_NAME";
	var P_BILL_CUSTOMER= "P_BILL_CUSTOMER";   
	var P_PICKUP_DATE= "P_PICKUP_DATE";
	var P_LOAD_NO= "P_LOAD_NO"; 
  
//do-- param constant			
	var P_DELIVERY_DATE= "P_DELIVERY_DATE";
	var P_DO_DATE_FROM= "P_DO_DATE_FROM";
	var P_DO_DATE_TO= "P_DO_DATE_TO"; 
	var P_DELIVERY_LOC= "P_DELIVERY_LOC";
	var P_TRUCK_TYPE= "P_TRUCK_TYPE";

//customer-- param constant			
	var P_CUSTOMER_NAME= "P_CUSTOMER_NAME";
	var P_CUSTOMER_CODE= "P_CUSTOMER_CODE";
	
// credit Note 
 //P_CN_NO,P_SHIPMENT_NO,P_BILLING_CUSTOMER,P_CN_FROM_DATE,P_CN_TO_DATE	
    var P_CN_NO= "P_CN_NO";
	var P_CN_FROM_DATE= "P_CN_FROM_DATE";
	var P_CN_TO_DATE= "P_CN_TO_DATE";
	
// debit Note 
 ////P_DN_NO,P_DN_FROM_DATE,P_DN_TO_DATE
    var P_DN_NO= "P_DN_NO";
	var P_DN_FROM_DATE= "P_DN_FROM_DATE";
	var P_DN_TO_DATE= "P_DN_TO_DATE";
	

	
//DMS-EventTypes
var DMS_SEARCH_DCUMENTS= '<%=com.giga.dms.helper.DMSHelper.DMS_SEARCH_DCUMENTS%>';
var DMS_VIEW_DOCUMENTS= '<%=com.giga.dms.helper.DMSHelper.DMS_VIEW_DOCUMENTS%>';
var DMS_DOWNLOAD_DOCUMENTS= '<%=com.giga.dms.helper.DMSHelper.DMS_DOWNLOAD_DOCUMENTS%>';
var DMS_DELETE_DOCUMENTS= '<%=com.giga.dms.helper.DMSHelper.DMS_DELETE_DOCUMENTS%>';

//DMS-Parameters-by-events
var SYSTEM_LINE= "SYSTEM_LINE";
var DMS_DOCUMENT_TITLE= "DMS_DOCUMENT_TITLE";
var DMS_DCUMENT_ID= "DMS_DCUMENT_ID";
var DMS_DOCUMENT_LINE= "DMS_DOCUMENT_LINE";
var DMS_DOCUMENT_TYPE= "DMS_DOCUMENT_TYPE";
var DMS_BOOKING_NO= "DMS_BOOKING_NO";
var DMS_JOB_NO= "DMS_JOB_NO";
var DMS_SI_NO= "DMS_SI_NO";
var DMS_CLAIM_NO= "DMS_CLAIM_NO";
var DMS_SHIPMENT_NO= "DMS_SHIPMENT_NO";
var DMS_INVOICE_NO= "DMS_INVOICE_NO";
var DMS_QUOTATION_NO= "DMS_QUOTATION_NO";
var DMS_PO_NO= "DMS_PO_NO";
var DMS_DO_NO= "DMS_DO_NO";
var DMS_LOAD_NO= "DMS_LOAD_NO";
var DMS_BOOKING_CUSTOMER= "DMS_BOOKING_CUSTOMER";
var DMS_CN_NO= "DMS_CN_NO";
var DMS_DN_NO= "DMS_DN_NO";
var DMS_UPLOAD_FORM_DATE= "DMS_UPLOAD_FORM_DATE";
var DMS_UPLOAD_TO_DATE= "DMS_UPLOAD_TO_DATE";

//Exceptions...
var SEARCH_DOCUMENT_EXCEPTION= "SEARCH_DOCUMENT_EXCEPTION"; 
var VIEW_DOCUMENT_EXCEPTION= "VIEW_DOCUMENT_EXCEPTION"; 
var DOWNLOAD_DOCUMENT_EXCEPTION= "DOWNLOAD_DOCUMENT_EXCEPTION"; 
var NO_DOCUMENT_FOUND_ERROR= "NO_DOCUMENT_FOUND_ERROR"; 
var DELETE_DOCUMENT_EXCEPTION= "DELETE_DOCUMENT_EXCEPTION"; 




var PRIVIEW_CELL="priview";
var DOWNLOAD_CELL="download";
var DELETE_CELL="deleteD";
 
var priviewImage =  "/images/icon_print.png";
function priviewIconFormatter(val,row){
	if(true){
	    if (val == PRIVIEW_CELL){
	        return '<img border="1" src='+priviewImage+' id="imazee" width="17" height="17"  class="priviewRow">';
	    } else {
	         return '<img border="1" src='+priviewImage+' id="imazee" width="17" height="17" class="priviewRow">';
     }
	}
}


var downloadImage =  "/images/download.png";
function downLoadIconFormatter(val,row){
	if(true){
	    if (val == DOWNLOAD_CELL){
	        return '<img border="1" src='+downloadImage+' id="imazee" width="17" height="17"  class="downloadRow">';
	    } else {
	         return '<img border="1" src='+downloadImage+' id="imazee" width="17" height="17" class="downloadRow">';
     }
	}
}
 
var deleteImage =  "/images/images.jpg"; 
function deleteIconFormatter(val,row){
	if(visibilityCheckDelIcon()){
	    if (val == DELETE_CELL){
	        return '<img border="1" src='+deleteImage+' id="imazee" width="17" height="17"  class="deleteRowD">';
	    } else {
	         return '<img border="1" src='+deleteImage+' id="imazee" width="17" height="17" class="deleteRowD">';
     }
	}
}


//*********************************************************Document Line Select Ajax Call  ****************************************************************//

$('#documentLineOpt').combobox({
	onSelect: function(row){
	      console.log("You are selected.....documentLineOpt ."); 
		  setDefaultValueToDocumentType(); 
		  var opts = $(this).combobox('options');
		  var selectDocLineText = row[opts.textField];  
		  var selectDocLineVal = row[opts.valueField]; 
		  
		  docuementLineSelect=selectDocLineText;// set the selected text value to var.. for claim seach validate
		  
		//  console.log("selected......DocumetnLine....Text :"+selectDocLineText+"---selectDocLineVal : "+selectDocLineVal);
		    if(selectDocLineText== '--Select a value--'){
			      console.log("in-valid selection.. on DocumetnLine..!!");
			   return;    
			}
			var urlParamDoc={DOC_LINE_TEXT : selectDocLineText, DOC_LINE_VAL : selectDocLineVal};
			
	    		$.ajax({ 
                    beforeSend: function () {  
				   },  
                    type : "POST",                    
                    async : true,
                    url : '<portlet:resourceURL id="LOAD_DOC_TYPE" />',
                    data: urlParamDoc, 
                    datatype : "json",                  
                   success: function(results)
                   {
				     console.log(results);  
					 validateSession(results);     
                	   if(results.length==0){
                		 //  console.log("No results return : length is : "+results.length);
                		
                	   }else{
                		   console.log("result return : length is : "+results.length);
						  // setDefaultValueToDocumentType();
						  $('#documentTypeOpt').combobox({
                				valueField: 'key',
								textField: 'value',
								data:  results});   
							$('#documentTypeOpt').combobox("setText","--Select a value--");
							$('#documentTypeOpt').combobox("setValue","");
						}
                   }  
                });  
	} 
});
 

 function setDefaultValueToDocumentType(){
var jsonDocType  = '[{"key":"","value":"--Select a value--"}]';
var docTypeJson = JSON.parse(jsonDocType);
console.log("docTypeJson :: "+docTypeJson);
	    $('#documentTypeOpt').combobox({
                		valueField: 'key',
						textField: 'value',
						data:  docTypeJson 
		});  
}

$(document).ready(function() {
console.log("document Ready...............loading ");
		 // Login USer Module CODE NAME..
				loginModuelType='<c:out value="${SYSTEM_LINE}"/>';
				console.log("loginModuelType : "+loginModuelType);
				if(loginModuelType!='YMS' &&  loginModuelType!='' && loginModuelType!= null){
					$('#showsYMS').css("display",'none');
				}
				 dmsApprover='<c:out value="${DMS_APPROVER}"/>';  
				 if(dmsApprover=='TRUE'){ 
					 deleteFlag=true;
				 }else{
					 deleteFlag=false;
				 }
				console.log("dmsApprover :: "+dmsApprover +"---deleteFlag : "+deleteFlag); 
});
 /*********************************************** window loading operations Start ***************************************************/
 $( window ).load(function() { 
    console.log("window.load.........  loading START"); 
		//comobo-box make as read only.....
		$('.combo .combo-text').attr('readonly', 'readonly'); 
		$('[name="dmsId"],[name="documentTitle"]').css('visibility','hidden');
		
		//load today date to date fields 
		console.log("load Today Date : "+loadTodayDate()); 
		$('.loadFromDate').datebox('setValue', pastOneMonth()); 
		$('.LoadTodaysDate').datebox('setValue', loadTodayDate()); 
		
		// enabled the form grid filters
		enablingGridAndFilter('dmsSearchTbldg'); 
		 		
		//validate the login user and make ready form page visibility to user  
		 var profileAccessError='<c:out value="${PROFILE_ACESS_FAILED}"/>';
		 var dataLoadingFalied='<c:out value="${DATA_LOADING_FAILED}"/>'; 
		   
		 //login user role
		 var userRole='<c:out value="${USER_ROLE}"/>';
		 //login user validate for delete the documents.
		
		 console.log("profileAccessError : "+profileAccessError+"-----loginModuelType : "+loginModuelType + "---- userRole :"+userRole +"--- dmsApprover : "+dmsApprover+"\n dataLoadingFalied : "+dataLoadingFalied);   
			 
			if(profileAccessError!=null && profileAccessError != "" ){  
			    var profileErrorValue='<%=com.giga.dms.helper.DMSHelper.PROFILE_ACESS_FAILED%>';
			    console.log("profileErrorValue : "+profileErrorValue);
				if(profileAccessError==profileErrorValue){
					diabledFormByOnload('DISABLED_ALL');    
				}  
			}      
			else{ 
			    
			    console.log("enabled..................");
				diabledFormByOnload('ENABLED_ALL');
				disableFieldsBySystemLine(loginModuelType);
   				   if(dataLoadingFalied!=null&& dataLoadingFalied!=""){
					showErorsByIdS('DataLoadingFalid');
					console.log("Master data loading failed due to DB Connection problem..");
				   }else{
					clearMessage();
				   }
			}
			
console.log("window.load.........  loading END"); 			 
});
/*********************************************** window loading operations END***************************************************/

		 
	/***
	* This function is used to disabled and enabled the all form field based on onloading Response.. 
	* if Exception at time onload : DISABLED_ALL 
	* if No-Exception at time onload : ENABLED_ALL 
	*/
	function diabledFormByOnload(typeDisable){
	  console.log("diabledFormByOnload process.. : " + typeDisable);
	  if(typeDisable=='DISABLED_ALL'){
	  // $("href").attr('disabled','disabled');
	        showErorsByIdS('InvalidUser');
		    $("input").attr('disabled','disabled');  
		    $("select").attr('disabled','disabled'); 
			$("img").attr('disabled','disabled'); 
			$('.combo-arrow').unbind('click'); 
			$('.disableCss').unbind("click"); 
			$('.disableCss').attr('disabled','disabled'); 
		}else if(typeDisable=='ENABLED_ALL'){
		// $("href").attr('enabled','enabled');
			clearMessage();
			$("input").attr('enabled','enabled');
			$("select").attr('enabled','enabled');
			$("img").attr('enabled','enabled');
			$('.combo-arrow href').bind('click');
			$('.disableCss').bind("click");
			$('.disableCss').attr('enabled','enabled'); 
		}
	}//END- diabledFormByOnload
	
/****
* This function is used to disabled form fields property based on the 
* Module(OFS,TMS,YMS) user login and validate to perform operation
****/
	function disableFieldsBySystemLine(systemLine){
	    console.log("disableFieldsBySystemLine :: process...START :: systemLine : "+systemLine);
		  switch(systemLine){
		   case "OFS" :
						$(".enabledByOFS").attr('enabled','enabled'); 
						break;
		   case "TMS" :
						$(".enabledByOFS").attr('enabled','enabled'); 
						//$('.siNoDMSSearchPopup').unbind("click");
						break;
		   case "YMS" :
						
						break;
		  }
		 console.log("disableFieldsBySystemLine :: process...END");  
	}

	
/**   **************************************************   SEARCH DMS ********************************************************************************
* This function is used to Search the DMS Content based on the parameter passing values(booking no, si no, job no. etc..)
*/	

$('.searchBtnDMS').on('click', function(e){
//systemLineOpt,documentLineOpt,documentTypeOpt,bookingNoTxt,jobNoTxt,siNoTxt,claimNoTxt,invoiceNoTxt,quotationNoTxt,poNoTxt,doNoTxt,loadNoTxt,bookingCustTxt,uploadDateFromTxt,uploadDateToTxt
     var systemLine=$('#systemLineOpt').val();
	 var documentLineVal=$('#documentLineOpt').combobox('getValue');
	 var documentLineTxt=$('#documentLineOpt').combobox('getText');
	      documentLineTxt= documentLineTxt =='--Select a value--'? "" : documentLineTxt;
	  var documentTypeVal=$('#documentTypeOpt').combobox('getValue');
	 var documentTypeTxt=$('#documentTypeOpt').combobox('getText');
	     documentTypeTxt= documentTypeTxt=='--Select a value--'? "":documentTypeTxt;
	 var bookingNo=$('#bookingNoTxt').val();
	 var jobNo=$('#jobNoTxt').val();
	 var siNo=$('#siNoTxt').val();
	 var claimNo=$('#claimNoTxt').val();
	 var shipmentNo=$('#shipmentNoTxt').val();//
	 var invoiceNo=$('#invoiceNoTxt').val();
	 var quotationNo=$('#quotationNoTxt').val();
	 var poNo=$('#poNoTxt').val();
	 var doNo=$('#doNoTxt').val();
	 var loadNo=$('#loadNoTxt').val();
	 var bookingCustomer=$('#bookingCustTxt').val();	 
	 var creditNote=$('#creditNoteTxt').val();
	 var debitNote=$('#debitNoteTxt').val();
	 
	 var uploadDateFrom=$('#uploadDateFromTxt').datebox('getValue');
	 var uploadDateTo=$('#uploadDateToTxt').datebox('getValue');
	
	//console.log(":: search DMS : [(systemLine :"+systemLine+"),(documentLineVal:"+documentLineVal+"),(documentLineTxt:"+documentLineTxt+"),(documentTypeVal:"+documentTypeVal+"),(documentTypeTxt:"+documentTypeTxt+"),(bookingNo:"+bookingNo+"),(jobNo:"+jobNo+"),(siNo:"+siNo+"),(claimNo:"+claimNo+"),(shpmentNo:"+shipmentNo+"),(invoiceNo:"+invoiceNo+"),(quotationNo:"+quotationNo+"),(poNo:"+poNo+"),(doNo:"+doNo+"),(loadNo:"+loadNo+"),(bookingCustomer:"+bookingCustomer+"),(creditNote:"+creditNote+"),(debitNote:"+debitNote+"),(uploadDateFrom:"+uploadDateFrom+"),(uploadDateTo:"+uploadDateTo+")]");
	
    var urlParam={ 
					DMS_DOCUMENT_LINE : documentLineTxt, 
					DMS_DOCUMENT_TYPE: documentTypeTxt,
					DMS_BOOKING_NO: bookingNo,
					DMS_JOB_NO: jobNo,
					DMS_SI_NO: siNo,
					DMS_CLAIM_NO: claimNo,
					DMS_SHIPMENT_NO : shipmentNo,
					DMS_QUOTATION_NO: quotationNo,
					DMS_INVOICE_NO: invoiceNo,
					DMS_PO_NO: poNo,
					DMS_DO_NO: doNo,
					DMS_LOAD_NO: loadNo,
					DMS_BOOKING_CUSTOMER: bookingCustomer,
					DMS_CN_NO: creditNote,
					DMS_DN_NO: debitNote,
					DMS_UPLOAD_FORM_DATE: uploadDateFrom,
					DMS_UPLOAD_TO_DATE: uploadDateTo,
					};
	   if(validateSearching(urlParam)){
				 if(!validateButtonClickByResult(false,'searchBtnDMS')){ console.log(" Sorry.. Process is going on..");return;};
					$.ajax({
							beforeSend: function () { },    
							type : "POST",                    
							async : true,
							url : '<portlet:resourceURL id="DMS_SEARCH_DCUMENTS" />',
							data: urlParam, 
							datatype : "json",                    
							success: function(results)   
							{
							//  console.log(" result  : is : "+results); 
							  validateSession(results);							  
							  validateButtonClickByResult(true,'searchBtnDMS');
							  clearMessage();
							  errorMarkHideFormField();	 						  
							  if(results!=null){
								validateSession(results);     
								   
								if(results.indexOf("Exception") > -1){
								//	console.log("Search EXCPETION  :  "+results);
									showExceptionMessege(results);
								}else		
								if(results.length==0){
									showExceptionMessege(results);
									//console.log("No result return : length is : "+results.length);
								}else{ 
									showResultsMessages('successMessages','successMsg','SearchSuccess'); 
								//    console.log("result return : length is : "+results.length+" results : "+results);
							//		console.log("StringFy Results : "+JSON.stringify(results));
									reloadResultDataOnGrid('dmsSearchTbldg',results);
								}
										
					 }//NOT NULL
				   }
				});
			}
						
});		
function validateSearching(urlParamArray){
   var validateFlag=true;
   var dmsDocLine=urlParamArray.DMS_DOCUMENT_LINE;
   var uploadFromDate=urlParamArray.DMS_UPLOAD_FORM_DATE;
   var uploadToDate=urlParamArray.DMS_UPLOAD_TO_DATE;
   console.log("dmsDocLine :: "+dmsDocLine+"-- uploadFromDate :"+uploadFromDate+"-- uploadToDate : "+uploadToDate);
   if(dmsDocLine == '-- Select a value --'||dmsDocLine == '' ){
   //validateFlag=false;
   validateFlag=searchValidateErrorShow();
   	   //$.messager.alert('Message',"Document Line is Mandatory ",'info');
   }
   return validateFlag;
}
//****************************************************** privew ---  download  --- delete START ******************************************************************//
//****************************************************************************************************************************************************************//
/**
* This function is used to perform the download , view and delete operation based on click the corresponding icon.
*/
//PRIVIEW_CELL,DOWNLOAD_CELL,DELETE_CELL
$('#dmsSearchTbldg').datagrid({
	 onClickCell : function(rowIndex, field, value) {
		 if(validateDelIcon(field)){
			 console.log("validate Author is true");
			 return;
		};
	//	console.log( "on celle Click :: rowIndex : "+rowIndex+"-- :: field : "+field+"::--value : "+value);
		var paramArray={}
		var currentRows = $('#dmsSearchTbldg').datagrid('getRows');    // get current page rows
		var currentRow = currentRows[rowIndex];
		//////var currentRow = $ ("#dmsSearchTbldg").datagrid("getSelected");
		if(currentRow!=null && currentRow!=''){
			paramArray=getListParam(currentRow);
			 switch(field){
				case PRIVIEW_CELL :
									priviewDocument(this,paramArray);
								 break;
				case DOWNLOAD_CELL :
									downloadDocument(this,paramArray);
								 break;
				case DELETE_CELL :
									deleteDocument(this,paramArray);
								 break;					 
			 }
		}
	  }
});
  
	/** ***************** This function is used to perform the print privew  of documents ********* ***/
  function priviewDocument(thisId,paramArray){
    		console.log("priviewDocument : paramArray : "+paramArray);
			priviewAndDownload(DMS_VIEW_DOCUMENTS,paramArray);
  }
  /** *****************This function is used to perform the download documents***************** ***/
  function downloadDocument(thisId,paramArray){
  		console.log("downloadDocument : paramArray : "+paramArray);
		priviewAndDownload(DMS_DOWNLOAD_DOCUMENTS,paramArray);
  }
  /** *****************This function is used to perform the delete the documents***************** ***/
  function deleteDocument(thisId,paramArray){
  		console.log(" deleteDocument : paramArray : "+paramArray);
  		    $.messager.confirm('Confirmation','The document will be permanently removed from DMS. Are you sure to continue ?',function(deleteFile){
	           if(deleteFile == true){        
					$.ajax({
							beforeSend: function () { },
							type : "POST",                    
							async : true,
							url : '<portlet:resourceURL id="DMS_DELETE_DOCUMENTS" />',
							data: paramArray,
							datatype : "json",                  
							success: function(results)
							{
							 console.log("delete : results : "+results);
							 validateSession(results);		
							 if(results.indexOf("Exception") > -1){
							  console.log("Search EXCPETION  :  "+results);
							  showExceptionMessege(results);
							}else	
								   if(results.length==0){
									   console.log("No result return : length is : "+results.length);
									   showExceptionMessege(results);
								   }else{
								         $.messager.alert('Message',"Document deleted successfully",'info');
									    // console.log("result return : length is : "+results.length+" results : "+results);
									    // console.log("StringFy Results : "+JSON.stringify(results));
										reloadResultDataOnGrid('dmsSearchTbldg',results);
									}
									
							   }
							});
	           }
	    	 }); 
  }
//************************************************* PRIVIEW_AND_DOWNLOAD DOC AJAX CALL SUBMIT FORM ***************************************************/
function priviewAndDownload(ProcessType,paramArray){
		console.log("priviewAndDownload :..process ");
        console.log("DMS_DOCUMENT_TITLE : "+paramArray.DMS_DOCUMENT_TITLE);
		 
		 document.DMSSerchDocumentForm.TypeOfProcess.value=PROCESS_TYPE_SEARCH;
		 document.DMSSerchDocumentForm.SYSTEM_LINE.value=paramArray.DMS_SYSTEM_LINE;
		 document.DMSSerchDocumentForm.DMS_DOCUMENT_TITLE.value=paramArray.DMS_DOCUMENT_TITLE;
		 document.DMSSerchDocumentForm.DMS_DCUMENT_ID.value=paramArray.DMS_DCUMENT_ID;
		 document.DMSSerchDocumentForm.DMS_DOCUMENT_LINE.value=paramArray.DMS_DOCUMENT_LINE;
		 document.DMSSerchDocumentForm.DMS_DOCUMENT_TYPE.value=paramArray.DMS_DOCUMENT_TYPE;
		 document.DMSSerchDocumentForm.DMS_BOOKING_NO.value=paramArray.DMS_BOOKING_NO;
		 document.DMSSerchDocumentForm.DMS_JOB_NO.value=paramArray.DMS_JOB_NO;
		 document.DMSSerchDocumentForm.DMS_SI_NO.value=paramArray.DMS_SI_NO;
		 document.DMSSerchDocumentForm.DMS_CLAIM_NO.value=paramArray.DMS_CLAIM_NO;
		 document.DMSSerchDocumentForm.DMS_SHIPMENT_NO.value=paramArray.DMS_SHIPMENT_NO;
		 document.DMSSerchDocumentForm.DMS_QUOTATION_NO.value=paramArray.DMS_QUOTATION_NO;
		 document.DMSSerchDocumentForm.DMS_INVOICE_NO.value=paramArray.DMS_INVOICE_NO;
		 document.DMSSerchDocumentForm.DMS_PO_NO.value=paramArray.DMS_PO_NO;
		 document.DMSSerchDocumentForm.DMS_DO_NO.value=paramArray.DMS_DO_NO;
		 document.DMSSerchDocumentForm.DMS_LOAD_NO.value=paramArray.DMS_LOAD_NO;
		 document.DMSSerchDocumentForm.DMS_BOOKING_CUSTOMER.value=paramArray.DMS_BOOKING_CUSTOMER;
		 document.DMSSerchDocumentForm.DMS_UPLOAD_FORM_DATE.value=paramArray.DMS_UPLOAD_FORM_DATE;
		 //document.DMSSerchDocumentForm.DMS_UPLOAD_TO_DATE.value=paramArray.DMS_UPLOAD_TO_DATE;
		// console.log("beforProcessing.....");
		 if(ProcessType==DMS_VIEW_DOCUMENTS){
			   document.DMSSerchDocumentForm.action = '<%=renderResponse.encodeURL(renderRequest.getContextPath()+"/_DMSWeb/jsp/html/DMSSearchPreviewPopUp.jsp")%>';
			   window.open('', 'targetPreviewWindow');
			   document.DMSSerchDocumentForm.submit();
			 }else
			 if(ProcessType==DMS_DOWNLOAD_DOCUMENTS){
				document.DMSSerchDocumentForm.action = "<portlet:resourceURL id="DMS_DOWNLOAD_DOCUMENTS"></portlet:resourceURL>";
				document.DMSSerchDocumentForm.submit();   
			 }
		
		//console.log("afterProcessing.....");
	};

//************************************************* getParam List  for all ajax call ***************************************************/
function getListParam(currentRow){
  console.log("priviewDocument : currentRow : "+currentRow);
			 //systemLine,documentLine,documentType,bookingNo,jobNo,siNo,claimNo,shipmentNo,invoiceNo,quotationNo,poNo,doNo,loadNo,bookingCustomer,uploadFromDate,dmsId;
			 
			 var systemLine_DMS=currentRow.systemLine;
			 var documentLine_DMS=currentRow.documentLine;
		     var documentType_DMS=currentRow.documentType;
			 var bookingNo_DMS=currentRow.bookingNo;
			 var jobNo_DMS=currentRow.jobNo;
			 var siNo_DMS=currentRow.siNo;
			 var claimNo_DMS=currentRow.claimNo;
			 var shipmentNo_DMS=currentRow.shipmentNo;
			 var invoiceNo_DMS=currentRow.invoiceNo;
			 var quotationNo_DMS=currentRow.quotationNo;
			 var poNo_DMS=currentRow.poNo;
			 var doNo_DMS=currentRow.doNo;
			 var loadNo_DMS=currentRow.loadNo;
			 var bookingCustomer_DMS=currentRow.bookingCustomer;
			 var uploadDateFrom_DMS=currentRow.uploadFromDate;
			 var dmsId_DMS=currentRow.dmsId; 
			 var docmentTitle_DMS=currentRow.documentTitle;
			 console.log("PrintPrivier...:: systemLine_DMS : "+systemLine_DMS+"\n  :: documentLine_DMS :"+documentLine_DMS
			 +"\n  :: documentType_DMS :"+documentType_DMS
			 +"\n  :: documentLine_DMS :"+documentLine_DMS
			 +"\n  :: bookingNo_DMS :"+bookingNo_DMS
			 +"\n  :: jobNo_DMS :"+jobNo_DMS
			 +"\n  :: siNo_DMS :"+siNo_DMS
			 +"\n  :: claimNo_DMS :"+claimNo_DMS
			 +"\n  :: invoiceNo_DMS :"+invoiceNo_DMS
			 +"\n  :: quotationNo_DMS :"+quotationNo_DMS
			 +"\n  :: poNo_DMS :"+poNo_DMS
			 +"\n  :: doNo_DMS :"+doNo_DMS
			 +"\n  :: loadNo_DMS :"+loadNo_DMS
			 +"\n  :: bookingCustomer_DMS :"+bookingCustomer_DMS
			 +"\n  :: uploadDateFrom_DMS :"+uploadDateFrom_DMS
			 +"\n  :: dmsId_DMS :"+dmsId_DMS);
		 var listParam ={
				 	DMS_SYSTEM_LINE : systemLine_DMS,
		            DMS_DOCUMENT_TITLE : docmentTitle_DMS,
			        DMS_DOCUMENT_LINE : documentLine_DMS, 
					DMS_DOCUMENT_TYPE: documentType_DMS,
					DMS_BOOKING_NO: bookingNo_DMS,
					DMS_JOB_NO: jobNo_DMS,
					DMS_SI_NO: siNo_DMS,
					DMS_CLAIM_NO: claimNo_DMS,
					DMS_SHIPMENT_NO : shipmentNo_DMS,
					DMS_QUOTATION_NO: invoiceNo_DMS,
					DMS_INVOICE_NO: quotationNo_DMS,
					DMS_PO_NO: poNo_DMS,
					DMS_DO_NO: doNo_DMS,
					DMS_LOAD_NO: loadNo_DMS,
					DMS_BOOKING_CUSTOMER: bookingCustomer_DMS,		
					DMS_UPLOAD_FORM_DATE: uploadDateFrom_DMS,
					DMS_DCUMENT_ID: dmsId_DMS
				}
		return listParam;		
  }

//****************************************************************************************************************************************************************//
//****************************************************** privew ---  download  --- delete  END *******************************************************************//

//******************************************************* CLEAR CONTENT OPERATIONS *******************************************************************//
$('#clearBtnDMS').on('click', function(e){

   console.log("DMSSearchPortletView JSP : Clearing the Search Document fields START...");
   //systemLineOpt,documentLineOpt,documentTypeOpt,bookingNoTxt,
	//jobNoTxt,siNoTxt,claimNoTxt,shipmentNoTxt,invoiceNoTxt,quotationNoTxt,poNoTxt,doNoTxt,loadNoTxt,bookingCustTxt
	clearMessage();
	errorMarkHideFormField();
	$('#systemLineOpt').combobox('setValue','');
	$('#systemLineOpt').combobox('setText','--Select a value--');
	$('#documentLineOpt').combobox('setValue','');
	$('#documentLineOpt').combobox('setText','--Select a value--');
	$('#documentTypeOpt').combobox('setValue','');
	$('#documentTypeOpt').combobox('setText','--Select a value--');
	$('#bookingNoTxt').val('');
	$('#jobNoTxt').val('');
	$('#siNoTxt').val('');
	$('#claimNoTxt').val('');
	$('#shipmentNoTxt').val('');
	$('#invoiceNoTxt').val('');
	$('#quotationNoTxt').val('');  
	$('#poNoTxt').val('');
	$('#doNoTxt').val('');
	$('#loadNoTxt').val('');
	$('#bookingCustTxt').val(''); 
	$('#debitNoteTxt').val(''); 
	$('#creditNoteTxt').val(''); 
	$('#uploadDateFromTxt').datebox('setValue',loadTodayDate());
	$('#uploadDateToTxt').datebox('setValue',loadTodayDate());
	
	// booking customer  field reset
	$('.bkCustomerCssDMS').css('enabled','enabled');
	$('#bookingCustTxt').width(99); 
	$('.customerSarchIconDMS').css('display','inline');
	$('#bookingCustTxt').val('');
	resetCssByModuleType(loginModuelType);
	enablingGridAndFilter('dmsSearchTbldg');
	console.log("DMSSearchPortletView JSP :: Clearing fields END..");
	
 });
 
 function resetCssByModuleType(moduleType){
 // for system line
 $('.systemLineLbl').css('display','none');
 $('.systemLineCss').css('display','none');
 }
  
function showExceptionMessege(results)
 {
 enablingGridAndFilter('dmsSearchTbldg');
 clearMessage();
	  var messageType="";
      console.log("showExceptionMessege : Results ::: "+results); 
      var errorMessage = results.substring(10);
	  if(errorMessage == SEARCH_DOCUMENT_EXCEPTION){
		//messageType="Exception in processing search documents ";
		messageType="SearchFailed";
	  }else
	  if(errorMessage == VIEW_DOCUMENT_EXCEPTION){
		//messageType="Exception in processing view documents";
		messageType="ViewFailed";
	  }else
	  if(errorMessage == DOWNLOAD_DOCUMENT_EXCEPTION){ 
		//messageType="Exception in processing download documents";
		messageType="DownloadFailed";
	  }else
	  if(errorMessage == DELETE_DOCUMENT_EXCEPTION){
		//messageType="Exception in processing delete documents";
		messageType="DeleteFailed";
	  }else
	  if(errorMessage == NO_DOCUMENT_FOUND_ERROR){
		//messageType="No result return, please change the search criteria";
		messageType="NoReusultReturn";
	  }
	   showResultsMessages('errorMsg','errorConent',messageType); 
      // $.messager.alert('Message',messageType,'info');
 }
 
$('.clearImg').on('click', function(){
     var clickEreaserID=this.id;
	console.log("Clear Field Brush clicked...... \ln Clicked ID : "+clickEreaserID); 
	//console.log(this.id);
	//BkCustomerEreaser,LoadNoEreaser,DONoEreaser,PONoEreaser,QuotationNoEreaser,InvoiceNoEreaser,ShipmentNoEreaser,ClaimNoEreaser,SiNoEreaser,JobNoEreaser,BookingNoEreaser
	//bookingNoTxt,jobNoTxt,jobNoTxt,siNoTxt,claimNoTxt,shipmentNoTxt,invoiceNoTxt,quotationNoTxt,doNoTxt,loadNoTxt,bookingCustTxt
	switch(clickEreaserID){
	
		case 'BookingNoEreaser' 	:
										console.log("booking clear...");
									    $('#bookingNoTxt').val('');	
									   break;
		case 'JobNoEreaser' 		: 	 $('#jobNoTxt').val('');		   
									   break;
		case 'SiNoEreaser' 			: 	 $('#siNoTxt').val('');	
									   break;
		case 'ClaimNoEreaser' 		:   $('#claimNoTxt').val('');	
									   break;
		case 'ShipmentNoEreaser'	: 	 $('#shipmentNoTxt').val('');		   
									   break;
		case 'InvoiceNoEreaser' 	: 	 $('#invoiceNoTxt').val('');		   
									   break;
		case 'QuotationNoEreaser'	: 	 $('#quotationNoTxt').val('');	
									   break;
		case 'PONoEreaser'			: 	 $('#poNoTxt').val('');	   
									   break;
		case 'DONoEreaser' 			: 	 $('#doNoTxt').val('');	 
									   break;
		case 'LoadNoEreaser'		: 	 $('#loadNoTxt').val('');	
									   break;
		case 'BkCustomerEreaser' 	:    $('#bookingCustTxt').val('');	
									   break;
		case 'CreditNoteEreaser' 	:    $('#creditNoteTxt').val('');	
									   break;							   
		case 'DebitNoteEreaser' 	:    $('#debitNoteTxt').val('');	
									   break;
		case 'deliveryNoteNoEreaser' 	:    $('#deliveryNoteTxt').val('');	
									   break;
		case 'pullingOrderNoEreaser' 	:    $('#pullingOrderNoTxt').val('');	
									   break;							   
   }
	//deliveryNoteNoEreaser,pullingOrderNoEreaser,deliveryNoteTxt,pullingOrderNoTxt  
 });

function validateDelIcon(field){
  if(field == 'deleteD'){
	  if(deleteFlag == true){
	     return false;
	  }else{
	  return true;
	  }
  }
}
function visibilityCheckDelIcon(){
   if(deleteFlag == false){
		$('td[field="deleteD"]').css("display" , "none"); 
		return false;
	}else{
		$('td[field="deleteD"]').css("display" , "inline-block"); 
		return true;
	}
}

function showErorsByIdS(errorSpanID)
{
      clearMessage();
	  $('#errorMsg').css("display","");
	  var t = '#'+errorSpanID+"_msg";
	  $('#errorConent').html($(t).html());
} 

// This function used to clear the ErrorMessage and Success message on screen
function clearMessage() {
$('#errorMsg').css("display", "none"); 
$('#successMessages').css("display", "none");
}

function validateButtonClickByResult(buttonFlag,buttonCssID){
     console.log("validateButtonClickByResult :: buttonFlag :"+buttonFlag+"---onclickChekFlag : "+onclickChekFlag);
	 if(onclickChekFlag == true && buttonFlag==false){
	    onclickChekFlag=false;
		 $('#'+buttonCssID).attr('disabled','disabled'); 
		 return true;  
	  }else
	     if(onclickChekFlag == false && buttonFlag==true){
		    onclickChekFlag=true;
		   $('#'+buttonCssID).removeAttr('disabled'); 
		   return true;
		 }else{
		  return false;
		 }
}
function  errorMarkHideFormField(){
     replaceErorrColorFromComobox('documentLineOpt');
	replaceErorrColorFromComobox('uploadDateToTxt');
	replaceErorrColorFromComobox('uploadDateFromTxt');
}
function errorHide(){
  console.log("Hiding the errors ..");
  clearMessage();
}
function sucessHide(){
console.log("Hiding the success ..");
clearMessage();
}
function searchValidateErrorShow(){
console.log("--validate  :: start " );
    $('#errorConent').html("");
	$('#errorMsg').css("display", "none");
	var searchVal=true;
   $(".requiredFieldDMSS").each(function(index) { 
		console.log("this.id :: "+this.id);
		if (this.tagName == "SELECT") {
			var opts = $('#' + this.id).combobox('getText');
			var val = $('#' + this.id).combobox('getValue'); 
			if (opts.trim() === '--Select a value--'|| val == '') {
				$('#errorMsg').css("display", "");
				$('.' + this.id + ' .combo ').css("border","1px solid #FF0000");
				showErorsByIdS("OneAndMore");
				searchVal = false;
			} else {
				replaceErorrColorFromComobox(this.id);
			}
		}
		//uploadDateToTxt,uploadDateFromTxt
		
		  console.log(" date box.1..");
				if(this.id == 'uploadDateToTxt' || this.id == 'uploadDateFromTxt'){
				     var fieldValue = $('#' + this.id).combobox('getValue');
					 console.log(" date box.2.."+fieldValue);
							if (fieldValue == '') {
							$('#errorMsg').css("display", "");
								var tempMsg = this.id;
								$('.' + tempMsg + ' .combo ').css("border","1px solid #FF0000");
								searchVal = false;
							} else {
								replaceErorrColorFromComobox(this.id);
							}
				}
		
	})
	
	console.log("--validate  :: start " );
	return searchVal;
}

 </script>
  
</body>




