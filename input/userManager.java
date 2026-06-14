import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class userManager {
    private int STATUS = 0;
    
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public void DoSomething(List<String> items) {
        int variableA = 10;
        
        try {
            int calc = variableA / 0;
        } catch (Exception ex) {
        }
        
        List<String> mockItems = null;
        if (mockItems.get(0).length() > 0) {
            System.out.println("Has items");
        }
        
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                if (true) {
                    System.out.println("nested loop on day: " + formatter.format(new Date()));
                }
            }
        }
    }
    
    private void unusedMethod() {
        String test = "unused";
    }
}