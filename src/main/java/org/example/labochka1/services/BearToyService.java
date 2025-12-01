package org.example.labochka1.services;

import org.example.labochka1.model.BearToy;
import org.example.labochka1.repositories.BearToyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BearToyService {

    private BearToyRepository bearToyRepository;

    @Autowired
    public BearToyService(BearToyRepository bearToyRepository){
        this.bearToyRepository = bearToyRepository;
    }


    public List<BearToy> getBears(){
        return (List<BearToy>) bearToyRepository.findAll();
    }

    public BearToy addBear(BearToy bear){
        return bearToyRepository.save(bear);
    }
}
