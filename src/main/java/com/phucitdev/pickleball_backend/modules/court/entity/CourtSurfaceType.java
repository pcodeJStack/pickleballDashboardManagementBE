package com.phucitdev.pickleball_backend.modules.court.entity;
public enum CourtSurfaceType {

    ACRYLIC("Acrylic", "Sân cứng, phổ biến, độ bám tốt"),
    POLYURETHANE("Polyurethane", "Sân mềm, giảm chấn thương, dùng trong nhà"),
    CONCRETE("Concrete", "Sân bê tông, chi phí thấp, ngoài trời"),
    MODULAR_TILES("Modular Tiles", "Nhựa lắp ghép, thoát nước tốt"),
    ARTIFICIAL_GRASS("Artificial Grass", "Cỏ nhân tạo, êm, dễ bảo trì");

    private final String label;
    private final String description;

    CourtSurfaceType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}