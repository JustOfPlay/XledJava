package de.justofplay.xled.utils;

import de.justofplay.xled.Login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for sending HTTP requests to Twinkly devices.
 */
public class Requester {

    /**
     * Sends a POST request with a JSON body and custom headers.
     * Returns the full HTTP response as a string (status, headers, body).
     *
     * @param url         The full URL to send the request to.
     * @param jsonContent The JSON body to send.
     * @param headers     The headers to include in the request.
     * @return The full HTTP response as a string.
     */
    public static String sendJsonPost(String url, String jsonContent, Map<String, String> headers) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("POST");

            boolean hasContentType = false;
            if (headers != null) {
                for (String key : headers.keySet()) {
                    if ("Content-Type".equalsIgnoreCase(key)) {
                        hasContentType = true;
                        break;
                    }
                }
            }
            if (!hasContentType) {
                connection.setRequestProperty("Content-Type", "application/json");
            }

            if (headers != null) {
                headers.forEach((key, value) -> connection.setRequestProperty(key, value));
            }
            connection.setDoOutput(true);

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (jsonContent != null && !jsonContent.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonContent.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = connection.getResponseCode();

            StringBuilder resp = new StringBuilder();
            resp.append("HTTP/1.1 ").append(responseCode).append(" ");
            String reason = switch (responseCode) {
                case 200 -> "OK";
                case 400 -> "Bad Request";
                case 401 -> "Unauthorized";
                case 404 -> "Not Found";
                case 500 -> "Internal Server Error";
                default -> "";
            };
            resp.append(reason).append("\n");

            for (Map.Entry<String, java.util.List<String>> entry : connection.getHeaderFields().entrySet()) {
                if (entry.getKey() != null) {
                    for (String value : entry.getValue()) {
                        resp.append(entry.getKey()).append(": ").append(value).append("\n");
                    }
                }
            }
            resp.append("\n");

            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
            }

            StringBuilder body = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                body.append(line);
            }
            in.close();

            resp.append(body);

            return resp.toString();
        } catch (java.net.SocketTimeoutException e) {
            return "Timeout: " + e.getMessage();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    /**
     * Sends a POST request with JSON body and authentication token (if available).
     *
     * @param path       The API path (e.g. "/xled/v1/login").
     * @param jsonContent The JSON body to send.
     * @param _login     The Login object containing the authentication token.
     * @param _ip        The device IP address.
     * @return The full HTTP response as a string.
     */
    public static String postWithAuth(String path, String jsonContent, Login _login, String _ip) {
        String url = "http://" + _ip + path;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        if (_login != null && _login.getAuthenticationToken() != null && !path.equals("/xled/v1/login")) {
            headers.put("X-Auth-Token", _login.getAuthenticationToken());
        }
        return Requester.sendJsonPost(url, jsonContent, headers);
    }

    /**
     * Sends a GET request with an authentication token.
     *
     * @param url   The full URL to send the request to.
     * @param token The authentication token to include in the "X-Auth-Token" header.
     * @return The full HTTP response as a string.
     */
    public static String sendGetWithToken(String url, String token) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("X-Auth-Token", token);
            }
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            StringBuilder resp = new StringBuilder();
            resp.append("HTTP/1.1 ").append(responseCode).append(" ");
            String reason = switch (responseCode) {
                case 200 -> "OK";
                case 400 -> "Bad Request";
                case 401 -> "Unauthorized";
                case 404 -> "Not Found";
                case 500 -> "Internal Server Error";
                default -> "";
            };
            resp.append(reason).append("\n");

            for (Map.Entry<String, java.util.List<String>> entry : connection.getHeaderFields().entrySet()) {
                if (entry.getKey() != null) {
                    for (String value : entry.getValue()) {
                        resp.append(entry.getKey()).append(": ").append(value).append("\n");
                    }
                }
            }
            resp.append("\n");

            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"));
            }

            StringBuilder body = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                body.append(line);
            }
            in.close();

            resp.append(body);

            return resp.toString();
        } catch (java.net.SocketTimeoutException e) {
            return "Timeout: " + e.getMessage();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

}
