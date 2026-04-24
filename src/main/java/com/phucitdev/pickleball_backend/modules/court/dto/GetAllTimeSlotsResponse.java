package com.phucitdev.pickleball_backend.modules.court.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTimeSlotsResponse {
    private List<TimeSlotItem> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
