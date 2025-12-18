package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.dtos.size.SizeDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeCreateDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeUpdateDto;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.SizeService;
import az.edu.itbrains.ecommerce.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/size")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;


    @GetMapping
    public String index(Model model){
        List<SizeDto> sizeDtoList = sizeService.getAllSizes();
        model.addAttribute("sizes", sizeDtoList);
        return "admin/size/index.html";
    }

    @GetMapping("/create")
    public String create(){
        return "admin/size/create.html";
    }

    @PostMapping("/create")
    public String create(SizeCreateDto sizeCreateDto){
        boolean result = sizeService.saveSize(sizeCreateDto);
        return "redirect:/dashboard/size";
    }


    @GetMapping("/update/{id}")
    public String edit(@PathVariable Long id, Model model){
        SizeUpdateDto sizeUpdateDto = sizeService.findUpdatedSize(id);
        model.addAttribute("size", sizeUpdateDto);
        return "admin/size/update.html";
    }

    @PostMapping("/update/{id}")
    public String edit(@PathVariable Long id, SizeUpdateDto sizeUpdateDto){
        boolean result = sizeService.updateSize(id, sizeUpdateDto);
        return "redirect:/dashboard/size";
    }




    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return "admin/size/delete.html";
    }

}
