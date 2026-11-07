package kenakata.util;

public final class Validate {
    private Validate(){
        throw new AssertionError("Utility class cannot be instantiated.");
    }
    public static <T> T notNull(T value,String fieldName){
        if(value==null){
            throw new IllegalArgumentException(fieldName+"cannot be null.");
        }
        return value;
    }
    public static String notBlank(String value,String fieldName){
        notNull(value,fieldName);

        if(value.isBlank()){
            throw new IllegalArgumentException(fieldName+"cannot be blank");
        }
        return value;
    }

    public static long nonNegative(long value,String fieldName){
        if(value<0){
            throw new IllegalArgumentException(fieldName+"cannot be negative.");
        }
        return value;
    }
    public static int nonNegative(int value,String fieldName){
        if(value<0){
            throw new IllegalArgumentException(fieldName+"cannot be negative");
        }
        return value;
    }

    public static double nonNegative(double value,String fieldName){
        if(Double.isNaN(value)|| Double.isInfinite(value)){
            throw new IllegalArgumentException(fieldName+"must be a finite number.");
        }
        if(value<0){
            throw new IllegalArgumentException(fieldName+"cannot be negative.");
        }
        return value;
    }
    public static int positive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }

        return value;
    }
    public static long positive(long value,String fieldName){
        if(value<=0){
            throw new IllegalArgumentException(fieldName + "must be greater than zero.");
        }
        return value;
    }

    public static double positive(double value,String fieldName){
        if(Double.isNaN(value)||Double.isInfinite(value)){
            throw new IllegalArgumentException(fieldName+ "must be a finite number.");
        }
        if(value<=0){
            throw new IllegalArgumentException(fieldName+"must be greater than zero");
        }
        return value;
    }
    public static int quantity(int quantity){
        return positive(quantity,"Quantity");
    }

    public static long money(long amount,String fieldName){
        return nonNegative(amount,fieldName);
    }

    public static double percentage(double percentage,String fieldName){
        return nonNegative(percentage,fieldName);
    }
}
