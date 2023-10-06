package predictions.client.components.requests;

import dto.AllocationDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import predictions.client.util.http.HttpClientUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static predictions.client.util.Constants.GSON_INSTANCE;
import static predictions.client.util.Constants.PREFIX_BASE_URL;

public class RequestsListRefresher extends TimerTask {

    private final Consumer<List<AllocationDTO>> allocationsListConsumer;

    public RequestsListRefresher(Consumer<List<AllocationDTO>> allocationsListConsumer) {
        this.allocationsListConsumer = allocationsListConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl.parse(PREFIX_BASE_URL + "/client/request").newBuilder().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                    alert.setHeaderText(null);
                    alert.show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() == 200) {
                    String jsonArrayOfAllocations = response.body().string();
                    AllocationDTO[] allocationDTOS = GSON_INSTANCE.fromJson(jsonArrayOfAllocations, AllocationDTO[].class);
                    allocationsListConsumer.accept(Arrays.asList(allocationDTOS));
                }
            }
        });
    }
}
