package com.skcodify.myshop.service;

import com.skcodify.myshop.domain.Society;
import com.skcodify.myshop.repository.SocietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SocietyService {

    private final SocietyRepository societyRepository;

    public SocietyService(SocietyRepository societyRepository) {
        this.societyRepository = societyRepository;
    }

    public Society createSociety(Society society) {
        // In the future, we can add validation here, e.g., for the GeoJSON area
        return societyRepository.save(society);
    }

    @Transactional(readOnly = true)
    public List<Society> getAllSocieties() {
        return societyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Society> getSocietyById(Long id) {
        return societyRepository.findById(id);
    }
}
