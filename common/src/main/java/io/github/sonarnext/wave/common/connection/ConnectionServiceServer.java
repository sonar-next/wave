package io.github.sonarnext.wave.common.connection;

import com.google.protobuf.compiler.PluginProtos;
import io.github.sonar.next.wave.EnumProto;
import io.github.sonar.next.wave.PollTaskRequest;
import io.github.sonar.next.wave.PollTaskResponse;
import io.github.sonar.next.wave.TaskServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

public class ConnectionServiceServer {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceServer.class.getName());


    public static class TaskService extends TaskServiceGrpc.TaskServiceImplBase {

        @Override
        public void pollTask(PollTaskRequest request, StreamObserver<PollTaskResponse> responseObserver) {
            logger.info("pollTask");
            responseObserver.onNext(PollTaskResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }


}
