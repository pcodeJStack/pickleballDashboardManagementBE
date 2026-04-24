package com.phucitdev.pickleball_backend.modules.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllBranchesResponse {
    private List<BranchResponse> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
