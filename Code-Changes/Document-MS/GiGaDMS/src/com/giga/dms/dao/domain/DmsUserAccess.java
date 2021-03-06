package com.giga.dms.dao.domain;

// Generated 7 Jul, 2015 5:27:41 PM by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * DmsUserAccess generated by hbm2java
 */
@Entity
@Table(name = "DMS_USER_ACCESS", schema = "DMSTxn", catalog = "OFSDB", uniqueConstraints = @UniqueConstraint(columnNames = {
		"DMS_URL", "ROLE" }))
public class DmsUserAccess implements java.io.Serializable {
 
	private long duaId;
	private String dmsUrl;
	private String role;
	private String userName;
	private String password;
	private boolean isEncripted;
	private Date modifiedDate;

	public DmsUserAccess() {
	}

	public DmsUserAccess(long duaId, String dmsUrl, String role,
			String userName, String password, boolean isEncripted,
			Date modifiedDate) {
		this.duaId = duaId;
		this.dmsUrl = dmsUrl;
		this.role = role;
		this.userName = userName;
		this.password = password;
		this.isEncripted = isEncripted;
		this.modifiedDate = modifiedDate;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "DUA_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public long getDuaId() {
		return this.duaId;
	}

	public void setDuaId(long duaId) {
		this.duaId = duaId;
	}

	@Column(name = "DMS_URL", nullable = false, length = 256)
	public String getDmsUrl() {
		return this.dmsUrl;
	}

	public void setDmsUrl(String dmsUrl) {
		this.dmsUrl = dmsUrl;
	}

	@Column(name = "ROLE", nullable = false, length = 256)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "USER_NAME", nullable = false, length = 256)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", nullable = false, length = 256)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "IS_ENCRIPTED", nullable = false)
	public boolean isIsEncripted() {
		return this.isEncripted;
	}

	public void setIsEncripted(boolean isEncripted) {
		this.isEncripted = isEncripted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", nullable = false, length = 23)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}



