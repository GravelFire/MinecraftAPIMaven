package vsauko.mineplayapi.api.utility.mojang;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.RandomStringUtils;
import vsauko.mineplayapi.api.utility.AsyncUtil;
import vsauko.mineplayapi.api.utility.JsonUtil;

@UtilityClass
public class MojangUtil {

    private final String UUID_URL_STRING = "https://api.mojang.com/users/profiles/minecraft/";
    private final String SKIN_URL_STRING = "https://sessionserver.mojang.com/session/minecraft/profile/";

    private final Map<String, MojangSkin> mojangSkinMap = new HashMap<>();


    private String readURL(String url) throws IOException, ExecutionException, InterruptedException {
        return AsyncUtil.supplyAsyncFuture(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", RandomStringUtils.randomAlphanumeric(16));
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoOutput(true);
                StringBuilder output = new StringBuilder();

                try (InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                    while (bufferedReader.ready()) {
                        output.append(bufferedReader.readLine());
                    }
                }
                return output.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SneakyThrows
    public MojangSkin getMojangSkin(@NonNull String playerSkin) {
        MojangSkin mojangSkin = mojangSkinMap.get(playerSkin.toLowerCase());

        if (mojangSkin != null && !mojangSkin.isExpired()) {
            return mojangSkin;
        }

        String playerUUID = JsonUtil.parse(MojangUtil.readURL(UUID_URL_STRING + playerSkin))

                .getAsJsonObject()
                .get("id")
                .getAsString();

        String skinUrl = MojangUtil.readURL(SKIN_URL_STRING + playerUUID + "?unsigned=false");

        JsonObject textureProperty = JsonUtil.parse(skinUrl)
                .getAsJsonObject()

                .get("properties")
                .getAsJsonArray()

                .get(0)
                .getAsJsonObject();

        String texture = textureProperty.get("value").getAsString();
        String signature = textureProperty.get("signature").getAsString();

        mojangSkin = new MojangSkin(playerSkin, playerUUID, texture, signature, System.currentTimeMillis());

        mojangSkinMap.put(playerSkin.toLowerCase(), mojangSkin);
        return mojangSkin;
    }

    public String getOriginalName(@NonNull String playerName) {
        try {
            return JsonUtil.parse(MojangUtil.readURL(UUID_URL_STRING + playerName))

                    .getAsJsonObject()
                    .get("name")
                    .getAsString();
        }

        catch (IOException | ExecutionException | InterruptedException exception) {
            return playerName;
        }
    }

}
