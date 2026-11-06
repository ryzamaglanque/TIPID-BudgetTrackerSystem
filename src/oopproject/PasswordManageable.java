package oopproject;

public interface PasswordManageable { //INTERFACE FOR ACCOUNTS THAT CAN CHANGE THEIR PASSWORD

    boolean accountExists(); //CHECKS IF THE ACCOUNT CAN BE FOUND

    boolean verifyCurrentPassword(String current); //CHECKS IF THE ENTERED CURRENT PASSWORD IS CORRECT

    void updatePassword(String newPassword); //SAVES THE NEW PASSWORD
} //INTERFACE FOR ACCOUNTS THAT CAN CHANGE THEIR PASSWORD