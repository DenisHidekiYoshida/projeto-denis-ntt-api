package br.com.ntt.bank.util;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        String s = cpf.replaceAll("\\D", "");
        if (s.length() != 11) return false;
        if (s.chars().distinct().count() == 1) return false;
        int[] nums = s.chars().map(c -> c - '0').toArray();
        int v1 = 0;
        for (int i = 0; i < 9; i++) v1 += nums[i] * (10 - i);
        v1 = v1 % 11;
        v1 = (v1 < 2) ? 0 : 11 - v1;
        if (v1 != nums[9]) return false;
        int v2 = 0;
        for (int i = 0; i < 10; i++) v2 += nums[i] * (11 - i);
        v2 = v2 % 11;
        v2 = (v2 < 2) ? 0 : 11 - v2;
        return v2 == nums[10];
    }
}
