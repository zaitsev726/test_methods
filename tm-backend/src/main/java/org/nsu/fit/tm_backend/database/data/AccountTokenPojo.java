package org.nsu.fit.tm_backend.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.nsu.fit.tm_backend.shared.Authority;

import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountTokenPojo {
    @JsonProperty("id")
    public UUID id;

    @JsonProperty("authorities")
    public Set<String> authorities;

    @JsonProperty("token")
    public String token;
}
