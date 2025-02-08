package hyperskill;

public class Main {
    public static void main(String[] args) {
        CommandInterface myInterface = new CommandInterface();
        System.out.println("Learning Progress Tracker");
        while (true) {
            myInterface.askForCommand();
            String result = myInterface.evaluateCommand();
            System.out.println(result);
            if (result.equals("Bye!")) {
                break;
            }
        }
    }
}
