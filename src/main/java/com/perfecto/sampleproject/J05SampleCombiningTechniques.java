package com.perfecto.sampleproject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.AppiumDriver;

public class J05SampleCombiningTechniques {
	static AppiumDriver driver;
	static ReportiumClient reportiumClient;
	private static final String NAME = "PerfectoUser";
	private static final String USERNAME = "PerfectoUser@perfecto.io";
	private static final String PASSWORD = "12345";
	
	public static void main(String[] args) throws Exception {
        System.out.println("Run started");
		//Replace <<cloud name>> with your perfecto cloud name (e.g. demo) or pass it as maven properties: -DcloudName=<<cloud name>>  
		//String cloudName = "<<cloud name>>";
		String cloudName = "ps";
		//Replace <<security token>> with your perfecto security token or pass it as maven properties: -DsecurityToken=<<SECURITY TOKEN>>  More info: https://developers.perfectomobile.com/display/PD/Generate+security+tokens
		//String securityToken = "<<security token>>";
		String securityToken = "<<TOKEN>>";

		//A sample perfecto connect appium script to connect with a perfecto android device and perform addition validation in calculator app.
		//String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		capabilities.setCapability("securityToken", Utils.fetchSecurityToken(securityToken));
		capabilities.setCapability("platformName", "Android");
		//capabilities.setCapability("model", "Galaxy S.*");
		capabilities.setCapability("model", "Galaxy S6");
		capabilities.setCapability("enableAppiumBehavior", true);
		capabilities.setCapability("openDeviceTimeout", 2);
		capabilities.setCapability("noReset", "true");
		capabilities.setCapability("appPackage", "io.perfecto.expense.tracker");
		driver = new AppiumDriver<>(new URL("https://" + Utils.fetchCloudName(cloudName)  + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"), capabilities); 
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		try
		{
		reportiumClient = Utils.setReportiumClient(driver, reportiumClient); //Creates reportiumClient
		reportiumClient.testStart("Training Session 5", new TestContext("JavaCourse", "Training")); //Starts the reportium test
		reportiumClient.stepStart("Start Expense App");
		Map<String, Object> params = new HashMap<>();
		params.put("identifier", "io.perfecto.expense.tracker");
		driver.executeScript("mobile:application:open", params);
		driver.context("NATIVE_APP");
		reportiumClient.stepEnd("Start Expense App");
		reportiumClient.stepStart("Verify Expense App is loaded"); //Starts a reportium step
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@resource-id='io.perfecto.expense.tracker:id/img_icon']")));
		reportiumClient.stepEnd("Verify Expense App is loaded"); //Stops a reportium step

		reportiumClient.stepStart("Click Signup");
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/login_signup_btn\"]")).click();
		reportiumClient.stepEnd("Click Signup");
		
		reportiumClient.stepStart("Enter Information");
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/signup_name\"]")).sendKeys(NAME);
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/signup_email\"]")).sendKeys(USERNAME);
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/signup_password\"]")).sendKeys(PASSWORD);
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/signup_confirm_password\"]")).sendKeys(PASSWORD);
		driver.findElement(By.xpath("//*[@resource-id=\"android:id/text1\"]")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text=\"USD-$\"]")));
		driver.findElement(By.xpath("//*[@text=\"USD-$\"]")).click();
		
		driver.hideKeyboard();
		driver.findElement(By.xpath("//*[@resource-id=\"io.perfecto.expense.tracker:id/signup_save_btn\"]")).click();
		
		/*Map<String, Object> params = new HashMap<>();
		params.put("start", "50%,60%");
		params.put("end", "50%,40%");
		params.put("duration", "3");
		Object res = driver.executeScript("mobile:touch:swipe", params);*/
		reportiumClient.stepEnd("Enter Information");
		reportiumClient.stepStart("Log into application");
		
		driver.findElementByXPath("//*[@resource-id=\"io.perfecto.expense.tracker:id/login_email\"]").sendKeys(USERNAME);
		driver.findElementByXPath("//*[@resource-id=\"io.perfecto.expense.tracker:id/login_password\"]").sendKeys(PASSWORD);
		driver.findElementByXPath("//*[@resource-id=\"io.perfecto.expense.tracker:id/login_login_btn\"]").click();
		
		reportiumClient.stepEnd("Log into application");
		
		//Assignment
		
		reportiumClient.stepStart("Click add button");
		
		reportiumClient.stepEnd("Click add button");
		reportiumClient.stepStart("Select Expense Type");
		
		reportiumClient.stepEnd("Select Expense Type");
		reportiumClient.stepStart("Enter expense amount");
		
		reportiumClient.stepEnd("Enter expense amount");
		reportiumClient.stepStart("Select Category");
		
		reportiumClient.stepEnd("Select Category");
		reportiumClient.stepStart("Click Save");
		
		reportiumClient.stepEnd("Click Save");
		reportiumClient.stepStart("Validate the Expense");
		
		reportiumClient.stepEnd("Validate the Expense");
		reportiumClient.stepStart("Logout");
		driver.findElementByXPath("//*[@content-desc=\"Open Drawer\"]").click();
		driver.findElementByXPath("//*[@text=\"Logout\"]").click();
		driver.findElementByXPath("//*[@resource-id=\"android:id/button1\"]").click();
		driver.findElementByXPath("//*[@resource-id=\"io.perfecto.expense.tracker:id/snackbar_text\"]");
		reportiumClient.stepEnd("Logout");
		
		
		reportiumClient.testStop(TestResultFactory.createSuccess());
		driver.close();
		driver.quit();
		}
		catch(Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e));
			throw e;
			
		}
		finally {
			driver.quit();
			// Retrieve the URL to the DigitalZoom Report 
			String reportURL = reportiumClient.getReportUrl();
			System.out.println(reportURL);	
		}
	}
	private static void switchToContext(RemoteWebDriver driver, String context) {
        RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
        Map<String,String> params = new HashMap<String,String>();
        params.put("name", context);
        executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
    }

}
