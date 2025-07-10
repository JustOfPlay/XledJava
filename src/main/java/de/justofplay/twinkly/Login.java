package de.justofplay.twinkly;

import org.json.JSONObject;

public class Login {
    private String _authentication_token;
    private String _authentication_token_expires_in;
    private String _challenge_response;
    private String _status_code;
    private String _status_message;

    public Login(String jsonString) {
        JSONObject obj = new JSONObject(jsonString);
        _authentication_token = obj.optString("authentication_token", "");
        if (_authentication_token.isEmpty()) {
            System.err.println("Warnung: authentication_token ist leer! Login-Response: " + jsonString);
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
                _status_message = "IInvalid argument value";
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
        }

    }

    public String getAuthenticationToken() { return _authentication_token; }
    public String getAuthenticationTokenExpiresIn() { return _authentication_token_expires_in; }
    public String getChallengeResponse() { return _challenge_response; }
    public String getStatusCode() { return _status_code; }
    public String getStatusMessage() { return _status_message;}
}