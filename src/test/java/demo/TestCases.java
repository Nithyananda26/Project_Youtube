package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.Assert;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
// import dev.failsafe.internal.util.Assert;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;
        WebDriverWait wait;
        Wrappers wr;
        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */
        // Test case-1 fetching the about page text
        @Test(enabled = true)
        public void testCase01() {
                System.out.println("Test case-1");
                wr.opensite("https://www.youtube.com/");
                String actualUrl = driver.getCurrentUrl();
                String expectedUrl = "https://www.youtube.com/";
                Assert.assertEquals(actualUrl, expectedUrl);
                wr.clickElement(By.xpath("//a[@href=\"https://www.youtube.com/about/\"]"));
                WebElement about_element = driver
                                .findElement(By.xpath("// section[contains(@class,\"ytabout__content\")]"));
                wr.scrollBy(0, 900);
                // printing the message of about
                System.out.println(about_element.getText());
        }

        //  Test case-2 Click on movies & fetch movie name by adding some filters 
        @Test(enabled = true)
        public void testCase02() throws InterruptedException {
                System.out.println("Test case-1");
                wr.opensite("https://www.youtube.com/");
                wr.clickElement(By.xpath("//a[@title='Movies']"));

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
                WebElement nextBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Next']")));

                while (nextBtn.isDisplayed()) {
                        nextBtn.click();
                }
                SoftAssert sAssert = new SoftAssert();

                // verify movie category
                List<WebElement> movieCategoryList = driver.findElements(By.xpath(
                                "//span[@class=\"grid-movie-renderer-metadata style-scope ytd-grid-movie-renderer\"]"));

                List<String> expectedCategory = Arrays.asList("Comedy", "Animation", "Drama");
                String lastmovieCategoryText = wr.getlastmovieCategory(movieCategoryList);
                String cleanCategoryText = lastmovieCategoryText.replaceAll("[^a-zA-Z ]", "").trim();

                boolean Movieflag = expectedCategory.contains(cleanCategoryText);
                sAssert.assertTrue(Movieflag);

                System.out.println(cleanCategoryText + " last movie Category text *** ");

                // verify movie markedType
                List<WebElement> movieMarkedTypeList = driver.findElements(By.xpath(
                                "//div[@class=\"badge  badge-style-type-simple style-scope ytd-badge-supported-renderer style-scope ytd-badge-supported-renderer\"]"));

                List<String> expectedMarkedCategory = Arrays.asList("U/A", "U/A13+", "U", "A");
                String lastmovieMarkedText = wr.getlastmovieMarked(movieMarkedTypeList);

                System.out.println(lastmovieMarkedText + " Last movie marked Text ");
                boolean MovieMarkedflag = expectedMarkedCategory.contains(lastmovieMarkedText);
                sAssert.assertTrue(MovieMarkedflag);
                sAssert.assertAll();
        }

        //  Test case-3 Click on music & select speific cetegory, fetech all the music text
        @Test(enabled = true)
        public void testCase03() throws InterruptedException {
                System.out.println("Test case-1");
                wr.opensite("https://www.youtube.com/");
                wr.clickElement(By.xpath("//a[@title='Music']"));
                wr.scrollBy(0, 1000);
                Thread.sleep(3000);
                // Step 1: Locate the section by heading text
                WebElement section = driver.findElement(By.xpath(
                                "//div[@id='title-text']//span[text()=\"India's Biggest Hits\"]/ancestor::ytd-rich-shelf-renderer"));

                // Step 2: Get all playlist titles under that section only
                List<WebElement> playlistTitles = section.findElements(
                                By.xpath(".//a[contains(@class,'yt-lockup-metadata-view-model-wiz__title')]"));

                // Step 3: Loop through visible elements
                WebElement rightMostVisible = wr.getrightMost(playlistTitles);

                // Step 4: Print the title text of the rightmost visible playlist
                if (rightMostVisible != null) {
                        String playlistTitle = rightMostVisible.getText();
                        System.out.println("Rightmost visible playlist title: " + playlistTitle);
                } else {
                        System.out.println("No visible playlists found in the India Biggest Hits");
                }

                Thread.sleep(5000);
                WebElement songCountElement = wr.waitForVisibility(By.xpath(
                                "//yt-thumbnail-badge-view-model//div[contains(@class, 'badge-shape-wiz__text')]"));
                // get the song count
                String songCountText = songCountElement.getText();

                // Extract the number from the text
                int numberOfSongs = Integer.parseInt(songCountText.replaceAll("[^0-9]", ""));

                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(numberOfSongs <= 50, "Song count is greater than 50: ");
                softAssert.assertAll();
                System.out.println("Number of songs is --> " + numberOfSongs);
        }

        //  Test case-4 Click on news & select Latest news posts, fetech all the news post text
        @Test(enabled = true)
        public void testCase04() throws InterruptedException {
                System.out.println("Test case-1");
                wr.opensite("https://www.youtube.com/");
                wr.clickElement(By.xpath("//a[@title='News']"));
                Thread.sleep(1000);
                wr.scrollBy(0, 1000);
                Thread.sleep(3000);

                // Locate all the latest news post cards
                List<WebElement> posts = driver.findElements(
                                By.xpath("// ytd-rich-item-renderer[@is-post]"));

                int totalLikes = 0;

                for (int i = 0; i < 3 && i < posts.size(); i++) {
                        WebElement post = posts.get(i);
                        int likes = wr.extractAndPrintPostDetails(post);
                        totalLikes += likes;
                }
                System.out.println("Total Likes from first 3 posts: " + totalLikes);
        }

        //  Test case-5 Search for specific keywords& get the information & printing
        @Test(enabled = true)
        public void testCase05() throws Exception {
                String[] searchTerms = { "Movies", "Music", "Games", "India", "UK" };

                // Step 2: Open YouTube
                wr.opensite("https://www.youtube.com");

                for (String value : searchTerms) {
                        System.out.println("Searching for: " + value);
                        // Search the term
                        wr.searchFor(value);
                        // Initialize total views
                        long totalViews = 0;

                        Set<String> collectedVideos = new HashSet<>(); // avoiding duplicate
                        while (totalViews < 100000000) { // 10 Crores
                                List<WebElement> videos = driver.findElements(By.xpath("//ytd-video-renderer"));

                                for (WebElement video : videos) {
                                        try {
                                                String title = video.findElement(By.id("video-title")).getText();
                                                String viewsText = video
                                                                .findElement(By.xpath(
                                                                                ".//span[contains(text(), 'views')]"))
                                                                .getText();
                                                if (!collectedVideos.contains(title)) {
                                                        long views = wr.parseViews(viewsText);
                                                        totalViews += views;
                                                        collectedVideos.add(title);
                                                        System.out.println("Video: " + title + " | Views: " + views
                                                                        + " | Total so far: " + totalViews);
                                                        if (totalViews >= 100000000)
                                                                break;
                                                }
                                        } catch (Exception ignored) {
                                        }
                                }
                                // Scroll down
                                wr.scrollBy(0, 2000);
                                Thread.sleep(3000);
                        }
                        System.out.println("Finished 10 Cr+ views for: " + value);
                        System.out.println("Total Views Collected: " + totalViews);
                        System.out.println("------------------------------------------------------------");
                }
        }

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");
                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");
                driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                wr = new Wrappers(driver);
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();
        }
}
