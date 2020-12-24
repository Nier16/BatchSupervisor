package fr.ag2rlamondiale.espacetiers.controller;

import fr.ag2rlamondiale.espacetiers.client.PlanificationClient;
import fr.ag2rlamondiale.espacetiers.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    PlanificationClient pc;

    @GetMapping("test")
    public String test(){
        new SupervisorService().traitement(pc);
        return "Test started";
    }
}
