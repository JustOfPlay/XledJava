import de.justofplay.xled.Device;
import de.justofplay.xled.Mode;

public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    private static void printHttpResult(String label, String response) {
        if (response != null && response.contains("HTTP/1.1 200 OK")) {
            System.out.println(label + ANSI_GREEN + response + ANSI_RESET);
        } else {
            System.out.println(label + ANSI_RED + response + ANSI_RESET);
        }
    }

    public static void main(String[] args) {
        Device device = new Device(args[0]);

        System.out.println("----- GET DEVICE INFO -----\n");

        System.out.println("Product Name: " + device.getProductName());
        System.out.println("Hardware Version: " + device.getHardwareVersion());
        System.out.println("Bytes per LED: " + device.getBytesPerLed());
        System.out.println("Hardware ID: " + device.getHwId());
        System.out.println("Flash Size: " + device.getFlashSize());
        System.out.println("Led Type: " + device.getLedType());
        System.out.println("Product Code: " + device.getProductCode());
        System.out.println("Fw Family: " + device.getFwFamily());
        System.out.println("Device Name: " + device.getDeviceName());
        System.out.println("Uptime: " + device.getUptime());
        System.out.println("Mac Address: " + device.getMac());
        System.out.println("UUID: " + device.getUuid());
        System.out.println("Max Supported LEDs: " + device.getMaxSupportedLeds());
        System.out.println("Number of LEDs: " + device.getNumberOfLed());
        System.out.println("Led Profile: " + device.getLedProfile());
        System.out.println("Frame Rate: " + device.getFrameRate());
        System.out.println("Measured Frame Rate: " + device.getMeasuredFrameRate());
        System.out.println("Movie Capacity: " + device.getMovieCapacity());
        System.out.println("Max Movies: " + device.getMaxMovies());
        System.out.println("Wire Type: " + device.getWireType());
        System.out.println("Copy Right: " + device.getCopyright());
        System.out.println("Device Info: " + device.getDeviceInfo());
        System.out.println("IP Address: " + device.getIp());
        System.out.println("Brightness: " + device.getBrightness());
        System.out.println("Color: " + device.getColor());
        System.out.println("Led Red: " + device.getLedRed());
        System.out.println("Led Green: " + device.getLedGreen());
        System.out.println("Led Blue: " + device.getLedBlue());
        System.out.println("Saturation: " + device.getSaturation());
        System.out.println("Mode: " + device.getMode());

        System.out.println("----- SET DEVICE INFO -----\n");

        printHttpResult("\nSETMODE: \n", device.setMode(Mode.COLOR));
        printHttpResult("\nSETBRIGHTNESS: \n", device.setBrightness(100));
        printHttpResult("\nSETCOLOR: \n", device.setColor(255, 0, 0));
        printHttpResult("\nSETSATURATION: \n", device.setSaturation(70));
        printHttpResult("\nSETDEVICENAME: \n", device.setDeviceName(device.getDeviceName()));
    }
}
