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

		queryHandler = handler;

		JsonObject jsonRoot = new JsonObject();
		JsonArray contentsArray = new JsonArray();
		JsonObject contentObj = new JsonObject();
		JsonArray partsArray = new JsonArray();
		JsonObject partObj = new JsonObject();

		partObj.addProperty("text", content);

		partsArray.add(partObj);
		contentObj.add("parts", partsArray);
		contentsArray.add(contentObj);

		jsonRoot.add("contents",  contentsArray);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String body = gson.toJson(jsonRoot);

		var request = HttpRequest.newBuilder()
		.uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"))
		.header("Content-Type", "application/json")
		.header("X-goog-api-key", apiKey)
		.POST(HttpRequest.BodyPublishers.ofString(body))
		.build();
		var client = HttpClient.newHttpClient();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body)
			.thenAccept((json) -> {
				System.out.println("Server Responded!");
				JsonElement jsonTree = JsonParser.parseString(json);
				queryHandler.handle(jsonTree
					.getAsJsonObject()
					.getAsJsonArray("candidates")
					.get(0)
					.getAsJsonObject()
					.get("content")
					.getAsJsonObject()
					.getAsJsonArray("parts")
					.get(0)
					.getAsJsonObject()
					.get("text")
					.getAsString());
			});
	}

	@FunctionalInterface
	interface QueryHandler {
		void handle(String json);
	}
}
