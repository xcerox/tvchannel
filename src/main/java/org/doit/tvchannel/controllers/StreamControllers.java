package org.doit.tvchannel.controllers;

import org.doit.tvchannel.services.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class StreamControllers extends BaseController {

    @Autowired
    private StreamService streamService;

    @GetMapping(value = "videos/{title}", produces = "video/mp4")
    public Mono<Resource> findVideo(@PathVariable String title, @RequestHeader("Range") String range){

        return streamService.findVideo(title);
    }

    @GetMapping(value = "videos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<String>>> findAllVideos() {
        return streamService.findAllVideosName()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).build()));
    }

    @PostMapping(value = "videos/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> uploadVideo(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .filter(filePart -> filePart.filename().endsWith(".mp4"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be an MP4 video")))
                .flatMap(filePart ->  streamService.saveVideo(filePart));

    }

    @GetMapping(value = "videos/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<String>> streamVideoList() {
        return streamService.streamVideoList();
    }
}
