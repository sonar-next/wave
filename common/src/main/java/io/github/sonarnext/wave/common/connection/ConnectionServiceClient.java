package io.github.sonarnext.wave.common.connection;

import io.github.sonar.next.wave.ConnectionLogProto;
import io.github.sonar.next.wave.PollTaskRequest;
import io.github.sonar.next.wave.TaskServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.ConnectionClientTransport;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceClient.class);

//    private final ManagedChannel channel;
    private final TaskServiceGrpc.TaskServiceStub taskServiceStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public ConnectionServiceClient(Channel channel) {
        taskServiceStub = TaskServiceGrpc.newStub(channel);
    }

    public void greet(String name) {
        logger.info("ConnectionServiceClient greet");
        PollTaskRequest request = PollTaskRequest.newBuilder()
                .setTime(System.currentTimeMillis())
                .build();
        io.grpc.stub.StreamObserver<io.github.sonar.next.wave.PollTaskResponse> responseObserver = new StreamObserver() {
            @Override
            public void onNext(Object o) {

                logger.info("{}", o);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("error", throwable);
            }

            @Override
            public void onCompleted() {

            }
        };

        taskServiceStub.pollTask(request, responseObserver);

        responseObserver.onCompleted();
    }

}
