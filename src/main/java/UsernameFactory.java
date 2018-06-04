

public class UsernameFactory {

    private static final String JOKER_FORMAT = "*";

    public static Username createUsername(String username) {
        if (username.equals(JOKER_FORMAT)) {
            return new JokerUsername();
        }
        else {
            return new BasicUsername(username);
        }
    }


    private static class BasicUsername implements Username {

        private final String usernameAsString;

        private BasicUsername(String usernameAsString) {
            this.usernameAsString = usernameAsString;
        }

        @Override
        public boolean isMatch(Username usernameToMatch) {
            return usernameAsString.equals(usernameToMatch.getName());
        }

        @Override
        public String getName() {
            return usernameAsString;
        }

    }

    private static class JokerUsername implements Username {

        private JokerUsername() {
        }
        @Override
        public boolean isMatch(Username usernameToMatch) {
            return true;
        }

        @Override
        public String getName() {
            return JOKER_FORMAT;
        }

    }
}
