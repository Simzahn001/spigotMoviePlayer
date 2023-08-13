package me.simzahn.spigotmovieplayer.pixelStackerRequests;

import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A class to retrieve schematics from the pixelstacker api by sending a post request with a multipart entity.
 * The request is sent to the url specified in the url variable.
 * The request is sent with the following parameters:
 * Format: Schematic
 * IsSideView: false
 * IsMultiLayer: true
 * EnableShadows: false
 * MaxHeight: 10
 * MaxWidth: 10
 * RgbBucketSize: 1
 * EnableDithering: false
 * The image is sent as a file in the multipart entity.
 * The response is a schematic file.
 * The response is saved to the file specified in the retrieveSchematic method.
 * <br>
 * @see <a href="https://github.com/Pangamma/PixelStacker">PixelStacker Github</a>
 */
public class ImageRequest {

    private static final String urlPattern = "https://taylorlove.info/projects/pixelstacker/api/Render/ByFileAdvanced?" +
            "Format=Schematic&IsSideView=false&IsMultiLayer=true&EnableShadows=false&MaxHeight=${MaxHeight}&MaxWidth=${MaxWidth}&" +
            "RgbBucketSize=1&EnableDithering=false";

    private final CloseableHttpClient client = HttpClients.createDefault();
    private final ClassicHttpRequest request;
    private final String url;

    private ClassicHttpResponse response;

    /**
     * Creates a new ImageRequest. The request is not sent until the sendRequest method is called. The response is not
     * retrieved until the retrieveSchematic method is called. The response is saved to the file specified in the
     * retrieveSchematic method.
     * @param image The image to send to the api.
     * @param maxWidth The maximum width of the schematic.
     * @param maxHeight The maximum height of the schematic.
     */
    public ImageRequest(File image, int maxWidth, int maxHeight) {

        //setup url
        url = urlPattern
                .replace("${MaxHeight}", String.valueOf(maxHeight))
                .replace("${MaxWidth}", String.valueOf(maxWidth));

        FileBody fileBody = new FileBody(image, ContentType.DEFAULT_BINARY);

        //build the multipart entity
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.EXTENDED);
        entityBuilder.addBinaryBody("file", image);
        HttpEntity multipartHttpEntity = entityBuilder.build();

        //build the request
        ClassicRequestBuilder requestBuilder = ClassicRequestBuilder.post(this.url);
        requestBuilder.setEntity(multipartHttpEntity);
        request = requestBuilder.build();

    }

    /**
     * Sends the request to the api. The response is saved in the response variable. The response is not retrieved until
     * the {@link ImageRequest#retrieveSchematic(File) retrieveSchematic} method is called.
     * @return True if the request was sent successfully, false otherwise.
     */
    public boolean sendRequest() {

        try {
            response = client.execute(request);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns true if the request was sent successfully and the response code is 200. If the request was not sent, the
     * method will return false.
     * @return True if the request was sent successfully and the response code is 200. If the request was not sent, the
     */
    public boolean isSuccessful() {
        return response != null && response.getCode() == 200;
    }

    /**
     * Retrieves the schematic from the response and saves it to the file specified. If the request was not sent or the
     * response code is not 200, the method will return an empty optional.
     * @param file The file to save the schematic to.
     * @return An optional containing the path to the schematic file if the request was sent successfully and the response
     */
    public Optional<Path> retrieveSchematic(File file) {

        try {
            file.createNewFile();
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        if (response == null) {
            return Optional.empty();
        }

        try (final OutputStream outStream = new FileOutputStream(file)) {
            response.getEntity().writeTo(outStream);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(Path.of(file.getAbsolutePath()));


    }

}
