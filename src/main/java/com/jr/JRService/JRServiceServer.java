package com.jr.JRService;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by JR on 2016/8/25.
 */
public class JRServiceServer {
  private static final Logger logger = Logger.getLogger(JRServiceServer.class.getName());

  /* The port on which the server should run */
  private int port = 50051;
  private Server server;

  private void start() throws IOException{
    server = ServerBuilder.forPort(port).addService(new JRServiceImpl()).build();
    server.start();
    logger.info("server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(){
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        JRServiceServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final JRServiceServer server = new JRServiceServer();
    server.start();
    server.blockUntilShutdown();
  }


  private static class JRServiceImpl extends JRServiceGrpc.JRServiceImplBase {
    @Override
    public void listSongs(SingerId request, StreamObserver<SongList> responseObserver) {
      SongList list = SongList.newBuilder().addAllSongs(genFakeSongs(request)).build();
      responseObserver.onNext(list);
      responseObserver.onCompleted();
    }

    @Override
    public void getSongs(SingerId request, StreamObserver<Song> responseObserver) {
      List<Song> songs = genFakeSongs(request);
      for (Song song: songs) {
        responseObserver.onNext(song);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          responseObserver.onError(e);
        }
      }
      responseObserver.onCompleted();
    }

    public List<Song> genFakeSongs(SingerId request) {
      Singer pf = Singer.newBuilder().setId(request.getId()).setName("Pink Floyd").build();
      List<Song> songs = new ArrayList<>();
      songs.add(Song.newBuilder().setId(1).setName("Wish You Were Here").setSinger(pf).build());
      songs.add(Song.newBuilder().setId(2).setName("The Dark Side of the Moon").setSinger(pf).build());
      songs.add(Song.newBuilder().setId(3).setName("The Wall").setSinger(pf).build());
      return songs;
    }
  }
}
