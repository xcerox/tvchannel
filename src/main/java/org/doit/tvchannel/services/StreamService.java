package org.doit.tvchannel.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class StreamService {
    private final Sinks.Many<List<String>> videoSink;

    private String storagePath = null;

    public StreamService(@Value("${video.storage.path}") String storagePath) {
        this.storagePath = storagePath;
        this.videoSink = Sinks.many().replay().latest();
        notifyVideoListChange();
    }

    public Mono<Resource> findVideo(String title) {
        return Mono.fromSupplier(() ->new FileSystemResource(Paths.get(storagePath+ title+ ".mp4")));
    }

    public Mono<Void> saveVideo(FilePart filePart) {
        Mono<Void> result = null;
        try {
            Path savePath = Paths.get(storagePath);
            result = filePart.transferTo(savePath.resolve(filePart.filename()));
            notifyVideoListChange();
        } catch (Exception e) {
            result = Mono.error(new RuntimeException("Could not create video storage directory", e));
        }

        return result;
    }

    public Mono<List<String>>findAllVideosName() {
        try {
            Path path = Paths.get(storagePath);
            var names =  Files.walk(path, 1)
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString().replace(".mp4", ""))
                    .toList();
            return Mono.just(names);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Could not load video list", e));
        }
    }

    public Flux<List<String>> streamVideoList() {
        return videoSink.asFlux();
    }
    public void notifyVideoListChange() {
        findAllVideosName().subscribe(videoSink::tryEmitNext);
    }

}
