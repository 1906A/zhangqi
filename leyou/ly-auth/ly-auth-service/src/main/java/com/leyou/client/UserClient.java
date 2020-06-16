package com.leyou.client;

import com.leyou.api.UserClientServer;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient extends UserClientServer {




}
