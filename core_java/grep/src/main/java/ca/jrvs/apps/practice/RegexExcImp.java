package ca.jrvs.apps.grep;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    /**
     * Return true if filename extension is jpg or jpeg (case insensitive)
     */
    public boolean matchJpeg(String filename) {
        if (filename == null) return false;
        // Regex: ends with .jpg or .jpeg, case insensitive
        return Pattern.compile("(?i)^.*\\.(jpg|jpeg)$").matcher(filename).matches();
    }

    /**
     * Return true if IP is valid
     * IP address simplified from 0.0.0.0 to 999.999.999.999
     */
    public boolean matchIp(String ip) {
        if (ip == null) return false;
        // Regex: four groups of 1-3 digits separated by dots
        return Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$").matcher(ip).matches();
    }

    /**
     * Return true if line is empty (empty, whitespace, tabs, etc.)
     */
    public boolean isEmptyLine(String line) {
        if (line == null) return true;
        // Regex: zero or more whitespace characters
        return Pattern.compile("^\\s*$").matcher(line).matches();
    }

    // Optional: main method to test
    public static void main(String[] args) {
        RegexExc regex = new RegexExcImp();

        // Test matchJpeg
        System.out.println(regex.matchJpeg("test.jpg"));      // true
        System.out.println(regex.matchJpeg("image.JPEG"));    // true
        System.out.println(regex.matchJpeg("doc.txt"));       // false

        // Test matchIp
        System.out.println(regex.matchIp("192.168.1.1"));     // true
        System.out.println(regex.matchIp("999.999.999.999")); // true
        System.out.println(regex.matchIp("256.256.256"));     // false

        // Test isEmptyLine
        System.out.println(regex.isEmptyLine(""));            // true
        System.out.println(regex.isEmptyLine("   "));         // true
        System.out.println(regex.isEmptyLine("\t\n"));        // true
        System.out.println(regex.isEmptyLine("text"));        // false
    }
}
