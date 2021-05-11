package com.product.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.product.dao.DepartmentRepo;
import com.product.model.Department;

@Controller
public class DepartmentController {
	
	@Autowired
	private DepartmentRepo repo;
	
	@RequestMapping("/adddept")
	public String addDept(ModelMap model) {
		Department d = new Department();
		model.addAttribute("dept",d);
		return "add_dept";
	}
	
	@RequestMapping(value = "/adddept",method = RequestMethod.POST)
	public String addDept(@Valid Department d, Errors errors, ModelMap model) {
		try {
			if(errors.getErrorCount()>0)
				throw new RuntimeException(errors.toString());
			
			if(repo.findById(d.getId()).isPresent())
				throw new RuntimeException("Dept Id exists");
			
			repo.save(d);
			return "redirect:/deptlist";
		}catch(Exception e) {
			model.addAttribute("dept",d);
			model.addAttribute("message", e.getMessage());
			return "add_dept";
		}
	}
	
	@RequestMapping("/deptlist")
	public String getDeptList(ModelMap model) {
		model.addAttribute("depts", repo.findAll());
		return "deptlist";
	}
	
	@RequestMapping("/deletedept/{id}")
	public String deleteDept(@PathVariable("id") int id,ModelMap model) {
		Optional<Department> dept = repo.findById(id);
		if(dept.isPresent()) {
			repo.delete(dept.get());
			return "redirect:/deptlist";
		}else {
			model.addAttribute("error", "Department Id Not Found!");
			return "delete_dept";
		}
	}
	@RequestMapping("/editdept/{id}")
    public String editDept(@PathVariable("id") int id, ModelMap model) {
        Optional<Department> dept = repo.findById(id);
        if(dept.isPresent())
            model.addAttribute("dept", dept.get());
        else
            model.addAttribute("error", "department id not found");
        return "edit_dept";
    }
    @RequestMapping(value="/editdept/{id}", method=RequestMethod.POST)
    public String updateDept(@PathVariable("id") int id, Department d, ModelMap model) {
        try {
            Optional<Department> dept = repo.findById(d.getId());
            if(dept.isPresent())
                repo.save(d);
            else
                throw new Exception("invalid");
            return "redirect:/deptList";
        }catch(Exception ex) {
            model.addAttribute("dept",d);
            model.addAttribute("message", ex.getMessage());
            return "deptList";
        }
    }

}
