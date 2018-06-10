public class Packet {

    private final Username username;
    private final IP ip;

    public Packet(Username username, IP ip) {
        this.username = username;
        this.ip = ip;
    }

    public Username getUsername() {
        return username;
    }

    public IP getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "username=" + username.getName() +
                ", ip=" + ip.toString() +
                '}';
    }

}
