package com.meuprojeto.banco.sistemabancario.security;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityOperator {

    public static String hashGenerator(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String savedHash) {
        return BCrypt.checkpw(password, savedHash);
    }
}
