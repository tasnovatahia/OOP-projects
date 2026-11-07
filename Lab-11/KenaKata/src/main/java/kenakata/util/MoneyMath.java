package kenakata.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyMath {
    private MoneyMath(){
        throw new AssertionError("Utility class cannot be instantiated");
    }
    public static long ceilPercentage(long amount,double percentage){
        if(amount<0){
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        if(percentage<0){
            throw new IllegalArgumentException("Percentage cannot be negative");
        }
        BigDecimal amountValue=BigDecimal.valueOf(amount);
        BigDecimal percentageValue=BigDecimal.valueOf(percentage);

        return amountValue.multiply(percentageValue).divide(BigDecimal.valueOf(100),0,RoundingMode.CEILING).longValueExact();
    }

    public static long ceilPercent(long amount,double percentage){
        return ceilPercentage(amount,percentage);
    }

    public static long percentageCeil(long amount,double percentage){
        return ceilPercentage(amount, percentage);
    }

    public static long ceilPercentage(long amount,long numerator,long denominator){
        if(amount<0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        if(numerator<0){
            throw new IllegalArgumentException("Percentage numerator cannot be negative");
        }
        if(denominator<=0){
            throw new IllegalArgumentException("Percentage denominator must be greater than zero");
        }
        return BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(numerator)).divide(BigDecimal.valueOf(denominator),0,RoundingMode.CEILING).longValueExact();
    }
    public static long ceilDivide(long value,long divisor){
        if(value<0){
            throw new IllegalArgumentException("Value cannot be negative.");
        }
        if(divisor<=0){
            throw new IllegalArgumentException("Divisor must be greater than zero.");
        }
        if(value==0){
            return 0;
        }
        return Math.addExact(value,divisor-1)/divisor;
    }

    public static  long gramsToBilledKilograms(long totalWeightGrams){
        if(totalWeightGrams<0){
            throw new IllegalArgumentException("Total weight cannot be negative.");
        }
        return ceilDivide(totalWeightGrams,1000);
    }
    public static long gramsToBilledKg(long totalWeightGrams){
        return gramsToBilledKilograms(totalWeightGrams);
    }

    public static long capped(long amount,long maximum){
        if(amount<0){
            throw new IllegalArgumentException("Amount cannot be negatve");
        }
        if(maximum<0){
            throw new IllegalArgumentException("Maximum cannot be negative.");
        }
        return Math.min(amount,maximum);
    }
public static long applyCap(long amount,long maximum){
        return capped(amount,maximum);
}
public static long ceilPercentageWithCap(long amount,double percentage,long maximum){
        return capped(ceilPercentage(amount,percentage),maximum);
}
public static long multipleExact(long amount,int quantity){
        if(amount<0){
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        if(quantity<=0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        return Math.multiplyExact(amount,quantity);
}
}