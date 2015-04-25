import org.junit.*;

import java.util.*;

import play.mvc.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class FunctionalTest {

    @Test
    public void redirectHomePage() {
        running(fakeApplication(), () -> {
            Result result = route(controllers.routes.Application.index());

            assertThat(result.status()).isEqualTo(SEE_OTHER);
            assertThat(result.header(LOCATION)).isEqualTo("/computers");
        });
    }
    
    @Test
    public void listComputersOnTheFirstPage() {
        running(fakeApplication(), () -> {
            Result result = route(controllers.routes.Application.list(0, "name", "asc", ""));

            assertThat(result.status()).isEqualTo(OK);
            assertThat(contentAsString(result)).contains("574 computers found");
        });
    }
    
    @Test
    public void filterComputerByName() {
        running(fakeApplication(), () -> {
            Result result = route(controllers.routes.Application.list(0, "name", "asc", "Apple"));

            assertThat(result.status()).isEqualTo(OK);
            assertThat(contentAsString(result)).contains("13 computers found");
        });
    }
    
    @Test
    public void createANewComputer() {
        running(fakeApplication(), () -> {
            Result result = route(controllers.routes.Application.save());

            assertThat(result.status()).isEqualTo(BAD_REQUEST);

            Map<String,String> data = new HashMap<>();
            data.put("name", "FooBar");
            data.put("introduced", "badbadbad");
            data.put("company.id", "1");

            Http.RequestBuilder request1 = fakeRequest(controllers.routes.Application.save());
            result = route(request1.bodyForm(data));

            assertThat(result.status()).isEqualTo(BAD_REQUEST);
            assertThat(contentAsString(result)).contains("<option value=\"1\" selected=\"selected\">Apple Inc.</option>");
            assertThat(contentAsString(result)).contains("<input type=\"date\" id=\"introduced\" name=\"introduced\" value=\"badbadbad\" />");
            assertThat(contentAsString(result)).contains("<input type=\"text\" id=\"name\" name=\"name\" value=\"FooBar\" />");

            data.put("introduced", "2011-12-24");

            Http.RequestBuilder request2 = fakeRequest(controllers.routes.Application.save());
            result = route(request2.bodyForm(data));

            assertThat(result.status()).isEqualTo(SEE_OTHER);
            assertThat(result.header(LOCATION)).isEqualTo("/computers");
            assertThat(result.flash().get("success")).isEqualTo("Computer FooBar has been created");

            result = route(controllers.routes.Application.list(0, "name", "asc", "FooBar"));
            assertThat(result.status()).isEqualTo(OK);
            assertThat(contentAsString(result)).contains("One computer found");

        });
    }
}
