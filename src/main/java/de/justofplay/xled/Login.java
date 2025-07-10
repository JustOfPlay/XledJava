package de.justofplay.xled;

import org.json.JSONObject;

/**
 * Represents the login/session information for a Twinkly device.
 * Parses the authentication token and status from a JSON response.
 */
public class Login {
    /** The authentication token for API requests. */
    private String _authentication_token;
    /** The expiration time (in seconds) for the authentication token. */
    private String _authentication_token_expires_in;
    /** The challenge-response string returned by the device. */
    private String _challenge_response;
    /** The status code returned by the device. */
    private String _status_code;
    /** The status message mapped from the status code. */
    private String _status_message;

    /**
     * Constructs a Login object from a JSON string.
     * @param jsonString The JSON string containing login/session information.
     */
    public Login(String jsonString) {
        JSONObject obj = new JSONObject(jsonString);
        _authentication_token = obj.optString("authentication_token", "");
        if (_authentication_token.isEmpty()) {
            System.err.println("Warning: authentication_token is empty! Login-Response: " + jsonString);
        }
        _authentication_token_expires_in = String.valueOf(obj.optInt("authentication_token_expires_in", 0));
        _challenge_response = obj.optString("challenge-response", "");
        _status_code = String.valueOf(obj.optInt("code", 0));

        switch (_status_code) {
            case "1000":
                _status_message = "Ok";
                break;
            case "1001":
                _status_message = "Error";
                break;
            case "1101":
                _status_message = "Invalid argument value";
                break;
            case "1102":
                _status_message = "Error";
                break;
            case "1103":
                _status_message = "Error - value too long? Or missing required object key?";
                break;
            case "1104":
                _status_message = "Error - malformed JSON on input?";
                break;
            case "1105":
                _status_message = "Invalid argument key";
                break;
            case "1107":
                _status_message = "Ok?";
                break;
            case "1108":
                _status_message = "Ok?";
                break;
            case "1205":
                _status_message = "Error with firmware upgrade - SHA1SUM does not match";
                break;
            default:
                _status_message = null;
        }
    }

    /**
     * Gets the authentication token for API requests.
     * @return The authentication token string.
     */
    public String getAuthenticationToken() { return _authentication_token; }

    /**
     * Gets the expiration time (in seconds) for the authentication token.
     * @return The expiration time as a string.
     */
    public String getAuthenticationTokenExpiresIn() { return _authentication_token_expires_in; }

    /**
     * Gets the challenge-response string returned by the device.
     * @return The challenge-response string.
     */
    public String getChallengeResponse() { return _challenge_response; }

    /**
     * Gets the status code returned by the device.
     * @return The status code as a string.
     */
    public String getStatusCode() { return _status_code; }

    /**
     * Gets the status message mapped from the status code.
     * @return The status message as a string.
     */
    public String getStatusMessage() { return _status_message;}
}
