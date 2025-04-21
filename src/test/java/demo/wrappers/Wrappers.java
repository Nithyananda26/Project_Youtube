package demo.wrappers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */

    WebDriver driver;
    WebDriverWait wait;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void opensite(String URL) {
        driver.get(URL);
    }

    public void inputForTextfield(WebElement element, String input) {
        element.sendKeys(input, Keys.ENTER);
    }

    public void clickElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public WebElement waitForElementClick(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public String get_Text(WebElement element) {
        return element.getText();
    }

    public void scrollBy(int x, int y) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public String getlastmovieCategory(List<WebElement> movieCategoryList) {
        String lastmovieCategory = "";
        if (!movieCategoryList.isEmpty()) {
            WebElement lastMovieType = movieCategoryList.get(movieCategoryList.size() - 1);
            lastmovieCategory = lastMovieType.getText();
        }
        return lastmovieCategory;
    }

    public String getlastmovieMarked(List<WebElement> movieMarkedTypeList) {
        String lastmovieMarkedText = "";
        if (!movieMarkedTypeList.isEmpty()) {
            WebElement lastMovieType = movieMarkedTypeList.get(movieMarkedTypeList.size() - 1);
            lastmovieMarkedText = lastMovieType.getText();
        }
        return lastmovieMarkedText;
    }

    public WebElement getrightMost(List<WebElement> playlistTitles) {

        WebElement rightMostVisible = null;
        for (WebElement title : playlistTitles) {
            if (title.isDisplayed()) {
                rightMostVisible = title;
            }
        }
        return rightMostVisible;
    }

    public int extractAndPrintPostDetails(WebElement post) {
        String title = "Title not found";
        String body = "Body not found";
        int likes = 0;

        try {
            title = post.findElement(By.id("author-text")).getText();
        } catch (Exception ignored) {
        }

        try {
            body = post.findElement(By.id("home-content-text")).getText();
        } catch (Exception ignored) {
        }

        try {
            String likesText = post.findElement(By.id("vote-count-middle")).getText().trim();
            if (!likesText.isEmpty()) {
                if (likesText.endsWith("K")) {
                    likes = (int) (Double.parseDouble(likesText.replace("K", "")) * 1000);
                } else if (likesText.endsWith("M")) {
                    likes = (int) (Double.parseDouble(likesText.replace("M", "")) * 1000000);
                } else {
                    likes = Integer.parseInt(likesText.replaceAll("[^0-9]", ""));
                }
            }
        } catch (Exception ignored) {
        }

        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
        System.out.println("Likes: " + likes);
        System.out.println("---------------------------");

        return likes;
    }

    public void searchFor(String value) throws InterruptedException {
        WebElement searchfiled = driver.findElement(By.xpath("//input[@name=\"search_query\"]"));
        searchfiled.clear();
        searchfiled.sendKeys(value, Keys.ENTER);
        Thread.sleep(2000);
    }

    public long parseViews(String viewsText) {
        viewsText = viewsText.toUpperCase().replaceAll("[^0-9KM\\.]", "");
        
        if (viewsText.endsWith("M")) {
            return (long) (Double.parseDouble(viewsText.replace("M", "")) * 1000000);
        } else if (viewsText.endsWith("K")) {
            return (long) (Double.parseDouble(viewsText.replace("K", "")) * 1000);
        } else {
            return Long.parseLong(viewsText.replaceAll("[^0-9]", ""));
        }
    }
}

