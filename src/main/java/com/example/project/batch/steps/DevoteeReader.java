package com.example.project.batch.steps;

import com.example.project.entities.Devotee;
import com.example.project.enums.DonationType;
import com.example.project.repositories.DevoteeRepository;
import com.example.project.specs.SpecificationsQueryBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class DevoteeReader extends IteratorItemReader<Devotee> {

    public DevoteeReader(DevoteeRepository repo, @Value("#{jobParameters['donationType']}") String donationTypeParam) {
        super(fetch(repo, donationTypeParam));
    }

    private static List<Devotee> fetch(DevoteeRepository repo, String donationTypeParam) {
        if (donationTypeParam == null || donationTypeParam.isBlank()) {
            throw new RuntimeException("job parameter 'donationType' is required");
        }

        DonationType type;
        try {
            type = DonationType.valueOf(donationTypeParam.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid donationType: " + donationTypeParam
                    + ". Allowed: " + java.util.Arrays.toString(DonationType.values()));
        }

        Specification<Devotee> spec = SpecificationsQueryBuilder
                .specificationForDevotee(null, null, null, null, type.getId());

        return repo.findAll(spec, Pageable.unpaged()).getContent();
    }
}
