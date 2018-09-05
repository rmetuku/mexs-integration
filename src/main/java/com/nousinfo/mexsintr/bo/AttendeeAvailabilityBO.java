/**
 * 
 */
package com.nousinfo.mexsintr.bo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author raghuramulum
 *
 */
public class AttendeeAvailabilityBO {

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="IST")
	@ApiModelProperty(example="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@ApiModelProperty(example="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="IST")
	private Date endTime;
	private List<String> attendees;
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
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
	 * @param endTime the endTime to set
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
	 * @param attendees the attendees to set
	 */
	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}
	
	
}
