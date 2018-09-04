/**
 * 
 */
package com.nousinfo.mexsintr.bo;

import microsoft.exchange.webservices.data.core.response.AttendeeAvailability;
import microsoft.exchange.webservices.data.property.complex.availability.Suggestion;

/**
 * @author raghuramulum
 *
 */
public class AttendeeAvailabilityResponseBO {

	private String attendee;
	private AttendeeAvailability attendeeAvailability;
	private Suggestion suggestion;
	
	/**
	 * 
	 */
	public AttendeeAvailabilityResponseBO() {
		
	}
	
	/**
	 * @param attendee
	 */
	public AttendeeAvailabilityResponseBO(String attendee) {
		this.attendee = attendee;
	}
	/**
	 * @return the attendee
	 */
	public String getAttendee() {
		return attendee;
	}
	/**
	 * @param attendee the attendee to set
	 */
	public void setAttendee(String attendee) {
		this.attendee = attendee;
	}
	/**
	 * @return the attendeeAvailability
	 */
	public AttendeeAvailability getAttendeeAvailability() {
		return attendeeAvailability;
	}
	/**
	 * @param attendeeAvailability the attendeeAvailability to set
	 */
	public void setAttendeeAvailability(AttendeeAvailability attendeeAvailability) {
		this.attendeeAvailability = attendeeAvailability;
	}
	/**
	 * @return the suggestion
	 */
	public Suggestion getSuggestion() {
		return suggestion;
	}
	/**
	 * @param suggestion the suggestion to set
	 */
	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}
	
	
}
