package net.pkhapps.idispatch.core.app.auth;

import io.grpc.stub.StreamObserver;
import net.pkhapps.idispatch.core.grpc.proto.auth.Auth;
import net.pkhapps.idispatch.core.grpc.proto.auth.AuthenticationServiceGrpc;

/**
 *
 */
public class AuthenticationServiceImpl extends AuthenticationServiceGrpc.AuthenticationServiceImplBase {

    @Override
    public void initAuthentication(Auth.AuthenticationRequest request, StreamObserver<Auth.AuthenticationChallenge> responseObserver) {
        super.initAuthentication(request, responseObserver);
    }

    @Override
    public void completeAuthentication(Auth.AuthenticationResponse request, StreamObserver<Auth.AuthenticationOutcome> responseObserver) {
        super.completeAuthentication(request, responseObserver);
    }
}
