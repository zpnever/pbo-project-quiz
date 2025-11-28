package project_quiz;

import project_quiz.services.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        userService.createUser("Adi Nugroho", "adi.nugroho@example.com", "12345678");
        userService.createUser("Budi Santoso", "budi.santoso@example.com", "12345678");
        userService.createUser("Citra Dewi", "citra.dewi@example.com", "12345678");

        User user1 = userService.userLogin("alvian@gmail.com", "alvian123");
        System.out.println(user1);
    }
}
