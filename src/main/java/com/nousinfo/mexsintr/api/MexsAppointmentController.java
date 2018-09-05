package com.nousinfo.mexsintr.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nousinfo.mexsintr.bo.AppointmentBO;
import com.nousinfo.mexsintr.bo.AppointmentUpdateBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityResponseBO;
import com.nousinfo.mexsintr.service.MexsCalendarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import microsoft.exchange.webservices.data.core.enumeration.property.MeetingResponseType;

@RestController
@RequestMapping("/${api.prefix}/v${api.version}/appointment")
@Api(tags= {"Appointment Management Service"})
public class MexsAppointmentController {

	@Autowired
	private MexsCalendarService mexsCalendarService;

	/*@RequestMapping(method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation("Create Appointment with Attachments")
	public String createAppointmentWithAttachments(
			@RequestPart("data") @ApiParam(value = "Appointment Details JSON in String format", required = true) String data,
			@RequestPart(name="file", required=false ) @ApiParam(value = "Attachment", required = false) MultipartFile[] attachments)
			throws Exception {
		return mexsCalendarService.createAppointment(readAppointment(data), attachments);

	}
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ApiOperation("Create an Appointment")
	public String createAppointment(
			@RequestBody @ApiParam(value = "Appointment Details JSON", required = true) AppointmentBO data)
			throws Exception {
		return mexsCalendarService.createAppointment(data, null);
	}

	@GetMapping("/status")
	@ApiOperation("Get Meeting acceptance status")
	public Map<String, MeetingResponseType> getMeetingInvitationStatus(@RequestParam("id") String meetingId)
			throws Exception {
		return mexsCalendarService.getMeetingInvitationStatus(meetingId);
	}

	@PostMapping("/attendee/availability")
	@ApiOperation("Get the Availability of Attendees")
	public Collection<AttendeeAvailabilityResponseBO> getAvailability(
			@RequestBody AttendeeAvailabilityBO attendeeAvailability) throws Exception {
		return mexsCalendarService.getAvailability(attendeeAvailability);
	}

	@PutMapping
	@ApiOperation("Update an Appointment")
	public String updateAppointment(@RequestBody AppointmentUpdateBO appointmentUpdateBO) throws Exception {
		return mexsCalendarService.updateAppointment(appointmentUpdateBO);
	}

	@DeleteMapping
	@ApiOperation("Cancel an Appointment")
	public void cancelAppointment(@RequestParam("id") String meetingId, @RequestParam("message") String message)
			throws Exception {
		mexsCalendarService.cancelAppointment(meetingId, message);
	}

	/**
	 * @param data
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private AppointmentBO readAppointment(String data) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(data, new TypeReference<AppointmentBO>() {
		});

	}
}
