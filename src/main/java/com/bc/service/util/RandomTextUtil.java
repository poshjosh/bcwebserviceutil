package com.bc.service.util;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author hp
 */
public final class RandomTextUtil {

    // THIS VALUE IS CONSTRAINED BY THE DATABASE. 
    private static final int transactionCodeLength = 32;
    
    // THIS VALUE IS CONSTRAINED BY THE DATABASE. 
    private static final int cardNumberLength = 16;

    // THIS VALUE IS CONSTRAINED BY THE DATABASE. 
    private static final int accountNumberLength = 10;
    
    // THIS VALUE IS CONSTRAINED BY THE DATABASE
    private static final int userCodeLength = 6;
    
    private static final AtomicInteger cardNumberCount = new AtomicInteger(0);
    
    private RandomTextUtil() { }
    
    public static String generateRandomPassword() {
        return generateCode(5) + '-' + generateCode(5);
    }

    public static String generateTransactionReference(String key) {
        // REF[EPOCH_MILLIS][PaymentKind]
        final long time = System.currentTimeMillis();
        return "REF-" + Long.toHexString(time) + "-" + key;
    }
    
    public static String generateTransactionCode() {
        final String suffix = "-" + Long.toHexString(System.currentTimeMillis());
        return generateCode(transactionCodeLength - suffix.length()) + suffix;
    }
    
    public static String generateUserCode() {
        return generateCode(userCodeLength);
    }
    
    public static String generateCode(int maxLength) {
        return RandomStringUtils.random(maxLength, true, true);
    }
    
    public static String generateUniqueAccountNumber(Long accountId) {
        
        final String prefix = "77";
        final String idStr = accountId.toString();
        
        final int zerosToAdd = accountNumberLength - (prefix.length() + idStr.length());
        
        char [] zeros = new char[zerosToAdd];
        Arrays.fill(zeros, '0');
        
        return prefix + new String(zeros) + idStr;
    }
    
    public static String generateUniquePaymentCardNumber(){
        final long currentTime = System.currentTimeMillis();
        final String currentTimeStr = Long.toString(currentTime);
        final int nonce;
        synchronized(cardNumberCount) {
            nonce = cardNumberCount.incrementAndGet();
            if(cardNumberCount.get() == 9) {
                cardNumberCount.set(0);
            }
        }
        final String nonceStr = Integer.toString(nonce);
        final int remainingChars = cardNumberLength - (nonceStr.length() + currentTimeStr.length());
        final String result;
        if(remainingChars < 0) {
            throw new RuntimeException("It should take 100s of years for this to happen");
        }else if(remainingChars == 0){
            result = nonceStr + currentTimeStr;
        }else{
            result = nonceStr + generateRandomNumeric(remainingChars) + currentTimeStr;
        }
        assert result.length() == cardNumberLength;
        return result;
    }
    
    public static String generatePaymentCardCode() {
        return generateRandomNumeric(3);
    }
    
    public static String generateRandomNumeric(int count) {
        return RandomStringUtils.randomNumeric(count);
    }
}
