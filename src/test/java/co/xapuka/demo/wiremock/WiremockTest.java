package co.xapuka.demo.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;


class WiremockTest {

    private WireMockServer wireMockServer;
    private WireMock wireMock;

    public WiremockTest() {
        wireMockServer = new WireMockServer(8080);
        wireMock = new WireMock("xapuka.com", 8080);
    }


    @BeforeEach
    public void setup() {
        wireMockServer.start();
    }

    @AfterEach
    public void tierdown() {
        wireMockServer.stop();
    }

    @Test
    public void smoke() throws IOException {
        wireMock.register(WireMock.get(WireMock.urlEqualTo("/stuff"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello world!")));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://xapuka.com:8080/stuff")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        MatcherAssert.assertThat(response.body().string(), equalTo("Hello world!"));
    }
}