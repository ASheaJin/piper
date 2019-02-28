
package com.syswin.pipeline;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhz
 * @date 2018-12-16
 */
@RestController
public class HealthController {
    @GetMapping(value = "/healthchk")
    public String healthCheck() {
        return "healthchk is ok";
    }
}
