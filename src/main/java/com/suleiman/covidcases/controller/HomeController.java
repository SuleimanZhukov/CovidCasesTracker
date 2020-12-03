package com.suleiman.covidcases.controller;

import com.suleiman.covidcases.model.Stats;
import com.suleiman.covidcases.service.CovidCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CovidCasesService covidCasesService;

    @GetMapping("/")
    public String home(Model model) {
        List<Stats> allStats = covidCasesService.getStats();
        int allCases = allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum();
        model.addAttribute("stats", allStats);
        model.addAttribute("allCases", allCases);
        return "home";
    }
}
