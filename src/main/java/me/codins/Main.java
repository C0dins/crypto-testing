package me.codins;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Helper;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Scanner;

public class Main {

    public static Argon2 argon2 = Argon2Factory.create();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("What would you like to do?\n * Find Optimal Iterations (iterations)\n * Hash with preconfigured settings (hash) \n > ");
        String option = scanner.nextLine();

        if(option.equalsIgnoreCase("iterations")){

            System.out.print("What is the max time wait per hash (in ms) > ");
            int maxMs = scanner.nextInt();
            System.out.print("What is the max memory cost per hash (in kb of memory) > ");
            int maxMem = scanner.nextInt();
            System.out.print("What is the max # of threads argon2 can use when hashing > ");
            int maxThreads = scanner.nextInt();

            int iterations = Argon2Helper.findIterations(argon2, maxMs, maxMem, maxThreads);
            System.out.println("Optimal number of iterations: " + iterations);

        } else if(option.equalsIgnoreCase("hash")) {

            System.out.print("What String would you like to hash? > ");
            String rawIn = scanner.nextLine();

            String bcryptHashed = hashWithBCrypt(15, rawIn);
            System.out.println(String.format("'%s' hashed with BCrypt > %s", rawIn, bcryptHashed));

            String argon2Hashed = hashWithArgon2(15, 65536, 4, rawIn);
            System.out.println(String.format("'%s' hashed with Argon2 (t=15,m=65536,p=4) > %s", rawIn, argon2Hashed));

        } else {
            System.out.println("Rerun with correct option (iterations / hash)");
        }
    }

    public static String hashWithBCrypt(int salt_length, String rawInput){
        String salt = BCrypt.gensalt(salt_length);
        return BCrypt.hashpw(rawInput, salt);

    }

    public static String hashWithArgon2(int iterations, int maxMemory, int threads, String rawInput ){
        return argon2.hash(iterations, maxMemory, threads, rawInput);
    }
}