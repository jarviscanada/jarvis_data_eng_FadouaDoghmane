package ca.jrvs.apps.grep;

public interface RegexExc {
    boolean matchJpeg(String filename);
    boolean matchIp(String ip);
    boolean isEmptyLine(String line);
}

