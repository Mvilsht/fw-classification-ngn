public class Packet {

    private final Username username;
    private final String ip;

    public Packet(Username username, String ip) {
        this.username = username;
        this.ip = ip;
    }

    public Username getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "username=" + username.getName() +
                ", ip=" + ip +
                '}';
    }

}
