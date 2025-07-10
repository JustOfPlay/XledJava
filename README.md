# XledJava

XledJava is a Java library for controlling Twinkly LED devices via their official HTTP API.  
It provides a simple object-oriented interface to authenticate, query device information, and control LEDs (color, brightness, mode, etc.) programmatically.

---

## Features

- **Login/Logout**: Authenticate with your Twinkly device and manage sessions.
- **Device Info**: Query device details such as product name, firmware version, hardware info, and more.
- **LED Control**: Set color (RGB/HSV), brightness, saturation, and mode.
- **Movie Control**: Set the current movie by ID.
- **Token Verification**: Verify authentication tokens as required by newer firmware.
- **Full HTTP Debug**: See raw HTTP requests and responses for troubleshooting.

---

## Requirements

- Java 17 or newer (Java 24 recommended)
- Maven (for building)
- Twinkly device with firmware 2.7.1 or newer (for full feature support)
- Device must be reachable in your network

---

## Installation

1. **Clone the repository**  
   ```
   git clone https://github.com/JustOfPlay/XledJava.git
   cd XledJava
   ```

2. **Build the library**  
   ```
   mvn clean package
   ```
   The resulting JAR will be in `target/XledJava-1.0.jar`.

3. **Add as a dependency**  
   - **Maven**: Install to your local repository with `mvn install` and add as a dependency in your project.
   - **Manual**: Add the JAR to your project's classpath.

---

## Usage

### Basic Example

```java
import de.justofplay.xled.Device;

public class Example {
    public static void main(String[] args) {
        String ip = "192.168.178.98"; // Replace with your device's IP
        Device device = new Device(ip);

        // Print device info
        System.out.println("Product: " + device.getProductName());
        System.out.println("Firmware: " + device.getFirmwareVersion());

        // Set LED color to red
        device.setColor(255, 0, 0);

        // Set brightness to 50%
        device.setBrightness(50);

        // Set mode to 'color'
        device.setMode(Mode.COLOR);

        // Logout
        device.logout();
    }
}
```

---

## API Overview

### Device

The `Device` class represents a Twinkly device and provides all main control methods.

#### Constructor

```java
Device device = new Device("192.168.178.98");
```

#### Main Methods

- `String getProductName()`
- `String getFirmwareVersion()`
- `String setColor(int red, int green, int blue)`
- `String setColorHSV(int hue, int saturation, int value)`
- `String setBrightness(int brightness)`
- `String setMode(String mode)`
- `String logout()`
- ...and many more (see JavaDoc in source)

### Login

The `Login` class represents authentication/session information.  
You do not need to use it directly; it is managed internally by `Device`.

### Requester

The `Requester` class provides low-level HTTP utilities.  
You do not need to use it directly unless you want to extend or debug the library.

---

## Advanced Usage

- **Get current color**:  
  `device.getColor()` returns the raw HTTP response with the current color.
- **Get/Set saturation**:  
  `device.setSaturation(128);`  
  `int sat = device.getSaturation();`
- **Set movie**:  
  `device.setCurrentMovie(1);`
- **Verify token**:  
  `device.verify();`

---

## Error Handling

- All methods return the raw HTTP response as a string.
- If the device returns an error (e.g., 401 Unauthorized), check your authentication and firmware version.
- For debugging, inspect the full HTTP request/response output.

---

## Development

- All code is documented with JavaDoc.
- Extend the `Device` class for more API endpoints as needed.
- Pull requests and issues are welcome!

---

## License

MIT License

---

## Credits

- [Twinkly Official API Documentation](https://xled-docs.readthedocs.io/)

---

## Contributors
