package in.clear.ap.india.unstructuredinputconsumer.clients;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class InvoiceAPIWebConfig {

    @Value("${module.invoiceApi.baseUrl}")
    private String baseUrl;


    @SneakyThrows
    @Bean
    InvoiceApiClient getClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultStatusHandler(
                        httpStatusCode -> HttpStatus.NOT_FOUND == httpStatusCode,
                        response -> Mono.empty())
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new InternalError(String.valueOf(response.statusCode().value()))))
                .build();

        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .build()
                .createClient(InvoiceApiClient.class);
    }
}
