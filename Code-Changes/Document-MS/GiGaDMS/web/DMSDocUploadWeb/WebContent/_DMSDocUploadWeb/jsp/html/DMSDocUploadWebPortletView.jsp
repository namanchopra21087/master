<%@page session="false" contentType="text/html"
	pageEncoding="ISO-8859-1"
	import="java.util.*,javax.portlet.*,com.giga.dms.controller.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib
	uri="http://www.ibm.com/xmlns/prod/websphere/portal/v6.1/portlet-client-model"
	prefix="portlet-client-model"%>
<portlet:defineObjects />
<portlet-client-model:init>
	<portlet-client-model:require module="ibm.portal.xml.*" />
	<portlet-client-model:require module="ibm.portal.portlet.*" />
</portlet-client-model:init>

<link rel="stylesheet" href="/css/icon.css" type="text/css">
<link rel="stylesheet" href="/css/demo.css" type="text/css">
<link rel="stylesheet" href="/css/button.css" type="text/css" />
<link rel="stylesheet" href="/css/easyui.css" type="text/css">
<link rel="stylesheet" href="/css/DMSSearchPopUp.css" type="text/css" />

<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.easyui.min.js"></script>
<script src="/js/datagrid-filter.js"></script>
<script src="/js/DMSWebUploadSearchPopUp.js"></script>
<script src="/js/CommonScriptFunctions.js"></script>

<style>
#search {
	background: white url(/icons/search.png) right no-repeat;
	padding-right: 17px;
}

.DisabledLink {
	pointer-events: none;
	cursor: default;
	background: #6699FF;
	color: grey !important;
}

.clrDisabled {
	background: #F8F8FF;
}

.pkgFldP1 {
	padding-left: 15px;
}

.FL {
	float: left;
}

.FR {
	float: right;
}

.clr {
	clear: both;
}

.fldAln {
	padding: 2px 0;
}

.lablFld {
	width: 110px;
}

.fldAlgn {
	margin-left: 0px;
}

.bookingont .panel-title {
	
}

.imgParent {
	position: relative;
}

.imgPrintExl {
	position: absolute;
	top: 3px;
	right: 24px;
	z-index: 99;
}

.doc-upload input {
	height: 22px !important;
}

.gridTbl .panel-header {
	width: 968px !important;
	margin-bottom: -1px;
}

.PopupSrchIcn input,.PopupSrchIcn img {
	border: none;
	float: left;
}

.PopupSrchIcn {
	border: 1px solid #a9abb0;
	display: inline-block;
	float: left;
	margin-right: 2px;
}

.PopupSrchIcn {
	border: 1px solid #a9abb0;
	display: inline-block;
}

.PopupSrchIcn input,.PopupSrchIcn img {
	border: none;
}
</style>

<%
	String message = "";
	String messageD = "";
	if (request.getAttribute("screenMessage") != null) {
		message = (String) request.getAttribute("screenMessage");
		messageD = (String) request.getAttribute("messageData");
	}
%>
<div id="successMessages" title="Upload Details"
	style="width: 960px; text-align: left; border-color: #95B8E7; display: none; border-style: solid; border-width: 1px;">
	<div style="width: 960px; text-align: right;"></div>
	<table class="easyui-panel"
		style="width: 960px; background-color: #EFF5FF">
		<tr>
			<td style="width: 90%; text-align: left;">
				<div>
					<b>Message</b>
				</div>
			</td>
			<td style="width: 10%; text-align: right;">
				<div>
					<img border="1" onclick="sucesshide();"
						src="/images/close-icon.png" id="closeSuccessDiv" width="17"
						height="17" class="closeErrorBox">
				</div>
			</td>
		</tr>
	</table>
	<div id="successMsg"
		style="color: green; background: #f0f0f0; padding: 0px 2px 6px;"></div>
</div>

<div id="errorMsg" title="Error Details"
	style="width: 960px; text-align: left; border-color: #95B8E7; display: none; border-style: solid; border-width: 1px;">

	<div style="width: 960px; text-align: right;"></div>
	<table class="easyui-panel"
		style="width: 960px; background-color: #EFF5FF">
		<tr>
			<td style="width: 90%; text-align: left;">
				<div>
					<b>Message</b>
				</div>
			</td>
			<td style="width: 20%; text-align: right;">
				<div>
					<img border="1" onclick="errorhide();" src="/images/close-icon.png"
						id="errorclose" width="17" height="17" class="closeErrorBox">
				</div>
			</td>
		</tr>
	</table>

	<div id="errorConent"
		style="color: red; background: #f0f0f0; padding: 0px 2px 6px;"></div>

</div>
<div style="margin: 5px 0;"></div>

<div class="webbUploadd">
	<span id="documentLineOpt_err" style="display: none"><font
		style="color: red"> </br> Document Line is required.
	</font></span> <span id="documentTypeOpt_err" style="display: none"><font
		style="color: red"> </br> Document Type is required.
	</font></span> 
	
	<!-- <span id="serviceTypeId_err" style="display: none"><font
		style="color: red"> </br> Service Type is required.
		</font>
	</span> --> 
	
	<span id="upload_success" style="display: none"><font
		style="color: green"> </br> Following documents successfully to DMS.                
	</font></span>
	
	<span id="upload_success_files" style="display: none">
	</span>
	
	 <span id="upload_err" style="display: none"><font
		style="color: red"> </br> Failed to upload documents !</br> Please make
			sure uploaded documents are only in
			pdf,images(tiff,bmp,jpg),excel,word format.
	</font></span>
</div>


<form id="webDocumentUpload" method="post" name="webDocumentUpload"
	enctype="multipart/form-data"
	action="<portlet:actionURL> <portlet:param name='javax.portlet.action' value='uploadDocuments'/> </portlet:actionURL>">

	<div class="easyui-panel" title="GMG - Document Upload"
		style="width: 960px; background: linear-gradient(#F5F5F5, #F5F5F5)">
		<div style="padding: 1px 0 10px 0px">
			<div id="" style="padding: 1px 0 10px 5px" align="center">
				<table width="950" border="0" class="doc-upload">
					<tr></tr>
					<tr></tr>
					<tr>
						<td width="140">
							<div align="right">
								Document Line:<label class="madantFld"> * </label>
							</div>
						</td>
						<td><span class="documentLineOpt"> <select
								class="easyui-combobox requiredGMG" name="documentLineOpt"
								data-options="editable: false"
								id="documentLineOpt"
								style="width: 178px; height: 25px; text-align: left;">

									<option value="">--Select a value--</option>
									<c:forEach var="documentType" items="${documentLineList}">
										<option value="${documentType.key}">${documentType.value}</option>
									</c:forEach>
							</select>
						</span></td>
						<td width="140">
							<div align="right">
								Document Type: <label class="madantFld"> * </label>
							</div>
						</td>
						<td><span class="documentTypeOpt"> <select
								class="easyui-combobox requiredGMG" name="documentTypeOpt"
								data-options="editable: false"
								id="documentTypeOpt" style="width: 180px; height: 25px;">
									<option value="">--Select a value--</option>
							</select>
						</span></td>
						<td width="180">
							<div align="right" width="180px">Booking Customer:&nbsp;</div>
						</td>
						<td>
							<div class="PopupSrchIcn">
								<input type="text" class="customSPopup bkCustomerCssDMS"
									name="bookingCustTxt" id="bookingCustTxt"
									style="width: 160px; height: 17px; text-align: left; background-color: #F8F8FF;"
									readonly="readonly"> <img
									id="bookingCustSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup bookingCustomerDMSSearchPopup customerSarchIconDMS"
									src="/images/search.png" /> <img id="bookingCustClear"
									style="background-color: #F8F8FF; border: none;"
									class="clearImg" onclick="clearBkCusFromBk();"
									src="/images/img_clear.gif" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">Booking No:&nbsp;</div>
						</td>
						<td>
							<div class="PopupSrchIcn">
								<input type="text" name="bookingNoTxt" id="bookingNoTxt"
									value='<%=request.getAttribute("bookingNoIPC")%>'
									class="customSPopup"
									style="width: 141px; height: 17px; text-align: left; background-color: #F8F8FF;"
									readonly="readonly"> <img id="bookingNosearch"
									class="serachPopup bookingNoDMSSearchPopup"
									style="background-color: #F8F8FF; border: none;"
									src="/images/search.png" /> <img id="bookingNoClear"
									style="background-color: #F8F8FF; border: none;"
									class="clearImg" onclick="clearBkNoFromBkNo();"
									src="/images/img_clear.gif" />
							</div>
							<input type="hidden" name="vesselNameTxt" id="vesselNameTxt"/>
							<input type="hidden" name="voyageNoTxt" id="voyageNoTxt"/>
						</td>
						<td>
							<div align="right" width="180px">Job No:&nbsp;</div>
						</td>
						<td>
							<div class="PopupSrchIcn">
								<input type="text" class="customSPopup" name="jobNoTxt"
									id="jobNoTxt"
									style="width: 141px; height: 17px; text-align: left; background-color: #F8F8FF;"
									readonly="readonly" /> <img id="jobNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup jobNoDMSSearchPopup"
									src="/images/search.png" /> <img id="jobNoClear"
									style="background-color: #F8F8FF; border: none;"
									class="clearImg" onclick="clearJbNoFromJbNo();"
									src="/images/img_clear.gif" />
							</div>
						</td>
						<td>
							<div align="right" width="180px">SI No:&nbsp;</div>
						</td>
						<td>
							<div class="PopupSrchIcn">
								<input name="siNoTxt" id="siNoTxt" type="text"
									class="customSPopup enabledByOFS"
									style="width: 160px; height: 17px; text-align: left; background-color: #F8F8FF;"
									readonly="readonly" /> <img id="siNumberSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup siNoDMSSearchPopup enabledByOFS"
									src="/images/search.png" /> <img id="siNumberClear"
									style="background-color: #F8F8FF; border: none;"
									class="clearImg" onclick="clearSiNoFromSiNo();"
									src="/images/img_clear.gif" />
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div align="right">Business Line:</div>
						</td>
						<td><input id="businessLineId" name="businessLineId"
							type="text"
							style="width: 174px; height: 15px; background-color: #FfFfFF; text-align: left"
							readonly /></td>
						<td>
							<div align="right">
								Service Type:
							</div>
						</td>
						<td><span class="serviceTypeId"> <select
								class="easyui-combobox" name="serviceTypeId"
								data-options="editable: false"
								id="serviceTypeId" style="width: 180px; height: 25px;">
									<option value="">--Select a value--</option>
							</select>
						</span></td>
						<td>
							<div align="right">Service Line:</div> <label class="madantFld"></label>
						</td>
						<td width="280"><span class="documentTypeCSS"> <select
								class="easyui-combobox" name="serviceLineId" id="serviceLineId"
								data-options="editable: false"
								style="width: 198px; height: 25px;">
									<option value="">--Select a value--</option>
							</select>
						</span></td>
					</tr>
					<tr>
						<td>
							<div align="right">
								Port of Discharge:
							</div>
						</td>
						<td><span class="podId"> <select
								class="easyui-combobox" name="podId" id="podId"
								data-options="editable: false"
								style="width: 180px; height: 25px;">
									<option value="">--Select a value--</option>
							</select>
						</span></td>
						<td>
							<div align="right" width="180px">Cargo Type:</div>
						</td>
						<td><span class="documentTypeCSS"> <select
								class="easyui-combobox" name="cargoTypeId" id="cargoTypeId"
								data-options="editable: false"
								style="width: 180px; background-color: #FfFfFF; height: 25px; text-align: left;">
									<option value="">--Select a value--</option>
							</select>
						</span></td>
						<td>
							<div align="right">Claim No:&nbsp;</div>
						</td>
						<td>
							<div class="PopupSrchIcn">
								<input type="text" class="customSPopup" name="claimNoTxt"
									id="claimNoTxt"
									style="width: 160px; height: 17px; text-align: left; background-color: #F8F8FF;"
									readonly="readonly" /> <img id="claimNoSearch"
									style="background-color: #F8F8FF; border: none;"
									class="serachPopup claimNoDMSSearchPopup"
									src="/images/search.png" /> <img id="claimNoClear"
									style="background-color: #F8F8FF; border: none;"
									class="clearImg" onclick="clearClaimNoFromClaimNo();"
									src="/images/img_clear.gif" />
							</div>
						</td>
					</tr>
					<tr>
					<tr>
						<td>
							<div align="right">Upload Date:</div>
						</td>
						<td><input id="uploadDateId"
							class="easyui-datebox LoadTodaysDate"
							style="width: 180px; height: 24px; text-align: left;"
							readonly="readonly" /></td>
						<td>
							<div align="right">Upload By:</div>
						</td>
						<td><input id="uploadById" type="text" name="uploadById"
							style="width: 178px; height: 17px; background-color: #FfFfFF; text-align: left"
							readonly /></td>
						<td></td>	
						<td align="middle" colspan="1"><input type="button"
							id="fakeButton" value=" Add "
							class="button blue bigrounded DisabledLink"
							onclick="HandleBrowseClick();"></td>
					</tr>
					<tr></tr>
				</table>

				<table>
					<tr>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<table>

		<div style="margin: 10px 0 0 0; border-bottom: none;" class="gridTbl">
			<table id="dg" style="width: 960px; height:350px;" class="easyui-datagrid"
				title="Document Upload details"
				data-options="

										rownumbers:true
								">
				<thead>
					<tr>
						<th data-options="field:'docLine',width:97" formatter="docLine">Document Line</th>
						<th data-options="field:'docType',width:97" formatter="docType">Document Type</th>
						<th data-options="field:'bookingNo',width:97" formatter="bookingNo">Booking No</th>
						<th data-options="field:'jobNo',width:80" formatter="jobNo">Job No</th>
						<th data-options="field:'siNo',width:80" formatter="siNo">SI No</th>
						<th data-options="field:'pod',width:80" formatter="pod">Port of Discharge</th>
						<th data-options="field:'cargoType',width:110" formatter="cargoType">Cargo Type</th>
						
						<th hidden="true" data-options="field:'bookingCustF'" formatter="bookingCustF"/>
						<th hidden="true" data-options="field:'businessLineF'" formatter="businessLineF"/>
						<th hidden="true" data-options="field:'serviceTypeF'" formatter="serviceTypeF"/>
						<th hidden="true" data-options="field:'serviceLineF'" formatter="serviceLineF"/>
						<th hidden="true" data-options="field:'claimNoF'" formatter="claimNoF"/>
						<th hidden="true" data-options="field:'vesselNameF'" formatter="vesselNameF"/>
						<th hidden="true" data-options="field:'voyageNoF'" formatter="voyageNoF"/>
						
						<th data-options="field:'file',width:250" formatter="inputTypeFile">Document Path</th>
						<th width="25" class="removeIcon" id="u" data-options="field:'edit',width:0,align:'center'" formatter="deleteLink1"></th>
						
					</tr>
				</thead>
			</table>

		</div>
		</tr>
		<tbody class="test clrDisabled"></tbody>
	</table>
	<table>
		<tr>
			<td></td>
			<td>
				<div
					style="float: right; width: 100px; position: relative; left: 850px;">
					<input id="uploadButtonz" value="Upload"
						class="button blue bigrounded topopup DisabledLink"
						style="float: right; width: 50px;"
						onclick="javascript:uploadDocs()" />
				</div>
			</td>
		</tr>
	</table>

	<div class="loader"></div>
	<div id="backgroundPopup"></div>

</form>

<%@ include file="DMSBookingSearchPopup.jspf"%>
<%@ include file="DMSJobSearchPopup.jspf"%>
<%@ include file="DMSSiSearchPopup.jspf"%>
<%@ include file="DMSClaimSearchPopup.jspf"%>
<%@ include file="DMSBookingCustomerSearchPopup.jspf"%>

<script type="text/javascript"> 
var loginModuelType="";
var docuementLineSelect="";

//CONSTANTS FOR SYSTEM LINE AND DOCUMENT LINE
var  DOC_LINE_TYPE_SYSTEM_OUTPUT="SYSTEM OUTPUT";
var  DOC_LINE_TYPE_OTHERS="OTHERS";

 //common-- param constant
	var P_BK_CUSTOMER= '<%=com.giga.dms.util.DMSGConstants.P_BK_CUSTOMER%>';
	var P_BK_NUMBER= '<%=com.giga.dms.util.DMSGConstants.P_BK_NUMBER%>';
	var P_POD= '<%=com.giga.dms.util.DMSGConstants.P_POD%>';
	var P_VOYAGENO= '<%=com.giga.dms.util.DMSGConstants.P_VOYAGENO%>';
	var P_VESSELNAME= '<%=com.giga.dms.util.DMSGConstants.P_VESSELNAME%>';
	var P_ETA_DATE= '<%=com.giga.dms.util.DMSGConstants.P_ETA_DATE%>';
	var P_PICKUPLOCATION= '<%=com.giga.dms.util.DMSGConstants.P_PICKUPLOCATION%>'; 
	
//booking-- param constant 
	var P_BK_DATE_FROM= '<%=com.giga.dms.util.DMSGConstants.P_BK_DATE_FROM%>';
	var P_BK_DATE_TO= '<%=com.giga.dms.util.DMSGConstants.P_BK_DATE_TO%>';
	var P_BUSINESSLINE= '<%=com.giga.dms.util.DMSGConstants.P_BUSINESSLINE%>';   
	
	
//DMS-Parameters-by-events
var DMS_DOCUMENT_TITLE= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_DOCUMENT_TITLE%>';
var DMS_DCUMENT_ID= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_DCUMENT_ID%>';
var DMS_DOCUMENT_LINE= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_DOCUMENT_LINE%>';
var DMS_DOCUMENT_TYPE= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_DOCUMENT_TYPE%>';
var DMS_BOOKING_NO= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_BOOKING_NO%>';
var DMS_JOB_NO= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_JOB_NO%>';
var DMS_SI_NO= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_SI_NO%>';
var DMS_CLAIM_NO= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_CLAIM_NO%>';
var DMS_BOOKING_CUSTOMER= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_BOOKING_CUSTOMER%>';
var DMS_UPLOAD_FORM_DATE= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_UPLOAD_FORM_DATE%>';
var DMS_UPLOAD_TO_DATE= '<%=com.giga.dms.helper.BookingDMSHelper.DMS_UPLOAD_TO_DATE%>';

//Exceptions...
var NO_DOCUMENT_FOUND_ERROR= '<%=com.giga.dms.helper.BookingDMSHelper.NO_DOCUMENT_FOUND_ERROR%>'; 

	
</script>

<script type="text/javascript">
	var d = new Date();
	var month = d.getMonth() + 1;
	var day = d.getDate();
	var output = (day < 10 ? '0' : '') + day + '/'	+ (month < 10 ? '0' : '') + month + '/' + d.getFullYear();
	$('.LoadTodaysDate').val(output);
</script>

<script type="text/javascript">
			$('.panel-header').css('width', '900px');
			$('.panel-tool').css('right', '-15px');

			$(function() {
				//Logged in user details
				var DMS_LOGGED_USER= '<%=com.giga.dms.helper.BookingDMSHelper.LOGGED_USER_NEW%>';									
				$('#uploadById').val('<%=com.giga.dms.helper.BookingDMSHelper.LOGGED_USER_NEW%>');
			});	
			
			//Ajax way of deleting
			$('#dg').datagrid({
				onClickCell : function(rowIndex, field, value) {
					if (field == 'edit') {
						$('#dg').datagrid('deleteRow',rowIndex);
					}
					//$('#dg').datagrid('enableFilter', []);
					$('#dg').datagrid('reload');
					$('#dg').datagrid('unselectAll');
					
					var chkButton = "";
					chkButton = $('#dg').datagrid('getData')['total'];
					if(JSON.stringify(chkButton) == 0){
						$('#fakeButton').removeClass('DisabledLink');
						$('#uploadButtonz').addClass('DisabledLink');			
					}
				}
			});

			function submitForm() {
				$('#ff').form('submit');
			}
			function addLink(val, row) {
				var linkImg = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/icons/images.jpg")%>';			
				if (val == "Edit") {
					return '<img src='+linkImg+'>';					
				} else {
					return '<img src='+linkImg+'>';
				}
			}
			function inputTypeFile(){
				return '<input type="file" name="fileName" onchange="enableDisableFun();">';
			}
			
			
			function docLine(){
				return '<input type="text" name="docLine[]" readonly style="border:none; font-size: 12px;">';
			}
			function docType(){
				return '<input type="text" name="docType[]" readonly style="border:none; font-size: 12px;">';
			}
			function bookingNo(){
				return '<input type="text" name="bookingNo[]" readonly style="border:none; font-size: 12px;">';
			}
			function jobNo(){
				return '<input type="text" name="jobNo[]" readonly style="border:none; font-size: 12px;">';
			}			
			function siNo(){
				return '<input type="text" name="siNo[]" readonly style="border:none; font-size: 12px;">';
			}			
			function pod(){
				return '<input type="text" name="pod[]" readonly style="border:none; font-size: 12px;">';
			}
			function cargoType(){
				return '<input type="text" name="cargoType[]" readonly style="border:none; font-size: 12px;">';
			}
			function bookingCustF(){
				return '<input type="text" name="bookingCustF[]" readonly style="border:none; font-size: 12px;">';
			}
			function businessLineF(){
				return '<input type="text" name="businessLineF[]" readonly style="border:none; font-size: 12px;">';
			}
			function serviceTypeF(){
				return '<input type="text" name="serviceTypeF[]" readonly style="border:none; font-size: 12px;">';
			}
			function serviceLineF(){
				return '<input type="text" name="serviceLineF[]" readonly style="border:none; font-size: 12px;">';
			}
			function claimNoF(){
				return '<input type="text" name="claimNoF[]" readonly style="border:none; font-size: 12px;">';
			}			
			function vesselNameF(){
				return '<input type="text" name="vesselNameF[]" readonly style="border:none; font-size: 12px;">';
			}
			function voyageNoF(){
				return '<input type="text" name="voyageNoF[]" readonly style="border:none; font-size: 12px;">';
			}

			function addLink1(val, row) {
				var linkImg1 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/icons/softdelete.png")%>';
				var linkImg2 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/images/softdelete.png")%>';
				if (val == "Edit") {
					return '<img src='+linkImg1+'>';					
				} else {
					return '<img src='+linkImg2+'>';					
				}
			}

			function deleteLink1(val, row) {
				var linkImg1 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/icons/softdelete.png")%>';
				var linkImg2 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/images/softdelete.png")%>';
				if (val == "Edit") {
					return '<img src='+linkImg1+'>';					
				} else {
					return '<img src='+linkImg2+'>';					
				}
			}

			function addLinkEdit(val, row) {
				var linkImg1 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/icons/edit.jpg")%>';
				var linkImg2 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/images/images.jpg")%>';			
				if (val == "Delete") {
					return '<img src='+linkImg1+'>';					
				} else {
					return '<img src='+linkImg2+'>';					
				}
			}
			function nchkBox(val, row) {
				var linkImg1 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/images/checkboxno.jpg")%>';			
				if (val == "nchk") {
					return '<img src='+linkImg1+'>';
				} else {
					return val;
				}
			}
			function chkBox(val, row) {
				var linkImg1 = '<%=renderResponse.encodeURL(renderRequest.getContextPath()
					+ "/images/checkbox.jpg")%>';
		if (val == "chk") {
			return '<img src='+linkImg1+'>';
		} else {
			return val;
		}
	}

	function HandleBrowseClick() {
		// Check whether max file upload count exceeds..
	
		if(checkValidations()){
			errorhide();
			console.log("Add event triggered!!!");

			var docline = $("#documentLineOpt").combobox('getText') != "--Select a value--" ? $("#documentLineOpt").combobox('getText') : "" ;							
 			var docType = $('#documentTypeOpt').combobox('getText') != "--Select a value--" ? $("#documentTypeOpt").combobox('getText') : "";
			var podischarge = $('#podId').combobox('getText') != "--Select a value--" ? $("#podId").combobox('getText') : "";
			var cargotype = $('#cargoTypeId').combobox('getText') != "--Select a value--" ? $("#cargoTypeId").combobox('getText') : "";
	
			var servicetype = $('#serviceTypeId').combobox('getText') != "--Select a value--" ? $("#serviceTypeId").combobox('getText') : "";
			var serviceline = $('#serviceLineId').combobox('getText') != "--Select a value--" ? $("#serviceLineId").combobox('getText') : "";			

 			$('#dg').datagrid('appendRow', {
				docLine		: docline,
				docType 	: docType,
				bookingNo 	: $('#bookingNoTxt').val(),
				jobNo 		: $('#jobNoTxt').val(),
				siNo 		: $('#siNoTxt').val(),
				pod			: podischarge,
				cargoType 	: cargotype,
				
				bookingCustF: $('#bookingCustTxt').val(),
				claimNoF	: $('#claimNoTxt').val(),
				serviceTypeF: servicetype,
				serviceLineF: serviceline,
				businessLineF:$('#businessLineId').val(),
				vesselNameF: $('#vesselNameTxt').val(),
				voyageNoF: $('#voyageNoTxt').val()
				
			});
			
			var ids=document.getElementsByName('docLine[]');
			var trueID = ids.length -1;
			
			$("input[name='docLine[]']").eq(trueID).val(docline);
			$("input[name='docType[]']").eq(trueID).val(docType);
			$("input[name='bookingNo[]']").eq(trueID).val($('#bookingNoTxt').val());
			$("input[name='jobNo[]']").eq(trueID).val($('#jobNoTxt').val());
			$("input[name='siNo[]']").eq(trueID).val($('#siNoTxt').val());
			$("input[name='pod[]']").eq(trueID).val(podischarge);			
			$("input[name='cargoType[]']").eq(trueID).val(cargotype);
						
			$("input[name='bookingCustF[]']").eq(trueID).val($('#bookingCustTxt').val());
			$("input[name='businessLineF[]']").eq(trueID).val($('#businessLineId').val());
			$("input[name='serviceTypeF[]']").eq(trueID).val(servicetype);
			$("input[name='serviceLineF[]']").eq(trueID).val(serviceline);			
			$("input[name='claimNoF[]']").eq(trueID).val($('#claimNoTxt').val());
			$("input[name='vesselNameF[]']").eq(trueID).val($('#vesselNameTxt').val());
			$("input[name='voyageNoF[]']").eq(trueID).val($('#voyageNoTxt').val());
			
			$('#fakeButton').addClass('DisabledLink');
			$('#uploadButtonz').addClass('DisabledLink');			
			console.log("Add event completed!!!");
		}
	}
	function softDelete(rowIndex) {
		alert("softDelete clicked!!");
		$('#dg').datagrid({
			onClickCell : function(rowIndex, field, value) {
				$('#dg').datagrid('deleteRow', rowIndex);
			}
		});
	}

	function checkValidations(){
		var submitFlag = true;
		$('.requiredGMG').css("border-color", "none");
		$('#errorConent').html("");	
		
		$(".requiredGMG" ).each(function( index ) {
		
		var tempId = this.id;

			if(this.tagName == "SELECT"){
				$('.'+this.id+' .combo').css("border", "1px solid #95B8E7");
				var val = $('#'+tempId).combobox('getValue');
				if(val == ""){
					if(this.id != "documentLineOpt"){
						$('#errorMsg').css("display","");
						var tempMsg = this.id;
						var t = '#'+tempMsg+"_err";
						$('.'+this.id+' .combo').css("border", "1px solid #FF0000");
						$('#errorConent').append($(t).html());
						submitFlag = false;
					}
					else{
						if('<%=request.getAttribute("bookingNoIPC")%>' !=""){
						$('.'+this.id+' .combo').css("border", "1px solid #95B8E7");
							submitFlag = true;
						}
						else{
							$('#errorMsg').css("display","");
							var tempMsg = this.id;
							$('.'+this.id+' .combo').css("border", "1px solid #95B8E7");
							var t = '#'+tempMsg+"_err";
							$('.'+this.id+' .combo').css("border", "1px solid #FF0000");
							$('#errorConent').append($(t).html());
							submitFlag = false;
						}
					}
				}	
			}		
		});
		return submitFlag;
	}

	function uploadDocs() {
		
		var docLineHdn = $("#documentLineOpt").combobox('getText') != "--Select a value--" ? $("#documentLineOpt").combobox('getText') : "" ;							
 		var docTypeHdn = $('#documentTypeOpt').combobox('getText') != "--Select a value--" ? $("#documentTypeOpt").combobox('getText') : "";
 		var docSrvTypeHdn = $('#serviceTypeId').combobox('getText') != "--Select a value--" ? $("#serviceTypeId").combobox('getText') : "";
		var docSrvLinHdn = $('#serviceLineId').combobox('getText') != "--Select a value--" ? $("#serviceLineId").combobox('getText') : "";
		var podischargeHdn = $('#podId').combobox('getText') != "--Select a value--" ? $("#podId").combobox('getText') : "";
		var cargotypeHdn = $('#cargoTypeId').combobox('getText') != "--Select a value--" ? $("#cargoTypeId").combobox('getText') : "";
						
		if(checkValidations()){
			$('#webDocumentUpload').submit();
		}	
	}

	/* to clear the error message */
	function errorhide() {
		$('#errorMsg').css("display", "none");
	}
	
	function closeSuccessBox(){
		$('#successMessages').css("display","none");
	}	
	
	$('#documentLineOpt').combobox({
		onSelect: function(row){
	      console.log("You are selected.....documentLineOpt .");
		  setDefaultValueToDocumentType(); 
		  var opts = $(this).combobox('options');
		  var selectDocLineText = row[opts.textField];
		  var selectDocLineVal = row[opts.valueField];
		  
		   docuementLineSelect=selectDocLineText;// set the selected text value to var.. for claim seach validate
		  
		  console.log("selected......DocumetnLine....Text :"+selectDocLineText+"---selectDocLineVal : "+selectDocLineVal+"--");
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
                	   if(results.length==0){
                		   console.log("No results return : length is : "+results.length);
                		
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
 

//This method is used to populate service line based on service type selected
	$('#serviceTypeId').combobox(
			{
				onSelect : function(row) {
					console.log("You are selected.....serviceTypeId .");
					setDefaultValueToServiceLine();
					
					var bookingNo = $('#bookingNoTxt').val();
					var opts = $(this).combobox('options');
					var selectSerTypeText = row[opts.textField];
					var selectSerTypeVal = row[opts.valueField];					

					var urlParamDoc = {
						DOC_LINE_TEXT	: selectSerTypeText,
						DOC_LINE_VAL	: selectSerTypeVal,
						BOOKING_NO		: bookingNo
					};

					$.ajax({
						beforeSend : function() {
						},
						type : "POST",
						async : true,
						url : '<portlet:resourceURL id="LOAD_DMS_BOOKING_SERVICELINES" />',
						data : urlParamDoc,
						datatype : "json",
						success : function(results) {
							console.log(results);
							if (results.length == 0) {
								console.log("No results return : length is : "
										+ results.length);

							} else {
								console.log("result return : length is : "
										+ results.length);
								$('#serviceLineId').combobox({
									valueField : 'key',
									textField : 'value',
									data : results
								});
								$('#serviceLineId').combobox("setText",
										"--Select a value--");
								$('#serviceLineId').combobox("setValue", "");
							}							
						}
					});
					
					//This call is to load POD's based on service type selected.
					setDefaultValueToPOD();
				   if(loginModuelType == 'OFS'){ //Data hiding for TMS					
					$.ajax({
						beforeSend : function() {
						},
						type : "POST",
						async : true,
						url : '<portlet:resourceURL id="LOAD_BOOKING_PODS" />',
						data : urlParamDoc,
						datatype : "json",
						success : function(results) {
							console.log(results);
							if (results.length == 0) {
								console.log("No results return : length is : "
										+ results.length);

							} else {
								console.log("result return : length is : "
										+ results.length);
								$('#podId').combobox({
									valueField : 'key',
									textField : 'value',
									data : results
								});
								$('#podId').combobox("setText",
										"--Select a value--");
								$('#podId').combobox("setValue", "");
							}							
						}
					});					
					}	
				}
			});

	function setDefaultValueToDocumentType() {
		var jsonDocType = '[{"key":"","value":"--Select a value--"}]';
		var docTypeJson = JSON.parse(jsonDocType);
		console.log("docTypeJson :: " + docTypeJson);

		$('#documentTypeOpt').combobox({
			valueField : 'key',
			textField : 'value',
			data : docTypeJson
		});
	}

	function setDefaultValueToServiceLine() {
		var jsonDocType = '[{"key":"","value":"--Select a value--"}]';
		var docTypeJson = JSON.parse(jsonDocType);
		console.log("docTypeJson :: " + docTypeJson);

		$('#serviceLineId').combobox({
			valueField : 'key',
			textField : 'value',
			data : docTypeJson
		});
	}

	function setDefaultValueToPOD() {
		var jsonDocType = '[{"key":"","value":"--Select a value--"}]';
		var docTypeJson = JSON.parse(jsonDocType);
		console.log("docTypeJson :: " + docTypeJson);

		$('#podId').combobox({
			valueField : 'key',
			textField : 'value',
			data : docTypeJson
		});
	}

	$(window)
			.load(
					function() {
						console.log("window.load......... final loading");
						$('#fakeButton').removeClass('DisabledLink');
						
						//Below code is used to show success or failure message
						$('#errorConent').html("");
						
						console.log("Screen message value is :: "+'<%=request.getAttribute("screenMessage")%>');
						console.log("22Screen message value is :: "+'<%=request.getAttribute("messageData")%>');
						
						if('null' != '<%=request.getAttribute("screenMessage")%>'
							&& '<%=request.getAttribute("screenMessage")%>' !=""){
							console.log("inside if");
							if('<%=request.getAttribute("screenMessage")%>' == '1001'){
								$('#errorMsg').css("display","");
								var t = '#upload_success';
								$('#errorConent').append($(t).html());

								$("#upload_success_files").html('<br><font style="color: orange; font-size: 12px;">'+
													'<%=request.getAttribute("messageData")%>'+
													'<font>');
								var t1 = '#upload_success_files'
								$('#errorConent').append($(t1).html());
							}
							else{
								$('#errorMsg').css("display","");
								var t = '#upload_err';
								$('#errorConent').append($(t).html());
							}

						}
						//End
						
						//Below code
						
												
						$('.combo .combo-text').attr('readonly', 'readonly');
						$('[name="dmsId"],[name="documentTitle"],[name="dmsId"],[name="bookingCustF"],[name="businessLineF"],[name="serviceTypeF"],[name="serviceLineF"],[name="claimNoF"],[name="vesselNameF"],[name="voyageNoF"]').css(
								'visibility', 'hidden');
						console.log("load Today Date : " + loadTodayDate());
						$('.LoadTodaysDate').datebox('setValue',
								loadTodayDate());
						enablingGridAndFilter('dmsSearchTbldg');
						var profileAccessError = '<c:out value="${PROFILE_ACC_FAILED}"/>';
						loginModuelType = '<c:out value="${SYSTEM_LINE}"/>';
						console.log("profileAccessError : "
								+ profileAccessError
								+ "-----loginModuelType : " + loginModuelType);

						if (profileAccessError != null
								&& profileAccessError != "") {
							var profileErrorValue = '<%=com.giga.dms.helper.BookingDMSHelper.PROFILE_ACESS_FAILED%>';
			   console.log("profileErrorValue : "+profileErrorValue);
				 if(profileAccessError==profileErrorValue){ 					   
				 } 
			 }  
			 else{ 
			    console.log("enabled..................");
			      $("input").attr('enabled','enabled');
				   $("select").attr('enabled','enabled');
				   $("img").attr('enabled','enabled');
				   $('.combo-arrow href').bind('click');
				   $('.disableCss').bind("click");
				   disableFieldsBySystemLine(loginModuelType);
				   if(loginModuelType == 'OFS'){
					  $('.veslNameCssLbl_BK').css("display","inline-block");
				      $('.blineCssLbl_BK').css("display","inline-block");
					  $('.trCssTxt_BK').css("display","inline-block");
					  
					  $('.pikLocCssLbl_BK').css("display","none");
					  $('.incNoCss_CL').css("display","none");
				   
				   }else 
				    if(loginModuelType == 'TMS'){
				      $('.blineCssLbl_BK').css("display","none");
					  $('.veslNameCssLbl_BK').css("display","none");
					   $('.trCssTxt_BK').css("display","none");
					  
					  $('.pikLocCssLbl_BK').css("display","inline-block");
					  $('.incNoCss_CL').css("display","inline-block");
					  
				   } 
				   
				 //Below code is used to populate booking ipc details.
				var hiddenIPCValue =  '<%=request.getAttribute("bookingNoIPC")%>' ;
				if(hiddenIPCValue != ""){
					console.log("HIDDEN IPC value is : "+hiddenIPCValue);
					
				var resourceURL =  '<portlet:resourceURL id="LOAD_BOOKING_SERVICETYPES" />';
            	var bookingNo_Bk= hiddenIPCValue;

			console.log("Booking No is : "+bookingNo_Bk);

			$('#bkNoTxt_claim').val(bookingNo_Bk);
			$('#bkNoTxt_JOB').val(bookingNo_Bk);
			$('#bkNoTxt_si').val(bookingNo_Bk);

            $.ajax({                                                       
                    type:"POST",
                    async: true,
                    url:resourceURL,
                    data: { bookingNo_Bk: bookingNo_Bk },
                    datatype : "json",                   
                   	
                   	success: function(result){	
                   	console.log("Service types are : "+result);
						$('#serviceTypeId').combobox({
							valueField: 'key',
							textField: 'value',
							data:  result
						});                             
                   }                    
              
            });
            
			//This method will fetch list of all cargo descriptions associated with booking No
			var resourceURL =  '<portlet:resourceURL id="LOAD_BOOKING_CARGOTYPES" />';
            var bookingNo_Bk= hiddenIPCValue;
            $.ajax({                                                       
                    type:"POST",
                    async: true,
                    url:resourceURL,
                    data: { bookingNo_Bk: bookingNo_Bk },
                    datatype : "json",                   
                   	
                   	success: function(result){	
						$('#cargoTypeId').combobox({
							valueField: 'key',
							textField: 'value',
							data:  result
						});                             
                   }                    
              
            });				
			
		$('#documentLineOpt').combobox('setText','CUSTOMS CLEARANCE');
		$("#documentLineOpt").combobox('disable');
		
		var ik =  $('#documentLineOpt').combobox('getData');
		
		var results = [];
		var searchField = "text";
		var searchVal = "CUSTOMS CLEARANCE";
		for (var i=0 ; i < ik.length ; i++)
		{
    		if (ik[i][searchField] == searchVal) {
        		results.push(ik[i]);
    		}
		}
		var selectDocLineVal=JSON.stringify(results[0]['value']).replace(/"/g, "");				
		var selectDocLineText = "";
		console.log("DocumetnLine---selectDocLineVal : "+selectDocLineVal);
		var urlParamDoc={DOC_LINE_TEXT : selectDocLineText, DOC_LINE_VAL : selectDocLineVal};
				
	    		$.ajax({
                    beforeSend: function () {
						console.log("Document type is triggered!!");
				   },
                    type : "POST",                    
                    async : true,
                    url : '<portlet:resourceURL id="LOAD_DOC_TYPE" />',
                    data: urlParamDoc,
                    datatype : "json",                  
                   success: function(results)
                   {
				     console.log(results);
                	   if(results.length==0){
                		   console.log("No results return  for document type ");
                		
                	   }else{
                		   console.log("result return for document type and length is : "+results.length);
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
               
               //Added on 20 May 2015, below code will fetch and show Booking Customer and business line based on
               // booking no retreived from booking IPC
               var bookingNo_Book= hiddenIPCValue;
        	   $.ajax({
                    beforeSend: function () { 
				   },
                    type : "POST",                    
                    async : true,
                    url : '<portlet:resourceURL id="LOAD_BOOKINGCUS_AND_BUSINESSLINE" />',
                    data: { bookingNo_Book: bookingNo_Book },
                    datatype : "json",                  
                   success: function(results)
                   {
						console.log("------------IPC---------- "+JSON.stringify(results));
						var ipcbusinessLine=JSON.stringify(results['businessLine']).replace(/"/g, "");
						var ipcbookingCustomer = JSON.stringify(results['bookingCustomer']).replace(/"/g, "");
						if(results.length==0){
                		   console.log("Failed to load LOAD_BOOKINGCUS_AND_BUSINESSLINE values");
						}else{
							console.log("Success retreiving LOAD_BOOKINGCUS_AND_BUSINESSLINE!");						
							$('#businessLineId').val(ipcbusinessLine);
							$('#bookingCustTxt').val(ipcbookingCustomer);
							
						$('#bkCustomerTxt_BK').val(ipcbookingCustomer);
						$('#bkCustomerTxt_claim').val(ipcbookingCustomer);
						$('#bkCustomerTxt_JOB').val(ipcbookingCustomer);
						$('#bkCustomerTxt_si').val(ipcbookingCustomer);							
											   
						}
                   }
                });        
                					
			
			}
				else{
					console.log("ELSE HIDDEN IPC value is : "+hiddenIPCValue)
				}
				   
			 }
		});
		
		
		function disableFieldsBySystemLine(systemLine){
		  switch(systemLine){
		   case "OFS" :
						$(".enabledByOFS").attr('enabled','enabled'); 
						break;
		   case "TMS" :
						$(".enabledByOFS").attr('enabled','enabled'); 
						$('.siNoDMSSearchPopup').unbind("click");
						break;
		   case "YMS" :
						
						break;
		  }
		}

		// systemLineOpt,documentLineOpt,documentTypeOpt,bookingNoTxt,jobNoTxt,siNoTxt,claimNoTxt,invoiceNoTxt,quotationNoTxt,poNoTxt,doNoTxt,loadNoTxt,bookingCustTxt,uploadDateFromTxt,uploadDateToTxt
$('.searchBtnDMS').on('click', function(e){
     var systemLine=$('#systemLineOpt').val();
	 var documentLineVal=$('#documentLineOpt').combobox('getValue');
	 var documentLineTxt=$('#documentLineOpt').combobox('getText');
	  var documentTypeVal=$('#documentTypeOpt').combobox('getValue');
	 var documentTypeTxt=$('#documentTypeOpt').combobox('getText');
	 
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
	 
	 var uploadDateFrom=$('#uploadDateFromTxt').datebox('getValue');
	 var uploadDateTo=$('#uploadDateToTxt').datebox('getValue');
	console.log(":: search DMS : [(systemLine :"+systemLine+"),(documentLineVal:"+documentLineVal+"),(documentLineTxt:"+documentLineTxt+"),(documentTypeVal:"+documentTypeVal
	+"),(documentTypeTxt:"+documentTypeTxt+"),(bookingNo:"+bookingNo+"),(jobNo:"+jobNo+"),(siNo:"+siNo+"),(claimNo:"+claimNo+"),(shpmentNo:"+shipmentNo+"),(invoiceNo:"+invoiceNo+"),(quotationNo:"+
	quotationNo+"),(poNo:"+poNo+"),(doNo:"+doNo+"),(loadNo:"+loadNo+"),(bookingCustomer:"+bookingCustomer+"),(uploadDateFrom:"+uploadDateFrom+"),(uploadDateTo:"+uploadDateTo+")]");
    var urlParam={ 
					DMS_DOCUMENT_LINE : documentLineVal, 
					DMS_DOCUMENT_TYPE: documentTypeVal,
					DMS_BOOKING_NO: bookingNo,
					DMS_JOB_NO: jobNo,
					DMS_SI_NO: siNo,
					DMS_CLAIM_NO: claimNo,
					DMS_BOOKING_CUSTOMER: bookingCustomer,
					};
	 
					$.ajax({
								beforeSend: function () { },  
								type : "POST",                    
								async : true,
								url : '<portlet:resourceURL id="DMS_SEARCH_DCUMENTS" />',
								data: urlParam, 
								datatype : "json",                    
							   success: function(results)   
							   {
								
							     console.log(" result  : is : "+results);    
								    if(results!=null){
										results = "AJAX_SESSION_EXPIRED";
										validateSession(results);     
									   
										if(results.indexOf("Exception") > -1){
											console.log("Search EXCPETION  :  "+results);
											showExceptionMessege(results);
										}else		
										if(results.length==0){
												showExceptionMessege(results);
												console.log("No result return : length is : "+results.length);
										   }else{
											   console.log("result return : length is : "+results.length+" results : "+results);
												console.log("StringFy Results : "+JSON.stringify(results));
												reloadResultDataOnGrid('dmsSearchTbldg',results);
											}
										
							     }//NOT NULL
							   }
							});
						
		});
		
/***
 * This common function is used to enable gird and filter
 * @param enableGridID
 */
function enablingGridAndFilter(enableGridID){
	var dg = $('#'+enableGridID).datagrid();
      dg.datagrid('enableFilter', []); 
	  $('#'+enableGridID).datagrid('loadData', {"total":0,"rows":[]});
}

/*** 
 * This method is used to load the Grid Results data and enabling filter 
 * @param reloadGridID
 * @param loadResults  
 */
function reloadResultDataOnGrid(reloadGridID,loadResults)
{
	  $('#'+reloadGridID).datagrid('loadData', loadResults);
	  $('#'+reloadGridID).datagrid('removeFilterRule');
	  $('#'+reloadGridID).datagrid('enableFilter');
	  $('#'+reloadGridID).datagrid({
						data: loadResults,
						});
	  var dg = $('#'+reloadGridID).datagrid();
	    dg.datagrid('enableFilter', []);  
}


/***
 * This function is used to load the current date as format
 */
function loadTodayDate(){
	
	var d = new Date();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var output = (day<10 ? '0' : '') + day +'/'+ (month<10 ? '0' : '') + month + '/' + d.getFullYear();
return output;
}
 
 function enableDisableFun(){
	$('#fakeButton').removeClass('DisabledLink');
	$('#uploadButtonz').removeClass('DisabledLink');
 }
  
function clearBkCusFromBk(){
	$('#bookingCustTxt').val('');
	$('#bkCustomerTxt_BK').val('');
	$('#bkCustomerTxt_claim').val('');
	$('#bkCustomerTxt_claim').val('');
	$('#bkCustomerTxt_JOB').val('');
	$('#bkCustomerTxt_si').val('');
} 
function clearBkNoFromBkNo(){
	$('#bookingNoTxt').val('');
	$('#bkNoTxt_claim').val('');
	$('#bkNoTxt_JOB').val('');
	$('#bkNoTxt_si').val('');
}
function clearJbNoFromJbNo(){
	$('#jobNoTxt').val('');
}
function clearSiNoFromSiNo(){
	$('#siNoTxt').val('');
}  
function clearClaimNoFromClaimNo(){
	$('#claimNoTxt').val('');
}
  
$('#dg').datagrid({
    rowStyler:function(index,row){
            return 'background-color:white;color:black';
    }
});  
  
function showExceptionMessege(results)
 {
	var messageType="No Results..";
      console.log("showExceptionMessege : Results ::: "+results);
      var errorMessage = results.substring(10);
	  if(errorMessage == SEARCH_DOCUMENT_EXCEPTION){
		messageType="Exception in processing search documents ";
	  }else
	  if(errorMessage == VIEW_DOCUMENT_EXCEPTION){
		messageType="Exception in processing view documents";
	  }else
	  if(errorMessage == DOWNLOAD_DOCUMENT_EXCEPTION){
		messageType="Exception in processing download documents";
	  }else
	  if(errorMessage == DELETE_DOCUMENT_EXCEPTION){
		messageType="Exception in processing delete documents";
	  }else
	  if(errorMessage == NO_DOCUMENT_FOUND_ERROR){
		messageType="Doument are not available, to perform operation";
	  }
       $.messager.alert('Message',messageType,'info');
 }				
</script> 