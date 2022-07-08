package com.noslen.lsaadminservice.utli.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "random-greeting-service")
public interface AuthClient {
}
