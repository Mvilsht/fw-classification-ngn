import org.junit.Assert;
import org.junit.Test;


public class RuleTest {

    public static final String EXAMPLE_USERNAME = "kaki";
    public static final String EXAMPLE_USERNAME2 = "kaki2";
    private final Packet examplePacket = new Packet(UsernameFactory.createUsername(EXAMPLE_USERNAME), IpFactory.createIp("1.2.3.4"));

    @Test
    public void isMatchGoodUsernameGoodIp() {
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME), IpFactory.createIp("1.2.3.4"), RULE_ACTION.ALLOW);

        final boolean match = rule.isMatch(examplePacket);
        Assert.assertTrue("failed isMatchGoodUsernameGoodIp", match);

    }

    @Test
    public void isMatchBadUsernameGoodIp() {
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME2), IpFactory.createIp("1.2.3.4"), RULE_ACTION.ALLOW);

        final boolean match = rule.isMatch(examplePacket);
        Assert.assertFalse("failed isMatchBadUsernameGoodIp", match);
    }

    @Test
    public void isMatchGoodUsernameBadIp() {
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME), IpFactory.createIp("1.2.3.5"), RULE_ACTION.ALLOW);

        final boolean match = rule.isMatch(examplePacket);
        Assert.assertFalse("failed isMatchGoodUsernameBadIp", match);
    }

    @Test
    public void isMatchBadUsernameBadIp() {
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME2), IpFactory.createIp("1.2.3.5"), RULE_ACTION.ALLOW);

        final boolean match = rule.isMatch(examplePacket);
        Assert.assertFalse("failed isMatchBadUsernameBadIp", match);
    }

    @Test
    public void getActionGoodAllow() {
        //RULE_ACTION action = RULE_ACTION.valueOf("ALLOW");
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME2), IpFactory.createIp("1.2.3.5"), RULE_ACTION.ALLOW);

        final boolean allowed = rule.getAction().isAllowed();
        Assert.assertTrue("failed getActionGoodAllow", allowed);
    }


    @Test
    public void getActionGoodDeny() {
        //RULE_ACTION action = RULE_ACTION.valueOf("DENY");
        Rule rule = new Rule(UsernameFactory.createUsername(EXAMPLE_USERNAME2), IpFactory.createIp("1.2.3.5"), RULE_ACTION.DENY);

        final boolean deny = rule.getAction().isAllowed();
        Assert.assertFalse("failed getActionGoodDeny", deny);
    }
}