package com.ruoyi.nursing.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;

/**
 * OpenAI-compatible elderly-care Q&A service.
 *
 * <p>Configure {@code zzyl.ai.api-key} to enable online model calls. When the
 * key is empty, the service falls back to local business knowledge so the demo
 * can still run normally.</p>
 */
@Service
public class AiConsultService
{
    private static final String SYSTEM_PROMPT =
            "你是中州养老管理系统的智能问答助手小智。请使用简洁、温和、专业的中文回答，"
                    + "优先解答养老院服务、护理项目、护理计划、探访预约、老人照护和健康管理问题。"
                    + "涉及疾病诊断或紧急情况时，提醒用户及时联系医生或拨打急救电话，不得替代专业医疗诊断。";

    @Value("${zzyl.ai.api-name:DashScope}")
    private String apiName;

    @Value("${zzyl.ai.api-key:}")
    private String apiKey;

    @Value("${zzyl.ai.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    @Value("${zzyl.ai.model:qwen-plus}")
    private String model;

    public JSONObject chat(String prompt)
    {
        JSONObject result = new JSONObject();
        result.put("apiName", apiName);
        result.put("model", model);

        if (StringUtils.isBlank(prompt))
        {
            result.put("content", "请告诉我您想咨询的养老服务问题。");
            result.put("mode", "local");
            return result;
        }

        if (StringUtils.isNotBlank(apiKey))
        {
            try
            {
                result.put("content", callModel(prompt));
                result.put("mode", "online");
                return result;
            }
            catch (Exception ex)
            {
                result.put("content", localAnswer(prompt) + "\n\n当前" + apiName + "连接暂时不可用，以上为本地知识库回答。");
                result.put("mode", "fallback");
                return result;
            }
        }

        result.put("content", localAnswer(prompt));
        result.put("mode", "local");
        return result;
    }

    private String callModel(String prompt) throws Exception
    {
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("temperature", 0.4);
        body.put("stream", false);

        JSONArray messages = new JSONArray();
        messages.add(new JSONObject().fluentPut("role", "system").fluentPut("content", SYSTEM_PROMPT));
        messages.add(new JSONObject().fluentPut("role", "user").fluentPut("content", prompt));
        body.put("messages", messages);

        String endpoint = baseUrl.endsWith("/") ? baseUrl + "chat/completions" : baseUrl + "/chat/completions";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(45))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300)
        {
            throw new IllegalStateException(apiName + " returned " + response.statusCode());
        }

        JSONObject responseBody = JSONObject.parseObject(response.body());
        return responseBody.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    private String localAnswer(String prompt)
    {
        String question = prompt.toLowerCase();
        if (question.contains("预约") || question.contains("探访") || question.contains("来访"))
        {
            return "可以在“来访管理-预约登记”中新建参观或探访预约。预约处于“待上门”状态时，可点击“到院”，确认到院后系统会自动生成来访登记记录。";
        }
        if (question.contains("护理项目") || question.contains("洗头") || question.contains("助浴"))
        {
            return "中州养老提供助浴、助餐、洗脸、洗头、足部护理、修剪指甲、翻身拍背和康复运动等护理项目。可在“服务管理-护理项目”中查看价格、服务时长和护理要求。";
        }
        if (question.contains("护理计划") || question.contains("护理等级"))
        {
            return "护理等级用于区分老人的照护强度，并关联相应护理计划。管理员可在“服务管理”中维护护理计划的项目、执行时间、周期和频次。";
        }
        if (question.contains("护理员") || question.contains("负责人") || question.contains("负责老人"))
        {
            return "在“服务管理-负责老人”页面可按楼层查看床位和老人，并为单个老人或整间房批量设置护理员；每位老人最多可选择4名护理员。";
        }
        if (question.contains("费用") || question.contains("价格") || question.contains("账单"))
        {
            return "护理费用由护理等级基础费用和实际护理项目组成。账单、预缴款、余额和欠费信息可在“财务统计”模块查询。";
        }
        if (question.contains("急") || question.contains("胸痛") || question.contains("昏迷") || question.contains("呼吸"))
        {
            return "这可能属于紧急健康情况，请立即通知现场医护人员，并根据情况拨打120。智能问答不能替代医生诊断。";
        }
        return "您好，我是中州养老智能助手小智。我可以解答护理项目、护理计划、护理等级、探访预约、负责老人、财务账单和养老服务流程等问题。您可以换一种更具体的方式提问。";
    }
}
