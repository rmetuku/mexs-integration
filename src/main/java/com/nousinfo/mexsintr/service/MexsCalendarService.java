/**
 * 
 */
package com.nousinfo.mexsintr.service;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nousinfo.mexsintr.bo.AppointmentBO;
import com.nousinfo.mexsintr.bo.AppointmentUpdateBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityResponseBO;
import com.nousinfo.mexsintr.bo.EmailMessageBO;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.misc.error.ServiceError;
import microsoft.exchange.webservices.data.core.enumeration.property.MeetingResponseType;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.response.AttendeeAvailability;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;
import microsoft.exchange.webservices.data.property.complex.availability.Suggestion;
import microsoft.exchange.webservices.data.property.complex.availability.TimeSuggestion;

/**
 * @author raghuramulum
 *
 */
@Service
public class MexsCalendarService {

	private ExchangeService service = null;

	/**
	 * @param emailAddress
	 * @param password
	 * @throws Exception
	 */
	public MexsCalendarService(@Value("${mex.server.uri}") String exServerURI,
			@Value("${mex.server.cred.email}") String emailAddress, @Value("${mex.server.cred.pwd}") String password)
			throws Exception {
		service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(emailAddress, password);
		service.setCredentials(credentials);
		service.setUrl(new URI(exServerURI));
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Collection<AttendeeAvailabilityResponseBO> getAvailability(AttendeeAvailabilityBO attendeeAvailabilityBO)
			throws Exception {
		// Create a list of attendees for which to request availability
		// information and meeting time suggestions.
		Map<Integer, AttendeeAvailabilityResponseBO> attendeeAvailabilityRespose = new HashMap<>();
		int attendeeIndex = 0;
		for (String attendee : attendeeAvailabilityBO.getAttendees()) {
			attendeeAvailabilityRespose.put(attendeeIndex, new AttendeeAvailabilityResponseBO(attendee));
			attendeeIndex++;
		}
		// Call the availability service.
		GetUserAvailabilityResults results = service.getUserAvailability(
				transformToAttendeeInfo(attendeeAvailabilityBO.getAttendees()),
				new TimeWindow(attendeeAvailabilityBO.getStartTime(), attendeeAvailabilityBO.getEndTime()),
				AvailabilityData.FreeBusyAndSuggestions);

		// Output attendee availability information.
		attendeeIndex = 0;

		for (AttendeeAvailability attendeeAvailability : results.getAttendeesAvailability()) {
			attendeeAvailabilityRespose.get(attendeeIndex).setAttendeeAvailability(attendeeAvailability);
			if (attendeeAvailability.getErrorCode() == ServiceError.NoError) {
				for (CalendarEvent calendarEvent : attendeeAvailability.getCalendarEvents()) {
					System.out.println("Calendar event");
					System.out.println("  Start time: " + calendarEvent.getStartTime().toString());
					System.out.println("  End time: " + calendarEvent.getEndTime().toString());

					if (calendarEvent.getDetails() != null) {
						System.out.println("  Subject: " + calendarEvent.getDetails().getSubject());
						// Output additional properties.
					}
				}
			}
			System.out.println("************");
			attendeeIndex++;
		}
		attendeeIndex = 0;
		// Output suggested meeting times.
		for (Suggestion suggestion : results.getSuggestions()) {
			attendeeAvailabilityRespose.get(attendeeIndex).setSuggestion(suggestion);
			System.out.println("Suggested day: " + suggestion.getDate().toString());
			System.out.println("Overall quality of the suggested day: " + suggestion.getQuality().toString());

			for (TimeSuggestion timeSuggestion : suggestion.getTimeSuggestions()) {
				System.out.println("  Suggested time: " + timeSuggestion.getMeetingTime().toString());
				System.out.println("  Suggested time quality: " + timeSuggestion.getQuality().toString());
				// Output additonal properties.
			}
			attendeeIndex++;
		}
		return attendeeAvailabilityRespose.values();
	}

	/**
	 * @param attendees
	 * @return
	 */
	private List<AttendeeInfo> transformToAttendeeInfo(List<String> attendees) {
		List<AttendeeInfo> attendeeInfo = new ArrayList<AttendeeInfo>();
		for (String attendee : attendees) {
			attendeeInfo.add(new AttendeeInfo(attendee));
		}

		return attendeeInfo;
	}

	/**
	 * @param attendees
	 * @param appointmentBO
	 * @throws Exception
	 */
	public String createAppointment(AppointmentBO appointmentBO, MultipartFile... attachments) throws Exception {
		Appointment appointment = createAppointment(appointmentBO);
		System.out.println("AppId: " + appointment.getId().toString());
		if (null != attachments && attachments.length > 0) {
			for (MultipartFile file : attachments) {
				appointment.getAttachments().addFileAttachment(file.getOriginalFilename(), file.getInputStream());
			}
		}
		for (String attendee : appointmentBO.getAttendees()) {
			appointment.getRequiredAttendees().add(attendee);
		}
		appointment.update(ConflictResolutionMode.AutoResolve);
		return appointment.getId().toString();
	}

	/**
	 * @param toEmailAddress
	 * @param subject
	 * @param body
	 * @throws Exception
	 */
	public void sendMessage(List<EmailMessageBO> emailMessages) throws Exception {
		for (EmailMessageBO message : emailMessages) {
			sendMessage(message.getToAddress(), message.getSubject(), message.getBody());
		}
	}

	/**
	 * @param toEmailAddress
	 * @param subject
	 * @param body
	 * @throws Exception
	 */
	public void sendMessage(String toEmailAddress, String subject, String body) throws Exception {
		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(subject);
		msg.setBody(MessageBody.getMessageBodyFromText(body));
		msg.getToRecipients().add(toEmailAddress);
		msg.send();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Map<String, MeetingResponseType> getMeetingInvitationStatus(String meetingId) throws Exception {
		Appointment appointment = Appointment.bind(service, new ItemId(meetingId));
		Map<String, MeetingResponseType> attendeeStatus = new HashMap<>();
		for (Attendee attendee : appointment.getRequiredAttendees()) {
			System.out.println(String.format("Attendee: %s meeting status: %s ", attendee.getAddress(),
					attendee.getResponseType()));
			attendeeStatus.put(attendee.getAddress(), attendee.getResponseType());

		}
		return attendeeStatus;
	}

	/**
	 * @param appointmentUpdateBO
	 * @throws Exception
	 */
	public String updateAppointment(AppointmentUpdateBO appointmentUpdateBO) throws Exception {
		Appointment appointment = Appointment.bind(service, new ItemId(appointmentUpdateBO.getMeetingId()));
		if (appointmentUpdateBO.getStartTime() != null) {
			appointment.setStart(appointmentUpdateBO.getStartTime());
		}
		if (appointmentUpdateBO.getEndTime() != null) {
			appointment.setEnd(appointmentUpdateBO.getEndTime());
		}
		if (!CollectionUtils.isEmpty(appointmentUpdateBO.getAttendees())) {
			for (String attendee : appointmentUpdateBO.getAttendees()) {
				appointment.getRequiredAttendees().add(attendee);
			}

		}
		if (!StringUtils.isEmpty(appointmentUpdateBO.getSubject())) {
			appointment.setSubject(appointmentUpdateBO.getSubject());
		}
		if (!StringUtils.isEmpty(appointmentUpdateBO.getBody())) {
			appointment.setBody(MessageBody.getMessageBodyFromText(appointmentUpdateBO.getBody()));
		}
		appointment.update(ConflictResolutionMode.AutoResolve);
		return appointment.getId().toString();
	}

	/**
	 * @param meetingId
	 * @param cancellationMessage
	 * @throws Exception
	 */
	public void cancelAppointment(String meetingId, String cancellationMessage) throws Exception {
		Appointment appointment = Appointment.bind(service, new ItemId(meetingId));
		appointment.cancelMeeting(cancellationMessage);
	}

	/**
	 * @param appointmentBO
	 * @return
	 * @throws Exception
	 */
	private Appointment createAppointment(AppointmentBO appointmentBO) throws Exception {
		Appointment appointment = new Appointment(service);
		appointment.setSubject(appointmentBO.getSubject());
		appointment.setBody(MessageBody.getMessageBodyFromText(appointmentBO.getBody()));
		appointment.setStart(appointmentBO.getStartTime());
		appointment.setEnd(appointmentBO.getEndTime());
		appointment.save();
		return appointment;
	}

}
