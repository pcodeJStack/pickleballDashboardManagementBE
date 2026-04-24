-- branch
CREATE TABLE IF NOT EXISTS branch (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(20),
    image_url TEXT,
    description TEXT,
    open_time TIME,
    close_time TIME,
    is_deleted BOOLEAN DEFAULT FALSE,
    owner_id UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_branch_owner FOREIGN KEY (owner_id) REFERENCES account(id),
    CONSTRAINT uk_branch_name_owner UNIQUE (name, owner_id)
);
-- Tạo index: Branch
CREATE INDEX IF NOT EXISTS idx_branch_owner_deleted
ON branch(owner_id, is_deleted);
CREATE INDEX IF NOT EXISTS idx_branch_name ON branch(name);
CREATE INDEX IF NOT EXISTS idx_branch_address ON branch(address);
CREATE INDEX IF NOT EXISTS idx_branch_phone ON branch(phone);



-- court
CREATE TABLE IF NOT EXISTS court (
    id CHAR(36) PRIMARY KEY,

    name VARCHAR(255),
    description TEXT,
    court_number INT,

    court_type VARCHAR(50),
    surface_type VARCHAR(50),
    court_status VARCHAR(50),

    image_url TEXT,
    location VARCHAR(255),
    max_players INT,

    zone_id CHAR(36) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_court_zone
        FOREIGN KEY (zone_id)
        REFERENCES zone(id),

    CONSTRAINT uk_zone_court_number
        UNIQUE (zone_id, court_number)
);

-- Tạo index: Court
CREATE INDEX idx_court_zone_id
ON court(zone_id);

CREATE INDEX idx_court_name
ON court(name);
CREATE INDEX idx_court_type
ON court(court_type);
CREATE INDEX idx_court_surface
ON court(surface_type);
CREATE INDEX idx_court_price
ON court(price_per_hour);
CREATE INDEX idx_court_main
ON court(branch_id, is_active, court_type, price_per_hour);
CREATE INDEX idx_court_created_at
ON court(created_at);

-- tạo index: Zone
CREATE TABLE IF NOT EXISTS zone (
    id UUID PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    description TEXT,
    max_courts INT,

    branch_id UUID NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_zone_branch
        FOREIGN KEY (branch_id)
        REFERENCES branch(id)
        ON DELETE CASCADE
);

-- filter theo branch
CREATE INDEX idx_zone_branch_id
ON zone(branch_id);
-- search name prefix
CREATE INDEX idx_zone_name
ON zone(name);
CREATE INDEX idx_zone_branch_name
ON zone(branch_id, name);