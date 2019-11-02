package locationsapp.controller;

import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/server")
public class LocationsServerController {

    private LocationsService locationsService;

    public LocationsServerController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView listLocations() {
        return new ModelAndView("locations", "page", locationsService.listLocations(PageRequest.of(0, 20_000, Sort.by("name")))).addObject("createLocationCommand", new CreateLocationCommand());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ModelAndView postSaveLocation(
            @Valid CreateLocationCommand createLocationCommand, BindingResult bindingResult,
            Pageable pageRequest, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("locations", "page", locationsService.listLocations(PageRequest.of(0, 20_000, Sort.by("name"))));
        }

        locationsService.createLocation(createLocationCommand);
        redirectAttributes.addFlashAttribute("message", "Location has been saved.");
        return new ModelAndView("redirect:/server");
    }

}
