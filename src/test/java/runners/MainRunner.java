package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"src/test/java/resources/features"},
        glue = {"stepDefinitions"},
        plugin = {"pretty", "html:target/report.html", "json:target/report.json", "json:target/cucumber-reports/Cucumber. json"},
        monochrome = false,
        dryRun = false,
        tags = "@complete"
)
public class MainRunner extends AbstractTestNGCucumberTests
{
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios()
    {
        return super.scenarios();
    }
}