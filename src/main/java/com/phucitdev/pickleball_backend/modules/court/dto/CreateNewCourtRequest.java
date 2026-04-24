package com.phucitdev.pickleball_backend.modules.court.dto;
import com.phucitdev.pickleball_backend.commo.validation.group.createCourt_group.*;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtStatus;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtSurfaceType;
import com.phucitdev.pickleball_backend.modules.court.entity.CourtType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewCourtRequest {
    @NotBlank(message = "Name must not be blank!", groups = {NameNotBlank.class})
    private String name;
    @NotBlank(message = "Description must not be blank!", groups = {DescriptionNotBlank.class})
    private String description;
    @NotNull(message = "CourtNumber must not be null!", groups = {CourtNumberNotNull.class})
    private Integer courtNumber;

    @NotNull(message = "Court type must not be null!", groups = {CourtTypeNotNull.class})
    private CourtType courtType;
    @NotNull(message = "CourtSurfaceType must not be blank!", groups = {CourtSurfaceTypeNotNull.class})
    private CourtSurfaceType surfaceType;
    @NotNull(message = "Court status must not be null!", groups = {CourtStatusNotNull.class})
    private CourtStatus courtStatus;

    @NotBlank(message = "ImageUrl must not be blank!", groups = {ImageNotBlank.class})
    private String imageUrl;
    @NotBlank(message = "Location must not be blank!", groups =  {LocationNotBlank.class})
    private String location;
    @NotNull(message = "Max players must not be null!", groups = {MaxPlayersNotNull.class})
    @Min(value = 1, message = "Max players must be at least 1", groups = {MinMaxPlayers.class})
    private Integer maxPlayers;
    @NotNull(message = "ZoneId must not be null!")
    private UUID zoneId;
}
