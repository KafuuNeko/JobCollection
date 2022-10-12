create table if not exists `config`
(
    `id`     INT UNSIGNED AUTO_INCREMENT,
    `key`   VARCHAR(128) NOT NULL,
    `value` VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) charset = utf8;

create table if not exists `jobs`
(
    `job_id`     INT UNSIGNED AUTO_INCREMENT,
    `job_name`   VARCHAR(128) NOT NULL,
    `start_time` DATETIME     NOT NULL,
    `end_time`   DATETIME     NOT NULL,
    PRIMARY KEY (job_id)
) charset = utf8;

create table if not exists `students`
(
    `student_id`    INT UNSIGNED NOT NULL,
    `student_name`  VARCHAR(128) NOT NULL,
    `student_sex`   VARCHAR(8)   NOT NULL,
    `student_class` INT UNSIGNED NOT NULL,
    `student_major` VARCHAR(128) NOT NULL,
    PRIMARY KEY (student_id)
) charset = utf8;

create table if not exists `uploads`
(
    `job_id`      INT UNSIGNED NOT NULL,
    `student_id`  INT UNSIGNED NOT NULL,
    `file_path`   VARCHAR(255) NOT NULL,
    `upload_time` DATETIME     NOT NULL,
    PRIMARY KEY (job_id, student_id),
    FOREIGN KEY (job_id) REFERENCES jobs (job_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id)
) charset = utf8;

