package com.iotguard.controller;

import com.iotguard.agent.AutoInspector;
import com.iotguard.agent.DataSimulator;
import com.iotguard.agent.NlpCommandParser;
import com.iotguard.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    @Autowired
    private DataSimulator dataSimulator;
    @Autowired
    private NlpCommandParser nlpCommandParser;
    @Autowired
    private AutoInspector autoInspector;

    @PostMapping("/simulator/start")
    public Result<String> startSimulator() {
        dataSimulator.start();
        return Result.ok("数据模拟器已启动");
    }

    @PostMapping("/simulator/stop")
    public Result<String> stopSimulator() {
        dataSimulator.stop();
        return Result.ok("数据模拟器已停止");
    }

    @PostMapping("/simulator/interval")
    public Result<String> setInterval(@RequestBody Map<String, Integer> body) {
        int interval = body.get("interval");
        dataSimulator.setInterval(interval);
        return Result.ok("采集频率已设置为" + interval + "秒");
    }

    @PostMapping("/query")
    public Result<String> query(@RequestBody Map<String, String> body) {
        String command = body.get("command");
        return Result.ok(nlpCommandParser.parse(command));
    }

    @PostMapping("/inspect")
    public Result<String> inspect() {
        return Result.ok(autoInspector.inspect());
    }
}
