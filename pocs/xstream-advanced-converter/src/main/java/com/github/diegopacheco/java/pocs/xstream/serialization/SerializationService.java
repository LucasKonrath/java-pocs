package com.github.diegopacheco.java.pocs.xstream.serialization;

public interface SerializationService {
    <T> T deserialize(String xml);
    <T> String serialize(T pojo);
}