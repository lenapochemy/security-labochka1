package org.example.labochka1.controllers;


import org.example.labochka1.dto.BearToyDTO;
import org.example.labochka1.model.BearToy;
import org.example.labochka1.services.BearToyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/api/bears")
public class BearToyController {

    private final BearToyService bearToyService;

//    @Autowired
    public BearToyController(BearToyService bearToyService){
        this.bearToyService = bearToyService;
    }

    @GetMapping
    public List<BearToy> getBears(){
        return bearToyService.getBears();
    }

    @PostMapping
    public BearToy addBear(@RequestBody BearToyDTO dto){
        BearToy bear = new BearToy(HtmlUtils.htmlEscape(dto.getName()), HtmlUtils.htmlEscape(dto.getMaterial()));
        return bearToyService.addBear(bear);
    }
}
