For any help/queries on this framework. Please feel free to drop an email @ ritulmishrampvm@gmail.com If you find it helpful,  **star** the repository and **share** with your network on LinkedIn,Twitter etc :)
Connect with me on [LinkedIn](https://www.linkedin.com/in/ritulmishra/)
Cheers!

# Mobile automation testing framework (Android and iOS) - supports testng tests

*Single code base framework to test android and iOS app using appium. It is a boilerplate code. Clone it and you are good to go!*

## Prequisites:

- Appium server installed on the machine. In case not, install it by running command `npm install -g appium`. For more details visit: https://appium.io/docs/en/about-appium/getting-started/?lang=en

- iOS Simulator or Android Emulator or real device to execute tests.

- Install Maven in your machine. Maven is a build tool (can be downloaded from [here](https://maven.apache.org/download.cgi)). pom.xml file is present in base directory which has all the required dependencies and code to invoke testng.xml file when executed from command line.

## Framework with testng tests setup and execution 

### Running sample test

1. Connect your device to your machine or start the emulator.

*Note: start appium server on your machine if not included programatically*

###### Run below commands to execute android testng test:

$ cd one_for_all_ui/
$ mvn test -Dos=android -Dsurefire.suiteXmlFiles=testng.xml

*Include iOS app on which you want to run test. Provide its path in config.xml file (iOSAppPath=src/app/path-to-your-iOSfile). And write  screen locators in IOSLoginScreen and methods in IOSLoginCorelogic. Now you are ready to run below commands.*

###### Run below commands to execute iOS testng test:

$ cd mobileautomationframework/
$ mvn test -Dos=iOS -Dsurefire.suiteXmlFiles=testng.xml

### Architecture Overview

**Package : allforone** : It includes the common classes (and methods) which are required by each test to perform actions. Below are classes in this package:

**retryLogic** : It has classes to implement retry in case of failure of a test. Retry count  is set to 1 as of now. Test will be run once if it fails during the execution.

Add below listener to testng.xml file to include retry functionality.

**CreateSession.java** : All the methods to create a new session and destroy the session after the test(s) execution is over. Each test extends this class. Below are the methods in CreateSession class in their execution order.

1. invokeAppium() - method starts Appium server. Calls startAppiumServer method to start the session depending upon your OS.

2. createDriver(String os, Method methodName) - method creates the driver depending upon the passed parameter (android or iOS) and loads the properties files (config and test data properties files).

3. Tests execution (tests are present in integrationtests.tests package)

4. teardown() - method quit the driver after the execution of test(s) 

5. stopAppium() - method to stop Appium server. Calls stopAppiumServer() method to stop session depending upon your OS.



**GenericMethods.java** : It is a common repository for all the  webdriver and appium methods which are called in each coreLogic classes. Every new method which is being used in coreLogic classes should be added in this class as well. It is to reduce the duplicate code. Each screen class extends this class. Below are few methods defined in this class:

waitForVisibility(By targetElement) - method to wait for an element to be visible
findElement(By locator) - method to find an element
findElements(By locator) - method to find all the elements of specific locator

**MysqlDatabase.java** : This can be used if any DB values need to be verifiedIt has method to read DB and get data from required table. For more help, read on this link: http://www.vogella.com/tutorials/MySQLJava/article.html


**Package : app** : It contains the app build against which tests would be executed. 


### Javadoc of the project can be found in doc folder. It contains information all classes and methods.

                                            xxxxxxxxxxxxxxxxxxxxxxxxxxxx
### How to execute a test

Maven is used as build tool (can be downloaded from [here](https://maven.apache.org/download.cgi)). pom.xml file is present in base directory which has all the required dependencies and code to invoke testng.xml file when executed from command line.

Connect your device to your machine or start the emulator.

*Note: start appium server on your machine if not included programatically*

###### Run below commands to execute android cucumber test:

$ cd one-for-all-ui/
$ mvn test -Dos=android -Dsurefire.suiteXmlFiles=testng.xml

*Include iOS app on which you want to run test. Provide its path in config.xml file (src/main/resources/iOSDevices.json). And write  screen locators in IOSLoginScreen and methods in IOSLoginCorelogic. Now you are ready to run below commands.*

###### Run below commands to execute iOS cucumber test:

$ cd one-for-all-ui/
$ mvn test -Dos=iOS -Dsurefire.suiteXmlFiles=testng.xml




