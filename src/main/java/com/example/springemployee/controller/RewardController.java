package com.example.springemployee.controller;

import com.example.springemployee.entity.Employee;
import com.example.springemployee.entity.Reward;
import com.example.springemployee.exception.FieldErrorResultMsg;
import com.example.springemployee.service.EmployeeService;
import com.example.springemployee.service.RewardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")

public class RewardController {
    private final RewardService rewardService;
    private final EmployeeService employeeService;
    private static final String VIEWS_REWARD_MANAGE = "html/pages-reward";
    private static final String VIEWS_REWARD_STATISTIC = "html/pages-personal";

    public RewardController(RewardService rewardService, EmployeeService employeeService) {
        this.rewardService = rewardService;
        this.employeeService = employeeService;
    }

    @GetMapping("/manage/pages-reward")
    public String pageManageReward(@RequestParam(defaultValue = "1") int pageCurrent,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        Page<Reward> rewardPage = rewardService.getAllDetailPageBySearchEmpSearchNameLike(pageCurrent, size, search);
        return addPaginationModel(model, rewardPage,VIEWS_REWARD_MANAGE);
    }
    @GetMapping("/statistic/pages-personal")
    public String pageStatisticRewardByEmployee(@RequestParam(defaultValue = "1") int pageCurrent,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(defaultValue = "") String search, Model model) {
        model.addAttribute("pageCurrent", pageCurrent);
        model.addAttribute("size", size);
        model.addAttribute("search", search);
        Page<Reward> rewardPage = rewardService.getAllDetailPageBySearchEmpSearchNameLike(pageCurrent, size, search);
        return addPaginationModel(model, rewardPage,VIEWS_REWARD_STATISTIC);
    }

    private String addPaginationModel(Model model, Page<Reward> paginated,String RETURN_PAGE) {
        List<Reward> listOwners = paginated.getContent();
        model.addAttribute("totalPages", paginated.getTotalPages());
        model.addAttribute("totalItems", paginated.getTotalElements());
        model.addAttribute("list", listOwners);
        return RETURN_PAGE;
    }

    @PostMapping("/manage/add-reward")
    public String addUser(@RequestParam int pageCurrent,
                          @RequestParam int size,
                          @RequestParam String search,
                          HttpServletRequest request,
                          @Valid Reward reward, BindingResult result, Model model) {
        String employeeId = request.getParameter("employeeId");
        String errorFiled = FieldErrorResultMsg.getMsgError(result);
        if (employeeId.isEmpty() || employeeId ==null || employeeId.equals("0")){
            model.addAttribute("error","*You need to add an Employee for reward!");
            return pageManageReward(pageCurrent, size, search, model);
        }
        if (!errorFiled.isEmpty()) {
            model.addAttribute("error",errorFiled);
            return pageManageReward(pageCurrent, size, search, model);
        }

        if (!result.hasErrors() || !employeeId.isEmpty() || employeeId !=null || !employeeId.equals("0")) {
            model.addAttribute("add_success", "Add Success!");
        }
        employeeService.updateStatusEmployee(reward.getEmployeeId(),1);
        rewardService.insertOrUpdateReward(reward);
        return pageManageReward(pageCurrent, size, search, model);
    }

    @PostMapping("/manage/edit-reward")
    public String editReward(@RequestParam int pageCurrent,
                              @RequestParam int size,
                              @RequestParam String search,
                              @Valid Reward reward, BindingResult result, Model model) {
        String errorFiled = FieldErrorResultMsg.getMsgError(result);
        if (!errorFiled.isEmpty()) {
            model.addAttribute("error",errorFiled);
            return pageManageReward(pageCurrent, size, search, model);
        }

        if (!result.hasErrors()) {
            model.addAttribute("edit_success", "Edit Success!");
        }
        rewardService.insertOrUpdateReward(reward);
        return pageManageReward(pageCurrent, size, search, model);
    }

    @GetMapping("/manage/del-reward")
    public String delReward(@RequestParam(defaultValue = "1") int pageCurrent,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "") String search,
                             @RequestParam int id, Model model) {
        employeeService.updateStatusEmployee(rewardService.findByIdReward(id).getEmployee().getId(),0);
        rewardService.deleteByIdReward(id);
        return pageManageReward(pageCurrent, size, search, model);
    }

    @ModelAttribute("USER")
    public Reward init() {
        return new Reward();
    }
    @ModelAttribute("EMP")
    public List<Employee> getALLEmp(){
        return employeeService.getAllEmployee();
    }
}
