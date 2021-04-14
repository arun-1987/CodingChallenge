import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;

import java.util.TreeSet;

public class Test {
     WebDriver driver;
     WebDriverWait wait;

    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver,10);
    }

    public void launchApp(){
        driver.get("https://www.noon.com/uae-en/");
        driver.manage().window().maximize();
    }


	//Function to scroll to any position of the element. 
    public void scrollToView(String locator) throws InterruptedException {
        try {
            WebElement element = driver.findElement(By.xpath(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
        catch(NoSuchElementException n){
            int attempt = 0;
            int yPosition = 100;
            while(attempt<20) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,"+yPosition+")","");
                Thread.sleep(2000);
                if(driver.findElements(By.xpath(locator)).size()>0) {
                    scrollToView(locator);
                    break;
                }
                yPosition = yPosition+100;
                attempt++;
            }
        }
    }

    public TreeSet<String> sectionName(String carousel) throws InterruptedException {
         String headerPath = "//div[div/h3[contains(.,'"+carousel+"')]]";
         String arrowPath = "//div[div/h3[contains(.,'"+carousel+"')]]/following-sibling::div/div[2]";
         String fetchName = "//div[div/h3[contains(.,'"+carousel+"')]]/following-sibling::div/div/div/div[itemnumber]/div/a/div/div[2]/div[1]/div";
         String itemSizePath = "//div[div/h3[contains(.,'"+carousel+"')]]/following-sibling::div/div/div/div";
         TreeSet<String> set = new TreeSet<String>();
         int itemCounter = 0;
         scrollToView(headerPath);
         int itemSize = driver.findElements(By.xpath(itemSizePath)).size();
         for(int i=1; i<itemSize;i++){
         if((i%7)==0){
             driver.findElement(By.xpath(arrowPath)).click();
         }
         String value = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.xpath(fetchName.replace("itemnumber",Integer.toString(i))))).getText().toLowerCase();
         set.add(value);
         }
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
            System.out.println(carousel+" : " + iterator.next());
        return set;
    }


    public static void main(String[] ar){
        Test test = new Test();
        try {
            test.setUp();
            test.launchApp();
           // test.sectionName("Recommended For You");
            test.sectionName("Save big on mobiles & tablets");
            //test.sectionName("Top picks in electronics");
           // test.sectionName("Top picks in laptops");
            test.driver.quit();
        }catch(Exception e){
            test.driver.quit();
            e.printStackTrace();
        }
    }

}
