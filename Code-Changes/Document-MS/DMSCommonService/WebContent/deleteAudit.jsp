<%@page import="com.giga.dms.util.CEManager"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="tcal.css" />
<script type="text/javascript" src="tcal.js"></script>

<link rel="stylesheet" href="./css/styles.css" />
<link rel="stylesheet" href="./css/jquery-ui-1.8.7.custom.css" />
<link rel="stylesheet"	href="./css/demo_table_jui.css" />
<script src="./js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript"	src="./js/jquery.blockUI.js"></script>
<script type="text/javascript"	src="./js/jquery.dataTables.min.js"></script>    
<script type="text/javascript"	src="./js/jquery-ui-1.8.7.custom.min.js"></script>		    

<script>
$(document).ready(function() {

$('#previousFeedbackTable').dataTable({
				"bJQueryUI": true,
				"iDisplayLength":100,
				"bPaginate":true,
				"bSortable":true,
				"sPaginationType": "full_numbers"
			});
});
</script>

</head>


<body>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="com.filenet.api.core.*"%>
<%@ page import="javax.security.auth.Subject"%>
<%@ page import="com.filenet.api.util.UserContext"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.filenet.api.collection.*"%>
<%@ page import="com.filenet.api.constants.*"%>
<%@ page import="com.filenet.api.query.*"%>
<%@ page import="com.filenet.api.events.*"%>
<%@ page import="com.filenet.api.property.*"%>
<%@ page import="com.giga.dms.util.*"%>
<br/>
<%!
public String getPropertyValue(String value)
{
	return value!=null?value:"";
}
%>
<%
try
{
	String rows="100";//default
	if(request.getParameter("rows")!=null)
	{
		rows=request.getParameter("rows");
	}
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	String ceURL = PropertyReader.get("ceURL");
	CEManager cm=CEManager.getInstance(ceURL,"P8admin","Password01");
	Domain domain = cm.getDomain();
	//out.println("Name of the domain: "+ domain.get_Name());	
	ObjectStore objectStores[] = new ObjectStore[2];
	objectStores[0] = cm.getObjectStore(PropertyReader.get("OFS"));
	objectStores[1] = cm.getObjectStore(PropertyReader.get("TMS"));
	//out.println("Name of the objectstore: "+ store.get_Name());
	String sql1 = "select Top "+rows+" * from DeletionEvent";	
	sql1=sql1+" ORDER BY DateCreated desc";
	SearchScope search = new SearchScope(objectStores,MergeMode.UNION);
	SearchSQL searchSQL = new SearchSQL(sql1);
	EventSet events = (EventSet)search.fetchObjects(searchSQL,Integer.getInteger("50"), null, Boolean.valueOf(true));
	DeletionEvent devent;
	Iterator it = events.iterator();
    %>
	<div id="creatediv" align="center" style="width: 100%">
	<fieldset>
	<legend><span class="bsllegend">Deleted Documents List</span></legend>
	
	<table id="previousFeedbackTable" width="100%" align="center">
	<thead>
	<tr>
	<th><b>S.No</b></th>
	<th><b>File Name</b></th>
	<th><b>System Line</b></th>
	<th><b>Document Line</b></th>
	<th><b>Document Type</b></th>
	<th><b>Booking Customer</b></th>
	<th><b>Upload by</b></th>
	<th><b>Deleted by</b></th>
	<th><b>Deleted On (DD/MM/YYYY)</b></th>
	</tr>
	</thead>
	<tbody>
<%
	int i=1;
	while (it.hasNext())
	{
		devent =(DeletionEvent)it.next();
		String id=devent.get_SourceClassId().toString();
		IndependentObject io=devent.get_SourceObject();
		Document doc=(Document)io;
		Properties p=doc.getProperties();
		
		String documentName="NA";
		String systemLine="NA";
		String documentLine="NA";
		String documentType="NA";
		String bookingCustomer="NA";
		String uploadBy="NA";
		String deletedBy="NA";
		String datecreated ="NA";
		
		documentName=getPropertyValue(doc.get_Name());
		
		if(p.isPropertyPresent(ServiceConstants.SYSTEMLINE))
		{
			systemLine=getPropertyValue(p.getStringValue(ServiceConstants.SYSTEMLINE));
		}
		if(p.isPropertyPresent(ServiceConstants.DOCUMENTLINE))
		{
			documentLine=getPropertyValue(p.getStringValue(ServiceConstants.DOCUMENTLINE));
		}
		if(p.isPropertyPresent(ServiceConstants.DOCUMENTTYPE))
		{
			documentType=getPropertyValue(p.getStringValue(ServiceConstants.DOCUMENTTYPE));
		}
		if(p.isPropertyPresent(ServiceConstants.BOOKINGCUSTOMER))
		{
			bookingCustomer=getPropertyValue(p.getStringValue(ServiceConstants.BOOKINGCUSTOMER));
		}
		if(p.isPropertyPresent(ServiceConstants.UPLOADBY))
		{
			uploadBy=getPropertyValue(p.getStringValue(ServiceConstants.UPLOADBY));
		}
		if(p.isPropertyPresent(ServiceConstants.DELETEBY))
		{
			deletedBy=getPropertyValue(p.getStringValue(ServiceConstants.DELETEBY));
		}
		
		
		
		//deletedBy=devent.get_Creator();
		datecreated=formatter.format(devent.get_DateCreated());
		
		out.println("<tr><td>"+i+"</td>");
		out.println("<td>"+documentName+"</td>");
		out.println("<td>"+systemLine+"</td>");
		out.println("<td>"+documentLine+"</td>");
		out.println("<td>"+documentType+"</td>");
		out.println("<td>"+bookingCustomer+"</td>");
		out.println("<td>"+uploadBy+"</td>");
		out.println("<td>"+deletedBy+"</td>");
		out.println("<td>"+datecreated+"</td></tr>");
		
		i++;
	}
}
catch(Exception e)
{
	out.println(e);
}
/*
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm
a");
String datecreated = formatter.format(doc.get_DateCreated());
*/
%>
	</tbody>
</table>
</fieldset>
</div>
</body>
</html>