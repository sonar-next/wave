package io.github.sonarnext.wave.server.webhook;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/wave/webhook")
@RestController
public class WebHookController {

    private final WebHookService webHookService;

    public WebHookController(WebHookService webHookService) {
        this.webHookService = webHookService;
    }

    @PostMapping("/gitlab")
    public String handleWebHook(HttpServletRequest request) {
        return this.webHookService.receive(request, WebHookTypeEnum.GITLAB);
    }

    @PostMapping("/github")
    public String handleWebHookWithGitLab(HttpServletRequest request) {
        return this.webHookService.receive(request, WebHookTypeEnum.GITHUB);
    }


}
