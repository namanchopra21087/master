package com.giga.dms.controller;

/**
 *
 * A sample Java bean that stores portlet instance data in portlet session.
 *
 */
public class DMSDocUploadWebPortletSessionBean {
	
	/**
	 * Last text for the text form
	 */
	private String formText = "";

	/**
	 * Set last text for the text form.
	 * 
	 * @param formText last text for the text form.
	 */
	public void setFormText(String formText) {
		this.formText = formText;
	}

	/**
	 * Get last text for the text form.
	 * 
	 * @return last text for the text form
	 */
	public String getFormText() {
		return this.formText;
	}

}
