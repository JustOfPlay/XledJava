package de.justofplay.twinkly;

import de.justofplay.twinkly.utils.Requester;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static de.justofplay.twinkly.utils.Requester.postWithAuth;
import static de.justofplay.twinkly.utils.Requester.sendGetWithToken;

/**
 * Represents a Twinkly device and provides methods to interact with it via the Twinkly API.
 */
public class Device {
    // Device IP address
    private String _ip;
    // Login/session information
    private Login _login;

    // Device information fields
    private String _prduct_name;
    private int _hardware_version;
    private int _bytes_per_led;
    private String _hw_id;
    private int _flash_size;
    private int _led_type;
    private String _product_code;
    private String _fw_family;
    private String _device_name;
    private long _uptime;
    private String _mac;
    private String _uuid;
    private int _max_supported_leds;
    private int _number_of_led;
    private String _led_profile;
    private int _frame_rate;
    private float _measured_frame_rate;
    private int _movie_capacity;
    private int _max_movies;
    private int _wire_type;
    private String _copyright;

    /**
     * Extracts the JSON body from a full HTTP response string.
     * @param httpResponse The full HTTP response as a string.
     * @return The JSON body as a string, or null if not found.
     */
    private String extractJsonBody(String httpResponse) {
        if (httpResponse == null) return null;
        int jsonStart = httpResponse.indexOf('{');
        if (jsonStart == -1) return null;
        return httpResponse.substring(jsonStart).trim();
    }

    /**
     * Constructs a Device object and initializes device info by logging in and querying the device.
     * @param ip The IP address of the Twinkly device.
     */
    public Device(String ip) {
        _ip = ip;
        String loginResponse = login();
        String loginJson = extractJsonBody(loginResponse);
        if (loginJson == null || !loginJson.trim().startsWith("{")) {
            _login = new Login("{}");
        } else {
            try {
                _login = new Login(loginJson);
            } catch (org.json.JSONException e) {
                _login = new Login("{}");
            }
        }
        String respone = getDeviceInfo();
        if (respone != null && !respone.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(respone);
                _prduct_name = obj.optString("product_name", "");
                _hardware_version = obj.optInt("hardware_version", 0);
                _bytes_per_led = obj.optInt("bytes_per_led", 0);
                _hw_id = obj.optString("hw_id", "");
                _flash_size = obj.optInt("flash_size", 0);
                _led_type = obj.optInt("led_type", 0);
                _product_code = obj.optString("product_code", "");
                _fw_family = obj.optString("fw_family", "");
                _device_name = obj.optString("device_name", "");
                _uptime = obj.optLong("uptime", 0);
                _mac = obj.optString("mac", "");
                _uuid = obj.optString("uuid", "");
                _max_supported_leds = obj.optInt("max_supported_led", 0);
                _number_of_led = obj.optInt("number_of_led", 0);
                _led_profile = obj.optString("led_profile", "");
                _frame_rate = obj.optInt("frame_rate", 0);
                _measured_frame_rate = (float) obj.optDouble("measured_frame_rate", 0.0);
                _movie_capacity = obj.optInt("movie_capacity", 0);
                _max_movies = obj.optInt("max_movies", 0);
                _wire_type = obj.optInt("wire_type", 0);
                _copyright = obj.optString("copyright", "");
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Getters for device information ---

    /** @return The product name of the device. */
    public String getProductName() {return _prduct_name;}
    /** @return The hardware version. */
    public int getHardwareVersion() {return _hardware_version;}
    /** @return The number of bytes per LED. */
    public int getBytesPerLed() {return _bytes_per_led;}
    /** @return The hardware ID. */
    public String getHwId() {return _hw_id;}
    /** @return The flash size. */
    public int getFlashSize() {return _flash_size;}
    /** @return The LED type. */
    public int getLedType() {return _led_type;}
    /** @return The product code. */
    public String getProductCode() {return _product_code;}
    /** @return The firmware family. */
    public String getFwFamily() {return _fw_family;}
    /** @return The device name. */
    public String getDeviceName() {return _device_name;}
    /** @return The uptime in seconds. */
    public long getUptime() {return _uptime;}
    /** @return The MAC address. */
    public String getMac() {return _mac;}
    /** @return The UUID. */
    public String getUuid() {return _uuid;}
    /** @return The maximum supported number of LEDs. */
    public int getMaxSupportedLeds() {return _max_supported_leds;}
    /** @return The number of LEDs. */
    public int getNumberOfLed() {return _number_of_led;}
    /** @return The LED profile. */
    public String getLedProfile() {return _led_profile;}
    /** @return The frame rate. */
    public int getFrameRate() {return _frame_rate;}
    /** @return The measured frame rate. */
    public float getMeasuredFrameRate() {return _measured_frame_rate;}
    /** @return The movie capacity. */
    public int getMovieCapacity() {return _movie_capacity;}
    /** @return The maximum number of movies. */
    public int getMaxMovies() {return _max_movies;}
    /** @return The wire type. */
    public int getWireType() {return _wire_type;}
    /** @return The copyright string. */
    public String getCopyright() {return _copyright;}

    /** @return The Login object containing authentication info. */
    public Login getLogin() {
        return _login;
    }

    /** @return The device IP address. */
    public String getIp() {
        return _ip;
    }

    /**
     * Sets the device IP address.
     * @param ip The new IP address.
     */
    public void setIp(String ip) {
        _ip = ip;
    }

    /**
     * Performs login to the device and returns the raw HTTP response.
     * @return The HTTP response as a string.
     */
    public String login() {
        String jsonContent = "{ \"challenge\": \"twinkly\" }";
        return postWithAuth("/xled/v1/login", jsonContent, _login, _ip);
    }

    /**
     * Gets device information (gestalt) as a JSON string.
     * @return The JSON body as a string.
     */
    public String getDeviceInfo() {
        return extractJsonBody(postWithAuth("/xled/v1/gestalt", "{}", _login, _ip));
    }

    /**
     * Gets the firmware version of the device.
     * @return The firmware version as a string.
     */
    public String getFirmwareVersion() {
        String response = postWithAuth("/xled/v1/fw/version", "{}", _login, _ip);
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optString("version", "");
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * Logs out from the device.
     * @return The HTTP response as a string.
     */
    public String logout() {
        verify();
        return postWithAuth("/xled/v1/logout", "{}", _login, _ip);
    }

    /**
     * Sets the brightness of the LEDs.
     * @param brightness Brightness value (0..100)
     * @return The HTTP response as a string.
     */
    public String setBrightness(int brightness) {
        verify();
        String jsonContent = "{\"mode\":\"enabled\",\"type\":\"A\",\"value\":"+ brightness +"}";
        return postWithAuth("/xled/v1/led/out/brightness", jsonContent, _login, _ip);
    }

    /**
     * Gets the current brightness value.
     * @return The brightness value (0..100)
     */
    public int getBrightness() {
        verify();
        String response = sendGetWithToken(_ip + "/xled/v1/led/out/brightness", _login.getAuthenticationToken());
        if (response.contains("404 Not Found")) {
            return 0;
        }
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optInt("value", 0);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Sets the LED color using RGB values.
     * @param red   Red component (0..255)
     * @param green Green component (0..255)
     * @param blue  Blue component (0..255)
     * @return The HTTP response as a string.
     */
    public String setColor(int red, int green, int blue) {
        verify();
        String jsonContent = "{\"red\":" + red + ",\"green\":" + green + ",\"blue\":" + blue + "}";
        return postWithAuth("/xled/v1/led/color", jsonContent, _login, _ip);
    }

    /**
     * Sets the LED color using HSV color space.
     *
     * @param hue        Hue component (0..359)
     * @param saturation Saturation component (0..255)
     * @param value      Value/Brightness component (0..255)
     * @return HTTP response as String
     */
    public String setColorHSV(int hue, int saturation, int value) {
        verify();
        String jsonContent = "{\"hue\":" + hue + ",\"saturation\":" + saturation + ",\"value\":" + value + "}";
        return postWithAuth("/xled/v1/led/color", jsonContent, _login, _ip);
    }

    /**
     * Gets the current LED color as a raw HTTP response.
     * @return The HTTP response as a string.
     */
    public String getColor() {
        verify();
        return sendGetWithToken(_ip + "/xled/v1/led/color", _login.getAuthenticationToken());
    }

    /**
     * Gets the red component of the current LED color.
     * @return The red value (0..255)
     */
    public int getLedRed() {
        String response = getColor();
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optInt("red", 0);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Gets the green component of the current LED color.
     * @return The green value (0..255)
     */
    public int getLedGreen() {
        String response = getColor();
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optInt("green", 0);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Gets the blue component of the current LED color.
     * @return The blue value (0..255)
     */
    public int getLedBlue() {
        String response = getColor();
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optInt("blue", 0);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Sets the saturation of the LEDs.
     * @param saturation Saturation value (0..255)
     * @return The HTTP response as a string.
     */
    public String setSaturation(int saturation) {
        verify();
        String jsonContent = "{\"mode\":\"enabled\",\"type\":\"A\",\"value\":"+ saturation +"}";
        return postWithAuth("/xled/v1/led/saturation", jsonContent, _login, _ip);
    }

    /**
     * Gets the current saturation value.
     * @return The saturation value (0..255)
     */
    public int getSaturation() {
        verify();
        String response = sendGetWithToken(_ip + "/xled/v1/led/out/saturation", _login.getAuthenticationToken());
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optInt("value", 0);
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Sets the operating mode of the LEDs.
     * @param mode The mode string (e.g., "movie", "color", etc.)
     * @return The HTTP response as a string.
     */
    public String setMode(String mode) {
        verify();
        String jsonContent = "{\"mode\":\"" + mode + "\"}";
        return postWithAuth("/xled/v1/led/mode", jsonContent, _login, _ip);
    }

    /**
     * Gets the current operating mode of the LEDs.
     * @return The mode string.
     */
    public String getMode() {
        verify();
        String response = sendGetWithToken(_ip + "/xled/v1/led/mode", _login.getAuthenticationToken());
        String json = extractJsonBody(response);
        if (json != null && !json.isEmpty()) {
            try {
                org.json.JSONObject obj = new org.json.JSONObject(json);
                return obj.optString("mode", "");
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * Sets the current movie by its ID.
     * @param movieId The movie ID.
     * @return The HTTP response as a string.
     */
    public String setCurrentMovie(int movieId) {
        verify();
        String jsonContent = "{\"id\":" + movieId + "}";
        return postWithAuth("/xled/v1/led/movies", jsonContent, _login, _ip);
    }

    /**
     * Verifies the authentication token with the device.
     * @return The HTTP response as a string.
     */
    public String verify() {
        String jsonContent = "{\"challenge-response\": \"" + _login.getChallengeResponse() + "\"}";
        return postWithAuth("/xled/v1/verify", jsonContent, _login, _ip);
    }


}
