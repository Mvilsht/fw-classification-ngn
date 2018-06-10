
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RulesReader {


    public static List<Rule> loadRules(Path rulesPath) throws Exception {
        List<Rule> ruleBase = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rulesPath.toString()))) {

            String currLine;
            String[] ruleParams;

            while ((currLine = br.readLine()) != null) {
                ruleParams = currLine.split("\\|", 3);
                if (ruleParams.length < 3) {
                    System.err.format("Invalid rule format: %s skipping..\n ", currLine);
                    continue;
                }
                final String username = ruleParams[0];
                final String ip = ruleParams[1];
                final String action = ruleParams[2];
                if (isValidRule(username, ip, action)) {
                    ruleBase.add(new Rule(UsernameFactory.createUsername(username),
                            IpFactory.createIp(ip),
                            RULE_ACTION.valueOf(action.toUpperCase())));
                }
                else {
                    throw new Exception("Invalid rule: " + currLine  ); //can also be ignored
                }
            }
        }

        return ruleBase;
    }

    //TODO can be changed to public and Unit tested
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static boolean isValidRule(String username, String ip, String action) {
        if (StringUtils.isBlank(username)) {
            System.err.println("invalid username");
            return false;
        }
        try {
            Inet4Address.getByName(ip);
        } catch (UnknownHostException e) {
            try {
                new SubnetUtils(ip);
            } catch (Exception e1) {
                System.err.println("invalid ip");
                return false;
            }
        }

        try {
            RULE_ACTION.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("invalid action");
            return false;
        }

        return true;

    }

}
