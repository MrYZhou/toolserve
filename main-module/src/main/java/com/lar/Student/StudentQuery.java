package com.lar.Student;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;



@Data
@EqualsAndHashCode(callSuper = false)
public class StudentQuery extends Page<StudentEntity> {
    
}