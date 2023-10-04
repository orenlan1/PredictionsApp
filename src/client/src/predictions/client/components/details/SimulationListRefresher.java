package predictions.client.components.details;

import javafx.scene.control.Alert;
import okhttp3.HttpUrl;
import predictions.client.util.http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static predictions.client.util.Constants.GSON_INSTANCE;
import static predictions.client.util.Constants.PREFIX_BASE_URL;

public class SimulationListRefresher extends TimerTask {

    private final Consumer<List<String>> usersListConsumer;
    private final Consumer<String> errorMessageConsumer;


    public SimulationListRefresher(Consumer<List<String>> usersListConsumer, Consumer<String> errorMessageConsumer) {
        this.usersListConsumer = usersListConsumer;
        this.errorMessageConsumer = errorMessageConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl.parse(PREFIX_BASE_URL + "/simulations/names").newBuilder().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                errorMessageConsumer.accept(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
                String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
