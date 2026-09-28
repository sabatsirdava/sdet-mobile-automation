SDET Mobile Automation – Sauce Labs My Demo App

Java/Maven mobile UI automation framework for the Android Sauce Labs My Demo App.

Built with Appium + UiAutomator2, TestNG, Page Object Model with Page Factory, and ExtentReports.

Scope: Task #1 – Android mobile automation. The API task is not included in this repository.

Technology Stack
Technology	Version
Java	17
Maven	3.9+
Appium Java Client	10.1.1
Selenium	4.43.0
TestNG	7.11.0
ExtentReports	5.1.2
SLF4J / Logback	2.0.17 / 1.5.18
Prerequisites
JDK 17
Maven 3.9+
Git
Android SDK with adb on PATH
Android emulator or physical Android device
Node.js
Appium 2.x/3.x with the UiAutomator2 driver
Sauce Labs My Demo App APK

Verify the environment:

java -version
mvn -version
adb devices
appium driver list --installed
Setup
Install the application

Download the APK from the official releases and install it:

adb install -r path\to\mydemoapp.apk

The APK is not committed to the repository.

Alternatively, set app.path in local.properties and Appium will install it when the session starts.

Configure the device

Default configuration is stored in:

src/main/resources/config/config.properties

For machine-specific settings, create the git-ignored local.properties file:

device.name=emulator-5556

Configuration precedence:

Maven/JVM system properties
local.properties
config.properties

For example:

mvn clean test -Ddevice.name=emulator-5556
Start Appium

Run Appium in a separate terminal:

appium

Default server:

http://127.0.0.1:4723

The framework connects to the Appium server but does not manage its lifecycle.

Running Tests

Run the full suite:

mvn clean test

Run smoke tests:

mvn clean test -Dsuite=testng-smoke.xml

Override a configuration value:

mvn clean test -Dwait.timeout.seconds=30

TestNG suite files are located in:

src/test/resources/

Individual tests can also be run from IntelliJ IDEA using TestNG.

Reporting

Each test run generates:

reports/
├── extent/
│   ├── report-<timestamp>.html
│   └── latest-report.html
├── screenshots/
└── logs/
└── test-run.log

The ExtentReports HTML report contains test status, groups, execution details, failure information, and failure screenshots.

Screenshots are captured automatically when a test fails.

Surefire reports are available under:

target/surefire-reports/

reports/ is git-ignored.

Test Coverage
Test Class	Coverage
AuthenticationTests	Valid login, invalid input validation, logout
NavigationTests	Drawer navigation, product details, empty cart
CartTests	Add product, cart contents/prices, multiple products, remove product
CheckoutTests	Guest checkout login requirement, address validation, successful checkout
EndToEndTests	Guest shopping journey through checkout, confirmation, cart cleanup and logout

TestNG groups:

smoke
regression
e2e
Project Structure
sdet-mobile-automation/
├── pom.xml
├── README.md
├── .gitignore
├── local.properties.example
├── reports/
└── src/
├── main/
│   ├── java/com/sdet/mobile/
│   │   ├── config/
│   │   ├── driver/
│   │   ├── model/
│   │   ├── pages/
│   │   └── utils/
│   └── resources/config/config.properties
└── test/
├── java/com/sdet/mobile/
│   ├── tests/
│   ├── listeners/
│   └── testdata/
└── resources/
├── testng.xml
├── testng-smoke.xml
└── logback.xml
Framework Design
Page Object Model – page objects contain locators, waits, UI interactions, and navigation; tests contain business flows and assertions.
Page Factory – Appium @AndroidFindBy annotations are used for page elements.
Explicit waits – synchronization is handled through WebDriverWait; tests do not use Thread.sleep() or implicit waits.
Driver management – DriverManager creates a new Appium session for each test and stores it in ThreadLocal.
Test isolation – noReset=false ensures each test starts from a clean application state.
Configuration – defaults are stored in config.properties and can be overridden locally or through Maven system properties.
Reporting – TestListener integrates TestNG with ExtentReports and captures screenshots on failure.
Known Application Issues
Login accepts arbitrary credentials

The application accepts arbitrary username/password combinations and proceeds to the authenticated state.

The invalid-credentials test case was removed from the data set because the application does not reject those credentials.

Validation scenarios such as missing username or missing password remain covered by the automated tests.

Application crashes when opening another product

After adding one product to the cart, returning to the catalogue, and opening another product, the application can crash.

The issue was reproduced on:

Android 17 / API 37 / 16 KB emulator
Android 14 / API 34 / 4 KB emulator

The scenario remains covered by CartTests because it exposes an application defect rather than hiding it with a test workaround.

Configuration Reference
Key	Default
appium.server.url	http://127.0.0.1:4723
platform.name	Android
automation.name	UiAutomator2
device.name	emulator-5554
app.package	com.saucelabs.mydemoapp.android
app.activity	.view.activities.SplashActivity
app.path	Empty
wait.timeout.seconds	15
reports.dir	reports
Troubleshooting
Problem	Check
Appium session cannot be created	Appium is running, adb devices shows the device, and device.name matches
Locator timeout	Inspect the current hierarchy with Appium Inspector and update the locator in the owning page object
No Extent report	Run the TestNG suite XML or register TestListener when running a class directly from the IDE
Tests start with previous state	Verify noReset=false and restart the test session
Keyboard hides a control	Check the emulator keyboard configuration
Future Improvements
Additional payment-form validation
Parallel execution across multiple Android devices
CI execution with an Android emulator
API-assisted test setup for faster preconditions
Additional checkout and product scenarios
Historical CI reporting