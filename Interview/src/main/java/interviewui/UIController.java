package interviewui;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView root(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("redirect:/login");
		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getloginPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {
		request.getSession().removeAttribute("Username");
		request.getSession().removeAttribute("UserId");

		ModelAndView mav = new ModelAndView("redirect:/login");
		return mav;
	}

	@RequestMapping(value = "/interviews", method = RequestMethod.GET)
	public ModelAndView getHomePage(ModelAndView mav, HttpServletRequest request) {
		if (request.getSession().getAttribute("Username") != null)
			mav.setViewName("interviews");
		else
			mav = new ModelAndView("redirect:/login");

		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView loginUser(@RequestParam("username") String username, @RequestParam("pwd") String password,
			ModelAndView mav, HttpServletRequest request) {

		Utilities utilities = new Utilities();

		try {

			JSONObject data = new JSONObject();
			data.put("username", username);
			data.put("password", password);
			ServiceResponse serviceResponse = utilities.sendServiceRequest(data.toString(), "POST", "/login", request);
			if (serviceResponse.getResponseCode() == 200) {
				JSONTokener tokener = new JSONTokener(serviceResponse.getResponse());
				JSONObject jsonValue = new JSONObject(tokener);
				if (jsonValue.getString("Status").equalsIgnoreCase("success")) {
					request.getSession().setAttribute("Username", jsonValue.getString("Username"));
					request.getSession().setAttribute("UserId", jsonValue.getInt("UserId"));

					mav = new ModelAndView("redirect:/interviews");

				} else {
					mav = new ModelAndView("login");
					mav.addObject("Error", jsonValue.getString("ErrorMessage"));

				}
				return mav;

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/getParticipants", method = RequestMethod.GET)
	@ResponseBody
	public String getParticipants(HttpServletRequest request) {

		Utilities utilities = new Utilities();

		try {

			ServiceResponse serviceResponse = utilities.sendServiceRequest("", "GET", "/participants", request);
			if (serviceResponse.getResponseCode() == 200) {

				return serviceResponse.getResponse().toString();

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/getInterviews", method = RequestMethod.GET)
	@ResponseBody
	public String getInterviews(HttpServletRequest request) {

		Utilities utilities = new Utilities();

		try {

			ServiceResponse serviceResponse = utilities.sendServiceRequest("", "GET", "/interviews", request);
			if (serviceResponse.getResponseCode() == 200) {

				return serviceResponse.getResponse().toString();

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/createInterview", method = RequestMethod.POST)
	@ResponseBody
	public String createInterview(@RequestParam("participantIds") String ids, @RequestParam("startT") String startTime,
			@RequestParam("endT") String endTime, HttpServletRequest request) {

		Utilities utilities = new Utilities();

		try {

			JSONObject data = new JSONObject();
			data.put("participantIds", ids);
			data.put("startTime", startTime);
			data.put("endTime", endTime);
			ServiceResponse serviceResponse = utilities.sendServiceRequest(data.toString(), "POST", "/createInterview",
					request);
			if (serviceResponse.getResponseCode() == 200) {

				return serviceResponse.getResponse().toString();

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/editInterview", method = RequestMethod.POST)
	@ResponseBody
	public String editInterview(@RequestParam("InterviewId") int id, @RequestParam("participantId") String Pid,
			@RequestParam("startT") String startTime, @RequestParam("endT") String endTime,
			HttpServletRequest request) {

		Utilities utilities = new Utilities();

		try {

			JSONObject data = new JSONObject();
			data.put("InterviewId", id);
			data.put("participantId", Pid);
			data.put("startTime", startTime);
			data.put("endTime", endTime);
			ServiceResponse serviceResponse = utilities.sendServiceRequest(data.toString(), "POST", "/editInterview",
					request);
			if (serviceResponse.getResponseCode() == 200) {

				return serviceResponse.getResponse().toString();

			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "";
	}

}
