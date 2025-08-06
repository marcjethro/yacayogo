import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QueryAI {
	static QueryHandler queryHandler;
	public static void asyncRequest(String content, QueryHandler handler) {
		String apiKey;

		try {
			File file = new File("API_KEY");
			Scanner scanner = new Scanner(file);
			apiKey = scanner.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		int max_tokens = 512;

		queryHandler = handler;

		JsonObject jsonRoot = new JsonObject();
		jsonRoot.addProperty("model",  "meta-llama/Llama-Vision-Free");
		JsonArray messagesArray = new JsonArray();
		JsonObject messageObj = new JsonObject();
		messageObj.addProperty("role", "user");
		messageObj.addProperty("content", content);
		messagesArray.add(messageObj);
		jsonRoot.add("messages",  messagesArray);
		jsonRoot.addProperty("max_tokens", max_tokens);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String body = gson.toJson(jsonRoot);

		var request = HttpRequest.newBuilder()
		.uri(URI.create("https://api.together.xyz/v1/chat/completions"))
		.header("Content-Type", "application/json")
		.header("Authorization", "Bearer " + apiKey)
		.POST(HttpRequest.BodyPublishers.ofString(body))
		.build();
		var client = HttpClient.newHttpClient();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept((json) -> {
				JsonElement jsonTree = JsonParser.parseString(json);
				queryHandler.handle(jsonTree
					.getAsJsonObject()
					.getAsJsonArray("choices")
					.get(0)
					.getAsJsonObject()
					.get("message")
					.getAsJsonObject()
					.get("content")
					.getAsString());
			});
	}

	@FunctionalInterface
	interface QueryHandler {
		void handle(String json);
	}
}
