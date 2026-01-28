package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class JavaGrepLambdaImp implements JavaGrepLambda {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

    private String rootPath;
    private String regex;
    private String outFile;

    /**
     * Top-level workflow using Streams
     */
    @Override
    public void process() throws IOException {

        Pattern pattern = Pattern.compile(regex);

        try (Stream<File> files = listFiles(rootPath)) {

            Stream<String> matchedLines =
                    files.flatMap(this::readLines)
                            .filter(line -> pattern.matcher(line).find());

            writeToFile(matchedLines);

        } catch (RuntimeException e) {
            logger.error("Error during grep process", e);
            throw new IOException("Grep process failed", e);
        }
    }

    /**
     * Lazily traverse directory
     */
    @Override
    public Stream<File> listFiles(String rootDir) {
        try {
            return Files.walk(new File(rootDir).toPath())
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Unable to traverse directory: " + rootDir, e);
        }
    }

    /**
     * Lazily read file lines
     */
    @Override
    public Stream<String> readLines(File inputFile) {
        if (!inputFile.isFile()) {
            throw new IllegalArgumentException(
                    "Not a file: " + inputFile);
        }

        try {
            return Files.lines(inputFile.toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Unable to read file: " + inputFile, e);
        }
    }

    /**
     * Regex matching
     */
    @Override
    public boolean containsPattern(String line) {
        return Pattern.compile(regex).matcher(line).find();
    }

    /**
     * Write matched lines to output file
     */
    @Override
    public void writeToFile(Stream<String> lines) throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(outFile))) {

            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (RuntimeException e) {
            throw new IOException("Failed writing output file", e);
        }
    }

    // ===== Getters & Setters =====

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException(
                    "USAGE: JavaGrep regex rootPath outFile"
            );
        }

        // Use default logger config
        BasicConfigurator.configure();

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }
}
