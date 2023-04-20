import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("");
        System.out.println("(1) Log in");
        System.out.println("(2) Create Account");
        System.out.println("");
        System.out.println("Please select: ");
        Scanner sc = new Scanner(System.in);
        int userChoice = sc.nextInt();
        switch(userChoice) {

            case 1:

                System.out.println("");
                System.out.println("Username: ");
                Scanner sc1 = new Scanner(System.in);
                String userName = sc1.nextLine();
                System.out.println("");
                System.out.println("Password: ");
                Scanner sc2 = new Scanner(System.in);
                String passWord = sc2.nextLine();
                logIn(userName, passWord);
                break;

            case 2:

                System.out.println("");
                System.out.println("Username: ");
                Scanner sc3 = new Scanner(System.in);
                String newUserName = sc3.nextLine();
                System.out.println("");
                System.out.println("Password: ");
                Scanner sc4 = new Scanner(System.in);
                String newPassWord = sc4.nextLine();
                createAccount(newUserName, newPassWord);
                break;

        }

    }

    public static void logIn(String userName, String passWord) throws IOException {

        String hashPass = sha256(passWord);
        String hashUser = sha256(userName);
        String path = hashUser + ".txt";
        File f = new File(path);

        if(f.exists() && !f.isDirectory()) {

            System.out.println("");
            System.out.println("Account Found!");
            String filePass = Files.readAllLines(Paths.get(path)).get(0);
            List<java.lang.String> account = Files.readAllLines(Paths.get(path));

            if (filePass.equals(hashPass)) {

                accountSuccess(account, hashUser, hashPass);

            } else {

                System.out.println("");
                System.out.println("Password Incorrect!");

            }

        } else {

            System.out.println("Account Not Found!");

        }

    }

    public static void createAccount(String newUserName, String newPassWord) {


        String hashPass = sha256(newPassWord);
        String hashUser = sha256(newUserName);
        String path = hashUser + ".txt";
        File f = new File(path);

        if (f.exists() && !f.isDirectory()) {

            System.out.println("");
            System.out.println("Account Already Exists!");

        } else {

            write(path, hashPass + "\n" + "0.00");
            System.out.println("");
            System.out.println("Account Created!");

        }

    }

    public static void accountSuccess(List<java.lang.String> account, String hashUser, String hashPass) {

        double accountBalance = Double.parseDouble(account.get(1));
        String path = hashUser + ".txt";
        System.out.println("Balance: $ " + makePretty(accountBalance));
        System.out.println("(1) Withdraw");
        System.out.println("(2) Deposit");
        Scanner sc = new Scanner(System.in);
        int userChoice = sc.nextInt();
        switch (userChoice) {

            case 1:
                System.out.println("");
                System.out.println("Amount to Withdraw: ");
                Scanner sc1 = new Scanner(System.in);
                double withdraw = sc1.nextDouble();

                if (withdraw < 0.01) {

                    System.out.println("Amount too small!");
                    break;

                }

                if (accountBalance < withdraw) {

                    System.out.println("Insufficient funds!");
                    break;

                }

                accountBalance = accountBalance - withdraw;
                write(path, hashPass + "\n" + String.format("%.2f", accountBalance));
                break;

            case 2:
                System.out.println("");
                System.out.println("Amount to Deposit: ");
                Scanner sc2 = new Scanner(System.in);
                double deposit = sc2.nextDouble();
                accountBalance = accountBalance + deposit;
                write(path, hashPass + "\n" + String.format("%.2f", accountBalance));
                break;

        }

    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static String makePretty(double number) {

        String stylized = (NumberFormat.getInstance().format(number)).replace(",", " ");
        String decimal = stylized.substring(stylized.indexOf(".")+1);
        decimal = decimal.trim();

        if (!stylized.contains(".")) {

            stylized = stylized + ".00";

        } else if (decimal.length() == 1) {

            stylized = stylized + "0";

        }

        return stylized;

    }

    public static void write(String fileName, String content)  {

        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}