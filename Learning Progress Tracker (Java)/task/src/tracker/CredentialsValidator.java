package tracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredentialsValidator {
    private Pattern pattern;
    private Matcher matcher;

    public CredentialsStatus validateCredentials(String credentialsInputByUser) {
        String[] credentialsToEvaluate = splitCredentials(credentialsInputByUser);

        if (credentialsInputByUser.split(" ").length < 3) {
            return CredentialsStatus.INCORRECT_CREDENTIALS;
        }

        // Checking first name
        if (!pattern.compile("^(?=.*[A-Za-z].*[A-Za-z])[A-Za-z]+(?:[-'][A-Za-z]+)*$").matcher(credentialsToEvaluate[0]).matches()) {
            return CredentialsStatus.INCORRECT_FIRST_NAME;
        }

        // Evaluating the last name in chunks
        String[] chunks = credentialsToEvaluate[1].split("\\s+");
        Pattern chunkPattern = Pattern.compile("^(?=.*[A-Za-z].*[A-Za-z])[A-Za-z]+(?:[-'][A-Za-z]+)*$");

        for (String chunk : chunks) {
            if (!chunkPattern.matcher(chunk).matches()) {
                return CredentialsStatus.INCORRECT_LAST_NAME;
            }
        }

        if (!pattern.compile("^[^@]+@[^@]+\\.[^@]+$").matcher(credentialsToEvaluate[2]).matches()) {
            return CredentialsStatus.INCORRECT_EMAIL;
        }

        for (Student student : CommandInterface.getStudents()) {
            if (student.getEmail().equals(credentialsToEvaluate[2])) {
                return CredentialsStatus.DUPLICATED_EMAIL;
            }
        }
        return CredentialsStatus.SUCCESS;
    }

    /**
     * A function that takes an array of credentials. It expects the format: firstName, lastName and email
     * @param credentialsRaw: The credentials inputed by the user when prompted to add a student.
     * @return: An array of Strings containing the data to evaluate credentials. The expected array length is 3
     */
    public String[] splitCredentials(String credentialsRaw) {
        String[] credentials = new String[3];
        String[] credentialsRawArray = credentialsRaw.split(" ");

        credentials[0] = credentialsRawArray[0];
        credentials[2] = credentialsRawArray[credentialsRawArray.length - 1];

        // A student's last name can be a single, or multiple words, but it is assigned to the array in a single index [2]
        StringBuilder lastName = new StringBuilder();
        for (int i = 1; i < credentialsRawArray.length - 1; i++) {
            lastName.append(credentialsRawArray[i]);
            if (i != credentialsRawArray.length - 2) {
                lastName.append(" ");
            }
        }

        credentials[1] = lastName.toString();

        return credentials;
    }


    enum CredentialsStatus {
        INCORRECT_CREDENTIALS,
        INCORRECT_EMAIL,
        DUPLICATED_EMAIL,
        INCORRECT_FIRST_NAME,
        INCORRECT_LAST_NAME,
        SUCCESS
    }
}