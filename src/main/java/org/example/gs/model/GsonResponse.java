package org.example.gs.model;

import lombok.Data;

@Data
public class GsonResponse<T> {
    String result;
    T response;
}
