import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PacketProcessor {
    private static final String ALLOWED = "ALLOWED";
    private static final String DENIED = "DENIED";

    private final List<Rule> ruleBase;
    private final Path hostsFolderPath;

    public PacketProcessor(List<Rule> ruleBase, Path hostsFolderPath) {
        this.ruleBase = ruleBase;
        this.hostsFolderPath = hostsFolderPath;
    }

    public void processPackets() throws IOException {
        List<FileProcessTask> tasks = Files.walk(hostsFolderPath)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .map(FileProcessTask::new)
                .collect(Collectors.toList());

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (FileProcessTask task : tasks) {
            executorService.execute(task);
        }

        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("waiting termination interrupted");
        } finally  {
            executorService.shutdownNow();
        }
    }


    private class FileProcessTask implements Runnable {
        private final String fileName;

        private FileProcessTask(File file) {
            this.fileName = file.getAbsolutePath();
        }

        @Override
        public void run() {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {

                String currLine;
                String[] packetParams;

                while ((currLine = bufferedReader.readLine()) != null) {
                    packetParams = currLine.split("\\|", 2);
                    if (packetParams.length < 2) {
                        System.err.format("Invalid packet format: %s skipping..\n ", currLine);
                        continue;
                    }

                    final String username = packetParams[0];
                    final String ip = packetParams[1];
                    if (isValidPacket(username, ip)) {
                        //boolean packetWasMatched = false;
                        boolean match = false;
                        final Packet packet = new Packet(UsernameFactory.createUsername(username),IpFactory.createIp(ip));
                        System.out.println("matching: " + packet);
                        for (Rule rule : ruleBase) {
                            match = rule.isMatch(packet);
                            System.out.format("current match result : %s for %s \n", match ? "MATCH" : "NO_MATCH" ,packet);
                            //packetWasMatched |= match;
                            if (match) {
                                System.out.println(createLineForPacket(username, ip, rule.getAction().isAllowed() ? ALLOWED : DENIED));
                                break;
                            }
                        }
                        if (!match) {
                            System.out.println(createLineForPacket(username, ip, ALLOWED)); //default
                        }
                    }
                    else{
                        System.out.format("Invalid packet params: %s skipping.. \n", currLine);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to read " + e.getMessage());
            }
        }

        //TODO can be made public and valid for unit test
        @SuppressWarnings("ResultOfMethodCallIgnored")
        private boolean isValidPacket(String username, String ip) {
            if (StringUtils.isBlank(username)) {
                System.err.println("invalid username");
                return false;
            }

            try {
                Inet4Address.getByName(ip);
            } catch (UnknownHostException e) {
                System.err.println("invalid ip");
                return false;
            }

            return true;
        }

        private String createLineForPacket(String username, String ip, String action) {
            return String.format("%s: %s access to %s was %s", fileName, username, ip, action);
        }
    }

}
