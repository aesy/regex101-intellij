package testData;

import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[abc]", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }
}
