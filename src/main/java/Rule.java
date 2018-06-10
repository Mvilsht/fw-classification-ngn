import java.util.Objects;

public class Rule {

    private Username username;
    private IP ip;
    private RULE_ACTION action;

    public Rule(Username username, IP ip, RULE_ACTION action) {
        this.username = username;
        this.ip = ip;
        this.action = action;
    }

    public Username getUsername() {
        return username;
    }

    public IP getIp() {
        return ip;
    }

    public RULE_ACTION getAction() {
        return action;
    }

    public boolean isMatch(Packet packet) {
        return username.isMatch(packet.getUsername()) && ip.isMatch(packet.getIp().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(username, rule.username) &&
                Objects.equals(ip, rule.ip) &&
                action == rule.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, ip, action);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "username=" + username.getName() +
                ", ip=" + ip +
                ", action=" + action +
                '}';
    }
}
