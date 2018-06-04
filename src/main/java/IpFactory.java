import org.apache.commons.net.util.SubnetUtils;

import java.net.UnknownHostException;

public class IpFactory {

    private static final String SUBNET_CHAR = "/";

    public static IP createIp(String ip) {
        if (!ip.contains(SUBNET_CHAR)) {
            return new BasicIp(ip);
        }
        else {
            return new RangeIp(ip);
        }
    }


    private static class BasicIp implements IP {

        private final String ip;

        private BasicIp(String ip) {
            this.ip = ip;
        }

        @Override
        public boolean isMatch(String ipToMatch) {
                return ip.equals(ipToMatch);
        }

        @Override
        public String toString() { return ip; }
    }

    private static class RangeIp implements IP {

        private final SubnetUtils subnetUtils;

        public RangeIp(String cidrNotationIp) {
            this.subnetUtils = new SubnetUtils(cidrNotationIp);
        }

        @Override
        public boolean isMatch(String ipToMatch) {
            return subnetUtils.getInfo().isInRange(ipToMatch);
        }

        @Override
        public String toString() { return subnetUtils.getInfo().getCidrSignature(); }
    }
}
