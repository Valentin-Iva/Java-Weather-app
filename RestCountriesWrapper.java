import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Wrapper class for the REST Countries API
 * Provides methods to fetch country data from https://restcountries.com
 */
public class RestCountriesWrapper {

    private static final String BASE_URL = "https://restcountries.com/v3.1";


    public String getCountryByName(String countryName) throws Exception {
        String encodedName = URLEncoder.encode(countryName, StandardCharsets.UTF_8.toString());
        String endpoint = BASE_URL + "/name/" + encodedName;
        return makeRequest(endpoint);
    }

    public String getCountryByName(String countryName, String fields) throws Exception {
        String encodedName = URLEncoder.encode(countryName, StandardCharsets.UTF_8.toString());
        String endpoint = BASE_URL + "/name/" + encodedName;
        if (fields != null && !fields.isEmpty()) {
            endpoint += "?fields=" + fields;
        }
        return makeRequest(endpoint);
    }


    public String getCountryByCode(String code) throws Exception {
        String endpoint = BASE_URL + "/alpha/" + code;
        return makeRequest(endpoint);
    }

    public String getCountryByCode(String code, String fields) throws Exception {
        String endpoint = BASE_URL + "/alpha/" + code;
        if (fields != null && !fields.isEmpty()) {
            endpoint += "?fields=" + fields;
        }
        return makeRequest(endpoint);
    }


    public String getCountriesByLanguage(String language) throws Exception {
        String endpoint = BASE_URL + "/lang/" + language;
        return makeRequest(endpoint);
    }

    public String getCountriesByLanguage(String language, String fields) throws Exception {
        String endpoint = BASE_URL + "/lang/" + language;
        if (fields != null && !fields.isEmpty()) {
            endpoint += "?fields=" + fields;
        }
        return makeRequest(endpoint);
    }

    public String getAllCountries(String fields) throws Exception {
        String endpoint = BASE_URL + "/all";
        if (fields != null && !fields.isEmpty()) {
            endpoint += "?fields=" + fields;
        }
        return makeRequest(endpoint);
    }


    private String makeRequest(String endpoint) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Create URL and open connection
            URL url = new URL(endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 10 second timeout
            connection.setReadTimeout(10000);

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return null; // Return null for non-200 responses
            }

            // Read response
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();

        } catch (Exception e) {
            // Return null if any error occurs
            return null;
        } finally {
            // Clean up resources
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
}
