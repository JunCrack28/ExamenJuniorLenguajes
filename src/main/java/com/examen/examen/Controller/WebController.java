package com.examen.examen.Controller;
import com.examen.examen.Exception.BarcoConContenedoresException;
import com.examen.examen.Exception.CapacidadExcedidaException;
import com.examen.examen.Model.Barco;
import com.examen.examen.Model.Contenedor;
import com.examen.examen.Service.BarcoService;
import com.examen.examen.Service.ContenedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @Autowired
    private BarcoService barcoService;

    @Autowired
    private ContenedorService contenedorService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("barcos", barcoService.findAll());
        model.addAttribute("barco", new Barco());
        model.addAttribute("contenedor", new Contenedor());
        return "index";
    }

    @PostMapping("/barcos/save")
    public String saveBarco(@ModelAttribute Barco barco, Model model, RedirectAttributes redirectAttributes) {
        try {
            barcoService.save(barco);
            return "redirect:/";
        } catch (CapacidadExcedidaException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("barcos", barcoService.findAll());
            model.addAttribute("barco", barco);
            model.addAttribute("contenedor", new Contenedor());
            return "index";
        }
    }

    @GetMapping("/barcos/edit/{id}")
    public String editBarco(@PathVariable Long id, Model model) {
        return barcoService.findById(id)
                .map(barco -> {
                    model.addAttribute("barco", barco);
                    model.addAttribute("barcos", barcoService.findAll());
                    model.addAttribute("contenedor", new Contenedor());
                    return "index";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/barcos/delete/{id}")
    public String deleteBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.delete(id);
            return "redirect:/";
        } catch (BarcoConContenedoresException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/contenedores/save")
    public String saveContenedor(@ModelAttribute Contenedor contenedor, Model model, RedirectAttributes redirectAttributes) {
        try {
            contenedorService.save(contenedor);
            return "redirect:/";
        } catch (CapacidadExcedidaException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("barcos", barcoService.findAll());
            model.addAttribute("barco", new Barco());
            model.addAttribute("contenedor", contenedor);
            return "index";
        }
    }
}