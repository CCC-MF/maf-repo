CREATE TABLE IF NOT EXISTS upload
(
    id         int auto_increment primary key,
    filename   varchar(255)                           not null,
    created_at timestamp  default current_timestamp() not null,
    content    mediumtext default ''                  not null
);

CREATE TABLE IF NOT EXISTS sample
(
    id                   int auto_increment primary key,
    tumor_sample_barcode varchar(255) not null,
    upload               int          not null,
    CONSTRAINT fk_upload FOREIGN KEY (upload) REFERENCES upload (id) ON UPDATE RESTRICT ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS simple_variant
(
    sample               int                     not null,
    tumor_sample_barcode varchar(255)            not null,
    hugo_symbol          varchar(255) default '' not null,
    chromosome           varchar(255) default '' not null,
    gene                 varchar(255) default '' not null,
    start_position       int                     not null,
    end_position         int                     not null,
    reference_allele     varchar(255) default '' not null,
    tumor_seq_allele2    varchar(255) default '' not null,
    hgvsc                varchar(255) default '' not null,
    hgvsp                varchar(255) default '' not null,
    t_depth              int                     not null,
    db_snp_rs            varchar(255) default '' not null,
    allelic_frequency    varchar(255) default '' not null,
    cosmic_id            varchar(255) default '' not null,
    interpretation       varchar(255) default '' not null,
    modified_at          timestamp,
    CONSTRAINT fk_sample FOREIGN KEY (sample) REFERENCES sample (id) ON UPDATE RESTRICT ON DELETE CASCADE
);
