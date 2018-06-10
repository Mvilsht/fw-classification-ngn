
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FwRunner {


    public int run(String[] args){
        if (args.length < 2) {
            System.err.println("Invalid input. need AT LEAST 2 params");
            return 1;
        }

        Path rulesPath;
        Path hostsFolderPath;
        try {
            rulesPath = Paths.get(args[0]);
            hostsFolderPath = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Invalid input. can't load given files paths");
            return 1;
        }

        if (!Files.exists(rulesPath)) {
            System.err.println("Invalid File input. invalid RULES PATH access permissions");
            return 1;
        }
        if (!Files.exists(hostsFolderPath) || !Files.isDirectory(hostsFolderPath)) {
            System.err.println("Invalid Dir input. invalid HOSTS PATH access permissions");
            return 1;
        }


        //RulesReader rulesReader = new RulesReader();
        final List<Rule> ruleBase;
        try {
            ruleBase = RulesReader.loadRules(rulesPath);
        } catch (Exception e) {
            System.err.format("Invalid input. invalid Rulebase configuration, %s bla bla...", e.getMessage());
            return 1;
        }

        System.out.println("------------------------------  RuleBase START ------------------------------");
        for (Rule rule : ruleBase) {
            System.out.println(rule);
        }
        System.out.println("------------------------------  RuleBase END ------------------------------");

        PacketProcessor packetProcessor = new PacketProcessor(ruleBase, hostsFolderPath);
        try {
            packetProcessor.processPackets();
        } catch (Exception e) {
            System.err.println("Failed to process with ex: " + e.getMessage());
            return 1;
        }

        return 0;
    }
}
