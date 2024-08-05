package step_definitions.googleSearch;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoogleSearchSteps
{
    static final Logger LOG = LogManager.getLogger(GoogleSearchSteps.class);

    @Given("I am on the google search page")
    public void iAmOnTheGoogleSearchPage()
    {
        LOG.info("THIS IS A TEST");
        System.out.println("TESTING 1");
    }

    @Then("I can see the search box")
    public void iCanSeeTheSearchBox()
    {
        LOG.info("THIS IS A TEST");
        System.out.println("TESTING 2");
    }

    @And("I run a search")
    public void iRunASearch()
    {
        LOG.info("THIS IS A TEST");
        System.out.println("TESTING 3");
    }

    @Then("I search results are returned")
    public void iSearchResultsAreReturned()
    {
        LOG.info("THIS IS A TEST");
        System.out.println("TESTING 4");
    }
}
