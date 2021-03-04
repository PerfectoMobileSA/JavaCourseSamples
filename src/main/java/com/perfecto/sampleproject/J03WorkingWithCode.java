package com.perfecto.sampleproject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

/**
 * This template is for users that use DigitalZoom Reporting (ReportiumClient).
 * For any other use cases please see the basic template at https://github.com/PerfectoCode/Templates.
 * For more programming samples and updated templates refer to the Perfecto Documentation at: http://developers.perfectomobile.com/
 */
public class J03WorkingWithCode {

    public static void main(String[] args) throws IOException {
        System.out.println("Run started");

        String browserName = "mobileOS";
        DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
        String host = "ps.perfectomobile.com";
        capabilities.setCapability("user", "<<USER>>");
        capabilities.setCapability("securityToken", "<<TOKEN>>");

        //TODO: Change your device ID
        capabilities.setCapability("deviceName", "RF8M329FK3X");

        // Name your script
        // capabilities.setCapability("scriptName", "RemoteWebDriverTest");

        RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        
        // Reporting client. For more details, see http://developers.perfectomobile.com/display/PD/Reporting
        PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
                .withProject(new Project("Training Project", "1.0"))
                .withJob(new Job("TrainingDemo", 1))
                .withContextTags("Shane","Demo")
                .withWebDriver(driver)
                .build();
        ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

        try {
            reportiumClient.testStart("Xpath Training", new TestContext("Training", "Xpath"));

            // write your code here
            switchToContext(driver, "WEBVIEW");
            reportiumClient.stepStart("Open Training Site"); // this is a logical step for reporting
            driver.get("https://training.perfecto.io/");
            driver.findElement(By.xpath("//h2[contains(text(),\"Training Website\")]"));
            reportiumClient.stepEnd("Open Training Site");
            reportiumClient.stepStart("Click Xpath");
            driver.findElementByXPath("//a[text()=\"X Path\"]").click();
            driver.findElement(By.xpath("//h2[text()=\"Lets Make a Xalad!\"]"));
            reportiumClient.stepEnd("Click Xpath");
            // more commands...
            //uncomment the following lines for the assignment
//            reportiumClient.stepStart("Validate Avacado Cost");
//            String avacadoCost = driver.findElement(By.xpath(<<AvacadoCostXPATH>>)).getText();
//            reportiumClient.reportiumAssert("The cost of Avacado", avacadoCost.contentEquals("$2.4"));
//            reportiumClient.stepStop("Validate Avacado Cost");
//            reportiumClient.stepStart("Count of Picture Objects");
//            int pictureCount = driver.findElements(By.xpath(<<PictureXPATH>>)).count;
//            reportiumClient.reportiumAssert("The count of Pictures", pictureCount==6);
//            reportiumClient.stepStop("Count of Picture Objects");
//            reportiumClient.stepStart("Click on Cucumber");
//            RemoteWebElement cucumber = driver.findElement(By.xpath(<<CUCUMBERXPATH>>));
//            cucumber.click();
//            driver.findElementByXPath(<<WIKIPEDIAXPATH>>);
//            reportiumClient.stepStop("Click on Cucumber");
            
            
            
            
            
            
            
            
            reportiumClient.testStop(TestResultFactory.createSuccess());
        } catch (Exception e) {
            reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
            e.printStackTrace();
        } finally {
            try {
                driver.quit();

                // Retrieve the URL to the DigitalZoom Report (= Reportium Application) for an aggregated view over the execution
                String reportURL = reportiumClient.getReportUrl();

                // Retrieve the URL to the Execution Summary PDF Report
                String reportPdfUrl = (String)(driver.getCapabilities().getCapability("reportPdfUrl"));
                // For detailed documentation on how to export the Execution Summary PDF Report, the Single Test report and other attachments such as
                // video, images, device logs, vitals and network files - see http://developers.perfectomobile.com/display/PD/Exporting+the+Reports

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Run ended");
    }

    private static void switchToContext(RemoteWebDriver driver, String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String,String> params = new HashMap<String,String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

    private static String getCurrentContextHandle(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
        return context;
    }

    private static List<String> getContextHandles(RemoteWebDriver driver) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
        return contexts;
    }
}
