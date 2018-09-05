package com.nousinfo.mexsintr.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.nousinfo.mexsintr.bo.AppointmentBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityBO;
import com.nousinfo.mexsintr.bo.AttendeeAvailabilityResponseBO;

import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;

/**
 * 
 */

/**
 * @author raghuramulum
 *
 */

public class MexsCalendarServiceTest {
	
	MexsCalendarService service = null;
	
	@Before
	public void beforeTest() throws Exception {
		service = new MexsCalendarService("https://outlook.office365.com/EWS/Exchange.asmx", "XXXXXXXXX", "XXXXXXXXX");
//		service = new MexsCalendarService("https://mail.XXXXXXXXX.com/EWS/Exchange.asmx", "XXXXXXXXX", "XXX");
	}

	@Test
	public void testSendMessage() throws Exception {
		service.sendMessage("raghuramulum@nousinfo.com", "Test", "Sample Body Message");
	}
	
	@Test
	public void testAvailability() throws Exception {
		List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
		attendees.add(new AttendeeInfo("raghuramulum@nousinfo.com"));
		attendees.add(new AttendeeInfo("raghu.metuku@gmail.com"));
		attendees.add(new AttendeeInfo("sukeshnambiar@nousinfo.com"));
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		AttendeeAvailabilityBO attendeeAvailabilityBO = new AttendeeAvailabilityBO();
		attendeeAvailabilityBO.setAttendees(Arrays.asList("raghuramulum@nousinfo.com", "raghu.metuku@gmail.com", "mohanavelp@nousinfo.com"));
		attendeeAvailabilityBO.setStartTime(formatter.parse("2018/09/05"));
		attendeeAvailabilityBO.setEndTime(formatter.parse("2018/09/06"));
		Collection<AttendeeAvailabilityResponseBO> response = service.getAvailability(attendeeAvailabilityBO);
		System.out.println(response);
	}
	
	@Test
	public void testSendInvitation() throws Exception {
		SimpleDateFormat formatter = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AppointmentBO appointmentBO = new AppointmentBO("Let's Meet", "No Agenda", formatter.parse("2018-09-05 10:30:00"), formatter.parse("2018-09-05 11:30:00"), Arrays.asList("raghuramulum@nousinfo.com", "raghu.metuku@gmail.com"));
		service.createAppointment(appointmentBO, "D:\\Fitch\\SPE-CS5-Java.pdf");
	}
	
	
	@Test
	public void getMeetingInvitationStatus() throws Exception {
		service.getMeetingInvitationStatus("Id********");
	}
	
	public void updateAppointment() {
		
	}

}
