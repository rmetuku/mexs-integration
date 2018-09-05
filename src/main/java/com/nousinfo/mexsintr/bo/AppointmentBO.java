/**
 * 
 */
package com.nousinfo.mexsintr.bo;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * A bean holding the Appointment configurations.<BR>
 * Both start and end time should be in the Date format "yyyy-MM-dd HH:mm:ss"
 * 
 * @author raghuramulum
 *
 */

public class AppointmentBO {

	private String subject;
	private String body;
	@ApiModelProperty(example="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date startTime;
	@ApiModelProperty(example="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date endTime;
	private List<String> attendees;

	public AppointmentBO() {
	}
	
	public AppointmentBO(String subject, String body, Date startTime, Date endTime, List<String> attendees) {
		this.subject = subject;
		this.body = body;
		this.startTime = startTime;
		this.endTime = endTime;
		this.attendees = attendees;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the attendees
	 */
	public List<String> getAttendees() {
		return attendees;
	}

	/**
	 * @param attendees
	 *            the attendees to set
	 */
	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}

}
