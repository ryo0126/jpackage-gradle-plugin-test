package resource;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;

public final class Resources implements Closeable {

    private static final Resources instance = new Resources();

    public static Resources getInstance() {
        return instance;
    }

    private FileSystem openedFileSystem;

    private Resources() {}

    public Path uriOf(String resourceName) {
        var url = Resources.class.getResource(resourceName);
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("URIの文法が不正です: " + e.getMessage());
        } catch (FileSystemNotFoundException ignore) {
            // ファイルシステムが見つからない場合は新しく作成する
            return createFileSystem(resourceName, url);
        }
    }

    @Override
    public void close() throws IOException {
        if (openedFileSystem == null) {
            return;
        }

        // ファイルシステムを開いている場合は閉じる
        openedFileSystem.close();
    }

    private Path createFileSystem(String resourceName, URL url) {
        var urlString = url.toString();
        var fileSystemUriStringWithExclamation = urlString.replaceAll(resourceName + "$", "");
        var fileSystemUriString = fileSystemUriStringWithExclamation.substring(0, fileSystemUriStringWithExclamation.length() - 1);
        var fileSystemUri = URI.create(fileSystemUriString);

        try {
            openedFileSystem = FileSystems.newFileSystem(fileSystemUri, new HashMap<>());
            return openedFileSystem.getPath(resourceName);
        } catch (IOException e) {
            throw new RuntimeException("ファイルシステムを作成できませんでした: " + e.getMessage());
        }
    }
}
