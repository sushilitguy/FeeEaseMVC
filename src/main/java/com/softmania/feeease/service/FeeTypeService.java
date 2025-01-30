package com.softmania.feeease.service;

import com.softmania.feeease.model.FeeType;
import com.softmania.feeease.repo.FeeTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeTypeService {
    @Autowired
    private FeeTypeRepo repo;

    public List<FeeType> getFeeTypes() {
        return repo.findAll();
    }

    public FeeType getFeeTypeById(int id) {
        return repo.findById(id).orElse(null);
    }

    public FeeType addFeeType(FeeType feeType) {
        return repo.save(feeType);
    }

    public FeeType updateFeeType(FeeType feeType) {
        FeeType updatedFeeType = null;
        if(repo.existsById(feeType.getId())) {
            updatedFeeType = repo.save(feeType);
        }
        return updatedFeeType;
    }

    public boolean deleteFeeType(int id) {
        boolean isDeleted = false;
        if(repo.existsById(id)) {
            repo.deleteById(id);
            isDeleted = true;
        }
        return isDeleted;
    }
}
