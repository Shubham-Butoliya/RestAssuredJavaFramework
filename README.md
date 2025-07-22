# 🧪 REST API Automation Framework

This is a robust and scalable API automation testing framework built using **Java**, **REST Assured**, **TestNG**, **POJOs**, **Lombok**, and **Excel (via Poiji)** for data-driven testing. It supports detailed **reporting** with **Extent Reports** and **Allure Reports**.

---
## ⭐ Star this repo if you find it useful!

## 🚀 Tech Stack

- **Java 17**
- **TestNG**
- **REST Assured**
- **Lombok**
- **Jackson (for POJO serialization/deserialization)**
- **Maven**
- **Extent Reports**
- **Allure Reports**

---

## 📁 Project Structure

- **src/main/java**
    - `com.api.framework.restutils` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# utility class required for rest API operations or logging
    - `com.api.framework.utils` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# utilities like Confi read, reporting
    
- **src/main/resources** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# can be used store config

- **src/test/java**
    - `com.api.framework.users.listeners` &nbsp;&nbsp;# TestNG Listeners (Extent, Retry, Skip, etc.)
    - `com.api.framework.tests` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# test case
    - `com.api.framework.testUtils` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# assertion util
    - `com.api.framework.pojo` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# pojo classes

- **src/test/resources**
    - `schema` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# schema files for schema validation
    - `testdata` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# can keep data required for testing (excel, json)

- **test-output** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Extent report
- **pom.xml** &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# Maven dependencies
- **testng.xml**
- **README.md**


---

## ✅ Features

- Supports **GET**, **POST**, **PUT**, **DELETE** methods
- **POJO-based request/response** modeling with Jackson
- **Parallel test execution** using TestNG
- **Retry logic** for flaky APIs
- **Group-based test execution** (e.g., sanity, regression)
- **Extent Report** with request/response logging
- **Allure report**
- **Environment-specific configuration** (via `.properties`)

---

## 🔧 How to Run

### Run all tests:

```bash
mvn clean test
mvn clean test -Dgroups=regression   ->> this will run all the test case which has been marked as group=regression
```
group execution can be managed through testng.xml as well

## 🧠 Data-Driven Testing
- Use Excel/JSON files as external data sources. You can implement @DataProvider or utility methods to read data.

## 📜 Logging
- ExtentLogger.logInfo() - logs text messages in Extent Reports
- ExtentLogger.logJson() - pretty prints request/response payload
- Custom masking for sensitive tokens (e.g., Bearer tokens)

## 📂 Reports

**After execution:**
- Navigate to: test-output/ExtentReport.html to view the interactive Extent report 
- To generate allure report 
  - the `allure-results` folder will be generated in your **project root directory**
  - Run the following command: ```allure serve allure-results``` (make sure you have downloaded allure into your system)
  - This will open the allure report in browser
