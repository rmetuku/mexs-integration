package com.nousinfo.mexsintr.api;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nousinfo.mexsintr.bo.AppointmentBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityResponseBO;
import com.nousinfo.mexsintr.service.MexsCalendarService;

import microsoft.exchange.webservices.data.core.enumeration.property.MeetingResponseType;

@RestController
@RequestMapping("/${api.prefix}/v${api.version}/appointment")
public class MexsAppointmentController {

	@Autowired
	private MexsCalendarService mexsCalendarService;

	@PostMapping
	public String createAppointment(@RequestBody AppointmentBO appointmentBO) throws Exception {
		return mexsCalendarService.createAppointment(appointmentBO, null);
		
	}
	
	@GetMapping("/status")
	public Map<String, MeetingResponseType> getMeetingInvitationStatus(@RequestParam("id") String meetingId) throws Exception {
		return mexsCalendarService.getMeetingInvitationStatus(meetingId);
	}
	
	@PostMapping("/attendee/availability")
	public Collection<AttendeeAvailabilityResponseBO> getAvailability(@RequestBody AttendeeAvailabilityBO attendeeAvailability) throws Exception {
		return mexsCalendarService.getAvailability(attendeeAvailability);
	}
}
