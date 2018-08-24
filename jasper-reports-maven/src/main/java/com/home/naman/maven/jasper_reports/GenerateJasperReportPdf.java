/**
 * 
 */
package com.home.naman.maven.jasper_reports;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author naman
 * 
 */
public class GenerateJasperReportPdf {

	public static void main(String[] args) throws Exception {

		JasperReport jsReport = JasperCompileManager
				.compileReport("C:\\Naman\\Git-Repo\\master\\jasper-reports-maven\\jasper-xml\\FirstJasperReport.jrxml");
		JRDataSource jrDataSrc = new JREmptyDataSource();
		Map<String,Object> parameters=new HashMap<>();
		parameters.put("companyName", "Giga");
		parameters.put("name", "OFS");
		parameters.put("address", "Carnauba Autospa,Shah Alam, Malaysia");
		JasperPrint jprint=JasperFillManager.fillReport(jsReport, parameters, jrDataSrc);
		JasperExportManager.exportReportToPdfFile(jprint, "C:\\Naman\\Git-Repo\\master\\jasper-reports-maven\\jasper-pdf\\FirstJasperReport.pdf");
	}

}
