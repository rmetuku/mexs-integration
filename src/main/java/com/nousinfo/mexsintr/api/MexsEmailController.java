/**
 * 
 */
package com.nousinfo.mexsintr.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nousinfo.mexsintr.bo.EmailMessageBO;
import com.nousinfo.mexsintr.service.MexsCalendarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author raghuramulum
 *
 */
@RestController
@RequestMapping("/${api.prefix}/v${api.version}/email")
@Api(tags= {"Email Management Service"})
public class MexsEmailController {
	
	@Autowired
	private MexsCalendarService mexsCalendarService;
	
	@PostMapping
	@ApiOperation("To send Emal messages")
	public void sendEmail(@RequestBody List<EmailMessageBO> emailMessages) throws Exception {
		mexsCalendarService.sendMessage(emailMessages);
	}

}
