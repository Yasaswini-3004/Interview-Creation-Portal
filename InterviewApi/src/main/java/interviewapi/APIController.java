package interviewapi;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import database.MySQL;

@RestController
public class APIController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> login(final @RequestBody String input,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws Exception {
		JSONObject jsonInput = new JSONObject(input);
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = login(jsonInput);

		return new ResponseEntity<String>(jsonOutput.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/participants", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getParticipants(final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse) throws Exception {
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = getParticipants();

		return new ResponseEntity<String>(jsonOutput.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/createInterview", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> createInterview(final @RequestBody String input,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws Exception {
		JSONObject jsonInput = new JSONObject(input);
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = createInterview(jsonInput);

		return new ResponseEntity<String>(jsonOutput.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/editInterview", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> editInterview(final @RequestBody String input,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws Exception {
		JSONObject jsonInput = new JSONObject(input);
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = editInterview(jsonInput);

		return new ResponseEntity<String>(jsonOutput.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/interviews", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getInterviews(final HttpServletRequest httpRequest,
			final HttpServletResponse httpResponse) throws Exception {
		JSONObject jsonOutput = new JSONObject();

		jsonOutput = getInterviews();

		return new ResponseEntity<String>(jsonOutput.toString(), HttpStatus.OK);
	}

	private JSONObject login(JSONObject login) throws JSONException, SQLException, IOException {

		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();
		JSONObject response = new JSONObject();
		String query = "select * from users where Username=? and Password=?;";
		PreparedStatement ps = obj.prepareStatement(query, conn, login.getString("username"),
				login.getString("password"));
		ResultSet rs = obj.executeQuery(ps);
		if (rs.next()) {
			System.out.println("if");
			response.put("Status", "Success");
			response.put("UserId", rs.getInt("UserId"));
			response.put("Name", rs.getString("Name"));
			response.put("Username", rs.getString("Username"));
		} else {
			response.put("Status", "Fail");
			response.put("ErrorMessage", "Incorrect credentials");
		}
		obj.disconnect();
		return response;
	}

	private JSONObject getParticipants() throws JSONException, SQLException, IOException {

		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();
		JSONObject response = new JSONObject();
		JSONArray arr = new JSONArray();

		String query = "select * from participants";
		ResultSet rs = obj.executeQuery(query, conn);
		while (rs.next()) {
			JSONObject p = new JSONObject();

			p.put("id", rs.getInt("Id"));
			p.put("name", rs.getString("Name"));
			p.put("emailId", rs.getString("EmailId"));
			arr.put(p);

		}
		response.put("participants", arr);
		obj.disconnect();

		return response;
	}

	private JSONObject getInterviews() throws JSONException, SQLException, IOException {

		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();
		JSONObject response = new JSONObject();
		// JSONArray arr = new JSONArray();

		JSONArray arr1 = new JSONArray();
		String currentDateTime = getCurrentDateTime();
		String query = "select Id,Name,InterviewId,StartTime,EndTime from Participants inner join Interview where StartTime >= ? and Id = ParticipantId;";
		PreparedStatement ps = obj.prepareStatement(query, conn, currentDateTime);
		ResultSet rs = obj.executeQuery(ps);
		while (rs.next()) {
			JSONObject p = new JSONObject();
			p.put("PId", rs.getInt("Id"));

			p.put("PName", rs.getString("Name"));
			p.put("IId", rs.getInt("InterviewId"));
			p.put("IStartTime", rs.getString("StartTime"));
			p.put("IEndTime", rs.getString("EndTime"));

			arr1.put(p);
		}
		response.put("Interviews", arr1);
		obj.disconnect();

		return response;
	}

	private JSONObject createInterview(JSONObject jsonInput) throws IOException, SQLException {
		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();
		JSONObject response = new JSONObject();
		JSONArray arr = new JSONArray();
		String query = "INSERT INTO interview(ParticipantId,StartTime,EndTime)VALUES(?,?,?);";
		String participantIds = jsonInput.getString("participantIds");
		String[] ids = participantIds.split(",");
		int records = 0;
		for (int i = 0; i < ids.length; i++) {
			String checkQuery = "select * from interview where ParticipantId=? and StartTime=? and EndTime=?;";
			PreparedStatement ps = obj.prepareStatement(checkQuery, conn, ids[i], jsonInput.getString("startTime"),
					jsonInput.getString("endTime"));
			ResultSet rs = obj.executeQuery(ps);
			if (rs.next()) {
				arr.put(getParticipantName(Integer.parseInt(ids[i])));
			} else {
				ps = obj.prepareStatement(query, conn, ids[i], jsonInput.getString("startTime"),
						jsonInput.getString("endTime"));
				records += obj.executeUpdate(ps);
				sendEmail(getParticipantEmail(Integer.parseInt(ids[i])),getParticipantName(Integer.parseInt(ids[i])),jsonInput.getString("startTime"),
						jsonInput.getString("startTime"));

			}
		}

		if (records > 0) {
			response.put("Success", records + " interview(s) created");

		}
		if (arr.length() > 0) {
			response.put("Error", arr);
		}
		obj.disconnect();

		return response;

	}

	

	private JSONObject editInterview(JSONObject jsonInput) throws IOException, SQLException {
		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();
		JSONObject response = new JSONObject();
		String checkQuery = "select * from interview where ParticipantId=? and StartTime=? and EndTime=?;";
		PreparedStatement ps = obj.prepareStatement(checkQuery, conn, jsonInput.getInt("participantId"),
				jsonInput.getString("startTime"), jsonInput.getString("endTime"));
		ResultSet rs = obj.executeQuery(ps);
		if (rs.next()) {
			response.put("Error", getParticipantName(jsonInput.getInt("participantId")));
		} else {
			String query = "update interview set ParticipantId=?,StartTime=?,EndTime=? where InterviewId=?;";
			int records = 0;
			ps = obj.prepareStatement(query, conn, jsonInput.getInt("participantId"), jsonInput.getString("startTime"),
					jsonInput.getString("endTime"), jsonInput.getInt("InterviewId"));
			records = obj.executeUpdate(ps);

			if (records > 0) {
				response.put("Success", "Interview edited");

			}
		}
		obj.disconnect();

		return response;

	}
	
	private String getParticipantName(int id) throws IOException, SQLException {
		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();

		String query = "select Name from participants where Id=" + id + ";";
		ResultSet rs = obj.executeQuery(query, conn);
		if (rs.next()) {
			return rs.getNString("Name");
		}
		obj.disconnect();

		return "";
	}

	private String getParticipantEmail(int id) throws IOException, SQLException {
		MySQL obj = new MySQL();

		Connection conn = (Connection) obj.connect();

		String query = "select EmailId from participants where Id=" + id + ";";
		ResultSet rs = obj.executeQuery(query, conn);
		if (rs.next()) {
			return rs.getNString("EmailId");
		}
		obj.disconnect();

		return "";
	}

	private static String getPropertyValue(String property, String filename) {
		String propertyValue = "";
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			prop.load(input);

			propertyValue = prop.getProperty(property);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return propertyValue;
	}

	private static Session getSessionObject() {

		Properties props = new Properties();
		props.put("mail.smtp.auth", getPropertyValue("mail.smtp.auth", "email.properties"));
		props.put("mail.smtp.ssl.enable", getPropertyValue("mail.smtp.ssl.enable", "email.properties"));
		props.put("mail.smtp.host", getPropertyValue("mail.smtp.host", "email.properties"));
		props.put("mail.smtp.port", getPropertyValue("mail.smtp.port", "email.properties"));
		props.put("mail.smtp.from", getPropertyValue("from", "email.properties"));

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getPropertyValue("username", "email.properties"),
						getPropertyValue("password", "email.properties"));
			}
		};
		Session session = Session.getInstance(props, auth);
		return session;
	}

	private static void sendEmail(String emailId, String name, String st, String et) {

		String Subject = "Interview";
		String body = "Hello " + name + ",\r\n"  + "You have an interview from '" + st.split("T")[0] + " " + st.split("T")[1]  + "' to '" + et.split("T")[0] + " " + et.split("T")[1]  + "'. All the best.";

		try {

			Message message = new MimeMessage(getSessionObject());
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailId));
			message.setSubject(Subject);
			Multipart mp = new MimeMultipart();
			BodyPart bp = new MimeBodyPart();
			bp.setText(body);
			mp.addBodyPart(bp);
			message.setContent(mp);
			Transport.send(message);
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());

		}
		
	     

	}

	private static String getCurrentDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);

	}

}
