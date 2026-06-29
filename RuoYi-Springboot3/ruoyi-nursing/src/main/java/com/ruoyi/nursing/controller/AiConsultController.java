package com.ruoyi.nursing.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.nursing.service.AiConsultService;

@RestController
@RequestMapping("/aiconsult/virtualagent")
public class AiConsultController extends BaseController
{
    @Autowired
    private AiConsultService aiConsultService;

    @PreAuthorize("@ss.hasPermi('aiconsult:virtualagent:view')")
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody Map<String, String> request)
    {
        return success(aiConsultService.chat(request.get("prompt")));
    }
}
