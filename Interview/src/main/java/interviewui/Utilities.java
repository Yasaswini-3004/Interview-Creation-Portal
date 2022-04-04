package interviewui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;

public class Utilities {

	public String getPropertyValue(String property, String filename) {
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

	public ServiceResponse sendServiceRequest(String jsonData, String methodType, String apiUrl,
			HttpServletRequest request) {

		ServiceResponse svcResponse = new ServiceResponse();
		try {
			jsonData = jsonData.replaceAll("&", "#26;").replaceAll("%", "#37;");
			URL url = new URL(request.getSession().getServletContext().getAttribute("URL") + apiUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
			conn.setRequestProperty("Content-Type", "application/json; utf-8");
			conn.setRequestProperty("Accept", "application/json");

			if (methodType.equalsIgnoreCase("get")) {
				conn.setRequestMethod("GET");
			} else if (methodType.equalsIgnoreCase("post")) {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				String jsonInputString = jsonData;

				try (OutputStream os = conn.getOutputStream()) {
					byte[] input = jsonInputString.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

			}else {}

			svcResponse.setResponseCode(conn.getResponseCode());
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				String replacedResponse = response.toString();

				replacedResponse = replacedResponse.replaceAll("#26;", "&").replaceAll("#37;", "%");

				svcResponse.setResponse(StringEscapeUtils.unescapeJava(replacedResponse));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svcResponse;
	}

}
