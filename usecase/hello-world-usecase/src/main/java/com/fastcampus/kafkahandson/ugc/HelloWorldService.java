package com.fastcampus.kafkahandson.ugc;

public class HelloWorldService implements HelloWorldUsecase {

    @Override
    public String getHelloWorldString() {
        return "Hello World ~ !";
    }
}
