package com.xcc.dto;


import com.xcc.domain.Setmeal;
import com.xcc.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
