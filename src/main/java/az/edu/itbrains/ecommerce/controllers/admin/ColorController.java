package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorCreateDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorUpdateDto;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ColorService;
import az.edu.itbrains.ecommerce.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/color")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;


    @GetMapping
    public String index(Model model){
        List<ColorDto> colorDtoList = colorService.getAllColors();
        model.addAttribute("colors", colorDtoList);
        return "admin/color/index.html";
    }

    @GetMapping("/create")
    public String create(){
        return "admin/color/create.html";
    }

    @PostMapping("/create")
    public String create(ColorCreateDto colorCreateDto){
        boolean result = colorService.saveColor(colorCreateDto);
        return "redirect:/dashboard/color";
    }


    @GetMapping("/update/{id}")
    public String edit(@PathVariable Long id, Model model){
        ColorUpdateDto colorUpdateDto = colorService.findUpdatedColor(id);
        model.addAttribute("color", colorUpdateDto);
        return "admin/color/update.html";
    }

    @PostMapping("/update/{id}")
    public String edit(@PathVariable Long id, ColorUpdateDto colorUpdateDto){
        boolean result = colorService.updateColor(id, colorUpdateDto);
        return "redirect:/dashboard/color";
    }




    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return "admin/color/delete.html";
    }

}
