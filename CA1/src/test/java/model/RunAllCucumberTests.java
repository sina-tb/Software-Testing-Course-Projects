package model;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/resources",
        plugin = {"pretty", "html:target/cucumberReports.html"},
        glue = "model"
)
public class RunAllCucumberTests {
}